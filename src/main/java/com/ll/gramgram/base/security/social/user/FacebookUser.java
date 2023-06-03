package com.ll.gramgram.base.security.social.user;

import com.ll.gramgram.base.security.social.OAuth2Provider;
import com.ll.gramgram.base.security.social.inter.DivideOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class FacebookUser extends DivideOAuth2User {

    public FacebookUser(OAuth2User oAuth2User) {
        super(oAuth2User, oAuth2User.getAttributes());

    }

    @Override
    public String getUsername() {
        return getProviderCode() + "__" + getOAuth2Name();
    }

    @Override
    public String getOAuth2Name() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProviderCode() {
        return OAuth2Provider.FACEBOOK.name();
    }

}
