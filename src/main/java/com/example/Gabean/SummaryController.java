package com.example.Gabean;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SummaryController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

    @PostMapping(value = {"/summarize", "/m/m_summarize"})
    public ResponseEntity<String> summarize(@RequestParam String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("document", new HashMap<String, Object>() {{
            put("content", text);
        }});

        requestPayload.put("option", new HashMap<String, Object>() {{
            put("language", "ko");
            put("model", "general");
            put("tone", 2);
            put("summaryCount", 3);
        }});

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                // 여기서 Naver API의 응답을 클라이언트에게 그대로 전달합니다.
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
            } else {
                // 에러 처리
                return ResponseEntity.status(response.getStatusCode()).body("Error: Server returned status code " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // 요약 폼을 보여주는 메서드
    @GetMapping(value = {"/summarize", "/m/m_summarize"})
    public String showSummarizeForm(Model model, HttpServletRequest request) {
        String viewName = "summarize";
        String requestURL = request.getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_summarize"; // 모바일에 맞는 뷰 이름
        }

        // 모델에 필요한 데이터가 있다면 추가
        return viewName;
    }
}
