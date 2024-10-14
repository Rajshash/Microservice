package com.comment.service;

import com.comment.config.RestTemplateConfig;
import com.comment.entity.Comment;
import com.comment.exception.ResourceNotFoundException;
import com.comment.payload.CommentDto;

import com.comment.repository.CommentRepository;
import com.post.entity.Post;
import com.post.payload.PostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    private RestTemplateConfig restTemplate;





    public CommentServiceImpl(CommentRepository commentRepository, RestTemplate restTemplate, RestTemplateConfig restTemplate1) {
        this.commentRepository = commentRepository;

        this.restTemplate = restTemplate1;


    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Post post1 = restTemplate.getRestTemplate().getForObject("http://localhost:8081/api/v1/post/" + commentDto.getPostId(), Post.class);

        if (post1 != null) {
            // If the post exists, map DTO to entity and save
            Comment comment = mapToEntity(commentDto);
            String commentId = UUID.randomUUID().toString();
            comment.setId(UUID.fromString(commentId));


            // Save the comment to the database
            Comment savedComment = commentRepository.save(comment);

            // Convert saved entity back to DTO
            return mapToDto(savedComment);
        } else {
            // Return a message or throw an exception if the post does not exist
            throw new ResourceNotFoundException("Post not found with ID: " + commentDto.getPostId());
        }

    }

    @Override
    public List<CommentDto> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        // Check if comments are retrieved successfully
        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found for Post ID: " + postId);
        }

        // Map the list of Comment entities to CommentDto
        return comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        comment.setPostId(commentDto.getPostId());
        return comment;
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        commentDto.setPostId(comment.getPostId());
        return commentDto;
    }
}
