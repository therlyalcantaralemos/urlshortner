package com.urlshortener.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UrlAccess {
    private Integer acessNumber;
    private LocalDateTime date;
}
