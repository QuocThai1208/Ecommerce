package com.ecommerce.order_service.configuration;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            var exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            var issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
            var header = signedJWT.getHeader().toJSONObject();

            return new Jwt(
                    token,
                    issueTime.toInstant(),
                    exp.toInstant(),
                    header,
                    signedJWT.getJWTClaimsSet().getClaims()
            );
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}