package ch.meineinitiative.service.mapper;

import ch.meineinitiative.domain.*;
import ch.meineinitiative.service.dto.InitiativeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Initiative and its DTO InitiativeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface InitiativeMapper extends EntityMapper <InitiativeDTO, Initiative> {

    @Mapping(source = "initiator.id", target = "initiatorId")
    InitiativeDTO toDto(Initiative initiative); 

    @Mapping(source = "initiatorId", target = "initiator")
    Initiative toEntity(InitiativeDTO initiativeDTO); 
    default Initiative fromId(Long id) {
        if (id == null) {
            return null;
        }
        Initiative initiative = new Initiative();
        initiative.setId(id);
        return initiative;
    }
}
