package com.ohgiraffers.requestmapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
/* 1. Class 레벨 매핑 */
@RequestMapping("/order/*")
public class ClassMappingController {

    @GetMapping("/regist")
    public String registOrder(Model model){
        model.addAttribute("message","GET 방식의 주문 등록용 핸들러 메소드 호출함..");
        return "mappingResult";
    }

    /* 2. 여러 개의 패턴 매핑 */
    @RequestMapping(value = {"modify","delete"}, method = RequestMethod.POST)
    public String modifyAndDelete(Model model){
        model.addAttribute("message", "POST 방식의 주문 정보 수정과 삭제 공통 처리용 핸들러 메소드 호출함..");
        return "mappingResult";
    }

    /* 3. path vatiable */
    /* @PathVatiable 어노테이션을 이용해 요청 path로부터 변수를 받아올 수 있다.
     * path variable로 전달 되는 {변수명} 값은 반드시 매개변수명과 동일해야 한다.
     * 만약 동일하지 않으면 @PathVariable("이름")을 설정해주어야 한다.
     * 이는 Rest형 웹 서비스를 설계할 때 유용하게 사용된다.
     */
    @GetMapping("/detail/{orderNo")
    public String selectOrderDetail(Model model, @PathVariable int orderNo){

        /* parsing 불가능한 PathVariable이 전달되면 400번 에러가 발생한다.
         * PathVariable이 없으면 해당 핸들러 메소드를 찾지 않는다. */
        model.addAttribute("message", orderNo + "번 주문 상세 내용 조회용 핸들러 메소드 호출함..");
        return "mappingResult";

    }

}
