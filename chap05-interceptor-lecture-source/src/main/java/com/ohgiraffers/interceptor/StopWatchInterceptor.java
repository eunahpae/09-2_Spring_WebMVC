package com.ohgiraffers.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class StopWatchInterceptor implements HandlerInterceptor {

    private final MenuService menuService;

    // 생성자를 통해 MenuService 의존성 주입
    public StopWatchInterceptor(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 컨트롤러의 핸들러 메소드가 호출되기 **이전**에 실행되는 메소드.
     * 요청 처리 전 로직을 수행하며, 여기서는 요청 시작 시각을 기록한다.
     *
     * 예: 인증 체크, 로깅, 성능 측정 시작 시점 기록 등
     *
     * @param request  클라이언트의 HTTP 요청 객체
     * @param response 서버의 HTTP 응답 객체
     * @param handler  실제 실행될 컨트롤러의 핸들러 객체
     * @return true 반환 시 컨트롤러로 요청이 전달되며, false 반환 시 처리 중단
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        System.out.println("preHandle 호출함...");
        long startTime = System.currentTimeMillis();  // 현재 시간 저장 (요청 시작 시간)
        request.setAttribute("startTime", startTime); // postHandle로 전달하기 위해 요청 범위에 저장
        return true;
    }

    /**
     * 컨트롤러의 핸들러 메소드가 실행된 **이후**, 뷰 렌더링 전에 호출되는 메소드.
     * 여기서는 요청 처리에 걸린 시간을 계산해 ModelAndView에 추가한다.
     *
     * 예: 성능 로그 기록, 뷰에 처리 시간 표시 등
     *
     * @param request      클라이언트의 HTTP 요청 객체
     * @param response     서버의 HTTP 응답 객체
     * @param handler      실제 실행된 컨트롤러의 핸들러 객체
     * @param modelAndView 컨트롤러가 반환한 모델 및 뷰 정보
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {

        long startTime = (long) request.getAttribute("startTime");  // 요청 시작 시간 조회
        long endTime = System.currentTimeMillis();                  // 현재 시간 (요청 종료 시간)

        request.removeAttribute("startTime");  // 요청 속성 정리

        modelAndView.addObject("interval", endTime - startTime);  // 처리 시간(ms)을 모델에 추가
        System.out.println("postHandle 호출함...");
    }

    /**
     * 뷰 렌더링까지 모든 요청 처리가 완료된 후 **항상 실행되는** 메소드.
     * 예외 발생 여부와 관계없이 호출되며, 자원 해제나 최종 로깅 등에 사용된다.
     *
     * @param request  클라이언트의 HTTP 요청 객체
     * @param response 서버의 HTTP 응답 객체
     * @param handler  실제 실행된 컨트롤러의 핸들러 객체
     * @param ex       처리 중 발생한 예외 객체 (없을 경우 null)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion 호출함...");
        menuService.method();
    }
}
