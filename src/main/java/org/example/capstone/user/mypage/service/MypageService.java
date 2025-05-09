package org.example.capstone.user.mypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstone.user.domain.User;
import org.example.capstone.user.login.dto.CustomUserDetails;
import org.example.capstone.user.mypage.dto.MypageRequest;
import org.example.capstone.user.mypage.dto.MypageResponse;
import org.example.capstone.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;

    public MypageResponse getMypage(CustomUserDetails customUserDetails){

        log.info("user: {}", customUserDetails.toString());
        log.info("user email: {}", customUserDetails.getUserEmail());
        log.info("user id: {}", customUserDetails.getUserId());
        User user = userRepository.findByEmail(customUserDetails.getUserEmail());
        return new MypageResponse(user);
    }

    public MypageResponse updateMypage(CustomUserDetails customUserDetails, MypageRequest request){
        User user = userRepository.findByEmail(customUserDetails.getUserEmail());
        return new MypageResponse(userRepository.save(user.updateUser(request)));
    }
}
