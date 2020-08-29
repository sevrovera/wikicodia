package com.ipi.wikicodia.repository;

import com.ipi.wikicodia.domain.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Article entity.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select article from Article article where article.author.login = ?#{principal.username}")
    List<Article> findByAuthorIsCurrentUser();
    
    @Query("select article from Article article where article.author.login = 'user'")
    Page<Article> findByAuthorIsCurrentUser(Pageable pageable);
    
    @Query("select article from Article article where article.isValidated = 0 and article.isPublished = 1")
    Page<Article> findByIsPublishedAndNotValidated(Pageable pageable);
    
    @Query("select article from Article article where article.isValidated = 1 and article.isPublished = 1")
    Page<Article> findByIsPublishedAndIsValidated(Pageable pageable);

    @Query(value = "select distinct article from Article article left join fetch article.ratings left join fetch article.languages left join fetch article.frameworks",
        countQuery = "select count(distinct article) from Article article")
    Page<Article> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct article from Article article left join fetch article.ratings left join fetch article.languages left join fetch article.frameworks")
    List<Article> findAllWithEagerRelationships();

    @Query("select article from Article article left join fetch article.ratings left join fetch article.languages left join fetch article.frameworks where article.id =:id")
    Optional<Article> findOneWithEagerRelationships(@Param("id") Long id);
}
