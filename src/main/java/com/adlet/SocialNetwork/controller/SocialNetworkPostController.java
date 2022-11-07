package com.adlet.SocialNetwork.controller;

import com.adlet.SocialNetwork.dto.SocialNetworkPostDto;
import com.adlet.SocialNetwork.model.request.SocialNetworkPostRequestModel;
import com.adlet.SocialNetwork.model.response.SocialNetworkPostResponseModel;
import com.adlet.SocialNetwork.service.SocialNetworkPostService;
import com.adlet.SocialNetwork.exception.SocialNetworkPostServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class SocialNetworkPostController {
    @Autowired
    SocialNetworkPostService postService;

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public SocialNetworkPostResponseModel createPost(@RequestBody SocialNetworkPostRequestModel postDetails) {

        if (postDetails.getAuthor() == null || postDetails.getAuthor().isEmpty() || postDetails.getContent().isEmpty())
            throw new SocialNetworkPostServiceException("Author or content are not specified");

        ModelMapper modelMapper = new ModelMapper();
        SocialNetworkPostDto postDto = modelMapper.map(postDetails, SocialNetworkPostDto.class);

        SocialNetworkPostDto createdPost = postService.createPost(postDto);
        SocialNetworkPostResponseModel postResponse = modelMapper.map(createdPost, SocialNetworkPostResponseModel.class);
        return postResponse;
    }

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public SocialNetworkPostResponseModel getPost(@PathVariable String id) {

        SocialNetworkPostDto postDto = postService.getPostById(id);
        ModelMapper modelMapper = new ModelMapper();

        if (postDto == null) throw new SocialNetworkPostServiceException("Post was not found");
        if (postDto.getContent() == null || postDto.getContent().isEmpty())
            throw new SocialNetworkPostServiceException("Missing content error");


        return modelMapper.map(postDto, SocialNetworkPostResponseModel.class);
    }

    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity deletePost(@PathVariable String id) {
        int res = postService.deletePostById(id);
        return res > 0 ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @PatchMapping (path = "view-count/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity increasePostViewCount(@PathVariable String id) {
        int res = postService.increasePostViewCount(id);
        if(res == 0) throw new SocialNetworkPostServiceException("Views count were not increased");
        return ResponseEntity.ok().build();

    }

    @GetMapping(path = "/highest-view-count", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<SocialNetworkPostResponseModel> getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {

        List<SocialNetworkPostResponseModel> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        List<SocialNetworkPostDto> posts = postService.getPostsByHighestView(page, limit);

        if (posts.isEmpty())
            throw new SocialNetworkPostServiceException("Posts were not found");

        for (SocialNetworkPostDto postDto : posts) {
            SocialNetworkPostResponseModel socialNetworkPostResponseModel = modelMapper.map(postDto,
                    SocialNetworkPostResponseModel.class);
            returnValue.add(socialNetworkPostResponseModel);
        }

        return returnValue;

    }

}
