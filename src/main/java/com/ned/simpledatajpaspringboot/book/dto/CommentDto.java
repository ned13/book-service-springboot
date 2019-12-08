package com.ned.simpledatajpaspringboot.book.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CommentDto {
  private Long createdByUserId;
  private String content = "";
  private Date createdFrom = null;

  public static final CommentDto INVALID_COMMENT_DTO = new CommentDto();
}
