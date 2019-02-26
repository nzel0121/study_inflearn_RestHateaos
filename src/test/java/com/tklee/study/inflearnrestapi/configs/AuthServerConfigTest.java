package com.tklee.study.inflearnrestapi.configs;

import com.tklee.study.inflearnrestapi.accounts.AccountService;
import com.tklee.study.inflearnrestapi.common.AppProperties;
import com.tklee.study.inflearnrestapi.common.BaseControllerTest;
import com.tklee.study.inflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰 발급받")
    public void getAuthToken() throws Exception {
        // Given
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username",appProperties.getUserUsername())
                    .param("password",appProperties.getUserPassword())
                    .param("grant_type","password")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("access_token").exists())
        ;
    }
}