package com.hadarych.pp311.security;

import com.hadarych.pp311.entity.User;
import com.hadarych.pp311.security.handler.LoginSuccessHandler;
import com.hadarych.pp311.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService service;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler())
                .loginProcessingUrl("/login")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();

        http.logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                //выключение кроссдоменной секьюрностьи
                .and().csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/user").hasAnyRole("admin", "user")
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            String name = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";
            User user = service.getByName(name);

            if (user == null) {
                throw new BadCredentialsException("1000");
            }
            if (!bCryptPasswordEncoder().matches(password, user.getPassword())) {
                throw new BadCredentialsException("1000");
            }

            return new UsernamePasswordAuthenticationToken(name, password, user.getRoles());
        };
    }
}
