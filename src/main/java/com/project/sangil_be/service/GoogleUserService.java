package com.project.sangil_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sangil_be.dto.SocialLoginDto;
import com.project.sangil_be.model.GetTitle;
import com.project.sangil_be.model.User;
import com.project.sangil_be.repository.GetTitleRepository;
import com.project.sangil_be.repository.UserRepository;
import com.project.sangil_be.securtiy.UserDetailsImpl;
import com.project.sangil_be.securtiy.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleUserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GetTitleRepository getTitleRepository;

    // 구글 로그인
    public void googleLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        // 1. 인가코드로 엑세스토큰 가져오기
        String accessToken = getAccessToken(code);

        // 2. 엑세스토큰으로 유저정보 가져오기
        SocialLoginDto googleUserInfo = getGoogleUserInfo(accessToken);

        // 3. 유저확인 & 회원가입
        User foundUser = getUser(googleUserInfo);

        // 4. 시큐리티 강제 로그인
        Authentication authentication = securityLogin(foundUser);

        // 5. jwt 토큰 발급
        jwtToken(authentication, response);
    }

    // 1. 인가코드로 엑세스토큰 가져오기
    private String getAccessToken(String code) throws JsonProcessingException {

        // 헤더에 Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 바디에 필요한 정보 담기
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id" , "77683946484-86n78jead6i4agakkjdf3482c3609des.apps.googleusercontent.com");
        body.add("client_secret", "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");
        body.add("code", code);
        body.add("redirect_uri", "https://kopite.shop/user/google/callback");
        body.add("grant_type", "authorization_code");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleToken = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST, googleToken,
                String.class
        );

        // response에서 엑세스토큰 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseToken = objectMapper.readTree(responseBody);
        String accessToken = responseToken.get("access_token").asText();
        return accessToken;
    }

    // 2. 엑세스토큰으로 유저정보 가져오기
    private SocialLoginDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {

        // 헤더에 엑세스토큰 담기, Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUser = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST, googleUser,
                String.class
        );

        // response에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String socialId = jsonNode.get("sub").asText();
        String username = jsonNode.get("name").asText()+ "_" + socialId;
        Random rnd = new Random();
        String s="";
        for (int i = 0; i < 8; i++) {
            s += String.valueOf(rnd.nextInt(10));
        }
        String nickname = "G" + "_" + s;

        return new SocialLoginDto(username, nickname,socialId);
    }

    // 3. 유저확인 & 회원가입
    private User getUser(SocialLoginDto googleUserInfo) {

        String socialId = googleUserInfo.getSocialId();
        User googleUser = userRepository.findBySocialId(socialId);

        if (googleUser == null) {
            String googlename = googleUserInfo.getUsername();
            String nickname = googleUserInfo.getNickname();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            String userImageUrl="없음";
            String userTitle="등린이";
            String userTitleImgUrl="없음";

            googleUser = new User(googlename,socialId,encodedPassword, nickname,userImageUrl,userTitle,userTitleImgUrl);
            userRepository.save(googleUser);
            GetTitle getTitle = new GetTitle(userTitle,userTitleImgUrl,googleUser);
            getTitleRepository.save(getTitle);
        }


        return googleUser;
    }

    // 4. 시큐리티 강제 로그인
    private Authentication securityLogin(User findUser) {

        UserDetails userDetails = new UserDetailsImpl(findUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    // 5. jwt 토큰 발급
    private void jwtToken(Authentication authentication,HttpServletResponse response) {
        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
        response.addHeader("Authorization", "BEARER" + " " + token);

    }
}