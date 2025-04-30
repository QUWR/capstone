package org.example.capstone.user.mypage.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone.user.domain.User;
import org.example.capstone.user.login.dto.CustomUserDetails;
import org.example.capstone.user.mypage.dto.MypageRequest;
import org.example.capstone.user.mypage.dto.MypageResponse;
import org.example.capstone.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;

    public MypageResponse getMypage(CustomUserDetails customUserDetails){
        User user = userRepository.findByEmail(customUserDetails.getUserEmail());
        return new MypageResponse(user);
    }

    public MypageResponse updateMypage(CustomUserDetails customUserDetails, MypageRequest request){
        User user = userRepository.findByEmail(customUserDetails.getUserEmail());
        return new MypageResponse(userRepository.save(user.updateUser(request)));
    }
}
