package com.bradf.jwtauth.config;

import com.bradf.jwtauth.security.JwtAuthEntryPoint;
import com.bradf.jwtauth.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by bradf on 2017-04-16.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint, JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Configure an in memory user store, probably want to use something else like JDBC or LDAP

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password("admin").roles("ADMIN").build());
        return manager;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                    .disable() // rely on the token (stateless)
                .exceptionHandling().authenticationEntryPoint(this.jwtAuthEntryPoint)
                .and()
                //TODO: Tried using property security.sessions=stateless but did not seem to work
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //TODO: Seem to need this OPTIONS permit all, feels like overkill or that I don't understand something as I have the RestConfig, RestRepositoryConfig and this line.
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.headers().cacheControl();
    }
}
