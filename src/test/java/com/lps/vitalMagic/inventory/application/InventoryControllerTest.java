package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.controller.InventoryController;
import com.lps.vitalMagic.inventory.application.usecase.FindItemsWithLowStockUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindItemsWithLowStockUseCase findItemsWithLowStockUseCase;

    @Test
    public void findItemsWithLowStockSuccessfully() throws Exception {

        mockMvc.perform(get("/low-stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
