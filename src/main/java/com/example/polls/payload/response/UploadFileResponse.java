package com.example.polls.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UploadFileResponse {
    private String fileName;
    private String fileType;
    private Long id;
    private Long size;
}
