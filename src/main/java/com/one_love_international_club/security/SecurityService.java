package com.one_love_international_club.security;

import com.one_love_international_club.auth.entity.UserLoginEntity;
import com.one_love_international_club.auth.repo.UserLoginRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private static final String NOT_AUTHENTICATED = "User not authenticated";
    private static final String USER_LOGIN_NOT_FOUND = "No user login record found for the authenticated user.";


    private final HttpServletRequest request;
    private final JwtTokenProvider tokenProvider;
    private final UserLoginRepository userLoginRepository;

    public Optional<TokenProps> getTokenProps() {

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            return Optional.of(tokenProvider.toTokenProps(tokenProvider.decodeToken(jwt)));
        }
        return Optional.empty();
    }

    public UserLoginEntity getCurrentUser() {
        String email = (String) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

       return userLoginRepository.findByEmail(email).orElse(null);
    }


}
