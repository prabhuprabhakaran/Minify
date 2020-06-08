/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.repositories;

import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Repository
public interface URLRepository extends JpaRepository<URLEntity, Long> {

    public Optional<URLEntity> findByUrlHashCodeAndCreatedBy(Integer hashcode, String createdBy);

    public List<URLEntity> findByCreatedBy(String createdBy);

    public Optional<URLEntity> findByShortenurlAndCreatedBy(String shortenurl, String createdBy);
}
