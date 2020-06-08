/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller.service;

import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import com.github.prabhuprabhakaran.minify.repositories.URLRepository;
import com.github.prabhuprabhakaran.minify.utils.MyEncoder;
import com.github.prabhuprabhakaran.minify.utils.Utils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Service
public class URLService {

    @Autowired
    URLRepository uRLRepository;
    @Autowired
    MyEncoder encoder;

    public boolean addURLEntity(URLEntity entity) {
        int hashCode = entity.getUrl().hashCode();
        entity.setUrlHashCode(hashCode);
        Optional<URLEntity> lURL = uRLRepository.findByUrlHashCodeAndCreatedBy(hashCode, Utils.getUserPrincipal());
        if (lURL.isPresent()) {
            return false;
        }
        entity.setShortenurl(encoder.encode(hashCode));
        uRLRepository.save(entity);
        return true;
    }

    public boolean deleteURLEntity(Long id) {
        uRLRepository.deleteById(id);
        return true;
    }

    public Optional<URLEntity> getURLEntity(String key) {
        return uRLRepository.findByShortenurl(key);
    }

    public List<URLEntity> ListURLEntity() {
        List<URLEntity> lURL = uRLRepository.findByCreatedBy(Utils.getUserPrincipal());
        return lURL;
    }

}
