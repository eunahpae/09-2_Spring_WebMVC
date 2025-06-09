package com.ohgiraffers.viewresolver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/*")
public class ResolverController {

    @GetMapping("string")
    public String stringReturning(Model model) {
        model.addAttribute("forwardMessage", "문자열로 뷰 이름 반환함...");

        /*
         * 문자열로 뷰 이름을 반환하는 방식
         * → DispatcherServlet은 ViewResolver를 통해 "result"라는 이름의 뷰를 탐색하게 됨.
         *
         * 예: ThymeleafViewResolver 사용 시, resources/templates/ 를 prefix로 html을 suffix로
         * resources/templates/ + "result" + .html => resources/templates/result.html 파일을
         * 응답 뷰로 성정하라는 의미.
         *
         * 따라서 해당 뷰 파일이 존재해야 정상 렌더링됨.
         */
        return "result";
    }

    @GetMapping("string-redirect")
    public String stringRedirect() {
        /*
         * 접두사로 "redirect:"를 붙이면 내부 forward가 아닌 외부 리다이렉트(302 응답) 방식으로 이동한다.
         * 브라우저 주소창도 변경되며, 새로운 요청으로 간주되어 request scope는 유지되지 않는다.
         */
        return "redirect:/";
    }

    @GetMapping("string-redirect-attr")
    public String stringRedirectFlashAttribute(RedirectAttributes rttr) {
        /*
         * RedirectAttributes는 리다이렉트 시 데이터를 임시로 저장할 수 있는 기능 제공.
         *
         * - addFlashAttribute() 사용 시, 세션에 일시적으로 데이터를 저장했다가
         *   다음 요청에서 한 번만 사용할 수 있도록 자동으로 전달됨 (request scope처럼 사용 가능)
         *
         * - 내부적으로 세션에 저장되므로 동일한 key값이 기존 세션에 있을 경우 충돌 가능성 있음.
         */
        rttr.addFlashAttribute("flashMessage1", "리다이렉트 attr 사용하여 redirect...");
        return "redirect:/";
    }

    @GetMapping("modelandview")
    public ModelAndView modelAndViewReturning(ModelAndView mv) {
        /*
         * ModelAndView는 모델 데이터와 뷰 이름을 함께 설정할 수 있는 객체.
         *
         * - addObject(): Model에 데이터를 담는 메서드
         * - setViewName(): View 이름을 설정
         *
         * DispatcherServlet이 핸들러로부터 ModelAndView를 반환받으면,
         * 내부 ViewResolver를 통해 뷰를 렌더링하고 모델 데이터를 함께 전달함.
         */
        mv.addObject("forwardMessage", "ModelAndView를 이용한 모델과 뷰 반환");
        mv.setViewName("result");

        return mv;
    }

    @GetMapping("modelandview-redirect")
    public ModelAndView modelAndViewRedirect(ModelAndView mv) {
        /*
         * ModelAndView를 사용할 때도 setViewName()에 "redirect:" 접두사를 사용하면 리다이렉트 가능.
         * → "redirect:/"는 루트 경로로 이동한다는 의미.
         */
        mv.setViewName("redirect:/");
        return mv;
    }

    @GetMapping("modelandview-redirect-attr")
    public ModelAndView ModelandViewRedirect(ModelAndView mv, RedirectAttributes rttr) {
        /*
         * RedirectAttributes는 ModelAndView와 함께 사용할 수도 있다.
         *
         * - flashAttribute는 다음 요청의 request scope에서 사용 가능하며,
         *   세션에서 자동 제거됨.
         */
        rttr.addFlashAttribute("flashMessage2", "ModelAndView를 이용한 redirect attr");
        mv.setViewName("redirect:/");
        return mv;
    }
}
