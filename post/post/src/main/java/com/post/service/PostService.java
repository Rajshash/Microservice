package com.post.service;

import com.post.payload.PostDto;

public interface PostService {

    public PostDto createPost(PostDto postDto);

    public PostDto getPostById(String postId);

    public PostDto getPostWithComments(String postId);
}
