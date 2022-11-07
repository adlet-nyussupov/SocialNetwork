package com.adlet.SocialNetwork.service.impl;

import com.adlet.SocialNetwork.dto.SocialNetworkPostDto;
import com.adlet.SocialNetwork.entity.SocialNetworkPostEntity;
import com.adlet.SocialNetwork.repository.SocialNetworkPostRepository;
import com.adlet.SocialNetwork.service.SocialNetworkPostService;
import com.adlet.SocialNetwork.shared.Utils;
import com.adlet.SocialNetwork.exception.SocialNetworkPostServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SocialNetworkPostServiceImpl implements SocialNetworkPostService {

    @Autowired
    SocialNetworkPostRepository postRepository;

    @Autowired
    Utils utils;

    @Override
    public SocialNetworkPostDto createPost(SocialNetworkPostDto postDto) {

        ModelMapper modelMapper = new ModelMapper();
        SocialNetworkPostEntity postEntity = modelMapper.map(postDto, SocialNetworkPostEntity.class);

        String publicPostId = utils.generateId(30);
        postEntity.setPostId(publicPostId);

        postEntity.setPostDate(new Date());
        postEntity.setContent(postDto.getContent());
        postEntity.setViewCount(0);

        SocialNetworkPostEntity storedPostDetails = postRepository.save(postEntity);

        SocialNetworkPostDto returnPostDto = modelMapper.map(storedPostDetails, SocialNetworkPostDto.class);

        return returnPostDto;
    }

    @Override
    public SocialNetworkPostDto getPostById(String postId) {

        ModelMapper modelMapper = new ModelMapper();
        SocialNetworkPostEntity postEntity = postRepository.findByPostId(postId);
        if(postEntity == null || postEntity.getPostId() == null) return null;
        return modelMapper.map(postEntity, SocialNetworkPostDto.class);
    }

    @Override
    public int increasePostViewCount(String postId) {

        int res = postRepository.increaseViewCountByPostId(postId);
        return res;
    }

    @Override
    public List<SocialNetworkPostDto> getPostsByHighestView(int page, int limit) {

        ModelMapper mapper = new ModelMapper();
        List<SocialNetworkPostDto> returnValue = new ArrayList<>();

        if (page > 0) page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("viewCount").descending());
        Page<SocialNetworkPostEntity> postPage = postRepository.findAll(pageableRequest);
        List<SocialNetworkPostEntity> postsList = postPage.getContent();

        for (SocialNetworkPostEntity postEntity : postsList) {
            SocialNetworkPostDto postDto = mapper.map(postEntity, SocialNetworkPostDto.class);
            returnValue.add(postDto);
        }

        return returnValue;
    }

    @Override
    public int deletePostById(String postId) {
        int res = postRepository.deleteByPostId(postId);
        return res;
    }


}
