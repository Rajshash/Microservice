package com.post.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommentDto {
    private String id;
    private String name;
    private String email;
    private String body;
    private String postId;
}
