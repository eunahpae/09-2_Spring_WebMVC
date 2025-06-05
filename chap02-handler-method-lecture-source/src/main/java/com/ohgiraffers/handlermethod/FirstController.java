package com.ohgiraffers.handlermethod;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/first/*")
public class FirstController {

    /*
     * [GET] /first/regist 요청을 처리하는 핸들러 메소드
     *
     * 반환 타입이 void이므로 요청 경로 "/first/regist"에서 마지막 경로인 "regist"가 뷰 이름이 된다.
     * 따라서 ViewResolver는 "first/regist"라는 이름의 뷰를 탐색하게 된다.
     * 예: src/main/resources/templates/first/regist.html (Thymeleaf 사용 시)
     */
    @GetMapping("regist")
    public void regist() {
    }

    /*
     * [POST] /first/regist 요청을 처리하는 핸들러 메소드
     *
     * 클라이언트가 form을 통해 전송한 요청 파라미터(name, price, categoryCode)를 WebRequest를 이용해 추출한다.
     *
     * - WebRequest: Spring에서 제공하는 추상화된 요청 정보 객체로, HttpServletRequest보다 간결하며 서블릿 API에 종속되지 않는다.
     *               요청 파라미터를 getParameter() 메소드로 추출할 수 있으며, 테스트나 유연한 처리에 적합하다.
     *
     * - Model: 컨트롤러에서 뷰로 데이터를 전달할 때 사용하는 객체로, key-value 형태로 데이터를 담는다.
     */
    @PostMapping("regist")
    public String registMenu(Model model, WebRequest request) {

        // 요청 파라미터 추출
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));

        // 사용자에게 전달할 메시지 생성
        String message =
            name + "을(를) 신규 메뉴 목록의 " + categoryCode + "번 카테고리에 " + price + "원으로 등록하셨습니다.";

        // 모델에 메시지를 담아 뷰로 전달
        model.addAttribute("message", message);

        // View 이름 반환: templates/first/messagePrinter.html 로 렌더링
        return "first/messagePrinter";
    }

    /*
     * [GET] /first/modify 요청을 처리하는 핸들러 메소드
     *
     * void 반환 시 요청 경로의 마지막 경로인 "modify"가 뷰 이름으로 사용됨
     * => templates/first/modify.html
     */
    @GetMapping("modify")
    public void modify() {
    }

    /*
     * [POST] /first/modify 요청을 처리하는 핸들러 메소드
     *
     * 클라이언트로부터 전달된 요청 파라미터를 @RequestParam 어노테이션을 사용하여 직접 매개변수에 바인딩한다.
     *
     * - @RequestParam:
     *   요청 파라미터의 name 속성과 동일한 이름의 메소드 매개변수에 값을 자동 주입한다.
     *   예: <input name="modifyName"> → @RequestParam String modifyName
     *
     * - 파라미터 이름이 다른 경우:
     *   @RequestParam("form의 name 속성")처럼 명시적으로 이름을 지정해야 한다.
     *
     * - required 속성:
     *   기본값은 true이며, 해당 파라미터가 존재하지 않으면 400 Bad Request 에러가 발생한다.
     *   → required=false 설정 시, 파라미터가 없어도 null 또는 기본형 타입의 경우 에러 없이 처리된다.
     *
     * - defaultValue 속성:
     *   값이 전달되지 않거나 빈 문자열("")이 전달된 경우에도 지정된 기본값을 사용한다.
     *   특히 int, double 등 기본형 타입에는 값이 없을 경우 parsing 예외(NumberFormatException)가 발생할 수 있으므로,
     *   defaultValue를 지정해두는 것이 안정적인 방법이다.
     */

    @PostMapping("modify")
    public String modifyMenuPrice(Model model, @RequestParam(required = false) String modifyName,
        @RequestParam(defaultValue = "0") int modifyPrice) {

        // 수정 결과에 대한 사용자 메시지 생성
        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "원으로 변경하였습니다.";

        // 모델에 메시지 추가
        model.addAttribute("message", message);

        // 메시지를 출력할 뷰 반환
        return "first/messagePrinter";
    }

    /*
     * [POST] /first/modifyAll 요청을 처리하는 핸들러 메소드
     *
     * 여러 개의 요청 파라미터를 @RequestParam과 Map을 함께 사용하여 한 번에 처리할 수 있다.
     *
     * - @RequestParam Map<String, String>:
     *   클라이언트가 전송한 form의 모든 name-value 쌍을 Map에 담아 전달받는다.
     *   → Map의 key는 form의 name 속성값, value는 해당 input에 입력된 문자열 값이다.
     *
     * - 장점:
     *   파라미터가 많은 경우 일일이 @RequestParam 으로 매핑하지 않고 유연하게 처리할 수 있다.
     *
     * - 주의:
     *   모든 값이 문자열(String)로 처리되므로, 숫자 등 필요한 경우 수동으로 파싱해야 하며,
     *   값이 누락되었을 경우 NPE 또는 parsing 예외가 발생할 수 있으므로 적절한 예외 처리가 필요하다.
     */
    @PostMapping("modifyAll")
    public String modifyMenu(Model model, @RequestParam Map<String, String> parameters) {
        String modifyMenu = parameters.get("modifyName2");
        int modifyPrice = Integer.parseInt(parameters.get("modifyPrice2"));
        String message = "메뉴의 이름을 " + modifyMenu + "(으)로, 가격을 " + modifyPrice + "원 으로 변경하였습니다.";

        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

}
