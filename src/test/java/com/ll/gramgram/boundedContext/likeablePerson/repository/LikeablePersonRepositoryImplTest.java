package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchCondition;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchDto;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
class LikeablePersonRepositoryImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    InstaMemberService instaMemberService;
    @Autowired
    LikeablePersonService likeablePersonService;

    private InstaMember user11;

    @BeforeEach
    void setUp() {
        Member memberUser1 = memberService.join("user11", "1234").getData();
        Member memberUser2 = memberService.join("user12", "1234").getData();
        Member memberUser3 = memberService.join("user13", "1234").getData();
        Member memberUser4 = memberService.join("user14", "1234").getData();
        Member memberUser5 = memberService.join("user15", "1234").getData();


        //user11 전용 like 객체
        instaMemberService.connect(memberUser1, "insta_user11", "M");
        instaMemberService.connect(memberUser2, "insta_user12", "M");
        instaMemberService.connect(memberUser3, "insta_user13", "W");
        instaMemberService.connect(memberUser4, "insta_user14", "M");
        instaMemberService.connect(memberUser5, "insta_user15", "W");

        //user11을 좋아하는
        LikeablePerson like11 = likeablePersonService.like(memberUser2, "insta_user11", 1).getData();
        Ut.reflection.setFieldValue(like11, "modifyUnlockDate", ChronoUnit.HOURS.addTo(LocalDateTime.now(), -4));
        LikeablePerson like12 = likeablePersonService.like(memberUser3, "insta_user11", 1).getData();
        Ut.reflection.setFieldValue(like12, "modifyUnlockDate", ChronoUnit.HOURS.addTo(LocalDateTime.now(), -3));
        LikeablePerson like13 = likeablePersonService.like(memberUser4, "insta_user11", 2).getData();
        Ut.reflection.setFieldValue(like13, "modifyUnlockDate", ChronoUnit.HOURS.addTo(LocalDateTime.now(), -2));
        LikeablePerson like14 = likeablePersonService.like(memberUser5, "insta_user11", 3).getData();
        Ut.reflection.setFieldValue(like14, "modifyUnlockDate", ChronoUnit.HOURS.addTo(LocalDateTime.now(), -1));

        //user12를 좋아하는
        likeablePersonService.like(memberUser3, "insta_user12", 1);
        likeablePersonService.like(memberUser4, "insta_user12", 2);

        //user13을 좋아하는
        likeablePersonService.like(memberUser5, "insta_user13", 3);


        user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();
    }

    @Test
    @DisplayName("toList 조건1: 아무 조건이 없다면 전부 가져와야 한다.")
    void likeTest() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, null));

        assertThat(list).size().isEqualTo(4);
    }

    @Test
    @DisplayName("toList 조건2: 성별 필터링 기능")
    void likeTest2() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition("W", null, null));

        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(0).getGender()).isEqualTo("W");
    }

    @Test
    @DisplayName("toList 조건3: 호감 필터링 기능")
    void likeTest3() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, 1, null));

        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(0).getAttractiveTypeCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("toList 조건4: 호감 및 성별 필터링 기능")
    void likeTest4() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition("W", 1, null));

        assertThat(list).size().isEqualTo(1);

        LikeableSearchDto likeablePerson = list.get(0);
        assertThat(likeablePerson.getAttractiveTypeCode()).isEqualTo(1);
        assertThat(list.get(0).getGender()).isEqualTo("W");
    }


    @Test
    @DisplayName("toList 조건5: sort 최근 날짜 기준")
    void likeTest5() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 1));
        assertThat(list.get(0).getCreateDate()).isAfter(list.get(1).getCreateDate());
    }

    @Test
    @DisplayName("toList 조건6: sort 오래된 날짜 기준")
    void likeTest6() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 2));

        assertThat(list.get(0).getCreateDate()).isBefore(list.get(1).getCreateDate());


    }

    @Test
    @DisplayName("toList 조건7: sort 인기 많은 순")
    void likeTest7() {

        InstaMember instaUser12 = instaMemberService.findByUsername("insta_user12").orElseThrow();
        InstaMember instaUser13 = instaMemberService.findByUsername("insta_user13").orElseThrow();
        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 3));

        assertThat(list.get(0).getFromInstaMemberId()).isEqualTo(instaUser12.getId());
        assertThat(list.get(1).getFromInstaMemberId()).isEqualTo(instaUser13.getId());
    }

    @Test
    @DisplayName("toList 조건8: sort 인기 적은 순")
    void likeTest8() {
        InstaMember instaUser12 = instaMemberService.findByUsername("insta_user12").orElseThrow();
        InstaMember instaUser13 = instaMemberService.findByUsername("insta_user13").orElseThrow();
        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 4));
        int num = list.size() - 1;
        assertThat(list.get(num - 1).getFromInstaMemberId()).isEqualTo(instaUser13.getId());
        assertThat(list.get(num).getFromInstaMemberId()).isEqualTo(instaUser12.getId());

    }

    @Test
    @DisplayName("toList 조건9: sort 성별순")
    void likeTest9() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 5));
        assertThat(list.get(0).getGender()).isEqualTo("W");
        assertThat(list.get(list.size() - 1).getGender()).isEqualTo("M");
    }

    @Test
    @DisplayName("toList 조건10: sort 호감사유순")
    void likeTest10() {

        List<LikeableSearchDto> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 6));
        assertThat(list.get(0).getAttractiveTypeCode()).isEqualTo(1);
        assertThat(list.get(2).getAttractiveTypeCode()).isEqualTo(2);
        assertThat(list.get(3).getAttractiveTypeCode()).isEqualTo(3);

    }

}