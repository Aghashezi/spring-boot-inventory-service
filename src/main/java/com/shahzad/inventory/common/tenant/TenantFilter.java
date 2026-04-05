package com.shahzad.inventory.common.tenant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String path = httpReq.getRequestURI();

        // Exclude Swagger UI, OpenAPI, and static resources from tenant check
        if (path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars") ||
            path.startsWith("/favicon.ico")) {
            chain.doFilter(request, response);
            return;
        }

        String tenantId = httpReq.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing X-Tenant-Id header\"}");
            ((jakarta.servlet.http.HttpServletResponse) response).setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }
        TenantContext.setTenantId(tenantId);
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
