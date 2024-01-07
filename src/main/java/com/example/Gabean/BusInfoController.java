package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BusInfoController {

    @Value("${api.key}")
    private String apiKey;

    @GetMapping(value = {"/info_bus", "/m/m_info_bus"})
    public String getBusInfo(Model model) {
        String[] stIds = {"222001597", "110000055", "110000018", "110000017", "107000057", "110000183"};
        String[] busRouteIds = {"100100039", "100100165"};
        String[] ords = {"16", "22", "18", "24", "106", "44", "42", "40", "38"};

        List<Map<String, String>> direction1 = new ArrayList<>();
        List<Map<String, String>> direction2 = new ArrayList<>();

        // RestTemplate을 생성할 때 DefaultUriBuilderFactory를 사용하여 인코딩을 변경
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);

        List<Map<String, String>> allBusInfos = new ArrayList<>();

        for (String stId : stIds) {
            for (String busRouteId : busRouteIds) {
                for (String ord : ords) {
                    String url = UriComponentsBuilder
                            .fromUriString("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute")
                            .queryParam("serviceKey", apiKey)
                            .queryParam("stId", stId)
                            .queryParam("busRouteId", busRouteId)
                            .queryParam("ord", ord)
                            .build().toUriString();

                    // API 호출하여 XML 응답 받기
                    String xmlResponse = restTemplate.getForObject(url, String.class);

                    try {
                        Document xmlDocument = convertStringToXMLDocument(xmlResponse);
                        NodeList stNmList = xmlDocument.getElementsByTagName("stNm");
                        NodeList rtNmList = xmlDocument.getElementsByTagName("rtNm");
                        NodeList arrmsg1List = xmlDocument.getElementsByTagName("arrmsg1");
                        NodeList arrmsg2List = xmlDocument.getElementsByTagName("arrmsg2");

                        for (int i = 0; i < stNmList.getLength(); i++) {
                            Map<String, String> busInfo = new HashMap<>();
                            busInfo.put("stNm", stNmList.item(i).getTextContent());
                            busInfo.put("rtNm", rtNmList.item(i).getTextContent());
                            busInfo.put("arrmsg1", arrmsg1List.item(i).getTextContent());
                            busInfo.put("arrmsg2", arrmsg2List.item(i).getTextContent());

                            if (busInfo.get("stNm").equals("삼육대앞") || busInfo.get("stNm").equals("삼육대후문.논골.포레나별내")) {
                                direction1.add(busInfo);
                            } else {
                                direction2.add(busInfo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 오류 처리 로직 (로그 출력, 모델에 오류 메시지 추가 등)
                    }
                }
            }
        }

        model.addAttribute("direction1", direction1);
        model.addAttribute("direction2", direction2);

        // 뷰 선택 로직
        String viewName = "info_bus";
        String requestURL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_info_bus";
        }

        return viewName;
    }

    // XML 문자열을 Document 객체로 파싱하는 메소드
    private Document convertStringToXMLDocument(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
