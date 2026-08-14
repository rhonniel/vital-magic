package com.lps.vitalMagic.sale.application;

import com.lps.vitalMagic.sales.application.controller.SaleController;
import com.lps.vitalMagic.sales.application.usecase.RegisterSaleUseCase;
import com.lps.vitalMagic.sales.application.usecase.SearchSaleUseCase;
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

@WebMvcTest(SaleController.class)
public class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterSaleUseCase registerSaleUseCase;

    @MockitoBean
    private SearchSaleUseCase searchSaleUseCase;



    @Test
    public void createSaleSuccessfully() throws Exception {

        when(registerSaleUseCase.execute(any()))
                .thenReturn(77L);
        mockMvc.perform(post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "items": [
                                     { "productId" : 5, "quantity": 2},
                                     { "productId" : 1, "quantity": 3}
                             ]
                          }
                        
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saleId").value(77));

    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    public void shouldRejectInvalidRequestForRegisterSale(String invalidJson) throws Exception {
        mockMvc.perform(post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(registerSaleUseCase);
    }

@Test
public void searchSaleWithQueryIsSuccessfully() throws Exception {
    mockMvc.perform(get("/sale")
                    .param("from", "2026-08-01")
                    .param("to", "2026-08-13")
                    .param("productId", "2")
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

    mockMvc.perform(get("/sale")
                    .param("shakeCategory", shakeCategory)
                    .param("shakeType", shakeType)
                    .param("page", page)
                    .param("size", size)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

    verifyNoInteractions(searchSaleUseCase);
}

    private static Stream<Arguments> invalidSearchParameters() {
        return Stream.of(
                Arguments.of("2026-08-01","2026-08-01","1","-4", "10"), // page: @PositiveOrZero
                Arguments.of("2026-08-01","2026-08-01","1","0", "0"), // size: @min 1
                Arguments.of("2026-08-01","2026-08-01","1", "101")// size: @max 100

        );
    }



    private static Stream<String> invalidRequests() {
        return Stream.of(
                // items: @NotEmpty
                """
                {
                 "items": []
                }
                """,

                // productId: @NotNull

                """
                {
                  "items": [
                                     { "quantity": 2}
                             ]
                  }
        }
        """,

                // productId: @Positive
                """
                {
                 "items": [
                                     { "productId" : -2, "quantity": 2},
                                     { "productId" : 1, "quantity": 3}
                             ]
                }
                """,

                // quantity: @Positive
                """
                {
             "items": [
                                     { "productId" : 2, "quantity": 0},
                                     { "productId" : 1, "quantity": 3}
                             ]
                 }
        """
        );
    }

}
