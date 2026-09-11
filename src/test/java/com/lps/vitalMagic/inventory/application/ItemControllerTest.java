package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.common.exception.ResourceNotFoundException;
import com.lps.vitalMagic.inventory.application.controller.ItemController;
import com.lps.vitalMagic.inventory.application.usecase.CreateItemUseCase;
import com.lps.vitalMagic.inventory.application.usecase.SearchAvailableItemsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Test
    void shouldSearchItemsWithQueryAndPagination() throws Exception {
        var query = new SearchItemsQuery(
                "Dragon", new Pagination(1, 2));
        var item = new ItemView(
                77L, "Dragon tail", "Rare ingredient", List.of());
        when(searchAvailableItemsUseCase.execute(query)).thenReturn(
                new PageResult<>(List.of(item), 1, 2, 3, 2));

        mockMvc.perform(get("/item").param("name", "Dragon")
                        .param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(77))
                .andExpect(jsonPath("$.content[0].name").value("Dragon tail"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));

        verify(searchAvailableItemsUseCase).execute(query);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateItemUseCase createItemUseCase;

    @MockitoBean
    private SearchAvailableItemsUseCase searchAvailableItemsUseCase;


    @ParameterizedTest
    @MethodSource("invalidRequests")
    void shouldRejectInvalidRequestForCreateItem(String invalidJson) throws Exception {
        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createItemUseCase);
    }

    private static Stream<String> invalidRequests() {
        return Stream.of(
                // name: @NotBlank
                """
                {
                  "name": "",
                  "description": "Dragon tail",
                  "attributes": {"1": 5},
                  "minStock": 10
                }
                """,

                // description: @NotBlank
                """
                {
                  "name": "Dragon tail",
                  "description": "   ",
                  "attributes": {"1": 5},
                  "minStock": 10
                }
                """,

                // attributes: @NotEmpty
                """
                {
                  "name": "Dragon tail",
                  "description": "Description",
                  "attributes": {},
                  "minStock": 10
                }
                """,

                // attributes: @Positive
                """
                {
                  "name": "Dragon tail",
                  "description": "Description",
                  "attributes": {"1": 0},
                  "minStock": 10
                }
                """
        );
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(createItemUseCase.execute(any()))
                .thenReturn(77L);

        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Dragon tail",
                          "description": "Rare ingredient",
                          "attributes": {
                            "1": 5
                          },
                          "minStock": 10
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(77));
    }

    @Test
    void shouldReturnNotFoundWhenAttributeDoesNotExist() throws Exception {
        when(createItemUseCase.execute(any()))
                .thenThrow(new ResourceNotFoundException("Attribute",1));

        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Dragon tail",
                          "description": "Rare ingredient",
                          "attributes": {
                            "1": 5
                          },
                          "minStock": 10
                        }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail")
                        .value("Attribute 1 not found"));
    }

    @ParameterizedTest
    @MethodSource("invalidSearchParameters")
    void shouldRejectInvalidRequestForSearchAvailableItems(
            String page,
            String size
    ) throws Exception {

        mockMvc.perform(get("/item")
                        .param("page", page)
                        .param("size", size)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(searchAvailableItemsUseCase);
    }

    private static Stream<Arguments> invalidSearchParameters() {
        return Stream.of(
                Arguments.of("-4", "10"), // page: @PositiveOrZero
                Arguments.of("0", "0"), // size: @min 1
                Arguments.of("0", "101")// size: @max 100

        );
    }


}
