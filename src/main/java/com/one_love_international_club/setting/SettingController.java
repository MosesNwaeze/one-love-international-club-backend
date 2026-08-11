package com.one_love_international_club.setting;

import com.one_love_international_club.setting.dto.AppDto;
import com.one_love_international_club.setting.dto.ObjectiveDto;
import com.one_love_international_club.setting.dto.RoleDto;
import com.one_love_international_club.setting.entity.AppEntity;
import com.one_love_international_club.setting.entity.ObjectiveEntity;
import com.one_love_international_club.setting.repo.AppRepository;
import com.one_love_international_club.setting.repo.ObjectiveRepository;
import com.one_love_international_club.setting.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/setting")
@RequiredArgsConstructor
@Slf4j
public class SettingController {

    private final AppRepository appRepository;
    private final ModelMapper modelMapper;
    private final ObjectiveRepository objectiveRepository;
    private final RoleRepository roleRepository;

    private static final String FILE_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String FILE_NAME_WORD = "ONE_LOVE_INT'L_NOBLE_CLUB_constitution_latest_26042026.docx";
    private static final String FILE_NAME_PDF = "ONE_LOVE_INT'L_NOBLE_CLUB_constitution_latest_26042026.pdf";
    private static final String APP = "ONE LOVE INTERNATIONAL NOBLE CLUB";

    @GetMapping("/download/constitution")
    public ResponseEntity<Resource> constitution() throws IOException {

        Resource resource = new ClassPathResource(FILE_NAME_PDF);

        log.info("constitution file downloaded successfully: {}", resource.getFilename());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/app")
    public ResponseEntity<AppDto> app() {
        Optional<AppEntity> appEntityOptional = appRepository.findByNameIgnoreCase(APP);

        AppDto appDto = appEntityOptional
                .map(app -> modelMapper.map(app, AppDto.class))
                .orElse(null);

        return ResponseEntity.ok(appDto);
    }

    @GetMapping("/objectives")
    public ResponseEntity<List<ObjectiveDto>> objectives() {
        List<ObjectiveDto> objectiveEntities = objectiveRepository
                .findAll()
                .stream()
                .map(obj -> modelMapper.map(obj, ObjectiveDto.class))
                .toList();

        return ResponseEntity.ok(objectiveEntities);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('CHIEF PROVOST')")
    public ResponseEntity<List<RoleDto>> roles() {
        List<RoleDto> roleEntities = roleRepository
                .findAll()
                .stream()
                .map(obj -> modelMapper.map(obj, RoleDto.class))
                .toList();

        return ResponseEntity.ok(roleEntities);
    }


}
