package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchCondition;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember.instaMember;
import static com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.likeablePerson;

@RequiredArgsConstructor
public class LikeablePersonRepositoryImpl implements LikeablePersonRepositoryCustom {
    public static final long LIKE_LIMIT_COUNT = 10L;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String toInstaMemberUsername) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(likeablePerson)
                        .where(
                                likeablePerson.fromInstaMember.id.eq(fromInstaMemberId)
                                        .and(
                                                likeablePerson.toInstaMember.username.eq(toInstaMemberUsername)
                                        )
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<LikeableSearchDto> findByCondition(Long instaId, LikeableSearchCondition condition) {

        return jpaQueryFactory
                .select(Projections.constructor(LikeableSearchDto.class,
                        likeablePerson.createDate, likeablePerson.fromInstaMember.id, likeablePerson.toInstaMember.id,
                        likeablePerson.fromInstaMember.gender, likeablePerson.attractiveTypeCode))
                .from(likeablePerson)
                .join(likeablePerson.fromInstaMember, instaMember)
                .where(equalId(instaId),
                        equalGender(condition.getGender()),
                        equalAttractiveTypeCode(condition.getAttractiveTypeCode()))
                .orderBy(sort(condition.getSortCode()))
                .limit(LIKE_LIMIT_COUNT)
                .fetch();
    }

    private BooleanExpression equalId(Long id) {
        return id != null ? likeablePerson.toInstaMember.id.eq(id) : null;
    }

    private BooleanExpression equalGender(String gender) {
        return StringUtils.hasText(gender) ? instaMember.gender.eq(gender) : null;
    }

    private BooleanExpression equalAttractiveTypeCode(Integer code) {
        return code != null ? likeablePerson.attractiveTypeCode.eq(code) : null;
    }

    private OrderSpecifier<?>[] sort(Integer code) {
        if (code == null)
            return new OrderSpecifier[]{};

        List<OrderSpecifier<?>> list = new ArrayList<>();
        switch (code) {
            case 2 -> list.add(likeablePerson.createDate.asc());
            case 3 -> list.add(likeablePerson.fromInstaMember.likes.desc());
            case 4 -> list.add(likeablePerson.fromInstaMember.likes.asc());
            case 5 -> {
                list.add(new CaseBuilder()
                        .when(likeablePerson.fromInstaMember.gender.eq("W")).then(1)
                        .otherwise(2)
                        .asc());
                list.add(likeablePerson.createDate.desc());
            }
            case 6 -> {
                list.add(likeablePerson.attractiveTypeCode.asc());
                list.add(likeablePerson.createDate.desc());
            }
            default -> list.add(likeablePerson.createDate.desc());
        }

        return list.toArray(new OrderSpecifier[0]);

    }
}
