package com.lps.vitalMagic.sales.application.service;

import com.lps.vitalMagic.inventory.domain.service.RegisterSaleTransactionService;
import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.data.ProductComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
import com.lps.vitalMagic.product.domain.service.ProductCompositionService;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.usecase.RegisterSaleUseCase;
import com.lps.vitalMagic.sales.domain.exception.SaleDomainException;
import com.lps.vitalMagic.sales.domain.model.entity.ProductSnapshot;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.model.input.SaleItemInput;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RegisterSaleService implements RegisterSaleUseCase {

    public final SaleRepository saleRepository;
    public final ProductAvailabilityService productAvailabilityService;
    public final ProductCompositionService productCompositionService;
    public final RegisterSaleTransactionService saleTransactionService;

  // TODO tiene sentido buscar cargando la instancia via spring lso servicios que son clases normales y no interfaces?
    public RegisterSaleService(SaleRepository saleRepository, ProductRepository productRepository,
                               ProductAvailabilityService productAvailabilityService,
                               ProductCompositionService productCompositionService,
                               RegisterSaleTransactionService saleTransactionService) {
        this.saleRepository = saleRepository;
        this.productAvailabilityService = productAvailabilityService;
        this.productCompositionService = productCompositionService;
        this.saleTransactionService = saleTransactionService;
    }

    @Override
    public Long execute(CreateSaleCommand command) {
        List<SaleItemInput> saleItemInputs = new ArrayList<>();
        List<Composition> compositions= new ArrayList<>();

        for(CreateSaleItemCommand itemCommand:command.items()){
            ProductComposition productComposition = productCompositionService.getProductComposition(itemCommand.productId(), itemCommand.quantity());

            if(!productAvailabilityService.checkAvailability(productComposition.composition())){
               throw new SaleDomainException("Product don't have enough stocks");
            }

            saleItemInputs.add(new SaleItemInput(
                    new ProductSnapshot(productComposition.product().getId(),productComposition.product().getName(),
                            productComposition.product().getPrice()), itemCommand.quantity()));

            compositions.add(productComposition.composition());

        }

        Sale sale= Sale.create(saleItemInputs);

        sale=saleRepository.save(sale);

        for (Composition composition:compositions){
            for (IngredientComposition ingredientComposition: composition.items()){
                saleTransactionService.registerInventoryTransaction(sale.getId(), ingredientComposition.itemId(), ingredientComposition.quantity());
            }

        }

        return sale.getId();
    }
}
