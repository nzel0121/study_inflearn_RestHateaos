package com.tklee.study.inflearnrestapi.configs;

import com.tklee.study.inflearnrestapi.accounts.Account;
import com.tklee.study.inflearnrestapi.accounts.AccountRepository;
import com.tklee.study.inflearnrestapi.accounts.AccountRoles;
import com.tklee.study.inflearnrestapi.accounts.AccountService;
import com.tklee.study.inflearnrestapi.common.AppProperties;
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

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {

                Set<AccountRoles> adminRoles = new HashSet<AccountRoles>();
                adminRoles.add(AccountRoles.ADMIN);
                adminRoles.add(AccountRoles.USER);

                Set<AccountRoles> userRoles = new HashSet<AccountRoles>();
                userRoles.add(AccountRoles.USER);

                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(adminRoles)
                        .build();
                accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(userRoles)
                        .build();
                accountService.saveAccount(user);
            }
        };
    }
}
