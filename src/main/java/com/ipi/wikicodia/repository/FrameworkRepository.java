package com.ipi.wikicodia.repository;

import com.ipi.wikicodia.domain.Framework;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Framework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrameworkRepository extends JpaRepository<Framework, Long> {
}
