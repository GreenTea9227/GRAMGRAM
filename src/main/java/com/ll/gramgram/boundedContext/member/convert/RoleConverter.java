package com.ll.gramgram.boundedContext.member.convert;

import com.ll.gramgram.base.security.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RoleConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {

        return attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData))
            return Arrays.stream(dbData.split(",")).map(Role::valueOf).collect(Collectors.toSet());
        return new HashSet<>();
    }
}
