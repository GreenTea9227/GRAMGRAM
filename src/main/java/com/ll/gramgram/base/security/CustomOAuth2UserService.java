package com.ll.gramgram.base.security;

import com.ll.gramgram.base.event.dto.insta.EventAuthenticateInstagram;
import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.base.security.social.SocialUserFactory;
import com.ll.gramgram.base.security.social.inter.DivideOAuth2User;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();


        if (providerTypeCode.equals("INSTAGRAM")) {
            if (rq.isLogout()) {
                throw new OAuth2AuthenticationException("로그인 후 이용해주세요.");
            }

            String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
            String accessToken = userRequest.getAccessToken().getTokenValue();
            userInfoUri = userInfoUri.replace("{access-token}", accessToken);

            publisher.publishEvent(new EventAuthenticateInstagram(userInfoUri, accessToken));

            Member member = rq.getMember();

            return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
        }

        OAuth2User oAuth2User = super.loadUser(userRequest);

        DivideOAuth2User customOAuth2User = SocialUserFactory.create(providerTypeCode, oAuth2User);

        Member member = memberService.whenSocialLogin(providerTypeCode, customOAuth2User.getUsername()).getData();

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}
