package com.ipi.wikicodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "is_liked", nullable = false)
    private Boolean isLiked;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "votes", allowSetters = true)
    private User author;

    @ManyToMany(mappedBy = "ratings")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Article> articleIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsLiked() {
        return isLiked;
    }

    public Vote isLiked(Boolean isLiked) {
        this.isLiked = isLiked;
        return this;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getComment() {
        return comment;
    }

    public Vote comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getAuthor() {
        return author;
    }

    public Vote author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Set<Article> getArticleIds() {
        return articleIds;
    }

    public Vote articleIds(Set<Article> articles) {
        this.articleIds = articles;
        return this;
    }

    public Vote addArticleId(Article article) {
        this.articleIds.add(article);
        article.getRatings().add(this);
        return this;
    }

    public Vote removeArticleId(Article article) {
        this.articleIds.remove(article);
        article.getRatings().remove(this);
        return this;
    }

    public void setArticleIds(Set<Article> articles) {
        this.articleIds = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vote)) {
            return false;
        }
        return id != null && id.equals(((Vote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", isLiked='" + isIsLiked() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
