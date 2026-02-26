package com.akjae.web3discussion.repository;

import com.akjae.web3discussion.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySlug(String slug);
    Optional<Tag> findByName(String name);
    boolean existsByName(String name);
    List<Tag> findByNameIn(List<String> names);
}
