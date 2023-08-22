package com.appsdevelpersblog.app.ws.ui.model.reponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressesRest {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postCode;
    private String type;

}
