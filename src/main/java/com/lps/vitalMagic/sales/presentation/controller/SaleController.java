package com.lps.vitalMagic.sales.presentation.controller;

import com.lps.vitalMagic.inventory.application.controller.ItemController;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.usecase.RegisterSaleUseCase;
import com.lps.vitalMagic.sales.application.usecase.SearchSaleUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {
    private final RegisterSaleUseCase registerSaleUseCase;
    private final SearchSaleUseCase searchSaleUseCase;

    public SaleController(RegisterSaleUseCase registerSaleUseCase, SearchSaleUseCase searchSaleUseCase) {
        this.registerSaleUseCase = registerSaleUseCase;
        this.searchSaleUseCase = searchSaleUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateSaleResponse>registerSale(@Valid @RequestBody CreateSaleRequest createSaleRequest){
        List<CreateSaleItemCommand> saleItemCommands= createSaleRequest.items.stream()
                .map(createSaleItemRequest ->
                        new CreateSaleItemCommand(createSaleItemRequest.productId,createSaleItemRequest.quantity))
                        .toList();
        CreateSaleCommand saleCommand= new CreateSaleCommand(saleItemCommands);
        Long saleId=registerSaleUseCase.execute(saleCommand);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saleId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new CreateSaleResponse(saleId));


    }




    public record CreateSaleResponse(
            Long saleId
    ){}


    public record CreateSaleRequest(
            @NotEmpty
            List<@Valid CreateSaleItemRequest> items
    ) {}

    public record CreateSaleItemRequest(
            @NotNull
            @Positive
            Long productId,

            @Positive
            int quantity
    ) {}

}
