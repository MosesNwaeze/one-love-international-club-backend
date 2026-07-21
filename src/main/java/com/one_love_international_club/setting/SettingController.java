package com.one_love_international_club.setting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/setting")
@RequiredArgsConstructor
@Slf4j
public class SettingController {
    private static final String FILE_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String FILE_NAME_WORD = "ONE_LOVE_INT'L_NOBLE_CLUB_constitution_latest_26042026.docx";
    private static final String FILE_NAME_PDF = "ONE_LOVE_INT'L_NOBLE_CLUB_constitution_latest_26042026.pdf";

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
}
