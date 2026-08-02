package com.one_love_international_club.poll;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PollService {

    private final PollRepository pollRepository;
    private final ModelMapper modelMapper;
    private final SecurityService securityService;

    @Transactional
    public Response<PollDto> createPoll(PollDto pollDto) {

        PollEntity pollEntity = new PollEntity();

        UserEntity currentUser = securityService.getCurrentUser();

        pollEntity.setOptions(pollDto.getOptions());
        pollEntity.setVotes(pollDto.getVotes());
        pollEntity.setQuestion(pollDto.getQuestion());
        pollEntity.setCloseDate(pollDto.getCloseDate());
        pollEntity.setCreatedBy(currentUser);

        PollEntity save = pollRepository.save(pollEntity);

        return Response.<PollDto>builder()
                .data(modelMapper.map(save, PollDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.CREATED.value())
                .message("Poll created successfully.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Transactional
    public Response<PollDto> vote(UUID pollId, String option) {
        UserEntity currentUser = securityService.getCurrentUser();
        PollEntity poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        List<UUID> votedBys = poll.getVotedBy();

        if(votedBys.contains(currentUser.getId())) {
            throw new ClubException(ErrorCode.VALIDATION_ERROR, "You have already voted.");
        }

        Map<String, Integer> votes = poll.getVotes();

        if (Objects.isNull(votes)) {
            votes = new HashMap<>();
            poll.setVotes(votes);
        }

        votes.remove(null);

        int currentCount = votes.getOrDefault(option, 0);
        votes.put(option, currentCount + 1);

        AtomicInteger total = new AtomicInteger();
        votes.forEach((k, v) -> {
            if (!k.equalsIgnoreCase("total_votes")) {
                total.addAndGet(v);
            }
        });

        votes.put("total_votes", total.get());

        poll.setVotes(votes);
        poll.getVotedBy().add(currentUser.getId());
        PollEntity save = pollRepository.save(poll);

        return Response.<PollDto>builder()
                .timestamp(LocalDateTime.now())
                .data(modelMapper.map(save, PollDto.class))
                .status(Status.SUCCESSFUL)
                .message("Vote captured successfully.")
                .code(HttpStatus.OK.value())
                .build();
    }

    public Response<PollDto> getPoll(UUID pollId) {
        PollEntity pollEntity = pollRepository
                .findById(pollId)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "Poll not found."));

        return Response.<PollDto>builder()
                .data(modelMapper.map(pollEntity, PollDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Poll found.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public Response<PaginatedResponse<PollDto>> getAllPolls(int page, int size) {
        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<PollEntity> pages = pollRepository.findAll(pageable);

        PaginatedResponse<PollDto> response = new PaginatedResponse<>();
        response.setFirst(pages.isFirst());
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setContent(pages.getContent().stream().map(item -> modelMapper.map(item, PollDto.class)).toList());
        response.setSize(pages.getSize());
        response.setPage(page);

        return Response.<PaginatedResponse<PollDto>>builder()
                .data(response)
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Polls returned successfully.")
                .build();
    }

    public Response<Void> remove(UUID pollId) {
        pollRepository.deleteById(pollId);

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .message("Poll removed successfully.")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .build();
    }

}
