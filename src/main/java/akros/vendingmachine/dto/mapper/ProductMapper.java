package akros.vendingmachine.dto.mapper;

import akros.vendingmachine.domain.Product;
import akros.vendingmachine.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);

    Product mapToProduct(ProductDTO productDTO);

    ProductDTO mapToProductDTO(Product product);
}
