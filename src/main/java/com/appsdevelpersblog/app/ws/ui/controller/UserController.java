package com.appsdevelpersblog.app.ws.ui.controller;

import com.appsdevelpersblog.app.ws.Exceptions.UserServiceException;
import com.appsdevelpersblog.app.ws.service.UserService;
import com.appsdevelpersblog.app.ws.shared.dto.UserDto;
import com.appsdevelpersblog.app.ws.ui.model.reponse.ErrorMessages;
import com.appsdevelpersblog.app.ws.ui.model.reponse.OperationStatusModel;
import com.appsdevelpersblog.app.ws.ui.model.reponse.RequestOperationStatus;
import com.appsdevelpersblog.app.ws.ui.model.reponse.UserRest;
import com.appsdevelpersblog.app.ws.ui.model.request.UserDetailsRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.appsdevelpersblog.app.ws.shared.dto.Utils.convertList;

@RestController
@RequestMapping("api/v1/users") //http://localhost:8080/users
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest returnedValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnedValue);
        return returnedValue;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws UserServiceException {
        if (userDetails.getFirstName().isEmpty())
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
        return returnValue;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {
        UserRest returnValue = new UserRest();
        UserDto userDto = new UserDto();
        if (userDetails.getFirstName().isEmpty())
            throw new NullPointerException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        BeanUtils.copyProperties(userDetails, userDto);
        UserDto updatedUser = userService.updateUser(userDto, id);
        BeanUtils.copyProperties(updatedUser, returnValue);
        return returnValue;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel res = new OperationStatusModel();
        res.setOperationName(RequestOperationStatus.DELETE.name());
        userService.deleteUser(id);
        res.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return res;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<UserDto> users = userService.getUsers(page, limit);
        return convertList(users, UserRest.class);
    }


}
