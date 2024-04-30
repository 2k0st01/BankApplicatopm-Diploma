package com.example.bankapplicatopm.config;

import com.example.bankapplicatopm.service.BankAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final BankAccountService bankAccountService;

    public SecurityConfig(@Lazy BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                    .antMatchers("/registration/**",
                            "/login",
                            "/registration",
                            "/forgot-password",
                            "/change-password",
                            "/",
                            "/api/currency/rate",
                            "/test"
                    ).permitAll()
                    .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                    .antMatchers("/panel", "/panel/getUsers").hasRole("ADMIN")
                    .anyRequest()
                        .authenticated()
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/account", true)
                .and()
                    .logout()
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                .and()
                    .authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(bankAccountService);
        return provider;
    }

}
