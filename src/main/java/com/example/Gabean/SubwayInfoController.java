package com.example.Gabean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubwayInfoController {

    @Value("${subway.api.key}")
    private String apiKey;

    @GetMapping(value = {"/info_subway", "/m/m_info_subway"})
    public String getSubwayInfo(Model model) {

        // RestTemplate을 생성할 때 DefaultUriBuilderFactory를 사용하여 인코딩을 변경
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);

        String[] stationNames = {"화랑대(서울여대입구)", "태릉입구", "석계", "별내"};
        Map<String, List<Map<String, String>>> groupedData = new HashMap<>();

        for (int i = 0; i < stationNames.length; i++) {
            String url = UriComponentsBuilder
                    .fromHttpUrl("http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/xml/realtimeStationArrival/0/5/" + stationNames[i])
                    .toUriString();

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                // 서버로부터 받은 응답 문자열이 잘못된 인코딩으로 해석된 경우, 올바른 UTF-8 인코딩으로 변환합니다.
                String xmlResponse = new String(response.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);


                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xmlResponse));
                Document doc = builder.parse(is);

                NodeList statnNmList = doc.getElementsByTagName("statnNm");
                NodeList trainLineNmList = doc.getElementsByTagName("trainLineNm");
                NodeList arvlMsg2List = doc.getElementsByTagName("arvlMsg2");
                NodeList recptnDtList = doc.getElementsByTagName("recptnDt");

                List<Map<String, String>> directionData = new ArrayList<>();
                for (int j = 0; j < statnNmList.getLength(); j++) {
                    Map<String, String> data = new HashMap<>();
                    data.put("statnNm", statnNmList.item(j).getTextContent());
                    data.put("trainLineNm", trainLineNmList.item(j).getTextContent());
                    data.put("arvlMsg2", arvlMsg2List.item(j).getTextContent());
                    data.put("recptnDt", recptnDtList.item(j).getTextContent());
                    directionData.add(data);
                }

                groupedData.put("direction_" + (i + 1), directionData);

                // 콘솔에 해당 방향의 데이터를 출력합니다.
                System.out.println("groupedData['direction_" + (i + 1) + "']: " + directionData);

            } catch (Exception e) {
                // Error handling logic here
                e.printStackTrace();
            }
        }

        model.addAttribute("groupedData", groupedData);
        // 뷰 선택 로직
        String viewName = "info_subway";
        String requestURL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_info_subway";
        }

        return viewName;
    }
}
