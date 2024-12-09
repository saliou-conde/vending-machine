package akros.vendingmachine.service.impl;

import akros.vendingmachine.domain.Inventar;
import akros.vendingmachine.domain.Product;
import akros.vendingmachine.dto.ProductDTO;
import akros.vendingmachine.dto.ProductResponseDto;
import akros.vendingmachine.dto.mapper.ProductMapper;
import akros.vendingmachine.plausibility.ProductValidation;
import akros.vendingmachine.plausibility.ProductValidator;
import akros.vendingmachine.repository.InventarRepository;
import akros.vendingmachine.repository.ProductRepository;
import akros.vendingmachine.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static akros.vendingmachine.AppConstant.PRODUCT_API_PATH;
import static akros.vendingmachine.AppConstant.PRODUCT_KEY;
import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventarRepository inventarRepository;
    private static final ProductMapper PRODUCT_MAPPER = ProductMapper.PRODUCT_MAPPER;

    @Override
    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper.PRODUCT_MAPPER::mapToProductDTO).toList();
    }

    @Override
    public ProductResponseDto getProduct(Integer id) {
        Product productById = findProductById(id);
        var validation = ProductValidator.findProductById(id).apply(PRODUCT_MAPPER.mapToProductDTO(productById));
        if(validation == ProductValidation.VALID) {
            return ProductResponseDto.builder()
                    .timestamp(Instant.now().toString())
                    .status(OK)
                    .message("Product found by ID: "+id)
                    .error(null)
                    .path(PRODUCT_API_PATH+id)
                    .statusCode(OK.value())
                    .data(Map.of(PRODUCT_KEY, productById))
                    .build();
        }
        return productNotFoundById(id);
    }

    @Override
    public ProductResponseDto createProduct(ProductDTO product) {

        var productName = product.getProductName();
        Inventar inventar = inventarRepository.findByProductName(productName);

        if(inventar == null) {
            inventar = new Inventar();
            inventar.setId(UUID.randomUUID().toString());
            inventar.setProductName(productName);
            inventar.setQuantity(1);
            inventarRepository.save(inventar);
        }
        else {
            if(inventar.getQuantity() > 9) {
                return ProductResponseDto.builder()
                        .timestamp(Instant.now().toString())
                        .status(BAD_REQUEST)
                        .error("Product cannot be added")
                        .path(PRODUCT_API_PATH)
                        .statusCode(BAD_REQUEST.value())
                        .data(Map.of(PRODUCT_KEY, product))
                        .build();
            }
            inventar.setQuantity(inventar.getQuantity() + 1);
            inventarRepository.save(inventar);
        }

        product.setInventar(inventar);
        Product save = productRepository.save(PRODUCT_MAPPER.mapToProduct(product));
        return ProductResponseDto.builder()
                .timestamp(Instant.now().toString())
                .status(CREATED)
                .error(null)
                .path(PRODUCT_API_PATH)
                .statusCode(CREATED.value())
                .data(Map.of(PRODUCT_KEY, PRODUCT_MAPPER.mapToProductDTO(save)))
                .build();
    }

    @Override
    public ProductResponseDto deleteProduct(Integer id) {
        Product product = findProductById(id);
        if (product != null) {
            var productName = product.getProductName();
            Inventar inventar = inventarRepository.findByProductName(productName);
            if(inventar != null) {
                inventar.setQuantity(inventar.getQuantity() - 1);
                inventarRepository.save(inventar);
            }

            productRepository.deleteById(id);
            return ProductResponseDto.builder()
                    .timestamp(Instant.now().toString())
                    .status(OK)
                    .error(null)
                    .statusCode(OK.value())
                    .path(PRODUCT_API_PATH+id)
                    .data(Map.of(PRODUCT_KEY, PRODUCT_MAPPER.mapToProductDTO(product)))
                    .build();
        }
        return productNotFoundById(id);
    }

    @Override
    public ProductResponseDto updateProduct(ProductDTO productDTO, Integer id) {
        Product findProductById = findProductById(id);
        if (findProductById != null && findProductById.getProductId().equals(id)) {
            var update = productRepository.save(PRODUCT_MAPPER.mapToProduct(productDTO));
            return ProductResponseDto.builder()
                    .timestamp(Instant.now().toString())
                    .status(OK)
                    .error(null)
                    .statusCode(OK.value())
                    .path(PRODUCT_API_PATH+id)
                    .data(Map.of(PRODUCT_KEY, PRODUCT_MAPPER.mapToProductDTO(update)))
                    .build();
        }

        return null;
    }


    private Product findProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    private ProductResponseDto productNotFoundById(Integer id) {
        return ProductResponseDto.builder()
                .timestamp(Instant.now().toString())
                .status(NOT_FOUND)
                .error("Product does not exist in the DB")
                .message("Product not found by ID: "+id)
                .statusCode(NOT_FOUND.value())
                .path(PRODUCT_API_PATH+id)
                .data(Map.of(PRODUCT_KEY, new ProductDTO()))
                .build();
    }
}
