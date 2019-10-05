package com.ned.simpledatajpaspringboot.book.domain;

import java.time.Instant;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Value
@NoArgsConstructor
@Builder(toBuilder = true)
@With
@Slf4j
public class Comment {
    private Long createdByUserId = 0L;
    private String content = "";
    private Instant createdFrom = null;

    public static final Comment INVALID_COMMENT = new Comment();
}