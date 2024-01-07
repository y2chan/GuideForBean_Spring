package com.example.Gabean;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;



@Controller
public class NewsController {
    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @RequestMapping(value = {"/news", "/m/m_news"})
    public String newsSearch(@RequestParam(value = "query", defaultValue = "") String query, Model model, HttpServletRequest request) {
        if (!query.isEmpty()) {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.naver.com/v1/search/news.json")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Naver-Client-Id", clientId)
                    .defaultHeader("X-Naver-Client-Secret", clientSecret)
                    .build();

            List<NewsItem> newsItems = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", query)
                            .queryParam("display", 20)
                            .build())
                    .retrieve()
                    .bodyToMono(NewsResponse.class)
                    .block()
                    .getItems();

            model.addAttribute("news", newsItems);
        } else {
            model.addAttribute("news", new ArrayList<>());
        }

        model.addAttribute("query", query);

        String viewName = "news";

        if (request.getRequestURI().startsWith("/m/")) {
            viewName = "mobile/m_news";
        }

        return viewName;
    }
}
