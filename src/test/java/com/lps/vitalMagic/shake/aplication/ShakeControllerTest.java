package com.lps.vitalMagic.shake.aplication;

import com.lps.vitalMagic.shake.application.controller.ShakeController;
import com.lps.vitalMagic.shake.application.usecase.CreateStandardShakeUseCase;
import com.lps.vitalMagic.shake.application.usecase.SearchShakeUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShakeController.class)
public class ShakeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateStandardShakeUseCase createStandardShakeUseCase;

    @MockitoBean
    private SearchShakeUseCase searchShakeUseCase;



    @Test
    public void createStandardShakeSuccessfully() throws Exception {
        when(createStandardShakeUseCase.execute(any()))
                .thenReturn(77L);

        mockMvc.perform(post("/shake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Egida Fresada",
                          "description": "Batido de fresas que potencia la defensa Magica",
                          "shakeCategory": "DEFENSIVE",
                          "ingredients": {
                            "1": 5,
                             "2": 2,
                             "3": 1
                          }
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(77));
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    public void shouldRejectInvalidRequestForCreateStandardShake(String invalidJson) throws Exception {
        mockMvc.perform(post("/shake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createStandardShakeUseCase);
    }

    public void searchShakeWithQueryIsSuccessfully() throws Exception {
        mockMvc.perform(get("/shake")
                        .param("shakeCategory", "DEFENSIVE")
                        .param("shakeType", "STANDARD")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidSearchParameters")
    public void shouldRejectInvalidRequestForSearchShake(
            String shakeCategory,
            String shakeType,
            String page,
            String size
    ) throws Exception {

        mockMvc.perform(get("/shake")
                        .param("shakeCategory", shakeCategory)
                        .param("shakeType", shakeType)
                        .param("page", page)
                        .param("size", size)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(searchShakeUseCase);
    }

    private static Stream<Arguments> invalidSearchParameters() {
        return Stream.of(
                Arguments.of("RARE","STANDARD","-4", "10"), // page: @PositiveOrZero
                Arguments.of("RARE","STANDARD","0", "0"), // size: @min 1
                Arguments.of("RARE","STANDARD","0", "101"),// size: @max 100
                Arguments.of("RARE","SPECIAL","0", "100"),// enum value
                Arguments.of("MAGIC","STANDARD","0", "100")// enum value

        );
    }



    private static Stream<String> invalidRequests() {
        return Stream.of(
                // name: @NotBlank
                """
                {
                   "name": " ",
                  "description": "Batido de fresas que potencia la defensa Magica",
                  "shakeCategory": "DEFENSIVE",
                  "ingredients": {
                    "1": 5,
                     "2": 2,
                     "3": 1
                  }
                }
                """,

                // description: @NotBlank
                """
                {
                   "name": "Egida Fresada",
                  "description": "",
                  "shakeCategory": "DEFENSIVE",
                  "ingredients": {
                    "1": 5,
                     "2": 2,
                     "3": 1
                  }
        }
        """,

                // shakeCategory: @NotNull (using a value  outside the enum)
                """
                { "name": "Egida Fresada",
                  "description": "Batido de fresas que potencia la defensa Magica",
                  "shakeCategory": "MAGIC",
                  "ingredients": {
                    "1": 5,
                     "2": 2,
                     "3": 1
                  }
                }
                """,

                // ingredients: @Positive
                """
                {
                  "name": "Egida Fresada",
                  "description": "Batido de fresas que potencia la defensa Magica",
                  "shakeCategory": "DEFENSIVE",
                  "ingredients": {
                    "1": -5,
                     "2": 2,
                     "3": 1
                  }
        }
        """
        );
    }




}
