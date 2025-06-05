package com.ohgiraffers.requestmapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * DispatcherServlet은 클라이언트의 웹 요청을 수신하면,
 * @Controller 어노테이션이 지정된 클래스의 핸들러 메소드에게 처리를 위임한다.
 * 이때 어떤 메소드가 호출될지는 @RequestMapping 또는 관련 어노테이션의 설정에 따라 결정된다.
 */
@Controller
public class MethodMappingTestController {

    /**
     * 1. 요청 메소드 방식 미지정
     * - method 속성을 지정하지 않으면 GET, POST 등 모든 HTTP 요청 방식에 대해 매핑된다.
     * - URL이 "/menu/regist"일 경우, 어떤 방식이든 이 메소드가 실행됨
     */
    @RequestMapping("/menu/regist")
    public String registMenu(Model model) {

        /**
         * Model 객체는 컨트롤러에서 View로 데이터를 전달하는 역할을 한다.
         * addAttribute(key, value) 메소드로 전달할 데이터를 설정할 수 있으며,
         * 설정된 데이터는 View(JSP 등)에서 ${key} 형태로 참조할 수 있다.
         */
        model.addAttribute("message", "신규 메뉴 등록용 핸들러 메소드 호출함...");
        return "mappingResult"; // View 이름
    }

    /**
     * 2. 요청 메소드 방식 지정
     * - method 속성을 이용하여 GET 방식의 요청만 처리하도록 제한함
     * - "/menu/modify" 경로로 GET 요청이 들어오면 이 메소드가 호출됨
     */
    @RequestMapping(value = "/menu/modify", method = RequestMethod.GET)
    public String modifyMenu(Model model) {
        model.addAttribute("message", "메뉴 수정용 핸들러 메소드 호출함...");
        return "mappingResult";
    }

    /**
     * 3. 요청 메소드 전용 어노테이션
     * - 요청 메소드별 전용 어노테이션 정리
     *   ┌──────────────┬────────────────────┐
     *   │   요청 메소드   │     어노테이션 종류     │
     *   ├──────────────┼────────────────────┤
     *   │     GET      │    @GetMapping     │
     *   │     POST     │    @PostMapping    │
     *   │     PUT      │    @PutMapping     │
     *   │   DELETE     │   @DeleteMapping   │
     *   │    PATCH     │   @PatchMapping    │
     *   └──────────────┴────────────────────┘
     * - 가독성이 좋고 명확한 의도를 표현할 수 있어 추천되는 방식이다.
     *
     * 아래는 동일한 URL("/menu/delete")에 대해 어노테이션 방식을 각각 다르게(GET/POST) 처리하는 예시이다.
     */

    /**
     * GET 방식으로 "/menu/delete" 요청이 들어왔을 때 처리
     */
    @GetMapping("/menu/delete")
    public String getDeleteMenu(Model model){
        model.addAttribute("message", "GET 방식의 메뉴 삭제용 핸들러 메소드 호출함...");
        return "mappingResult";
    }

    /**
     * POST 방식으로 "/menu/delete" 요청이 들어왔을 때 처리
     */
    @PostMapping("/menu/delete")
    public String postDeleteMenu(Model model){
        model.addAttribute("message", "POST 방식의 메뉴 삭제용 핸들러 메소드 호출함...");
        return "mappingResult";
    }

}
