package com.ohgiraffers.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* 전역 예외 처리를 담당하는 클래스
 * @ControllerAdvice 어노테이션은 모든 컨트롤러에 대한 예외를 공통으로 처리할 수 있도록 해준다.
 * 예외 처리 메서드들은 예외 종류에 따라 자동으로 매핑되어 실행된다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /* NullPointerException이 발생했을 때 실행되는 전역 예외 처리 메서드
     * 해당 예외가 발생하면 "error/nullPointer" 뷰 페이지로 포워딩된다.
     */
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        System.out.println("Global 레벨의 NullPointerException 처리");

        return "error/nullPointer";
    }

    /* 사용자 정의 예외인 MemberRegistException이 발생했을 때 실행되는 처리 메서드
     * 예외 객체를 Model에 담아 뷰에서 메시지를 출력할 수 있도록 한다.
     * 해당 예외가 발생하면 "error/memberRegist" 뷰 페이지로 포워딩된다.
     */
    @ExceptionHandler(MemberRegistException.class)
    public String userExceptionHandler(Model model, MemberRegistException exception) {
        System.out.println("Global 레벨의 MemberRegistException 처리");
        model.addAttribute("exception", exception);

        return "error/memberRegist";
    }

    /* Exception은 모든 예외 클래스의 최상위 타입으로,
     * 앞에서 명시적으로 처리하지 않은 예외가 발생했을 때 이 메서드가 실행된다.
     * 일종의 기본 예외 처리자 역할을 하며, "error/default" 뷰 페이지로 포워딩된다.
     */
    @ExceptionHandler(Exception.class)
    public String defaultExceptionHandler(Exception exception) {
        System.out.println("Global 레벨의 기타 Exception 처리");
        return "error/default";
    }

}
