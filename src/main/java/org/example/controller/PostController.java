package org.example.controller;

import com.google.gson.Gson;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void getAllPosts(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        final var gson = new Gson();
        final var post = service.getById(id);
        response.getWriter().print(gson.toJson(post));
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        try {
            response.setContentType(APPLICATION_JSON);
            final var gson = new Gson();
            final var post = gson.fromJson(body, Post.class);
            final var data = service.save(post);
            response.getWriter().print(gson.toJson(data));
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            response.getWriter().print(e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(service.getById(id)) + "\n Post was deleted!");
        service.removeById(id);
    }
}