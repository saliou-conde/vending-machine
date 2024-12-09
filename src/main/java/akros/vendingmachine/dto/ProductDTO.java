package akros.vendingmachine.dto;

import akros.vendingmachine.domain.Inventar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private Inventar inventar;
}
