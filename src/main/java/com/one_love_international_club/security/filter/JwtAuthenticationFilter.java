package com.one_love_international_club.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.one_love_international_club.auth.dto.UserRegisterDto;
import com.one_love_international_club.auth.entity.UserLoginEntity;
import com.one_love_international_club.auth.repo.UserLoginRepository;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.JwtTokenProvider;
import com.one_love_international_club.security.TokenProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserLoginRepository userLoginRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {

                DecodedJWT decodedJWT = jwtTokenProvider.decodeToken(jwt);

                TokenProps tokenDetails = jwtTokenProvider.toTokenProps(decodedJWT);

                UserLoginEntity user = getUser(tokenDetails.username());

                UserDetails userDetails = new User(
                        tokenDetails.username(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_".concat(user.getUserType().name())))
                );

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, tokenDetails, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private UserLoginEntity getUser(String email) {
        return userLoginRepository
                .findByEmail(email)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "User with email: " + email + " not found"));
    }

}
