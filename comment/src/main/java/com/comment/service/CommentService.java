package com.comment.service;

import com.comment.payload.CommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto createComment(CommentDto commentDto);
    public List<CommentDto> getCommentsByPostId(String postId);
}
