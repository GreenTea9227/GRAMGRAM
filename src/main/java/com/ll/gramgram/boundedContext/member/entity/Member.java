package com.ll.gramgram.boundedContext.member.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.base.security.Role;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.member.convert.RoleConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String providerTypeCode; // 일반회원인지, 카카오로 가입한 회원인지, 구글로 가입한 회원인지
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    @OneToOne // 1:1
    @Setter // memberService::updateInstaMember 함수 때문에
    private InstaMember instaMember;
    private boolean messagesStatus;

    @Builder.Default
    @Convert(converter = RoleConverter.class)
    private Set<Role> roles = new HashSet<>();

    // 이 함수 자체는 만들어야 한다. 스프링 시큐리티 규격
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        return roles.stream().map(i -> new SimpleGrantedAuthority("ROLE_"+i.name())).toList();
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    // 이 회원이 본인의 인스타ID를 등록했는지 안했는지
    public boolean hasConnectedInstaMember() {
        return roles.contains(Role.INSTAGRAM);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    public String getNickname() {
        // 최소 6자 이상
        return "%1$4s".formatted(Long.toString(getId(), 36)).replace(' ', '0');
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public boolean hasNewMessage() {
        return messagesStatus;
    }

    public void changeMessageStatus(boolean status) {
        this.messagesStatus = status;
    }
}
