package com.ll.gramgram.base.security.social;


import com.ll.gramgram.base.exceptionHandler.NotSupportUserLoginException;
import com.ll.gramgram.base.security.social.inter.DivideOAuth2User;
import com.ll.gramgram.base.security.social.user.FacebookUser;
import com.ll.gramgram.base.security.social.user.GoogleUser;
import com.ll.gramgram.base.security.social.user.KakaoUser;
import com.ll.gramgram.base.security.social.user.NaverUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static com.ll.gramgram.base.security.social.OAuth2Provider.*;

public class SocialUserFactory {

    public static DivideOAuth2User create(String providerTypeCode, OAuth2User oAuth2User) {

        if (isMatchWithProvider(providerTypeCode, GOOGLE)) {
            return new GoogleUser(oAuth2User);
        }

        if (isMatchWithProvider(providerTypeCode, KAKAO)) {
            return new KakaoUser(oAuth2User);
        }

        if (isMatchWithProvider(providerTypeCode, NAVER)) {
            return new NaverUser(oAuth2User);
        }

        if (isMatchWithProvider(providerTypeCode, FACEBOOK)) {
            return new FacebookUser(oAuth2User);
        }

        throw new NotSupportUserLoginException("지원하지 않는 로그인 방식입니다.");
    }

    private static boolean isMatchWithProvider(String providerTypeCode, OAuth2Provider provider) {
        return providerTypeCode.equals(provider.name());
    }

}


