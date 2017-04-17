package com.bradf.jwtauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by bradf on 2017-04-16.
 */
@Component
public class TokenUtil {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Integer minutes;

    @Value("${jwt.token.issuer}")
    private String issuer;

    public String createToken(List<String> roles, String username){
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC512("Secrets!");

            // Add roles to header claims
            Map<String, Object> headerClaims = new HashMap<>();
            headerClaims.put("roles", roles);

            // Create the token
            token = JWT.create()
                    .withIssuer(this.issuer)
                    .withSubject(username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + this.minutes * 60 * 1000)) // Set millis to expire at.
                    .withHeader(headerClaims)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.info(String.format("Failed to create token for user: %s" , username));
        }

        return token;
    }

    public Optional<DecodedJWT> decodeToken(String token){
        Optional<DecodedJWT> decodedToken = Optional.empty();

        try {
            Algorithm algorithm = Algorithm.HMAC512(this.secret);

            // TODO: Create a reusable verifier bean.
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.issuer)
                    .build(); //Reusable verifier instance

            decodedToken = Optional.of(verifier.verify(token));
        } catch (UnsupportedEncodingException exception){
            logger.info("Unsupported encoding encountered when decoding token.");
        } catch (JWTVerificationException exception){
            logger.info("JWT Verification exception when decoding token.");
        }
        return decodedToken;
    }

    public boolean isValidToken(String token){
        return this.decodeToken(token).isPresent();
    }

    public List<String> determineRoles(String token){
        Optional<DecodedJWT> decoded = this.decodeToken(token);
        return decoded.map(decodedJWT -> decodedJWT.getHeaderClaim("roles").asList(String.class)).orElseGet(ArrayList::new);
    }
}
