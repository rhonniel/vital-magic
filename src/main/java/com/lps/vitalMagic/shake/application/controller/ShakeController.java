package com.lps.vitalMagic.shake.application.controller;


import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.common.presentation.pagination.Pagination;
import com.lps.vitalMagic.shake.application.command.CreateShakeIngredientCommand;
import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.usecase.CreateStandardShakeUseCase;
import com.lps.vitalMagic.shake.application.usecase.SearchShakeUseCase;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shake")
public class ShakeController {

    private final CreateStandardShakeUseCase createStandardShakeUseCase;
    private final SearchShakeUseCase searchShakeUseCase;


    public ShakeController(CreateStandardShakeUseCase createStandardShakeUseCase, SearchShakeUseCase searchShakeUseCase) {
        this.createStandardShakeUseCase = createStandardShakeUseCase;
        this.searchShakeUseCase = searchShakeUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateShakeResponse> CreateStandardShake(@Valid @RequestBody CreateShakeRequest request){

        List<CreateShakeIngredientCommand> ingredientCommands=   request.ingredients()
                .entrySet()
                .stream()
                .map(entry -> new CreateShakeIngredientCommand(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
        CreateStandardShakeCommand shakeCommand= new CreateStandardShakeCommand(request.name(), request.description(),
                request.shakeCategory(),ingredientCommands);

        Long shakeId=createStandardShakeUseCase.execute(shakeCommand);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(shakeId)
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ShakeController.CreateShakeResponse(shakeId));
    }


    @GetMapping
    public PageResult<ShakeView> searchItem(@Valid SearchShakeRequest request){
        SearchShakeQuery query = new SearchShakeQuery(
                request.shakeType(),
                request.shakeCategory,
                new Pagination(request.page(), request.size())
        );

        return searchShakeUseCase.execute(query);
    }

    public record CreateShakeRequest(
            @NotBlank
            String name,

            @NotBlank
            String description,

            @NotNull
            ShakeCategory shakeCategory,
            @NotEmpty
            Map<@Positive Long, @Positive Integer> ingredients
    ) {}

    public record CreateShakeResponse(Long id) {}

    public record SearchShakeRequest(
            ShakeType shakeType,
            ShakeCategory shakeCategory,

            @PositiveOrZero
            int page,

            @Min(1)
            @Max(100)
            int size
    ) {}


}
