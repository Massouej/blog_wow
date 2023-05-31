package fr.simplon.blog_wow.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @PostMapping("logout")
    public void logout() {
        SecurityContextHolder.clearContext(); // Efface le contexte de sécurité actuel
    }
}
