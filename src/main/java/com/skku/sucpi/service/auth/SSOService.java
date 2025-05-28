package com.skku.sucpi.service.auth;

import SafeIdentity.SsoAuthInfo;
import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.util.UserUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class SSOService {

    private SafeIdentity.SSO sso;

    @Value("${SSO_API_KEY}")
    private String SSO_API_KEY;
    @Value("${SSO_HOST}")
    private String SSO_HOST;
    @Value("${SSO_PORT}")
    private String SSO_PORT;

    @PostConstruct
    public void init() {
        this.sso = new SafeIdentity.SSO(SSO_API_KEY);
        sso.setHostName(SSO_HOST);
        sso.setPortNumber(Integer.parseInt(SSO_PORT));
    }

    public boolean verifyToken(String pToken) {
        log.info("verfyToken 결과 : {}", sso.verifyToken(pToken));
        return sso.verifyToken(pToken) >= 0;
    }

    public SSOUserDto getInfoFromSSO(String pToken) {
        SsoAuthInfo ssoAuthInfo = sso.userView(pToken);
        String ssoProfile = ssoAuthInfo.getProfile();
        String[] profileArr = ssoProfile.split("\\*");
        log.info(Arrays.toString(profileArr));

        log.info("{} {} {} {} {} {} {} {}", ssoAuthInfo.getUserId(), ssoAuthInfo.getUserName(), getInfo(profileArr[0]));

        String hakbun = getInfo(profileArr[0]);
        String role = "student";
        // 관리자(교직원)일 때
        if (hakbun.equals("N/A")) {
            hakbun = getInfo(profileArr[15]);
        }

        return SSOUserDto.builder()
                .userName(ssoAuthInfo.getUserName())
                .hakbun(hakbun)
                .hakgwaCd(UserUtil.getCodeFromDepartment(getInfo(profileArr[2])))
                .department(getInfo(profileArr[2]))
                .degree(getInfo(profileArr[4]))
                .build();
    }

    private String getInfo(String str) {
        String[] tokens = str.split("-");
        return ((tokens.length > 1) ? tokens[1] : "N/A");
    }
}
