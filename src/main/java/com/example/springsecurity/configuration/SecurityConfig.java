package com.example.springsecurity.configuration;

import com.example.springsecurity.component.CustomAuthenticationFailureHandler;
import com.example.springsecurity.component.CustomLogoutSuccessHandler;
import com.example.springsecurity.component.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAuthenticationSuccessHandler refererAuthenticationSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/unprotected", "/actuator/health").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/actuator/**").hasAnyRole("ADMIN")
                .antMatchers("/protected").hasAnyRole("ADMIN", "USER")

                .and()
                .logout().permitAll()
                .and()
                // Preventing csrf -> https://owasp.org/www-community/attacks/csrf
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/actuator/**")
                .and()
                .formLogin()
                .failureHandler(authenticationFailureHandler)
                .successHandler(refererAuthenticationSuccessHandler)
                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler);


        super.configure(http);
    }

    // RBA -> Role Based Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(encoder().encode("user"))
                .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(encoder().encode("admin"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
