package com.ned.simpledatajpaspringboot.book.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"})
public class Comment {
  private Long createdByUserId = 0L;
  private String content = "";
  private Instant createdFrom = null;

  public static final Comment INVALID_COMMENT = new Comment();
}
