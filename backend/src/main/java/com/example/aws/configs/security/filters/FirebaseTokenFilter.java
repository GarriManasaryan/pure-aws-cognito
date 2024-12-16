package com.example.aws.configs.security.filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class FirebaseTokenFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        // Check if the user is already authenticated (i.e., authentication set in the SecurityContext)
        // видимо, придется это во все след фильтры вставлять
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            // If authentication exists, skip Firebase Token processing
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // все последующие запросы должны иметь куки в запросах, чтобы FirebaseAuth.getInstance().verifyIdToken(idToken) смог проверить креды
        // но первый login запрос уже внутри контроллера сделает verifyIdToken. А текущий FirebaseTokenFilter запускается же при каждом запросе
        // поэтому для login его надо тут пропустить, контроллер проверит и утсанвоит куки, а остальные уже с куки получат данные
        String idToken = getTokenFromCookies(request);

        if (idToken != null) {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                System.out.println(decodedToken);

                // Extract user ID or other claims from the token
                String uid = decodedToken.getUid();

                // Create Spring Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set Authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
