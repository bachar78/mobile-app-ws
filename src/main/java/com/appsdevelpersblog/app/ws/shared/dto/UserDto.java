package com.appsdevelpersblog.app.ws.shared.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class UserDto implements Serializable {
//    @Setter(AccessLevel.NONE)
//    @Getter(AccessLevel.NONE)
//    private static final long serialVersionUID = 7841705728362396095L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<AddressDto> addresses;
}
