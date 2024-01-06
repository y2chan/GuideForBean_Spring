package com.example.Gabean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem {
    private String title;
    private String description;
    private String link;
    private String pubDate;

    // getter, setter

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.replace("<b>", "<strong>").replace("</b>", "</strong>");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.replace("<b>", "<strong>").replace("</b>", "</strong>");
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        // API에서 받아온 날짜 형식을 원하는 형식으로 변환
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        try {
            Date date = originalFormat.parse(pubDate);
            this.pubDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.pubDate = pubDate;
        }
    }
}