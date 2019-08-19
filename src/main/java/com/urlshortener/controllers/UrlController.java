package com.urlshortener.controllers;

import com.urlshortener.models.UrlRequest;
import com.urlshortener.models.UrlDTO;
import com.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping()
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/v1")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String create(@Valid @RequestBody UrlRequest urlRequest , HttpServletRequest request){
        return urlService.create(urlRequest.getUrl(), request.getRequestURL().toString());
    }

    @GetMapping("v1/access")
    public UrlDTO getUrlAcess(@RequestParam String urlShort) {
        return urlService.getByUrlShort(urlShort);
    }

    @GetMapping("/{urlPath}")
    public RedirectView getUrl(@PathVariable("urlPath") String urlPath) {
        urlService.createOrUpdateUrlAccess(urlPath);
        return urlService.redirectToUrlOriginal(urlPath);
    }
}
