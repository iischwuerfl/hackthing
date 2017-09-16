package ch.meineinitiative.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Commentar entity.
 */
public class CommentarDTO implements Serializable {

    private Long id;

    private String text;

    private ZonedDateTime creationDate;

    private Long creatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentarDTO commentarDTO = (CommentarDTO) o;
        if(commentarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentarDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
