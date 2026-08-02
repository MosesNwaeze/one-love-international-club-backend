package com.one_love_international_club.poll;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/polls")
@Tag(name = "Poll Controller", description = "Controller class for all poll related endpoints.")
@RequiredArgsConstructor
public class PollController {
    private final PollService pollService;

    @PostMapping
    public ResponseEntity<Response<PollDto>> createPoll(
            @Valid @RequestBody PollDto pollDto
    ) {
        Response<PollDto> poll = pollService.createPoll(pollDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(poll.getCode()))
                .body(poll);
    }

    @PatchMapping
    public ResponseEntity<Response<PollDto>> vote(
            @Valid @RequestBody PollVoteDto pollDto
    ) {
        Response<PollDto> poll = pollService.vote(pollDto.getPollId(), pollDto.getOption());

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(poll.getCode()))
                .body(poll);
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Response<PollDto>> getPoll(@PathVariable("pollId") UUID pollId) {
        Response<PollDto> poll = pollService.getPoll(pollId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(poll.getCode()))
                .body(poll);
    }


    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<PollDto>>> getAllPoll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        Response<PaginatedResponse<PollDto>> poll = pollService.getAllPolls(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(poll.getCode()))
                .body(poll);
    }


    @DeleteMapping("/{pollId}")
    public ResponseEntity<Response<Void>> removePoll(@PathVariable("pollId") UUID pollId) {
        Response<Void> poll = pollService.remove(pollId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(poll.getCode()))
                .body(poll);
    }

}
