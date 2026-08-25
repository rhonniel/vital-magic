package com.lps.vitalMagic.purchase.application.controller;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseItemCommand;
import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.usecase.RegisterPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.usecase.SearchPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final RegisterPurchaseUseCase registerPurchaseUseCase;
    private final SearchPurchaseUseCase searchPurchaseUseCase;

    public PurchaseController(
            RegisterPurchaseUseCase registerPurchaseUseCase,
            SearchPurchaseUseCase searchPurchaseUseCase
    ) {
        this.registerPurchaseUseCase = registerPurchaseUseCase;
        this.searchPurchaseUseCase = searchPurchaseUseCase;
    }

    @PostMapping
    public ResponseEntity<CreatePurchaseResponse> registerPurchase(@Valid @RequestBody CreatePurchaseRequest request) {
        List<CreatePurchaseItemCommand> itemCommands = request.items()
                .stream()
                .map(item -> new CreatePurchaseItemCommand(
                        item.itemId(),
                        item.quantity(),
                        item.unitCost()
                ))
                .toList();

        Long purchaseId = registerPurchaseUseCase.execute(
                new CreatePurchaseCommand(itemCommands)
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(purchaseId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new CreatePurchaseResponse(purchaseId));
    }

    @GetMapping
    public PageResult<PurchaseView> searchPurchases(@Valid SearchPurchaseRequest request) {
        SearchPurchasesQuery query = new SearchPurchasesQuery(
                request.from(),
                request.to(),
                request.itemId(),
                new Pagination(request.page(), request.size())
        );

        return searchPurchaseUseCase.execute(query);
    }

    public record CreatePurchaseRequest(
            @NotEmpty
            List<@Valid CreatePurchaseItemRequest> items
    ) {}

    public record CreatePurchaseItemRequest(
            @NotNull
            @Positive
            Long itemId,

            @Positive
            int quantity,

            @NotNull
            @DecimalMin(value = "0.0", inclusive = false) //DUDA
            BigDecimal unitCost
    ) {}

    public record CreatePurchaseResponse(
            Long purchaseId
    ) {}

    public record SearchPurchaseRequest(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            @Positive
            Long itemId,

            @PositiveOrZero
            int page,

            @Min(1)
            @Max(100)
            int size
    ) {}
}