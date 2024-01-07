package com.example.Gabean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이 어노테이션은 선택적이지만, 클래스의 역할을 명확히 하기 위해 추가할 수 있습니다.
public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository에는 기본적인 CRUD 메소드들이 이미 정의되어 있습니다.
    // 필요한 경우 추가적인 메소드를 여기에 정의할 수 있습니다.
}
