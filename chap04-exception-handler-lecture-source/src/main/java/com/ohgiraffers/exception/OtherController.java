package com.ohgiraffers.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherController {

    /* 다른 controller에서 Exception 이 발생 했을 때는 ExceptionHandlerController에 정의한 @ExceptionHandler가 동작하지 않는다. */
    @GetMapping("other-controller-null")
    public String otherControllerExceptionTest() {
        String str = null;
        System.out.println(str.charAt(0));

        return "/";
    }

    @GetMapping("other-controller-user")
    public String otherUserExceptionTest() throws MemberRegistException {

        boolean check = true;
        if (check) {
            throw new MemberRegistException("당신 같은 사람은 회원으로 받을 수 없습니다.");
        }
        return "/";
    }

    @GetMapping("other-controller-array")
    public String otherArrayExceptionTest() {

        double[] array = new double[0];
        System.out.println("array = " + array[0]);

        return "/";
    }


}
