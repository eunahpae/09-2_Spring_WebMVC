package com.ohgiraffers.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionHandlerController {

    @GetMapping("controller-null")
    public String nullPointerException() {
        String str = null;
        System.out.println(str.charAt(0));  // NullPointerException 발생

        return "/";
    }

    /* 컨트롤러 레벨에서 NullPointerException을 처리하기 위한 예외 처리 메서드.
     * 전역 예외 처리용 클래스를 따로 작성했기 때문에 주석 처리
     */
//    @ExceptionHandler(NullPointerException.class)
//    public String nullPointerExceptionHandler(NullPointerException exception) {
//
//        System.out.println("controller 레벨의 exception 처리");
//        return "error/nullPointer";
//    }

    @GetMapping("controller-user")
    public String userExceptionTest() throws MemberRegistException {

        boolean check = true;
        if (check) {
            // 커스텀 예외 발생
            throw new MemberRegistException("당신 같은 사람은 회원으로 받을 수 없습니다.");
        }
        return "/";
    }

    /* 컨트롤러 레벨에서 사용자 정의 예외(MemberRegistException)를 처리하기 위한 예외 처리 메서드.
     * 예외 객체를 Model에 담아 뷰에서 사용할 수 있게 한다.
     * 전역 예외 처리 클래스를 통해 공통 처리하고 있기 때문에 주석 처리
     */
//    @ExceptionHandler(MemberRegistException.class)
//    public String userExceptionHandler(Model model, MemberRegistException exception) {
//        System.out.println("controller 레벨의 exception 처리");
//        model.addAttribute("exception", exception);
//
//        return "error/memberRegist";
//    }

}
