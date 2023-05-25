package com.ll.gramgram.boundedContext.notification.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember.instaMember;
import static com.ll.gramgram.boundedContext.notification.entity.QNotification.notification;

@Transactional
@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;


    public List<Notification> findByMember(InstaMember instaUser) {

        return queryFactory
                .selectFrom(notification)
                .join(notification.toInstaMember, instaMember)
                .on(getEqualInsta(instaUser.getId()))
                .orderBy(notification.createDate.desc())
                .limit(10)
                .fetch();
    }

    private static BooleanExpression getEqualInsta(Long instaId) {
        return instaMember.id.eq(instaId);
    }


}
