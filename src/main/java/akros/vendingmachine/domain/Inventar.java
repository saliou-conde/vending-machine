package akros.vendingmachine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventar {

    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String productName;
    private Integer quantity;

//    @OneToMany(mappedBy = "inventar")
//    private List<Product> product;
}
