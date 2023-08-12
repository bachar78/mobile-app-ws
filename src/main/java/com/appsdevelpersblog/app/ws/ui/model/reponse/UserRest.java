package com.appsdevelpersblog.app.ws.ui.model.reponse;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRest {
    private String userId; //public userId
    private String firstName;
    private String lastName;
    private String email;
}
