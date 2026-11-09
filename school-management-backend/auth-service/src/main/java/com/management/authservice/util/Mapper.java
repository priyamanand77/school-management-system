package com.management.authservice.util;

import org.modelmapper.ModelMapper;

public class Mapper {

    static ModelMapper mapper = new ModelMapper();

    public static <T> T map(Object source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }
}
