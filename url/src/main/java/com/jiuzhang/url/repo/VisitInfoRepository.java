package com.jiuzhang.url.repo;

import com.jiuzhang.url.domain.VisitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitInfoRepository extends JpaRepository<VisitInfo, Long> {

    @Query(value =
            "SELECT longUrl, COUNT(longUrl) as sum\n" +
            "        FROM VISIT_INFO\n" +
            "        GROUP BY longUrl\n" +
            "        order by sum DESC LIMIT 10",
            nativeQuery = true)
    List<Object[]> findLatestSumMax();

    List<VisitInfo> findTop10ByOrderByGmtCreateDesc();
}
