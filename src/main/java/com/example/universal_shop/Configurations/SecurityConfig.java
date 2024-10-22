package com.example.universal_shop.Configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    @Value("${RM_KEY}")
    private String rmKey;

    @Autowired
    public SecurityConfig(@Lazy UserDetailsService userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                (rq) -> {
                    rq.requestMatchers("/","/register", "/login", "/css/**", "/js/**", "/images/**",
                                "/errors/fail-registration", "/general-pages/**", "/basket", "/categories",
                            "/categories/image/**", "/goods").permitAll();
                    rq.requestMatchers("admin-panel/**").hasRole("ADMIN");
                    rq.requestMatchers(HttpMethod.GET).permitAll();
                    rq.requestMatchers(HttpMethod.POST).permitAll();
                    rq.anyRequest().authenticated();

                })
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll()
                        .failureUrl("/login?error=true").permitAll()
                )
                .sessionManagement(session -> {
                    session.sessionFixation().migrateSession();
                    session.invalidSessionUrl("/login?session=invalid");
                    session.maximumSessions(2).maxSessionsPreventsLogin(true);
                })
                .rememberMe(rm -> rm.tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(1209600)
                        .userDetailsService(userDetailsService)
                        .key(rmKey))
                .logout(logout -> logout.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
