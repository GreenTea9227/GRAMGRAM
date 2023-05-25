package com.ll.gramgram.base.event.eventListener;

import com.ll.gramgram.base.event.dto.notification.NotificationEventDto;
import com.ll.gramgram.base.exceptionHandler.DataNotFoundException;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEvent {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createNotification(NotificationEventDto dto) {

        LikeablePerson likeablePerson = dto.getLikeablePerson();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        notificationService.createNotification(likeablePerson,dto.getStatus());

        Member member = memberRepository.findByInstaMember(toInstaMember)
                .orElseThrow(() -> new DataNotFoundException("데이터 없음"));

        member.changeMessageStatus(true);
    }


}
