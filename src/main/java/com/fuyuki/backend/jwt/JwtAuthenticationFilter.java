package com.fuyuki.backend.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> protectedPaths = Arrays.asList(
            "/ums/user/info",
            "/ums/user/update",
            "/ums/user/ban/*",
            "/ums/user/unban/*",
            "/ums/user/upload_avatar",
            "/ums/user/delete_avatar/*",
            "/post/create",
            "/post/update",
            "/post/delete/*",
            "/comment/add_comment",
            "/comment/delete_comment/*",
            "/relationship/subscribe/*",
            "/relationship/unsubscribe/*",
            "/relationship/validate/*",
            "/file/upload"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (isProtectedUrl(request)) {
//                System.out.println(request.getMethod());
                if (!request.getMethod().equals("OPTIONS")) request = JwtUtil.validateTokenAndAddUserIdToHeader(request);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedUrl(HttpServletRequest request) {

        for (String passedPath : protectedPaths) {
            if (pathMatcher.match(passedPath, request.getServletPath())) {
                return true;
            }
        }
        return false;
    }

}