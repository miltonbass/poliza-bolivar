package com.segurosbolivar.polizas.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().startsWith("/h2-console")
                || req.getRequestURI().startsWith("/core-mock")) {
            chain.doFilter(request, response);
            return;
        }

        String key = req.getHeader("x-api-key");
        if (key == null || !key.equals(apiKey)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"API Key inválida o ausente\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}