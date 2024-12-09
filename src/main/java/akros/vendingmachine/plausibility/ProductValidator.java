package akros.vendingmachine.plausibility;

import akros.vendingmachine.dto.ProductDTO;

import java.util.function.Function;

import static akros.vendingmachine.plausibility.ProductValidation.PRODUCT_NOT_FOUND_BY_ID;
import static akros.vendingmachine.plausibility.ProductValidation.VALID;

public interface ProductValidator extends Function<ProductDTO, ProductValidation> {

    static ProductValidator findProductById(Integer productId) {
        return productDTO -> productDTO!=null && productDTO.getProductId().equals(productId) ? VALID : PRODUCT_NOT_FOUND_BY_ID;
    }
}
