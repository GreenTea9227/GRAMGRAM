package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchCondition;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.OrderSpecifier;
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
    public List<LikeablePerson> findByCondition(Long id, LikeableSearchCondition condition) {
        //toinsta가 == id
        QInstaMember toInstaMember = new QInstaMember("toInstaMember");

        return jpaQueryFactory
                .selectFrom(likeablePerson)
                .leftJoin(likeablePerson.toInstaMember, toInstaMember)
                .leftJoin(likeablePerson.fromInstaMember, instaMember)
                .where(getId(id, toInstaMember),
                        getGender(condition.getGender(), instaMember),
                        getAttractiveTypeCode(condition.getAttractiveTypeCode()))
                .orderBy(sort(condition.getSortCode()))
                .limit(10L)
                .fetch();

    }

    private  BooleanExpression getId(Long id, QInstaMember toInstaMember) {
        return id != null ? toInstaMember.id.eq(id) : null;
    }

    private  BooleanExpression getGender(String gender, QInstaMember toInstaMember) {
        return StringUtils.hasText(gender) ? toInstaMember.gender.eq(gender) : null;
    }

    private  BooleanExpression getAttractiveTypeCode(Integer code) {
        return code != null ? likeablePerson.attractiveTypeCode.eq(code) : null;
    }

    private OrderSpecifier[] sort(Integer code) {
        if (code == null)
            return new OrderSpecifier[]{};

        switch (code) {
            case 2 -> {
                return new OrderSpecifier[]{likeablePerson.modifyDate.desc()};
            }
            case 3 -> {
                return new OrderSpecifier[]{likeablePerson.fromInstaMember.toLikeablePeople.size().desc()};
            }
            case 4 -> {
                return new OrderSpecifier[]{likeablePerson.fromInstaMember.toLikeablePeople.size().asc()};
            }
            case 5 -> {
                return new OrderSpecifier[]{new CaseBuilder()
                        .when(likeablePerson.fromInstaMember.gender.eq("W")).then(1)
                        .otherwise(2)
                        .asc(),likeablePerson.modifyDate.desc()};
            }
            case 6 -> {
                return new OrderSpecifier[]{likeablePerson.attractiveTypeCode.asc(),
                        likeablePerson.modifyDate.desc()};
            }
            default -> {
                return new OrderSpecifier[]{likeablePerson.modifyDate.asc()};
            }
        }
    }
}
