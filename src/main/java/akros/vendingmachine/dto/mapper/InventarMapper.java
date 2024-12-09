package akros.vendingmachine.dto.mapper;

import akros.vendingmachine.domain.Inventar;
import akros.vendingmachine.dto.InventarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InventarMapper {

    InventarMapper INVENTAR_MAPPER = Mappers.getMapper(InventarMapper.class);

    InventarDTO mapToInventarDTO(Inventar inventar);
    Inventar mapToInventar(InventarDTO inventarDTO);
}
