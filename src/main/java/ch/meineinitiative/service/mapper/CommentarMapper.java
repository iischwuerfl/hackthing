package ch.meineinitiative.service.mapper;

import ch.meineinitiative.domain.*;
import ch.meineinitiative.service.dto.CommentarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Commentar and its DTO CommentarDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CommentarMapper extends EntityMapper <CommentarDTO, Commentar> {

    @Mapping(source = "creator.id", target = "creatorId")
    CommentarDTO toDto(Commentar commentar); 

    @Mapping(source = "creatorId", target = "creator")
    Commentar toEntity(CommentarDTO commentarDTO); 
    default Commentar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commentar commentar = new Commentar();
        commentar.setId(id);
        return commentar;
    }
}
