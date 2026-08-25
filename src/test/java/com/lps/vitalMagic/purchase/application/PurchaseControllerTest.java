package com.lps.vitalMagic.purchase.application;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseItemCommand;
import com.lps.vitalMagic.purchase.application.controller.PurchaseController;
import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.usecase.RegisterPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.usecase.SearchPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.view.PurchaseItemView;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterPurchaseUseCase registerPurchaseUseCase;

    @MockitoBean
    private SearchPurchaseUseCase searchPurchaseUseCase;

    @Test
    void shouldRegisterPurchaseSuccessfully() throws Exception {
        when(registerPurchaseUseCase.execute(any()))
                .thenReturn(77L);

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {
                                      "itemId": 1,
                                      "quantity": 5,
                                      "unitCost": 35.50
                                    },
                                    {
                                      "itemId": 2,
                                      "quantity": 3,
                                      "unitCost": 20.00
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/purchase/77"
                ))
                .andExpect(jsonPath("$.purchaseId").value(77));

        verify(registerPurchaseUseCase).execute(
                new CreatePurchaseCommand(
                        List.of(
                                new CreatePurchaseItemCommand(
                                        1L,
                                        5,
                                        new BigDecimal("35.50")
                                ),
                                new CreatePurchaseItemCommand(
                                        2L,
                                        3,
                                        new BigDecimal("20.00")
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterPurchaseRequests")
    void shouldRejectInvalidRegisterPurchaseRequest(
            String invalidJson
    ) throws Exception {
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(registerPurchaseUseCase);
    }

    @Test
    void shouldSearchPurchasesSuccessfully() throws Exception {
        PurchaseItemView itemView = new PurchaseItemView(
                2L,
                "Dragon Tail",
                3,
                new BigDecimal("20.00"),
                new BigDecimal("60.00")
        );

        PurchaseView purchaseView = new PurchaseView(
                77L,
                LocalDateTime.of(2026, 8, 24, 10, 30),
                new BigDecimal("60.00"),
                List.of(itemView)
        );

        when(searchPurchaseUseCase.execute(any()))
                .thenReturn(
                        new PageResult<>(
                                List.of(purchaseView),
                                0,
                                10,
                                1,
                                1
                        )
                );

        mockMvc.perform(get("/purchase")
                        .param("from", "2026-08-01")
                        .param("to", "2026-08-24")
                        .param("itemId", "2")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(77))
                .andExpect(jsonPath("$.content[0].totalAmount").value(60.00))
                .andExpect(jsonPath("$.content[0].items[0].itemId").value(2))
                .andExpect(jsonPath("$.content[0].items[0].itemName")
                        .value("Dragon Tail"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(searchPurchaseUseCase).execute(
                new SearchPurchasesQuery(
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 24),
                        2L,
                        new Pagination(0, 10)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidSearchParameters")
    void shouldRejectInvalidSearchPurchaseRequest(
            String from,
            String to,
            String itemId,
            String page,
            String size
    ) throws Exception {
        mockMvc.perform(get("/purchase")
                        .param("from", from)
                        .param("to", to)
                        .param("itemId", itemId)
                        .param("page", page)
                        .param("size", size)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(searchPurchaseUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenDateRangeIsInvalid() throws Exception {
        when(searchPurchaseUseCase.execute(any()))
                .thenThrow(
                        new IllegalArgumentException(
                                "From should be before To."
                        )
                );

        mockMvc.perform(get("/purchase")
                        .param("from", "2026-08-24")
                        .param("to", "2026-08-01")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Invalid operation input"))
                .andExpect(jsonPath("$.detail")
                        .value("From should be before To."));
    }

    private static Stream<String> invalidRegisterPurchaseRequests() {
        return Stream.of(
                // items: @NotEmpty
                """
                {
                  "items": []
                }
                """,

                // itemId: @NotNull
                """
                {
                  "items": [
                    {
                      "quantity": 5,
                      "unitCost": 35.50
                    }
                  ]
                }
                """,

                // itemId: @Positive
                """
                {
                  "items": [
                    {
                      "itemId": -1,
                      "quantity": 5,
                      "unitCost": 35.50
                    }
                  ]
                }
                """,

                // quantity: @Positive
                """
                {
                  "items": [
                    {
                      "itemId": 1,
                      "quantity": 0,
                      "unitCost": 35.50
                    }
                  ]
                }
                """,

                // unitCost: @NotNull
                """
                {
                  "items": [
                    {
                      "itemId": 1,
                      "quantity": 5
                    }
                  ]
                }
                """,

                // unitCost: @Positive
                """
                {
                  "items": [
                    {
                      "itemId": 1,
                      "quantity": 5,
                      "unitCost": 0
                    }
                  ]
                }
                """
        );
    }

    private static Stream<Arguments> invalidSearchParameters() {
        return Stream.of(
                // from: formato inválido
                Arguments.of(
                        "01-08-2026",
                        "2026-08-24",
                        "2",
                        "0",
                        "10"
                ),

                // to: formato inválido
                Arguments.of(
                        "2026-08-01",
                        "24-08-2026",
                        "2",
                        "0",
                        "10"
                ),

                // itemId: @Positive
                Arguments.of(
                        "2026-08-01",
                        "2026-08-24",
                        "0",
                        "0",
                        "10"
                ),

                // page: @PositiveOrZero
                Arguments.of(
                        "2026-08-01",
                        "2026-08-24",
                        "2",
                        "-1",
                        "10"
                ),

                // size: @Min(1)
                Arguments.of(
                        "2026-08-01",
                        "2026-08-24",
                        "2",
                        "0",
                        "0"
                ),

                // size: @Max(100)
                Arguments.of(
                        "2026-08-01",
                        "2026-08-24",
                        "2",
                        "0",
                        "101"
                )
        );
    }
}