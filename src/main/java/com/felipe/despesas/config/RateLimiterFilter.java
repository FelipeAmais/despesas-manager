package com.felipe.despesas.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket criarBalde() {
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));

        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, refill))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        Bucket balde = buckets.computeIfAbsent(ip, k -> criarBalde());

        if(!balde.tryConsume(1)){
            response.setStatus(429);
            response.getWriter().write("Tente novamente mais tarde");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
