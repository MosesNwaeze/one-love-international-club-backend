package com.one_love_international_club.auth.service;

import com.one_love_international_club.auth.dto.ApproveUserDto;
import com.one_love_international_club.auth.dto.TokenDto;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.PasswordResetTokenRepository;
import com.one_love_international_club.auth.repo.UserRepository;
import com.one_love_international_club.auth.repo.UserLoginTokenRepository;
import com.one_love_international_club.config.JwtConfig;
import com.one_love_international_club.enums.ApprovalStatus;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.request.LoginRequestDto;
import com.one_love_international_club.setting.dto.request.PasswordResetRequestDto;
import com.one_love_international_club.setting.dto.request.RegisterRequestDto;
import com.one_love_international_club.setting.dto.response.LoginResponseDto;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.setting.entity.PasswordResetToken;
import com.one_love_international_club.setting.entity.UserLoginToken;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.exception.ResourceNotFoundException;
import com.one_love_international_club.exception.UnauthorizedException;
import com.one_love_international_club.security.JwtTokenProvider;
import com.one_love_international_club.security.TokenProps;
import com.one_love_international_club.setting.repo.RoleRepository;
import com.one_love_international_club.util.EmailService;
import com.one_love_international_club.util.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserLoginTokenRepository userLoginTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtConfig jwtConfig;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    private static final String INVALID_LOGIN_MESSAGE = "Invalid email or password";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String UPLOAD_PATH = "uploads";
    private static final String REGISTER_NEW_USER = "New User Registration";
    private static final String NEW_lLOGIN = "New login detected.";

    private static final Long MAX_MEMBER_SIZE = 100L;

    @Transactional
    public Response<Void> register(RegisterRequestDto requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        UserEntity userLogin = modelMapper
                .map(requestDto, UserEntity.class);
        Long totalMembers = userRepository.totalMembers();

        if (totalMembers > MAX_MEMBER_SIZE) {
            throw new ClubException(ErrorCode.VALIDATION_ERROR, "The allowed member size has been exceeded.");
        }

        if (StringUtils.isNotBlank(requestDto.getProfilePic())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getProfilePic(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setProfilePic(fileUrl);
            userLogin.setPicPublicId(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getRegistrationFeeUrl())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getRegistrationFeeUrl(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setRegistrationFeeUrl(fileUrl);
            userLogin.setRegistrationFeePublicId(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getRegistrationFormUrl())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getRegistrationFormUrl(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setRegistrationFormUrl(fileUrl);
            userLogin.setRegistrationFormPublic(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getLetterOfUndertaking())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getLetterOfUndertaking(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setLetterOfUndertaking(fileUrl);
            userLogin.setLetterOfUndertakingPublicId(publicId);
        }

        roleRepository
                .findByNameIgnoreCase("General Assembly")
                .ifPresent(userLogin::setRoleEntity);

        UserEntity save = userRepository.saveAndFlush(userLogin);

        emailService
                .sendEmail(save.getEmail(),
                        REGISTER_NEW_USER,
                        "Your account for One Love International Club, have been created successfully. " +
                                "Awaiting verification from executive member.");

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .message("User created awaiting verification from Executive member.")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Transactional
    public Response<Void> approveUser(ApproveUserDto approveUserDto) {

        UserEntity user = userRepository
                .findById(approveUserDto.getUserId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "User not found with id: " + approveUserDto.getUserId()));

        roleRepository
                .findById(user.getRoleEntity().getId())
                .ifPresent(roleEntity -> {
                    user.setRoleEntity(roleEntity);
                    user.setApprovalComment(approveUserDto.getApprovalComment());
                    user.setApprovalStatus(approveUserDto.getApprovalStatus());
                });

        userRepository.saveAndFlush(user);

        emailService.sendEmail(
                user.getEmail(),
                "Approval status",
                "Your request to join One love international noble club has been approved by the executive.");

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .message("Request to join club has been approved by the executive.")
                .code(HttpStatus.OK.value())
                .build();
    }

    public Response<PaginatedResponse<UserDto>> findAllUnapprovedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<UserEntity> pendingUsers = userRepository
                .findAllUnapprovedUsers(ApprovalStatus.PENDING, pageable);

        return findAllUsers(page, size, pendingUsers);
    }

    public Response<PaginatedResponse<UserDto>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<UserEntity> pendingUsers = userRepository
                .findAll(pageable);

        return findAllUsers(page, size, pendingUsers);
    }


    @Transactional
    public Response<UserDto> updateUser(UserDto requestDto) {
        UserEntity userLogin = userRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with  id " + requestDto.getId() + " not found"));

        modelMapper.map(requestDto, userLogin);

        if (StringUtils.isNotBlank(requestDto.getProfilePic()) &&
                StringUtils.isNotBlank(requestDto.getPicPublicId())) {

            fileService.deleteFile(requestDto.getPicPublicId());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getProfilePic(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setProfilePic(fileUrl);
            userLogin.setPicPublicId(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getRegistrationFeeUrl()) &&
                StringUtils.isNotBlank(requestDto.getRegistrationFeePublicId())) {

            fileService.deleteFile(requestDto.getRegistrationFeePublicId());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getRegistrationFeeUrl(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setRegistrationFeeUrl(fileUrl);
            userLogin.setRegistrationFeePublicId(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getRegistrationFormUrl()) &&
                StringUtils.isNotBlank(requestDto.getRegistrationFormPublic())) {

            fileService.deleteFile(requestDto.getRegistrationFormPublic());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getRegistrationFormUrl(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setRegistrationFormUrl(fileUrl);
            userLogin.setRegistrationFormPublic(publicId);
        }

        if (StringUtils.isNotBlank(requestDto.getLetterOfUndertaking()) &&
                StringUtils.isNotBlank(requestDto.getLetterOfUndertakingPublicId())) {

            fileService.deleteFile(requestDto.getLetterOfUndertakingPublicId());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(requestDto.getLetterOfUndertaking(), UPLOAD_PATH);

            String publicId = uploaded.get("publicId");
            String fileUrl = uploaded.get("fileUrl");

            userLogin.setLetterOfUndertaking(fileUrl);
            userLogin.setLetterOfUndertakingPublicId(publicId);
        }

        UserEntity save = userRepository.save(userLogin);

        return Response.<UserDto>builder()
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(save, UserDto.class))
                .message("User updated successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ClubException(
                        ErrorCode.NOT_AUTHENTICATED, INVALID_LOGIN_MESSAGE));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            throw new ClubException(ErrorCode.NOT_AUTHENTICATED, INVALID_LOGIN_MESSAGE);
        }

        TokenDto tokenDto = generateToken(userEntity);

        emailService.sendEmail(
                loginRequestDto.getEmail(),
                NEW_lLOGIN,
                "New Login activity detect. Let us know if you not the one"
        );


        return LoginResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .expiresIn(tokenDto.getExpiresIn())
                .refreshToken(tokenDto.getRefreshToken())
                .tokenType(tokenDto.getTokenType())
                .build();

    }

    private TokenDto generateToken(UserEntity userEntity) {

        String role = userEntity.getRoleEntity().getName().toUpperCase();
        String clubOrgan = userEntity.getRoleEntity().getClubOrgan().getName().toUpperCase();

        TokenProps tokenProps = TokenProps.builder()
                .id(userEntity.getId())
                .username(userEntity.getEmail())
                .roles(List.of(role, clubOrgan))
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(tokenProps);
        String refreshToken = jwtTokenProvider.generateRefreshToken(tokenProps);

        UserLoginToken token = UserLoginToken.builder()
                .userLogin(userEntity)
                .accessKey(accessToken)
                .refreshKey(refreshToken)
                .tokenType(TOKEN_TYPE)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        userLoginTokenRepository.save(token);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TOKEN_TYPE)
                .expiresIn(jwtConfig.getExpiration())
                .build();
    }

    @Transactional
    public void forgotPassword(String email) {

        try {
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            // Remove any existing reset tokens
            passwordResetTokenRepository.findByUserLogin(userEntity)
                    .ifPresent(passwordResetTokenRepository::delete);

            // Generate reset token
            String resetToken = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

            // Save the token to the database
            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .token(resetToken)
                    .userLogin(userEntity)
                    .expiryDate(expiryDate)
                    .build();

            passwordResetTokenRepository.save(passwordResetToken);


        } catch (ResourceNotFoundException e) {
            // Don't throw exception to prevent email enumeration attacks
            log.warn("Password reset requested for non-existent email: {}", email);
        } catch (Exception e) {
            log.error("Unexpected error during password reset request for email: {}", email, e);
            throw new RuntimeException("Failed to process password reset request", e);
        }
    }

    @Transactional
    public void resetPassword(@Valid PasswordResetRequestDto passwordResetRequestDto) {

        try {
            // Find the token
            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(
                            passwordResetRequestDto.getToken())
                    .orElseThrow(() -> new UnauthorizedException("Invalid reset token"));

            UserEntity userEntity = passwordResetToken.getUserLogin();

            // Check if token is expired
            if (passwordResetToken.isExpired()) {
                passwordResetTokenRepository.delete(passwordResetToken);
                throw new UnauthorizedException("Reset token has expired");
            }

            // Verify the user email matches the token's user
            if (!userEntity.getEmail().equals(passwordResetRequestDto.getEmail())) {
                throw new UnauthorizedException("Email does not match token");
            }

            // Update password
            userEntity.setPassword(passwordEncoder.encode(passwordResetRequestDto.getNewPassword()));
            userRepository.save(userEntity);

            // Delete the used token
            passwordResetTokenRepository.delete(passwordResetToken);

            // Invalidate all existing tokens for security
            userLoginTokenRepository.deleteAllByUserLogin(userEntity);


        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during password reset", e);
            throw new RuntimeException("Failed to reset password due to system error", e);
        }
    }

    @Transactional
    public void logout(String refreshToken) {


        try {
            UserLoginToken token = userLoginTokenRepository.findByRefreshKey(refreshToken)
                    .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

            UserEntity userEntity = token.getUserLogin();

            // Delete the token
            userLoginTokenRepository.delete(token);

        } catch (UnauthorizedException e) {

            throw e;
        } catch (Exception e) {
            // Audit unexpected errors

            log.error("Unexpected error during logout", e);
            throw new RuntimeException("Logout failed due to system error", e);
        }
    }

    @Transactional
    public LoginResponseDto refreshToken(String refreshToken) {

        try {
            UserLoginToken token = userLoginTokenRepository.findByRefreshKey(refreshToken)
                    .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

            UserEntity userEntity = token.getUserLogin();

            // Check if token is expired
            if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                userLoginTokenRepository.delete(token);

                throw new UnauthorizedException("Refresh token expired");
            }

            if (!userEntity.getStatus().equals(Status.ACTIVE)) {
                userLoginTokenRepository.delete(token);
                throw new UnauthorizedException("User account is inactive");
            }

            String role = userEntity.getRoleEntity().getName().toUpperCase();
            String clubOrgan = userEntity.getRoleEntity().getClubOrgan().getName().toUpperCase();

            TokenProps tokenProps = TokenProps.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getEmail())
                    .roles(List.of(role, clubOrgan))
                    .build();

            String newAccessToken = jwtTokenProvider.generateAccessToken(tokenProps);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(tokenProps);

            // Update token in database
            token.setAccessKey(newAccessToken);
            token.setRefreshKey(newRefreshToken);
            token.setExpiresAt(LocalDateTime.now().plusDays(7));
            userLoginTokenRepository.save(token);

            return LoginResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(3600L)
                    .build();

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token refresh", e);
            throw new RuntimeException("Token refresh failed due to system error", e);
        }
    }

    private Response<PaginatedResponse<UserDto>> findAllUsers(int page, int size, Page<UserEntity> pages) {

        PaginatedResponse<UserDto> response = new PaginatedResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setFirst(pages.isFirst());
        response.setLast(pages.isLast());
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setContent(pages.getContent().stream().map(user -> modelMapper
                .map(user, UserDto.class)).toList());

        return Response.<PaginatedResponse<UserDto>>builder()
                .data(response)
                .status(Status.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .message("Successfully found all users.")
                .build();

    }

}
