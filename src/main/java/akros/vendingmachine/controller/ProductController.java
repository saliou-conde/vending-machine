package akros.vendingmachine.controller;

import akros.vendingmachine.dto.ProductDTO;
import akros.vendingmachine.dto.ProductResponseDto;
import akros.vendingmachine.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductDTO productDTO) {
        var save = productService.createProduct(productDTO);
        return ResponseEntity.ok(save);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("id") Integer id) {
        return  ResponseEntity.ok(productService.getProduct(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> deleteProductById(@PathVariable("id") Integer id) {
        return  ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable("id")Integer id) {
        return  ResponseEntity.ok(productService.updateProduct(productDTO, id));
    }

}
