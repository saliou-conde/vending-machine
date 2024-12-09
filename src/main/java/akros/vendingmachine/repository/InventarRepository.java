package akros.vendingmachine.repository;

import akros.vendingmachine.domain.Inventar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarRepository extends JpaRepository<Inventar, String> {

    Inventar findByProductName(String productName);

}
