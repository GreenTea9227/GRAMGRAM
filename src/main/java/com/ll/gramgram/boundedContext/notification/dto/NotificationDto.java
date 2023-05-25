package com.ll.gramgram.boundedContext.notification.dto;

import lombok.Getter;


@Getter
public class NotificationDto {

    private final String message;
    private final Long differDate;

    public NotificationDto(String message, Long differDate) {
        this.message = message;
        this.differDate = differDate;
    }
}
