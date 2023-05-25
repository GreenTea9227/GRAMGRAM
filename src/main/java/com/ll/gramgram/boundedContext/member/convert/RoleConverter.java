package com.ll.gramgram.boundedContext.member.convert;

import com.ll.gramgram.base.security.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RoleConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {

        return attribute.stream().map(i -> "ROLE_" + i.name()).collect(Collectors.joining(","));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(",")).map(r -> Role.valueOf(r.substring(5))).collect(Collectors.toSet());
    }
}