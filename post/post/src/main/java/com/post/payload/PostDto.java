package com.post.payload;

import lombok.Data;

import java.util.List;

@Data
public class PostDto {

//    private String id;
    private String title;
    private String content;
    private String description;

    // Add a list of comments
    private List<CommentDto> comments;
}
