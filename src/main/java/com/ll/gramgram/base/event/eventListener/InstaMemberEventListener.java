package com.ll.gramgram.base.event.eventListener;

import com.ll.gramgram.base.event.dto.like.EventAfterFromInstaMemberChangeGender;
import com.ll.gramgram.base.event.dto.like.EventAfterLike;
import com.ll.gramgram.base.event.dto.like.EventAfterModifyAttractiveType;
import com.ll.gramgram.base.event.dto.like.EventBeforeCancelLike;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InstaMemberEventListener {

    private final InstaMemberService instaMemberService;

    @EventListener
    @Transactional
    public void listen(EventAfterModifyAttractiveType event) {
        instaMemberService.whenAfterModifyAttractiveType(event.getLikeablePerson(), event.getOldAttractiveTypeCode());
    }

    @EventListener
    public void listen(EventAfterLike event) {
        instaMemberService.whenAfterLike(event.getLikeablePerson());
    }

    @EventListener
    public void listen(EventBeforeCancelLike event) {
        instaMemberService.whenBeforeCancelLike(event.getLikeablePerson());
    }

    @EventListener
    public void listen(EventAfterFromInstaMemberChangeGender event) {
        instaMemberService.whenAfterFromInstaMemberChangeGender(event.getInstaMember(), event.getOldGender());
    }

}
