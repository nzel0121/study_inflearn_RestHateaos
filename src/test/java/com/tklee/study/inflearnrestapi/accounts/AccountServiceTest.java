package com.tklee.study.inflearnrestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void findByUsername(){
        //Given
        Set<AccountRoles> roles = new HashSet<AccountRoles>();
        roles.add(AccountRoles.ADMIN);
        roles.add(AccountRoles.USER);

        String password = "tklee";
        String username = "2tk.java@gmail.com";
        Account tklee = Account.builder()
                .email(username)
                .password(password)
                .roles(roles)
                .build();
        this.accountService.saveAccount(tklee);


        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertThat(passwordEncoder.matches(password,userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameFail(){
        // Expected
        String username = "random@random.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        this.accountService.loadUserByUsername(username);
    }
}