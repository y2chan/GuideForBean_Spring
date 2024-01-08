package com.example.Gabean;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class MyPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // 여기서 원하는 대로 이름을 변경합니다.
        // 예시: 실제 입력된 이름을 그대로 사용하려면 아래와 같이 반환하면 됩니다.
        return name;
    }
}
