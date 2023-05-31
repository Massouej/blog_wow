package fr.simplon.blog_wow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private DataSource dataSource;
    @Autowired
    public SpringSecurityConfig(DataSource pDataSource) { dataSource = pDataSource;}
    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {return new JdbcUserDetailsManager(dataSource);}

    @Bean
    public PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        return http.csrf().disable() // Pour l'instant, on désactive la protection CSRF
                .authorizeHttpRequests() //
                .requestMatchers("/commentaires/**").authenticated() //
                .requestMatchers(HttpMethod.PUT).authenticated() //
                .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN") //
                .requestMatchers("/admin/**").hasRole("ADMIN") //
                .anyRequest().permitAll() //
                .and() //
                .httpBasic()
                .and()
                .formLogin() //
                .loginPage("/login").permitAll() //
                .and().passwordManagement(management -> management.changePasswordPage("/change-password")) //
                .build();

    }
}
