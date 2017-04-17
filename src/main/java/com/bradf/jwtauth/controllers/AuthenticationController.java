package com.bradf.jwtauth.controllers;

import com.bradf.jwtauth.controllers.helpers.AuthenticationRequest;
import com.bradf.jwtauth.security.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bradf on 2017-04-16.
 */
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenUtil tokenUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenUtil tokenUtil) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
    }

    @RequestMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Authenticate against our in memory authentication
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Assume we passed
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String token = tokenUtil.createToken(roles, authenticationRequest.getUsername());

        // TODO: Currently sending the token back in the body, may want to create an actual object to send this back in
        return ResponseEntity.ok(token);
    }

    @RequestMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdminRole() {
        return ResponseEntity.ok("You are an admin!");
    }
}
