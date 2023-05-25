package com.ll.gramgram.base.event.eventListener;

import com.ll.gramgram.base.event.dto.insta.EventAuthenticateInstagram;
import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class InstaLoginListener {

    private final Rq rq;
    private final RestTemplate restTemplate;
    private final InstaMemberService instaMemberService;

    @EventListener
    public void instaLogin(EventAuthenticateInstagram data) {

        String gender = rq.getSessionAttr("connectByApi__gender", "W");
        rq.removeSessionAttr("connectByApi__gender");

        String userInfoUri = data.userInfoUri();
        String accessToken = data.accessToken();

        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        Map<String, String> userAttributes = response.getBody();

        instaMemberService.connect(rq.getMember(), gender, userAttributes.get("id"), userAttributes.get("username"), accessToken);
    }
}
