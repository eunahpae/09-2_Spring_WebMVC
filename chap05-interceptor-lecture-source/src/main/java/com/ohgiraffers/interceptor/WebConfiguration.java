package com.ohgiraffers.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 스프링 MVC 의 인터셉터 등록 메소드.
     * StopWatchInterceptor 인스턴스를 생성하여 모든 요청 경로에 대해 동작하도록 등록한다.
     * 단, 정적 리소스(css, images, js)와 /error 경로는 인터셉터 적용 대상에서 제외한다.
     *
     * @param registry 인터셉터를 등록할 수 있는 인터셉터 레지스트리 객체
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new StopWatchInterceptor(new MenuService()))
            // 모든 1단계 하위 경로에 대해 인터셉터를 적용
            .addPathPatterns("/*")
            // 정적 리소스(css, images, js) 경로는 인터셉터 적용 제외
            .excludePathPatterns("/css/**")
            .excludePathPatterns("/images/**")
            .excludePathPatterns("/js/**")
            // 스프링 내부 에러 처리 경로는 제외하여 인터셉터 중복 적용 방지
            .excludePathPatterns("/error");
    }
}
