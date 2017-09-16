package ch.meineinitiative.domain;

import ch.meineinitiative.domain.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Initiative.
 */
@Entity
@Table(name = "initiative")
public class Initiative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "initiative", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @Column(name = "tag")
    private String tag;

    @ManyToOne
    private User initiator;

    @ManyToMany
    @JoinTable(name = "initiative_citizen_supporters",
        joinColumns = @JoinColumn(name = "initiatives_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "citizen_supporters_id", referencedColumnName = "id"))
    private Set<User> citizenSupporters = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "initiative_politician_supporters",
        joinColumns = @JoinColumn(name = "initiatives_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "politician_supporters_id", referencedColumnName = "id"))
    private Set<User> politicianSupporters = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Initiative title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public Initiative text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public Initiative status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Initiative creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Initiative comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Initiative addComments(Comment comment) {
        this.comments.add(comment);
        comment.setInitiative(this);
        return this;
    }

    public Initiative removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setInitiative(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public User getInitiator() {
        return initiator;
    }

    public Initiative initiator(User user) {
        this.initiator = user;
        return this;
    }

    public void setInitiator(User user) {
        this.initiator = user;
    }

    public Set<User> getCitizenSupporters() {
        return citizenSupporters;
    }

    public Initiative citizenSupporters(Set<User> users) {
        this.citizenSupporters = users;
        return this;
    }

    public Initiative addCitizenSupporters(User user) {
        this.citizenSupporters.add(user);
        return this;
    }

    public Initiative removeCitizenSupporters(User user) {
        this.citizenSupporters.remove(user);
        return this;
    }

    public void setCitizenSupporters(Set<User> users) {
        this.citizenSupporters = users;
    }

    public Set<User> getPoliticianSupporters() {
        return politicianSupporters;
    }

    public Initiative politicianSupporters(Set<User> users) {
        this.politicianSupporters = users;
        return this;
    }

    public Initiative addPoliticianSupporters(User user) {
        this.politicianSupporters.add(user);
        return this;
    }

    public Initiative removePoliticianSupporters(User user) {
        this.politicianSupporters.remove(user);
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPoliticianSupporters(Set<User> users) {
        this.politicianSupporters = users;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Initiative initiative = (Initiative) o;
        if (initiative.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), initiative.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Initiative{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", text='" + getText() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
