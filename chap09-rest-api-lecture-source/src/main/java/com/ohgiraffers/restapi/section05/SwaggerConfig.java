package com.ohgiraffers.restapi.section05;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Swagger(OpenAPI 3) 설정 클래스
 *
 * comment. Swagger 란?
 * Swagger는 REST API 문서를 자동 생성해주는 라이브러리
 * Spring Boot 에서는 springdoc-openapi를 통해 OpenAPI 3.0 명세 기반으로 연동할 수 있다.
 *
 * - 이 클래스는 OpenAPI 문서 설정을 정의한다.
 * - @Configuration : 해당 클래스가 스프링 설정 클래스임을 명시
 * - @EnableWebMvc : Swagger UI 경로 등을 위한 WebMvc 기능 활성화
 */
@Configuration
@EnableWebMvc
public class SwaggerConfig {

    /**
     * Swagger/OpenAPI 문서 정의용 빈 등록
     *
     * @return OpenAPI 인스턴스
     */
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
            // components(): 보안 설정, 스키마 등을 정의할 수 있는 부분 (현재는 빈 설정)
            .components(new Components())
            // info(): API 문서의 기본 정보(제목, 설명, 버전 등) 설정
            .info(swaggerInfo());
    }

    /**
     * OpenAPI 문서 Info 설정 메서드
     *
     * @return Info 객체 (문서 메타데이터)
     */
    private Info swaggerInfo() {

        return new Info()
            .title("ohgiraffers API")                      // 문서 제목
            .description("spring boot swagger 연동 테스트") // 문서 설명
            .version("1.0.0");                              // 문서 버전
    }
}
