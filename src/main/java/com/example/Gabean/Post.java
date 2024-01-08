package com.example.Gabean;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Table(name = "GaBean_post") // 데이터베이스 테이블 이름을 지정합니다.
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // 데이터베이스에서 해당 컬럼의 제약 조건을 지정할 수 있습니다.
    private String title;

    @Column(nullable = false, length = 1000) // 내용이 길 수 있으니 length를 넉넉하게 설정합니다.
    private String content;

    // 기본 생성자는 JPA에서 필요합니다.
    public Post() {
    }

    // 필드를 초기화하는 생성자를 추가할 수 있습니다.
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter 및 Setter 메소드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pub_date", nullable = false, updatable = false)
    private Date pubDate = new Date();

    // getters and setters
    public Date getPubDate() {
        return pubDate;
    }

    // 필요한 경우 toString, equals, hashCode 메소드를 오버라이드할 수 있습니다.
}
