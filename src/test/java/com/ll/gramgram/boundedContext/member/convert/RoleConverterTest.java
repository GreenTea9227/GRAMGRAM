package com.ll.gramgram.boundedContext.member.convert;

import com.ll.gramgram.base.security.Role;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RoleConverterTest {

    @Test
    void t1() {
        RoleConverter roleConverter = new RoleConverter();
        Set<Role> role = new HashSet<>();
        role.add(Role.INSTAGRAM);
        role.add(Role.USER);
        String s = roleConverter.convertToDatabaseColumn(role);
        System.out.println(s);
    }

    @Test
    void t2() {
        RoleConverter roleConverter = new RoleConverter();
        String s = "USER,INSTAGRAM";
        Set<Role> roles = roleConverter.convertToEntityAttribute(s);
        for (Role role : roles) {
            System.out.println("role = " + role);
        }
    }

}