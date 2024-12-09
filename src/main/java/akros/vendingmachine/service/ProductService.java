package akros.vendingmachine.service;

import akros.vendingmachine.dto.ProductDTO;
import akros.vendingmachine.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getProducts();
    ProductResponseDto getProduct(Integer id);
    ProductResponseDto createProduct(ProductDTO product);
    ProductResponseDto deleteProduct(Integer id);
    ProductResponseDto updateProduct(ProductDTO product, Integer id);
}
