package fr.simplon.blog_wow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig implements WebMvcConfigurer {
    private DataSource dataSource;

    @Autowired
    public SpringSecurityConfig(DataSource pDataSource) {
        dataSource = pDataSource;
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
        formConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        converters.add(formConverter);
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable() // Pour l'instant, on désactive la protection CSRF
                .authorizeHttpRequests(authorize -> authorize//
                        .requestMatchers(HttpMethod.GET,"/","/index").permitAll()
                        .requestMatchers(HttpMethod.GET,"/view/articles").permitAll()
                        .requestMatchers(HttpMethod.GET,"/fragments/articles/**").authenticated() //
                        .requestMatchers(HttpMethod.PUT).authenticated() //
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN") //
                        .requestMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN") //
                        .anyRequest().permitAll() //
                        .and()
                )
                .httpBasic()
                .and()
                .formLogin() //
                .loginPage("/login").permitAll() //
                .and()
                .passwordManagement(management -> management.changePasswordPage("/change-password")) //
                .logout()
                .permitAll()
                .and()
                .build();

    }
}
