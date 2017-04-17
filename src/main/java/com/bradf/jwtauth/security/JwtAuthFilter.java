package com.bradf.jwtauth.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by bradf on 2017-04-16.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    public JwtAuthFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    // TODO: Proper Property
    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader(this.tokenHeader);

        // Ensure the token is there and at least the length of "Bearer "
        if(authToken != null && authToken.length() > 6){
            String token = authToken.substring(7);

            // TODO: Not sure I like this decode/valid/roles logic may need to rethink.....seems complex and prone to error.
            Optional<DecodedJWT> decodedToken = this.tokenUtil.decodeToken(token);

            if(decodedToken.isPresent()){
                List<String> roles = this.tokenUtil.determineRoles(token);
                JwtUser user = new JwtUser(decodedToken.get().getSubject(), AuthorityUtils.createAuthorityList(roles.toArray(new String[roles.size()])));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
