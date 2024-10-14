package com.post.service;

import com.post.entity.Post;
import com.post.payload.CommentDto;
import com.post.payload.PostDto;
import com.post.repository.PostRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PostserviceImpl implements PostService{

    private PostRepository postRepository;
    private RestTemplate restTemplate;  // Assuming RestTemplate is already configured and injected

    public PostserviceImpl(PostRepository postRepository, RestTemplate restTemplate) {
        this.postRepository = postRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        String postId = UUID.randomUUID().toString();

        Post entity = mapToEntity(postDto);
        entity.setId(postId);
        Post saved = postRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public PostDto getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        return mapToDto(post);
    }

    @Override
    public PostDto getPostWithComments(String postId) {

        PostDto postDto = getPostById(postId);  // Fetch the post details
        String commentServiceUrl = "http://COMMENT-SERVICE/api/v1/comment/" + postId ;

        try {
            ResponseEntity<CommentDto[]> response = restTemplate.getForEntity(commentServiceUrl, CommentDto[].class);
            CommentDto[] commentArray = response.getBody();
            if (commentArray != null && commentArray.length > 0) {
                List<CommentDto> comments = Arrays.asList(commentArray);  // Convert array to List
                postDto.setComments(comments);  // Set comments in PostDto
            } else {
                postDto.setComments(new ArrayList<>());  // Set empty list if no comments found
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching comments from Comment service", e);
        }
        return postDto;  // Return PostDto with comments
    }

    public Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        return post;
    }

    public PostDto mapToDto(Post post){
        PostDto dto = new PostDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDescription(post.getDescription());

        return dto;
    }
}
