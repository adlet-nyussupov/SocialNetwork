package com.adlet.SocialNetwork.repository;


import com.adlet.SocialNetwork.dto.SocialNetworkPostDto;
import com.adlet.SocialNetwork.entity.SocialNetworkPostEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SocialNetworkPostRepository extends PagingAndSortingRepository<SocialNetworkPostEntity, Long> {

    @Query("SELECT p FROM Posts p WHERE p.postId = ?1")
    SocialNetworkPostEntity findByPostId(String postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Posts p WHERE p.postId = ?1")
    int deleteByPostId(String postId);

    @Transactional
    @Modifying
    @Query("UPDATE Posts p SET p.viewCount = p.viewCount+1 WHERE p.postId = ?1")
    int increaseViewCountByPostId(String postId);

}
