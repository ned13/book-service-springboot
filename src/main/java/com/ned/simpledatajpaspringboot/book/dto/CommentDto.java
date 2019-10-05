package com.ned.simpledatajpaspringboot.book.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Long createdByUserId;
    private String content = "";
    private Date createdFrom = null;

    public static final CommentDto INVALID_COMMENT_DTO = new CommentDto();

}