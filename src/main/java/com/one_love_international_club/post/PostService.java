package com.one_love_international_club.post;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.UserRepository;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final SecurityService securityService;


    private static final String UPLOAD_PATH = "posts";

    @Transactional
    public Response<PostDto> create(PostDto postDto) {

        UserEntity currentUser = securityService.getCurrentUser();

        PostEntity postEntity = modelMapper.map(postDto, PostEntity.class);

        if (StringUtils.isNotBlank(postDto.getPostImage())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(postDto.getPostImage(), UPLOAD_PATH);

            postEntity.setPostImage(uploaded.get("fileUrl"));
            postEntity.setPostPublicId(uploaded.get("publicId"));
        }

        postEntity.setCreatedBy(currentUser);

        PostEntity save = postRepository.saveAndFlush(postEntity);

        return Response.<PostDto>builder()
                .data(modelMapper.map(save, PostDto.class))
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .message("Post created successfully")
                .build();
    }

    @Transactional
    public Response<PostDto> update(PostDto postDto) {
        PostEntity post = postRepository
                .findById(postDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "Post not found"));

        modelMapper.map(postDto, post);

        if (StringUtils.isNotBlank(postDto.getPostPublicId()) && StringUtils.isNotBlank(postDto.getPostImage())) {
            fileService.deleteFile(postDto.getPostPublicId());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(postDto.getPostImage(), UPLOAD_PATH);

            post.setPostImage(uploaded.get("fileUrl"));
            post.setPostPublicId(uploaded.get("publicId"));
        }

        PostEntity saved = postRepository.saveAndFlush(post);

        return Response.<PostDto>builder()
                .data(modelMapper.map(saved, PostDto.class))
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .message("Post updated successfully")
                .build();
    }


    @Transactional
    public Response<PostDto> getPost(UUID id) {
        PostEntity postEntity = postRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "Post not found"));

        postEntity.setTotalViewed(postEntity.getTotalViewed() + 1);
        PostEntity save = postRepository.save(postEntity);

        return Response.<PostDto>builder()
                .data(modelMapper.map(save, PostDto.class))
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .message("Post found")
                .build();
    }


    public Response<PaginatedResponse<PostDto>> getAllPosts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<PostEntity> pages = postRepository.findAll(pageable);


        PaginatedResponse<PostDto> response = new PaginatedResponse<>();
        response.setContent(pages.getContent().stream().map(item -> modelMapper.map(item, PostDto.class)).toList());
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setPage(page);
        response.setSize(size);
        response.setLast(pages.isLast());
        response.setFirst(pages.isFirst());

        return Response.<PaginatedResponse<PostDto>>builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .status(Status.SUCCESSFUL)
                .message("All posts found")
                .message("Post found")
                .data(response)
                .build();
    }

    public Long getTotalViewed(UUID postId) {
        return postRepository.getTotalViewed(postId);
    }

    @Transactional
    public Response<Void> deletePost(UUID id) {
        postRepository.deleteById(id);

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .message("Post deleted successfully")
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
