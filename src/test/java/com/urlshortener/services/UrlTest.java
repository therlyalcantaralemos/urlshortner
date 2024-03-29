package com.urlshortener.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.models.Url;
import com.urlshortener.models.UrlDTO;
import com.urlshortener.models.builder.UrlBuilder;
import com.urlshortener.repositories.UrlRepository;
import com.urlshortener.services.exceptions.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UrlTest {

    @InjectMocks
    private UrlService urlService;
    @Mock
    private UrlRepository urlRepository;
    @Mock
    private ObjectMapper objectMapper;

    private Url url;
    private String urlLocal;
    private String urlPath;

    private UrlBuilder urlBuilder;

    @Before
    public void setup(){
        urlBuilder = new UrlBuilder();
        url = urlBuilder.build();
        urlLocal = urlBuilder.getUrlLocal();
        urlPath = urlBuilder.getUrlPath();
    }

    @Test
    public void shouldCreate(){
        when(urlRepository.findByUrlOriginal(url.getUrlOriginal())).thenReturn(Optional.empty());
        String urlExpected =  urlService.create(url.getUrlOriginal(), urlLocal);

        assertEquals(0, urlExpected.indexOf(urlLocal));
    }

    @Test
    public void sholdNotCreateWhenUrlExists(){
        when(urlRepository.findByUrlOriginal(url.getUrlOriginal())).thenReturn(Optional.of(url));
        String urlExpected = urlService.create(url.getUrlOriginal(), urlLocal);

        assertEquals(0, urlExpected.indexOf(urlLocal));
    }

    @Test
    public void shouldRedirectToUrlOriginal(){
        when(urlRepository.findByUrlShortEndingWith(urlPath)).thenReturn(Optional.of(url));
        RedirectView redirect = urlService.redirectToUrlOriginal(urlPath);

        assertEquals(url.getUrlOriginal(), redirect.getUrl());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotRedirectToUrlOriginalException(){
        when(urlRepository.findByUrlShortEndingWith(urlPath)).thenThrow(ObjectNotFoundException.class);
        urlService.redirectToUrlOriginal(urlPath);
    }

    @Test
    public void shouldCreateOrUpdateUrlAccess(){
        when(urlRepository.findByUrlShortEndingWith(urlPath)).thenReturn(Optional.of(url));
        urlService.createOrUpdateUrlAccess(urlPath);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotCreateOrUpdateUrlAccessException(){
        when(urlRepository.findByUrlShortEndingWith(urlPath)).thenThrow(ObjectNotFoundException.class);
        urlService.createOrUpdateUrlAccess(urlPath);
    }

    @Test
    public void shouldGetByUrlShort(){
        when(urlRepository.findByUrlShort(url.getUrlShort())).thenReturn(Optional.of(url));
        when(objectMapper.convertValue(url, UrlDTO.class)).thenReturn(urlBuilder.getUrlDtofromUrl(url));

        UrlDTO urlDto = urlService.getByUrlShort(url.getUrlShort());

        assertEquals(url.getUrlOriginal(), urlDto.getUrlOriginal());
        assertEquals(url.getUrlShort(), urlDto.getUrlShort());
        assertEquals(url.getAccess(), urlDto.getAccess());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldNotGetByUrlShortException(){
        when(urlRepository.findByUrlShort(urlPath)).thenThrow(ObjectNotFoundException.class);
        urlService.getByUrlShort(url.getUrlShort());
    }
}
