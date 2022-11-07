package com.adlet.SocialNetwork.service;

import com.adlet.SocialNetwork.dto.SocialNetworkPostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

public interface SocialNetworkPostService {

    public SocialNetworkPostDto createPost(SocialNetworkPostDto postDto);
    public SocialNetworkPostDto getPostById(String postId);
    public int increasePostViewCount(String postId);
    public List<SocialNetworkPostDto> getPostsByHighestView(int page, int limit);
    public int deletePostById(String postId);



}
