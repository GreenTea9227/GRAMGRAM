package com.ll.gramgram.boundedContext.likeablePerson.entity.dto;

import com.ll.gramgram.standard.util.Ut;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikeableSearchDto {

    private LocalDateTime createDate;
    private Long fromInstaMemberId; // 호감을 표시한 사람(인스타 멤버)
    private Long toInstaMemberId; // 호감을 표시한 사람(인스타 멤버)

    private String genderDisplayName;
    private String genderDisplayNameWithIcon;

    private int attractiveTypeCode;

    @QueryProjection
    public LikeableSearchDto(LocalDateTime createDate, Long fromInstaMemberId, Long toInstaMemberId,
                             String genderDisplayName, String genderDisplayNameWithIcon, int attractiveTypeCode) {
        this.createDate = createDate;
        this.fromInstaMemberId = fromInstaMemberId;
        this.toInstaMemberId = toInstaMemberId;
        this.genderDisplayName = genderDisplayName;
        this.genderDisplayNameWithIcon = genderDisplayNameWithIcon;
        this.attractiveTypeCode = attractiveTypeCode;
    }

    public String getAttractiveTypeDisplayName() {
        return switch (attractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public String getAttractiveTypeDisplayNameWithIcon() {
        return switch (attractiveTypeCode) {
            case 1 -> "<i class=\"fa-solid fa-person-rays\"></i>";
            case 2 -> "<i class=\"fa-regular fa-face-smile\"></i>";
            default -> "<i class=\"fa-solid fa-people-roof\"></i>";
        } + "&nbsp;" + getAttractiveTypeDisplayName();
    }

    public String getJdenticon() {
        return Ut.hash.sha256(fromInstaMemberId + "_likes_" + toInstaMemberId);
    }
}
