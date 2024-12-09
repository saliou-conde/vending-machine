package akros.vendingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarDTO {

    private String id;
    private String productName;
    private Integer quantity;
    //private List<Product> product;
}
