package com.one_love_international_club.file_upload;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.Base64Validator;
import com.one_love_international_club.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final UploadRepository uploadRepository;
    private final SecurityService securityService;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    private final static String UPLOAD_PATH = "uploads";


    @Transactional
    public Response<UploadDto> upload(UploadDto uploadDto) {

        boolean validBase64 = Base64Validator.isValidBase64(uploadDto.getFile());

        if (!validBase64) {
            throw new ClubException(ErrorCode.VALIDATION_ERROR,
                    "File is not a valid base64 string");
        }

        Map<String, String> uploaded = fileService.uploadBase64Image(uploadDto.getFile(), UPLOAD_PATH);

        UploadEntity uploadEntity = modelMapper
                .map(uploadDto, UploadEntity.class);

        UserEntity currentUser = securityService.getCurrentUser();

        uploadEntity.setUploadedBy(currentUser);
        uploadEntity.setFile(uploaded.get("fileUrl"));
        uploadEntity.setFilePublicId(uploaded.get("publicId"));

        UploadEntity save = uploadRepository.save(uploadEntity);

        uploadDto = modelMapper.map(save, UploadDto.class);
        uploadDto.setUploadedBy(getUploadedBy(currentUser));

        return Response.<UploadDto>builder()
                .message("File uploaded successfully")
                .data(uploadDto)
                .code(201)
                .timestamp(save.getCreatedAt())
                .build();

    }


    public Response<UploadDto> getUpload(UUID id) {

        UserEntity currentUser = securityService.getCurrentUser();

        UploadEntity uploadEntity = uploadRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Upload with id: " + id + " not found"));

        if (!Objects.equals(currentUser.getId(), uploadEntity.getUploadedBy().getId())) {

            throw new ClubException(ErrorCode.ACCESS_DENIED,
                    "You are not authorized to view this upload");
        }

        return Response.<UploadDto>builder()
                .message("Upload found")
                .data(modelMapper.map(uploadEntity, UploadDto.class))
                .code(200)
                .timestamp(uploadEntity.getCreatedAt())
                .build();
    }


    public Response<PaginatedResponse<UploadDto>> getAllUploads(int page, int size) {

        Pageable pageable = PageRequest
                .of(page, size, Sort.by("createdAt").descending());

        Page<UploadEntity> pages = uploadRepository.findAll(pageable);

        PaginatedResponse<UploadDto> response = new PaginatedResponse<>();

        response.setLast(pages.isLast());
        response.setTotalElements(pages.getTotalElements());
        response.setSize(size);
        response.setContent(
                pages.getContent()
                        .stream()
                        .map(item -> modelMapper.map(item, UploadDto.class)).toList()
        );
        response.setPage(page);
        response.setFirst(pages.isFirst());

        return Response.<PaginatedResponse<UploadDto>>builder()
                .message("All uploads retrieved successfully")
                .data(response)
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }


    private UploadDto.UploadedBy getUploadedBy(UserEntity user) {

        return UploadDto.UploadedBy.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }
}
