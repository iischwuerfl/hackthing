package ch.meineinitiative.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import ch.meineinitiative.domain.enumeration.Status;

/**
 * A DTO for the Initiative entity.
 */
public class InitiativeDTO implements Serializable {

    private Long id;

    private String title;

    private String text;

    private Status status;

    private ZonedDateTime creationDate;

    private Long initiatorId;

    private Set<UserDTO> citizenSupporters = new HashSet<>();

    private Set<UserDTO> politicianSupporters = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long userId) {
        this.initiatorId = userId;
    }

    public Set<UserDTO> getCitizenSupporters() {
        return citizenSupporters;
    }

    public void setCitizenSupporters(Set<UserDTO> users) {
        this.citizenSupporters = users;
    }

    public Set<UserDTO> getPoliticianSupporters() {
        return politicianSupporters;
    }

    public void setPoliticianSupporters(Set<UserDTO> users) {
        this.politicianSupporters = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InitiativeDTO initiativeDTO = (InitiativeDTO) o;
        if(initiativeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), initiativeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InitiativeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
