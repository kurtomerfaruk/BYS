package tr.bel.gaziantep.bysweb.webservice.api.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:23
 */
@ApplicationScoped
public class RateLimitService {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    public boolean isAllowed(Integer apiUserId,
                             String path,
                             String method,
                             int limitPerMinute) {

        String minuteKey = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        String key = apiUserId + ":" + path + ":" + method + ":" + minuteKey;

        requestCounts.putIfAbsent(key, new AtomicInteger(0));

        int current = requestCounts.get(key).incrementAndGet();

        return current <= limitPerMinute;
    }
}