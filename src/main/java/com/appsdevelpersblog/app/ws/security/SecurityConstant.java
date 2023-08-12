package com.appsdevelpersblog.app.ws.security;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 864000000; //10 DAYS
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "hlkjhjbkjhblkjhblkjhblkjhbjkhbkjhbkjhblkjhbjljkbuiyboiuyboiuvfrftuiygtiuytfgoiuugoiuygouygoygyu";

}
