package com.appsdevelpersblog.app.ws.shared.dto;

import com.appsdevelpersblog.app.ws.Exceptions.UserServiceException;
import com.appsdevelpersblog.app.ws.ui.model.reponse.UserRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public
class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static UserRest generateUserRest(UserDto user) {
        UserRest returnedUser = new UserRest();
        BeanUtils.copyProperties(user, returnedUser);
        return returnedUser;
    }

    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> {
                    ModelMapper modelMapper = new ModelMapper();
                    try {
                        T target = targetClass.getDeclaredConstructor().newInstance();
                        modelMapper.map(source, target);
                        return target;
                    } catch (Exception e) {
                        throw new RuntimeException("Conversion failed for an element in the list");
                    }
                }).toList();
    }
}
