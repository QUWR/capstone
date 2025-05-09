package org.example.capstone.global.config;

import java.util.Arrays;
import java.util.List;

public class SecurityUrls {

    /**
     *인증 생략할 URL 목록
     */

    public static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/api/auth/login",  //로그인
            "/api/auth/register",   //회원가입
            "/docs/**",  //swagger
            "/v3/api-docs/**"   //swagger api
    );

    /**
     * cors 허용할 URL
     */
    public static final List<String> ALLOWED_ORIGIN = Arrays.asList(
        "http://localhost:3000/**"
    );
}
