package com.tklee.study.inflearnrestapi.configs;

import com.tklee.study.inflearnrestapi.accounts.Account;
import com.tklee.study.inflearnrestapi.accounts.AccountRoles;
import com.tklee.study.inflearnrestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set<AccountRoles> roles = new HashSet<AccountRoles>();
                roles.add(AccountRoles.ADMIN);
                roles.add(AccountRoles.USER);

                Account tklee = Account.builder()
                        .email("2tk.java@gmail.com")
                        .password("tklee")
                        .roles(roles)
                        .build();
                accountService.saveAccount(tklee);
            }
        };
    }
}
