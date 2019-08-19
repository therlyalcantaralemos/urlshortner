package com.urlshortener.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.models.UrlAccess;
import com.urlshortener.models.Url;
import com.urlshortener.models.UrlDTO;
import com.urlshortener.repositories.UrlRepository;
import com.urlshortener.services.exceptions.ObjectNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UrlService(UrlRepository urlRepository, ObjectMapper objectMapper) {
        this.urlRepository = urlRepository;
        this.objectMapper = objectMapper;
    }

    public String create(String urlOriginal, String urlLocal){
        Optional<Url> urlShort = urlRepository.findByUrlOriginal(urlOriginal);
        String urlShortener;

        if(urlShort.isEmpty()){
            urlShortener = getUrlShortener(urlLocal);
            urlRepository.save(Url.builder()
                    .urlOriginal(urlOriginal)
                    .urlShort(urlShortener)
                    .access(Collections.singletonList(UrlAccess.builder().acessNumber(0).date(LocalDateTime.now()).build()))
                    .build());
        }else{
            urlShortener = urlShort.get().getUrlShort();
        }
        return urlShortener;
    }

    private String getUrlShortener(String urlLocal){
        return urlLocal + RandomStringUtils.random(8, true, true);
    }

    private Url getUrlShortFromUrlPath(String urlPath){
        return urlRepository.findByUrlShortEndingWith(urlPath).orElseThrow(ObjectNotFoundException::new);
    }

    public RedirectView redirectToUrlOriginal(String urlPath){
        Url url = getUrlShortFromUrlPath(urlPath);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url.getUrlOriginal());
        return redirectView;
    }

    public void createOrUpdateUrlAccess(String urlPath){
        LocalDateTime localDate = LocalDateTime.now();
        LocalDate dateFromDateTime = localDate.toLocalDate();

        Url url = getUrlShortFromUrlPath(urlPath);
        List<UrlAccess> urlAccess = url.getAccess();
        List<UrlAccess> access;

        access = urlAccess.stream()
                     .filter(x -> x.getDate().toLocalDate().isEqual(dateFromDateTime))
                     .peek(y -> y.setAcessNumber(y.getAcessNumber() + 1)).collect(Collectors.toList());

        if(access.isEmpty()) {
            urlAccess.add(UrlAccess.builder().acessNumber(1).date(localDate).build());
        }
        urlRepository.save(url);
    }

    public UrlDTO getByUrlShort(String urlShort){
        Url url = urlRepository.findByUrlShort(urlShort).orElseThrow(ObjectNotFoundException::new);
        return objectMapper.convertValue(url, UrlDTO.class);
    }

}
