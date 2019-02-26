package com.tklee.study.inflearnrestapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;
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
        this.accountRepository.save(tklee);

        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);


    }

    @Test
    public void findByUsernameFail(){
        String username = "random@random.com";
        try{
            this.accountService.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            assertThat(e.getMessage()).containsSequence(username);
        }
    }
}