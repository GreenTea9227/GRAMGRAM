package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.notification.NotificationStatus;
import com.ll.gramgram.boundedContext.notification.dto.NotificationDto;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationJpaRepository;
import com.ll.gramgram.boundedContext.notification.repository.NotificationQueryRepository;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationQueryRepository queryRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMemberOrderByIdDesc(toInstaMember);
    }

    @Transactional
    public RsData<Notification> makeLike(LikeablePerson likeablePerson) {
        return make(likeablePerson, "LIKE", 0, null);
    }

    @Transactional
    public RsData<Notification> makeModifyAttractive(LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        return make(likeablePerson, "MODIFY_ATTRACTIVE_TYPE", oldAttractiveTypeCode, likeablePerson.getFromInstaMember().getGender());
    }

    private RsData<Notification> make(LikeablePerson likeablePerson, String typeCode, int oldAttractiveTypeCode, String oldGender) {
        Notification notification = Notification
                .builder()
                .typeCode(typeCode)
                .toInstaMember(likeablePerson.getToInstaMember())
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .oldGender(oldGender)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .newGender(likeablePerson.getFromInstaMember().getGender())
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }

    public List<Notification> findByToInstaMember_username(String username) {
        return notificationRepository.findByToInstaMember_usernameOrderByIdDesc(username);
    }

    @Transactional
    public RsData markAsRead(List<Notification> notifications) {
        notifications
                .stream()
                .filter(notification -> !notification.isRead())
                .forEach(Notification::markAsRead);

        return RsData.of("S-1", "읽음 처리 되었습니다.");
    }

    public boolean countUnreadNotificationsByToInstaMember(InstaMember instaMember) {
        return notificationRepository.countByToInstaMemberAndReadDateIsNull(instaMember) > 0;
    }

    public List<NotificationDto> findByMember(Member member) {
        List<Notification> findList = queryRepository.findByMember(member.getInstaMember());
        return readNotification(member, findList);
    }

    private static List<NotificationDto> readNotification(Member member, List<Notification> findList) {
        member.changeMessageStatus(false);
        findList.stream().filter(i -> i.getReadDate() == null).forEach(Notification::changeReadDate);
        return getDtoList(findList);
    }

    private static List<NotificationDto> getDtoList(List<Notification> findList) {

        return findList.stream()
                .map(i -> new NotificationDto(i.getMessage(), ChronoUnit.MINUTES.between(i.getCreateDate(), LocalDateTime.now())))
                .toList();
    }

    public void createNotification(LikeablePerson likeablePerson, NotificationStatus status) {

        InstaMember toInstaMember = likeablePerson.getToInstaMember();
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        String attractive = likeablePerson.getAttractiveTypeDisplayName();
        String username = fromInstaMember.getUsername();

        String message = "";

        switch (status) {
            case CREATE ->
                    message = "%s가 당신을 %s 이유로 좋아합니다.".formatted(username, attractive);
            case CHANGE ->
                    message = "%s가 좋아요를 취소하였습니다.".formatted(username);
            case DELETE ->
                    message = "%s가 당신을 좋아하는 이유를 %s로 변경하였습니다.".formatted(username, attractive);
        }

        notificationJpaRepository.save(Notification
                .builder()
                .fromInstaMember(fromInstaMember)
                .toInstaMember(toInstaMember)
                .newTypeCode(attractive)
                .readDate(LocalDateTime.now())
                .message(message)
                .build());

    }
}
