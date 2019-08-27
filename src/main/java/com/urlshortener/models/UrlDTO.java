package com.urlshortener.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UrlDTO {
    private String urlOriginal;
    private String urlShort;
    private List<UrlAccess> access;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
