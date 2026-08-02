package com.one_love_international_club.post;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Post Controller", description = "Controller class for all post related endpoints.")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('Secretary')")
    public ResponseEntity<Response<PostDto>> createPost(
            @Valid @RequestBody PostDto postDto) {
        Response<PostDto> response = postService.create(postDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @PutMapping
    @PreAuthorize("hasRole('Secretary')")
    public ResponseEntity<Response<PostDto>> updatePost(
            @Valid @RequestBody PostDto postDto) {
        Response<PostDto> response = postService.update(postDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @GetMapping("/{postId}")
    public ResponseEntity<Response<PostDto>> getPost(
            @PathVariable("postId") UUID postId) {
        Response<PostDto> response = postService.getPost(postId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<PostDto>>> getAllPost(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size
    ) {
        Response<PaginatedResponse<PostDto>> response = postService.getAllPosts(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('Secretary')")
    public ResponseEntity<Response<Void>> deletePost(
            @PathVariable("postId") UUID postId) {
        Response<Void> response = postService.deletePost(postId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @GetMapping("/total-viewed/{postId}")
    public ResponseEntity<Long> getTotalViewed(
            @PathVariable("postId") UUID postId) {
        Long response = postService.getTotalViewed(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
}
