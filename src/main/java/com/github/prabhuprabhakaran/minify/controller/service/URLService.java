/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller.service;

import com.github.prabhuprabhakaran.minify.entity.URLEntity;
import com.github.prabhuprabhakaran.minify.entity.Users;
import com.github.prabhuprabhakaran.minify.repositories.URLRepository;
import com.github.prabhuprabhakaran.minify.repositories.UserRepository;
import com.github.prabhuprabhakaran.minify.utils.MyEncoder;
import com.github.prabhuprabhakaran.minify.utils.Utils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Service
public class URLService {

    @Autowired
    URLRepository uRLRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MyEncoder encoder;

    public boolean addURLEntity(URLEntity entity) {
        if (ObjectUtils.isEmpty(entity.getUrl())) {
            return false;
        }
        int hashCode = entity.getUrl().hashCode();
        int encodingHashCode = entity.getUrl().hashCode();
        boolean addRandom = false;
        entity.setUrlHashCode(hashCode);
        Users lUser = Utils.getUserPrincipalObject();
        if (lUser == null) {
            lUser = userRepository.findByToken(Utils.getUserPrincipal()).get();
        }
        List<URLEntity> lURL = uRLRepository.findByUrlHashCodeAndCreatedBy(hashCode, lUser.getUsername());
        if (!lURL.isEmpty()) {
            addRandom = true;
            for (URLEntity uRLEntity : lURL) {
                if (uRLEntity.getUrl().equals(entity.getUrl())) {
                    return false;
                }
            }
        }
        entity.setShortenurl(encoder.encode(encodingHashCode, lUser.getId().intValue(), addRandom));
        uRLRepository.save(entity);
        return true;
    }

    public boolean deleteURLEntity(Integer id) {
        uRLRepository.deleteById(id.longValue());
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
