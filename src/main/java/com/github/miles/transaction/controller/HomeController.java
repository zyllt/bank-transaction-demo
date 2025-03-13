package com.github.miles.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

/**
 * Web controller for serving HTML pages
 * 
 * @author miles.zeng
 * @since 2025-03-13
 */
@Controller
public class HomeController {

    /**
     * Serves the main transaction management page
     * 
     * @return the name of the view to render
     */
    @GetMapping("/")
    public Mono<Rendering> index() {
        return Mono.just(Rendering.redirectTo("/index.html").build());
    }
} 