package com.ll.gramgram.base.event.dto.notification;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.NotificationStatus;
import lombok.Getter;

@Getter
public class NotificationEventDto {

    private LikeablePerson likeablePerson;
    private int newAttractiveTypecode;
    private NotificationStatus status;

    public NotificationEventDto(LikeablePerson likeablePerson, NotificationStatus status) {
        this.likeablePerson = likeablePerson;
        this.status = status;
    }

    public NotificationEventDto(LikeablePerson likeablePerson, int newAttractiveTypecode, NotificationStatus status) {
        this.likeablePerson = likeablePerson;
        this.newAttractiveTypecode = newAttractiveTypecode;
        this.status = status;
    }
}
