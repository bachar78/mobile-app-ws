package com.appsdevelpersblog.app.ws.service.impl;

import com.appsdevelpersblog.app.ws.Exceptions.UserServiceException;
import com.appsdevelpersblog.app.ws.UserRepository;
import com.appsdevelpersblog.app.ws.oi.entity.UserEntity;
import com.appsdevelpersblog.app.ws.service.UserService;
import com.appsdevelpersblog.app.ws.shared.dto.UserDto;
import com.appsdevelpersblog.app.ws.shared.dto.Utils;
import com.appsdevelpersblog.app.ws.ui.model.reponse.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) throws UserServiceException {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(utils.generateUserId(30));
        UserEntity storedEntity = userRepository.findByEmail(user.getEmail());
        if (storedEntity != null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        UserEntity storedValue = userRepository.save(userEntity);
        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedValue, returnedValue);
        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        UserDto returnedUser = new UserDto();
        BeanUtils.copyProperties(user, returnedUser);
        return returnedUser;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException(userId);
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String id) {
        UserDto returnedValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        UserEntity updateUserDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updateUserDetails, returnedValue);
        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new User(username, user.getEncryptedPassword(), new ArrayList<>()); //this list is the list of granted authorities. These are user's roles and permissions
    }
}
