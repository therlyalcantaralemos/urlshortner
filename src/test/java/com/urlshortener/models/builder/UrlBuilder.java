package com.urlshortener.models.builder;

import com.urlshortener.models.Url;
import com.urlshortener.models.UrlAccess;
import com.urlshortener.models.UrlDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class UrlBuilder {

    private Url url;

    public UrlBuilder() {
    }

    public Url getNewUrl(){
       return Url.builder()
                .id("1")
                .urlOriginal("http://www.google.com")
                .urlShort("http://localhost/OkjUj")
                .access(getNewUrlAccess())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public List<UrlAccess> getNewUrlAccess(){
        return Collections.singletonList(UrlAccess.builder()
                .acessNumber(0)
                .date(LocalDateTime.now())
                .build());
    }

    public String getUrlLocal(){
        return "http://localhost/";
    }

    public String getUrlPath(){
        return "OkjUj";
    }

    public Url build() {
        return getNewUrl();
    }

    public UrlDTO getUrlDtofromUrl(Url url){
        return UrlDTO.builder()
                .urlOriginal(url.getUrlOriginal())
                .urlShort(url.getUrlShort())
                .access(url.getAccess())
                .createdAt(url.getCreatedAt())
                .updatedAt(url.getUpdatedAt())
                .build();

    }
}
