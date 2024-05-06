package com.example.bankapplicatopm.config;

import com.example.bankapplicatopm.componet.JwtAuthenticationFilter;
import com.example.bankapplicatopm.service.BankAccountService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final BankAccountService bankAccountService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(@Lazy BankAccountService bankAccountService, @Lazy JwtAuthenticationFilter jwtAuthFilter) {
        this.bankAccountService = bankAccountService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                    .antMatchers("/registration/**",
                            "/login",
                            "/registration",
                            "/forgot-password",
                            "/change-password",
                            "/",
                            "/api/currency/rate",
                            "/api/auth/login"
                    ).permitAll()
                    .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                    .antMatchers("/panel", "/panel/getUsers").hasRole("ADMIN")
                    .anyRequest()
                        .authenticated()
                .and()
                    .logout()
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // or use "*" for all
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(bankAccountService);
        return provider;
    }

}
