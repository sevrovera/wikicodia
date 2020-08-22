package com.ipi.wikicodia.repository;

import com.ipi.wikicodia.domain.Vote;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Vote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select vote from Vote vote where vote.author.login = ?#{principal.username}")
    List<Vote> findByAuthorIsCurrentUser();
}
