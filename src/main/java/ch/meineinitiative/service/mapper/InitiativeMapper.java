package ch.meineinitiative.service.mapper;

import ch.meineinitiative.domain.*;
import ch.meineinitiative.service.dto.InitiativeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Initiative and its DTO InitiativeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class, })
public interface InitiativeMapper extends EntityMapper <InitiativeDTO, Initiative> {

    InitiativeDTO toDto(Initiative initiative);

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
