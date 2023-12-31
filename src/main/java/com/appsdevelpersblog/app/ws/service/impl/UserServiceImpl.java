package com.appsdevelpersblog.app.ws.service.impl;

import com.appsdevelpersblog.app.ws.Exceptions.UserServiceException;
import com.appsdevelpersblog.app.ws.UserRepository;
import com.appsdevelpersblog.app.ws.oi.entity.UserEntity;
import com.appsdevelpersblog.app.ws.service.UserService;
import com.appsdevelpersblog.app.ws.shared.dto.AddressDto;
import com.appsdevelpersblog.app.ws.shared.dto.UserDto;
import com.appsdevelpersblog.app.ws.shared.dto.Utils;
import com.appsdevelpersblog.app.ws.ui.model.reponse.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.appsdevelpersblog.app.ws.shared.dto.Utils.convertList;

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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        user.getAddresses().stream().map(address -> {
//            address.setUserDetails(user);
//            address.setAddressId(utils.generateAddressId(30));
//            return address;
//        });
        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDto address = user.getAddresses().get(i);
            address.setAddressId(utils.generateAddressId(30));
            address.setUserDetails(user);
            user.getAddresses().set(i, address);
        }
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(utils.generateUserId(30));
        UserEntity storedEntity = userRepository.findByEmail(user.getEmail());
        if (storedEntity != null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        UserEntity storedValue = userRepository.save(userEntity);
        UserDto returnedValue =  modelMapper.map(storedValue, UserDto.class);
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
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
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
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.deleteByUserId(id);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        if (page > 0) page -= 1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();
        return convertList(users, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException(username);
        return new User(username, user.getEncryptedPassword(), new ArrayList<>()); //this list is the list of granted authorities. These are user's roles and permissions
    }
}
