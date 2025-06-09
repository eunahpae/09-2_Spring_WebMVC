package com.ohgiraffers.handlermethod;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * 다양한 방식의 요청 파라미터 처리와 세션, 바디, 헤더, 쿠키 등 HTTP 요소를 활용하는 컨트롤러 예시
 */
@Controller
@RequestMapping("/first/*")
@SessionAttributes("id") // 모델에 "id"가 추가되면 자동으로 세션에도 저장
public class FirstController {

    /**
     * [GET] /first/regist 요청 처리
     * 반환 타입이 void이므로 ViewResolver는 "/first/regist" 요청의 마지막 경로인 "regist"를 뷰 이름으로 사용
     * → 예: templates/first/regist.html
     */
    @GetMapping("regist")
    public void regist() {
    }

    /**
     * [POST] /first/regist 요청 처리
     * WebRequest를 이용하여 폼 데이터(name, price, categoryCode) 추출 후 메시지를 모델에 담아 뷰로 전달
     */
    @PostMapping("regist")
    public String registMenu(Model model, WebRequest request) {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));

        String message = name + "을(를) 신규 메뉴 목록의 " + categoryCode + "번 카테고리에 " + price + "원으로 등록하셨습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    /**
     * [GET] /first/modify 요청 처리
     * → templates/first/modify.html 뷰로 이동
     */
    @GetMapping("modify")
    public void modify() {
    }

    /**
     * [POST] /first/modify 요청 처리
     * 단일 요청 파라미터를 @RequestParam으로 추출
     * required=false와 defaultValue를 적절히 설정하여 안정적으로 값 처리 가능
     */
    @PostMapping("modify")
    public String modifyMenuPrice(Model model,
        @RequestParam(required = false) String modifyName,
        @RequestParam(defaultValue = "0") int modifyPrice) {
        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "원으로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    /**
     * [POST] /first/modifyAll 요청 처리
     * 모든 요청 파라미터를 Map으로 수신하여 유연하게 처리
     * 다만 모든 값이 문자열이므로 타입 변환 시 주의 필요
     */
    @PostMapping("modifyAll")
    public String modifyMenu(Model model, @RequestParam Map<String, String> parameters) {
        String modifyMenu = parameters.get("modifyName2");
        int modifyPrice = Integer.parseInt(parameters.get("modifyPrice2"));

        String message = "메뉴의 이름을 " + modifyMenu + "(으)로, 가격을 " + modifyPrice + "원 으로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    /**
     * [GET] /first/search 요청 처리
     * → templates/first/search.html 뷰로 이동
     */
    @GetMapping("search")
    public void search() {
    }

    /**
     * [POST] /first/search 요청 처리
     * @ModelAttribute를 이용해 DTO 객체에 폼 데이터 바인딩 및 모델에 자동 저장
     * @ModelAttribute("menu")로 명시적으로 키 이름 지정
     */
    @PostMapping("search")
    public String searchMenu(@ModelAttribute("menu") MenuDTO menu) {
        System.out.println(menu); // 콘솔 출력 확인
        return "first/searchResult";
    }

    /**
     * [GET] /first/login 요청 처리
     * → templates/first/login.html 뷰로 이동
     */
    @GetMapping("login")
    public void login() {
    }

    /**
     * [POST] /first/login1 요청 처리
     * HttpSession을 직접 사용하여 세션에 사용자 ID 저장
     */
    @PostMapping("login1")
    public String sessionTest1(HttpSession session, @RequestParam String id) {
        session.setAttribute("id", id);
        return "first/loginResult";
    }

    /**
     * [GET] /first/logout1 요청 처리
     * invalidate()로 세션 완전 종료
     */
    @GetMapping("logout1")
    public String logoutTest1(HttpSession session) {
        session.invalidate();
        return "first/loginResult";
    }

    /**
     * [POST] /first/login2 요청 처리
     * @SessionAttributes("id")에 의해 model에 "id"가 추가되면 session에도 자동 등록됨
     */
    @PostMapping("login2")
    public String sessionTest2(Model model, @RequestParam String id) {
        model.addAttribute("id", id);
        return "first/loginResult";
    }

    /**
     * [GET] /first/logout2 요청 처리
     * SessionAttributes 기반 세션은 SessionStatus의 setComplete() 호출로 만료
     */
    @GetMapping("logout2")
    public String logoutTest2(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "first/loginResult";
    }

    /**
     * [GET] /first/body 요청 처리
     * → templates/first/body.html 뷰로 이동
     */
    @GetMapping("body")
    public void body() {
    }

    /**
     * [POST] /first/body 요청 처리
     * - @RequestBody: 요청 본문 전체를 문자열로 읽음 (예: JSON 또는 x-www-form-urlencoded)
     * - @RequestHeader: 요청 헤더에서 정보 추출
     * - @CookieValue: 클라이언트의 쿠키 값 추출 (선택적으로 사용 가능)
     */
    @PostMapping("body")
    public void bodyTest(@RequestBody String body,
        @RequestHeader("content-type") String contentType,
        @CookieValue(required = false, value = "JSESSIONID") String sessionID) {
        System.out.println("body = " + body);
        System.out.println("contentType = " + contentType);
        System.out.println("sessionID = " + sessionID);
    }
}
