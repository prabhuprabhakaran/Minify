/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.entity;

import com.github.prabhuprabhakaran.minify.utils.Utils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Entity
@Table(name = "urls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class URLEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    @Size(max = 2147483647)
    @Column(name = "url")
    String url;
    @Column(name = "urlhashcode")
    Integer urlHashCode;
    @Size(max = 2147483647)
    @Column(name = "shortenurl")
    String shortenurl;
    @Size(max = 2147483647)
    @Column(name = "active")
    String active;
    @Size(max = 2147483647)
    @Column(name = "createdby")
    String createdBy;
    @Size(max = 2147483647)
    @Column(name = "updatedby")
    String updatedBy;
    @Column(name = "createdat")
    Timestamp createdAt;
    @Column(name = "updatedat")
    Timestamp updatedAt;
    @Version
    private Integer version;

    @PrePersist
    private void prePersistFunction() {
        active = "Y";
        createdBy = Utils.getUserPrincipal();
        createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void preUpdateFunction() {
        updatedBy = Utils.getUserPrincipal();
        updatedAt = Timestamp.from(Instant.now());
    }

    public HashMap getReturn() {
        HashMap lHashMap = new HashMap();
        lHashMap.put("url", url);
        lHashMap.put("shorentUrl", shortenurl);
        lHashMap.put("user", createdBy);
        return lHashMap;
    }
}
