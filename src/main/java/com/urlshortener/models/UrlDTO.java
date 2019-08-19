package com.urlshortener.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UrlDTO {
    private String urlOriginal;
    private String urlShort;
    private List<UrlAccess> access;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
