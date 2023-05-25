package com.ll.gramgram.boundedContext.notification.repository;

import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

}
