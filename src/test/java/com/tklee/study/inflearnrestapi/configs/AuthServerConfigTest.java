package com.tklee.study.inflearnrestapi.configs;

import com.tklee.study.inflearnrestapi.accounts.Account;
import com.tklee.study.inflearnrestapi.accounts.AccountRoles;
import com.tklee.study.inflearnrestapi.accounts.AccountService;
import com.tklee.study.inflearnrestapi.common.BaseControllerTest;
import com.tklee.study.inflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰 발급받")
    public void getAuthToken() throws Exception {
        // Given
        Set<AccountRoles> roles = new HashSet<AccountRoles>();
        roles.add(AccountRoles.ADMIN);
        roles.add(AccountRoles.USER);

        String username = "2tk.java2@gmail.com";
        String password = "tklee";
        Account tklee = Account.builder()
                .email(username)
                .password(password)
                .roles(roles)
                .build();

        accountService.saveAccount(tklee);

        String clientId = "myApp";
        String clientSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId,clientSecret))
                    .param("username",username)
                    .param("password",password)
                    .param("grant_type","password")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("access_token").exists())
        ;
    }
}