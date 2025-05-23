package org.example.capstone.global.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstone.global.config.SecurityUrls;
import org.example.capstone.global.util.JwtUtil;
import org.example.capstone.user.domain.User;
import org.example.capstone.user.login.dto.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //인증 생략 경로
        if(isWhitelistedPath(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더 찾음
        String auth = request.getHeader("Authorization");

        //검증
        if(auth == null || !auth.startsWith("Bearer ")){
            log.error("토큰이 존재하지 않거나 형식이 잘못되었습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = auth.split(" ")[1];

        // 토큰 유효성 검증
        if (!jwtUtil.validateToken(token)) {

            log.error("JWT토큰이 유효하지 않습니다.");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // 토큰에서 username 획득
        String username = jwtUtil.getUsername(token);
        String userEmail = jwtUtil.getUserEmail(token);

        //user를 생성하여 값 set
        User user = User.builder()
                .username(username)
                .email(userEmail)
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

    /**
     * 인증 생략
     */
    private boolean isWhitelistedPath(String uri) {
        return SecurityUrls.AUTH_WHITELIST.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}
