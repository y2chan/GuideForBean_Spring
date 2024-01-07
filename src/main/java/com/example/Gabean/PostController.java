package com.example.Gabean;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository; // PostRepository는 Post 엔티티에 대한 Spring Data JPA 인터페이스입니다.

    @GetMapping(value = {"/sound_kong", "/m/sound_kong"})
    public String soundKong(Model model, HttpServletRequest request) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);

        // 요청된 URL에 따라 다른 뷰를 반환합니다.
        String requestURL = request.getRequestURI();
        if (requestURL.startsWith("/m/")) {
            return "mobile/m_sound_kong"; // 모바일 경로에 대한 뷰
        } else {
            return "sound_kong"; // 기본 경로에 대한 뷰
        }
    }

    // 게시물 작성 폼을 보여주는 메소드
    @GetMapping("/sound_kong_write")
    public ModelAndView showWriteForm() {
        return new ModelAndView("sound_kong_write", "postForm", new PostForm());
    }

    @GetMapping("/m/m_sound_kong_write")
    public ModelAndView mobileshowWriteForm() {
        return new ModelAndView("mobile/sound_kong_write", "postForm", new PostForm());
    }

    // 게시물 작성 요청을 처리하는 메소드
    @PostMapping("/sound_kong_write")
    public String handleWriteForm(@ModelAttribute PostForm postForm) {
        Post newPost = new Post(); // Post 객체를 생성하고 폼의 데이터를 설정합니다.
        newPost.setTitle(postForm.getTitle());
        newPost.setContent(postForm.getContent());
        postRepository.save(newPost); // Post 객체를 저장합니다.
        return "redirect:/sound_kong"; // 게시물 목록 페이지로 리다이렉트합니다.
    }

    @PostMapping("m/m_sound_kong_write")
    public String mobilehandleWriteForm(@ModelAttribute PostForm postForm) {
        Post newPost = new Post(); // Post 객체를 생성하고 폼의 데이터를 설정합니다.
        newPost.setTitle(postForm.getTitle());
        newPost.setContent(postForm.getContent());
        postRepository.save(newPost); // Post 객체를 저장합니다.
        return "redirect:/mobile/m_sound_kong"; // 게시물 목록 페이지로 리다이렉트합니다.
    }

    // 일반 및 모바일 경로 모두 처리합니다.
    @GetMapping(value = {"/sound_kong_detail/{id}", "/m/m_sound_kong_detail/{id}"})
    public String soundKongDetail(@PathVariable("id") Long postId, Model model) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        model.addAttribute("post", post);

        // 뷰 이름을 경로에 따라 다르게 지정합니다.
        String viewName = "sound_kong_detail";
        String requestURL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_sound_kong_detail";
        }

        return viewName;
    }
}
