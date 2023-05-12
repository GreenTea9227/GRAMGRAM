package com.ll.gramgram.boundedContext.likeablePerson.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeableSearchCondition {

    private String gender;
    private Integer attractiveTypeCode;
    private Integer sortCode;
}
