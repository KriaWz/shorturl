package com.jiuzhang.url.repo;

import com.jiuzhang.url.domain.LongToShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LongToShortRepository extends JpaRepository<LongToShort, Long> {

    Optional<LongToShort> findByLongUrl(String longUrl);

    Optional<LongToShort> findByShortUrl(String shortUrl);
}
