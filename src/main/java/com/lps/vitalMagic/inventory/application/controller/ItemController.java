package com.lps.vitalMagic.inventory.application.controller;

import com.lps.vitalMagic.inventory.application.command.CreateItemAttributeCommand;
import com.lps.vitalMagic.inventory.application.command.CreateItemCommand;
import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.usecase.CreateItemUseCase;
import com.lps.vitalMagic.inventory.application.usecase.FindItemsWithLowStockUseCase;
import com.lps.vitalMagic.inventory.application.usecase.SearchAvailableItemsUseCase;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.pagination.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final SearchAvailableItemsUseCase searchAvailableItemsUseCase;

    public ItemController(CreateItemUseCase createItemUseCase, SearchAvailableItemsUseCase searchAvailableItemsUseCase, FindItemsWithLowStockUseCase findItemsWithLowStockUseCase) {
        this.createItemUseCase = createItemUseCase;
        this.searchAvailableItemsUseCase = searchAvailableItemsUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateItemResponse> create(
            @Valid @RequestBody CreateItemRequest request
    ) {
        List<CreateItemAttributeCommand> attributes =
                request.attributes()
                        .entrySet()
                        .stream()
                        .map(entry -> new CreateItemAttributeCommand(
                                entry.getKey(),
                                entry.getValue()
                        ))
                        .toList();

        CreateItemCommand command = new CreateItemCommand(
                request.name(),
                request.description(),
                attributes,
                request.minStock()
        );

        Long itemId = createItemUseCase.execute(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new CreateItemResponse(itemId));
    }

    @GetMapping
    public PageResult<ItemView> searchItem(SearchItemsRequest request){
        SearchItemsQuery query = new SearchItemsQuery(
                request.name(),
                new Pagination(request.page(), request.size())
        );

        return searchAvailableItemsUseCase.execute(query);
    }


    public record CreateItemRequest(
            @NotBlank
            String name,

            @NotBlank
            String description,

            @NotEmpty
            Map<@Positive Long, @Positive Integer> attributes,

            @PositiveOrZero
            int minStock
    ) {}

    public record CreateItemResponse(Long id) {}

    public record SearchItemsRequest(
            String name,

            @PositiveOrZero
            int page,

            @Min(1)
            @Max(100)
            int size
    ) {}

}
