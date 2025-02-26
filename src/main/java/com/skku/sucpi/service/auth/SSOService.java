package com.skku.sucpi.service.auth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public int verifyToken(String pToken) {
        return sso.verifyToken(pToken);
    }

}
