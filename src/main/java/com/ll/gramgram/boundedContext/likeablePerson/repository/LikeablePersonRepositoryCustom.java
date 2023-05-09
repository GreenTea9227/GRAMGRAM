package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchCondition;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchDto;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepositoryCustom {
    Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String toInstaMemberUsername);

    List<LikeableSearchDto> findByCondition(Long id, LikeableSearchCondition condition);
}
