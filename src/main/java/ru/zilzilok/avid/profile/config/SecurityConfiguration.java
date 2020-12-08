package ru.zilzilok.avid.profile.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.zilzilok.avid.profile.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(UserService userDetailService,
                                 PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/registration").not().fullyAuthenticated()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .rememberMe()
                .and()
                    .logout().logoutSuccessUrl("/").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }
}
