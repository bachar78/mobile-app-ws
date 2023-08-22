package com.appsdevelpersblog.app.ws.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class AddressDto implements Serializable {
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postCode;
    private String type;
    private UserDto userDetails;
}
