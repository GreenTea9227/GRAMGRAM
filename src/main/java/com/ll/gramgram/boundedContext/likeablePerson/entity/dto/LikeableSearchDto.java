package com.ll.gramgram.boundedContext.likeablePerson.entity.dto;

import com.ll.gramgram.standard.util.Ut;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikeableSearchDto {

    private LocalDateTime createDate;
    private Long fromInstaMemberId; // 호감을 표시한 사람(인스타 멤버)
    private Long toInstaMemberId; // 호감을 표시한 사람(인스타 멤버)
    private String gender;
    private int attractiveTypeCode;
    private Long likes;


    public LikeableSearchDto(LocalDateTime createDate, Long fromInstaMemberId,
                             Long toInstaMemberId, String gender, int attractiveTypeCode,Long likes) {
        this.createDate = createDate;
        this.fromInstaMemberId = fromInstaMemberId;
        this.toInstaMemberId = toInstaMemberId;
        this.gender = gender;
        this.attractiveTypeCode = attractiveTypeCode;
        this.likes =likes;
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

    public String getGenderDisplayName() {
        return switch (gender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public String getGenderDisplayNameWithIcon() {
        return switch (gender) {
            case "W" -> "<i class=\"fa-solid fa-person-dress\"></i>";
            default -> "<i class=\"fa-solid fa-person\"></i>";
        } + "&nbsp;" + getGenderDisplayName();
    }
}
