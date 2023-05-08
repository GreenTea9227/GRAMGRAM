package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.dto.LikeableSearchCondition;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;


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

    @BeforeEach
    void setUp() {
        Member memberUser1 = memberService.join("user11", "1234").getData();
        Member memberUser2 = memberService.join("user12", "1234").getData();
        Member memberUser3 = memberService.join("user13", "1234").getData();
        Member memberUser4 = memberService.join("user14", "1234").getData();
        Member memberUser5 = memberService.join("user15", "1234").getData();

        Member memberUser6 = memberService.join("user16", "1234").getData();
        Member memberUser7 = memberService.join("user17", "1234").getData();
        Member memberUser8 = memberService.join("user18", "1234").getData();

        //user11 전용 like 객체
        instaMemberService.connect(memberUser1, "insta_user11", "M");
        instaMemberService.connect(memberUser2, "insta_user12", "M");
        instaMemberService.connect(memberUser3, "insta_user13", "W");
        instaMemberService.connect(memberUser4, "insta_user14", "M");
        instaMemberService.connect(memberUser5, "insta_user15", "W");

        //인기도 순 구현을 위한 객체
        instaMemberService.connect(memberUser6, "insta_user16", "W");
        instaMemberService.connect(memberUser7, "insta_user17", "W");
        instaMemberService.connect(memberUser8, "insta_user18", "W");

        likeablePersonService.like(memberUser3,"insta_user12",1);
        likeablePersonService.like(memberUser6,"insta_user13",1);
        likeablePersonService.like(memberUser7,"insta_user13",1);


        LikeablePerson like11 = likeablePersonService.like(memberUser2, "insta_user11", 1).getData();
        Ut.reflection.setFieldValue(like11, "modifyUnlockDate", ChronoUnit.HOURS.addTo(LocalDateTime.now(),-4));
        LikeablePerson like12 = likeablePersonService.like(memberUser3, "insta_user11", 1).getData();
        Ut.reflection.setFieldValue(like12, "modifyUnlockDate",  ChronoUnit.HOURS.addTo(LocalDateTime.now(),-3));
        LikeablePerson like13 = likeablePersonService.like(memberUser4, "insta_user11", 2).getData();
        Ut.reflection.setFieldValue(like13, "modifyUnlockDate",  ChronoUnit.HOURS.addTo(LocalDateTime.now(),-2));
        LikeablePerson like14 = likeablePersonService.like(memberUser5, "insta_user11", 3).getData();
        Ut.reflection.setFieldValue(like14, "modifyUnlockDate",  ChronoUnit.HOURS.addTo(LocalDateTime.now(),-1));

    }

    @Test
    @DisplayName("toList 조건1: 아무 조건이 없다면 전부 가져와야 한다.")
    void likeTest() {

        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, null));

        assertThat(list).size().isEqualTo(4);
    }

    @Test
    @DisplayName("toList 조건2: 성별 필터링 기능")
    void likeTest2() {

        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition("W", null, null));

        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(0).getFromInstaMember().getGender()).isEqualTo("W");
    }

    @Test
    @DisplayName("toList 조건3: 호감 필터링 기능")
    void likeTest3() {

        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, 1, null));

        assertThat(list).size().isEqualTo(2);
        assertThat(list.get(0).getAttractiveTypeCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("toList 조건4: 호감 및 성별 필터링 기능")
    void likeTest4() {

        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition("W", 1, null));

        assertThat(list).size().isEqualTo(1);

        LikeablePerson likeablePerson = list.get(0);
        assertThat(likeablePerson.getAttractiveTypeCode()).isEqualTo(1);
        assertThat(list.get(0).getFromInstaMember().getGender()).isEqualTo("W");
    }


    @Test
    @DisplayName("toList 조건5: sort 최근 날짜 기준")
    void likeTest5() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 1));
        assertThat(list.get(list.size()-1).getFromInstaMemberUsername()).isEqualTo("insta_user12");
        assertThat(list.get(0).getFromInstaMemberUsername()).isEqualTo("insta_user15");
    }

    @Test
    @DisplayName("toList 조건6: sort 오래된 날짜 기준")
    void likeTest6() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 2));


        assertThat(list.get(list.size()-1).getFromInstaMemberUsername()).isEqualTo("insta_user15");
        assertThat(list.get(0).getFromInstaMemberUsername()).isEqualTo("insta_user12");


    }

    @Test
    @DisplayName("toList 조건7: sort 인기 많은 순")
    void likeTest7() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 3));

        assertThat(list.get(0).getFromInstaMemberUsername()).isEqualTo("insta_user13");
        assertThat(list.get(1).getFromInstaMemberUsername()).isEqualTo("insta_user12");
    }

    @Test
    @DisplayName("toList 조건8: sort 인기 적은 순")
    void likeTest8() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 4));
        assertThat(list.get(0).getFromInstaMember().getToLikeablePeople()).size().isEqualTo(0);

    }
    @Test
    @DisplayName("toList 조건9: sort 최신순")
    void likeTest9() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 5));
        assertThat(list.get(0).getFromInstaMember().getGender()).isEqualTo("W");
        assertThat(list.get(1).getFromInstaMemberUsername()).isEqualTo("insta_user13");
        assertThat(list.get(list.size()-1).getFromInstaMember().getGender()).isEqualTo("M");
    }

    @Test
    @DisplayName("toList 조건10: sort 최신순")
    void likeTest10() {
        InstaMember user11 = instaMemberService.findByUsername("insta_user11").orElseThrow();

        List<LikeablePerson> list =
                likeablePersonService.findByCondition(user11.getId(),
                        new LikeableSearchCondition(null, null, 6));
        assertThat(list.get(0).getAttractiveTypeCode()).isEqualTo(1);
        assertThat(list.get(1).getFromInstaMemberUsername()).isEqualTo("insta_user12");
        assertThat(list.get(2).getAttractiveTypeCode()).isEqualTo(2);
        assertThat(list.get(3).getAttractiveTypeCode()).isEqualTo(3);


    }

}