package ch.meineinitiative.service.mapper;

import ch.meineinitiative.domain.*;
import ch.meineinitiative.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {InitiativeMapper.class, UserMapper.class, })
public interface CommentMapper extends EntityMapper <CommentDTO, Comment> {

    @Mapping(source = "initiative.id", target = "initiativeId")

    @Mapping(source = "creator.id", target = "creatorId")
    CommentDTO toDto(Comment comment); 

    @Mapping(source = "initiativeId", target = "initiative")

    @Mapping(source = "creatorId", target = "creator")
    Comment toEntity(CommentDTO commentDTO); 
    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
