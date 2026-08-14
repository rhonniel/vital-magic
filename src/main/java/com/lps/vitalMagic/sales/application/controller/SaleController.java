package com.lps.vitalMagic.sales.application.controller;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.usecase.RegisterSaleUseCase;
import com.lps.vitalMagic.sales.application.usecase.SearchSaleUseCase;
import com.lps.vitalMagic.sales.application.view.SaleView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
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

    @GetMapping
    public PageResult<SaleView> searchSale(@Valid SearchSaleRequest request){
        SearchSaleQuery query = new SearchSaleQuery(request.from,request.to,request.productId,
        new Pagination(request.page(), request.size()));
        return searchSaleUseCase.execute(query);

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


    public record SearchSaleRequest(

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            @Positive
            Long productId,

            @PositiveOrZero
            int page,

            @Min(1)
            @Max(100)
            int size
    ) {}

}
