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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"));

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
public void shouldRejectInvalidRequestForSearchSale(
        String from,
        String to,
        String productId,
        String page,
        String size,
        String invalidField
) throws Exception {

    mockMvc.perform(get("/sale")
                    .param("from", from)
                    .param("to", to)
                    .param("productId", productId)
                    .param("page", page)
                    .param("size", size)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors." + invalidField).exists());

    verifyNoInteractions(searchSaleUseCase);
}

    private static Stream<Arguments> invalidSearchParameters() {
        return Stream.of(
                Arguments.of("2026-08-01","2026-08-01","1","-4", "10", "page"),
                Arguments.of("2026-08-01","2026-08-01","1","0", "0", "size"),
                Arguments.of("2026-08-01","2026-08-01","1","0", "101", "size")

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
