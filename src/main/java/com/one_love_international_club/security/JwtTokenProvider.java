package com.one_love_international_club.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.one_love_international_club.config.JwtConfig;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final Algorithm signingAlgorithm;
    private final JWTVerifier jwtVerifier;
    public static final String ISSUER = "ONE-LOVE-INTERNATIONAL-CLUB";

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {

        this.jwtConfig = jwtConfig;
        byte[] secretBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        this.signingAlgorithm = Algorithm.HMAC512(secretBytes);
        this.jwtVerifier = JWT.require(this.signingAlgorithm).withIssuer(ISSUER).build();
    }

    public String generateToken(@NonNull TokenProps tokenProps, TokenType tokenType, Instant expiresAt) {

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(TokenClaim.USERLOGIN_ID.name().toLowerCase(), tokenProps.id().toString())
                .withClaim(TokenClaim.USERNAME.name().toLowerCase(), tokenProps.username())
                .withClaim(TokenClaim.TOKEN_TYPE.name().toLowerCase(), tokenType.toString())
                .withSubject(tokenProps.id().toString())
                .withAudience(ISSUER)
                .withExpiresAt(expiresAt)
                .sign(signingAlgorithm);
    }

    public String generateAccessToken(@NonNull TokenProps tokenProps) {
        Instant expiresAt = Instant.now().plusSeconds(jwtConfig.getExpiration());
        return generateToken(tokenProps, TokenType.ACCESS, expiresAt);
    }

    public String generateRefreshToken(@NonNull TokenProps tokenProps) {
        Instant expiresAt = Instant.now().plusSeconds(jwtConfig.getRefreshExpiration());
        return generateToken(tokenProps, TokenType.REFRESH, expiresAt);
    }

    public DecodedJWT decodeToken(@NonNull String token) {

        try {
            return jwtVerifier.verify(token);
        } catch (TokenExpiredException tee) {
            throw new ClubException(ErrorCode.INVALID_AUTH_TOKEN, "Token already expired", tee);
        } catch (JWTVerificationException exception) {
            throw new ClubException(ErrorCode.INVALID_AUTH_TOKEN, "Invalid token", exception);
        }
    }

    public TokenProps toTokenProps(@NonNull DecodedJWT decodedJWT) {

        String subject = decodedJWT.getSubject();
        UUID id = subject != null ? UUID.fromString(subject) : null;

        return TokenProps.builder()
                .username(getClaim(decodedJWT, TokenClaim.USERNAME))
                .id(id)
                .build();
    }

    private String getClaim(@NonNull DecodedJWT decodedJWT, TokenClaim tokenClaim) {

        Map<String, Claim> claims = decodedJWT.getClaims();
        Claim claim = claims.getOrDefault(tokenClaim.name().toLowerCase(), null);
        if (claim != null && !claim.isNull()) {
            return claim.asString();
        }
        return null;
    }

}