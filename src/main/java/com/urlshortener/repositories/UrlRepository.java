package com.urlshortener.repositories;

import com.urlshortener.models.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {

    Optional<Url> findByUrlOriginal(String urlOriginal);

    Optional<Url> findByUrlShort(String urlShort);

    Optional<Url> findByUrlShortEndingWith(String urlShort);

}
