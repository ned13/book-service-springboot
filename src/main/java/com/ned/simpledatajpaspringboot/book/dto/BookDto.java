package com.ned.simpledatajpaspringboot.book.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@With
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2", "ES_COMPARING_PARAMETER_STRING_WITH_EQ", "RC_REF_COMPARISON"})
public class BookDto {
  private Long id;
  private String name;
  private Date publishDate;
  private String contactEmail;

  @Singular private List<CommentDto> comments = new ArrayList<CommentDto>();

  public static final BookDto INVALID_BOOKDTO = new BookDto();

  public BookDto(Long id, String name, Date publishDate, String contactEmail) {
    this.id = id;
    this.name = name;
    this.publishDate = publishDate;
    this.contactEmail = contactEmail;
  }
}
