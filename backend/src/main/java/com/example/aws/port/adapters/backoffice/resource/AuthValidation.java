package com.example.aws.port.adapters.backoffice.resource;

import com.example.aws.application.UserService;
import com.example.aws.domain.Product;
import com.example.aws.domain.User;
import com.example.aws.domain.UserRole;
import com.example.aws.port.adapters.persistence.PostgresqlProducts;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.builder.qual.NotCalledMethods;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class AuthValidation {

    private UserService userService;

    public AuthValidation(UserService userService) {
        this.userService = userService;
    }

    public record AuthResponse(String message){};

    @PostMapping("/api/login")
    public AuthResponse login(@RequestBody String idToken, HttpServletResponse response) {
        try {
            // Validate the token with Firebase Admin SDK
            idToken = idToken.replaceAll("\"", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            // Set the token in an HTTP-only cookie
            Cookie cookie = new Cookie("session", idToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // Ensure this is set in production (HTTPS)
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 hour expiry
            // если SameSite, то setSecure(true)! иначе не ставит + Chrome как раз подсказал по ошибке
            cookie.setAttribute("SameSite", "None"); // Allow cross-origin cookie usage
            response.addCookie(cookie);

//            // Set a non-HTTP-only cookie for debugging
//            Cookie debugCookie = new Cookie("debug_session", idToken);
//            debugCookie.setHttpOnly(false); // Accessible via JavaScript
//            debugCookie.setSecure(true); // Not restricted to HTTPS (optional)
//            debugCookie.setPath("/");
//            debugCookie.setMaxAge(60 * 60); // 1 hour expiry
//            // если SameSite, то setSecure(true)! иначе не ставит + Chrome как раз подсказал по ошибке
//            debugCookie.setAttribute("SameSite", "None"); // Allow cross-origin cookie usage
//            response.addCookie(debugCookie);

            // Redirect to homepage
            response.setStatus(200);

//            // add users
//            updateUsers(decodedToken);

            return new AuthResponse("ok");
//            response.getWriter().write("Authorized");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Return 401 Unauthorized response
            var authRespBody = new AuthResponse("not ok");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid Token");
            response.setStatus(401);
            return authRespBody;
        }
    }

    private void updateUsers(FirebaseToken decodedToken){
        var user = new User(decodedToken.getUid(), decodedToken.getName(), decodedToken.getEmail(), UserRole.VIEWER);
        if (userService.getUserById(user.id()) == null){
            userService.addUser(user);
        }

    }

}
