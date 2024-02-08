package com.practice.deploypractice.web.dto;

import com.practice.deploypractice.domain.posts.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
