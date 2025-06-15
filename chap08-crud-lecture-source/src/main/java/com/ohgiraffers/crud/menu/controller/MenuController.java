package com.ohgiraffers.crud.menu.controller;

import static java.awt.SystemColor.menu;

import com.ohgiraffers.crud.menu.model.dto.CategoryDTO;
import com.ohgiraffers.crud.menu.model.dto.MenuDTO;
import com.ohgiraffers.crud.menu.model.service.MenuService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    public String findMenuList(Model model) {

        List<MenuDTO> menuList = menuService.findAllMenu();
        for (MenuDTO menus : menuList) {
            System.out.println(menus);
        }

        model.addAttribute("menuList", menuList);

        return "menu/list";

    }

    @GetMapping("/regist")
    public void registPage() {
    }

    @GetMapping(value = "category", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        menuService.findAllCategory().forEach(System.out::println);
        return menuService.findAllCategory();
    }

    @PostMapping("/regist")
    public String registMenu(MenuDTO newMenu, RedirectAttributes rttr) {
        menuService.registNewMenu(newMenu);
        rttr.addFlashAttribute("successMessage", "신규 메뉴 등록에 성고하셨습니다!");
        return "redirect:/menu/list";
    }

    @GetMapping("/detail/{code}")
    public String showMenuDetail(@PathVariable("code") int code, Model model) {
        MenuDTO menu = menuService.findMenuByCode(code);
        model.addAttribute("menu", menu);
        return "menu/detail";
    }

    @GetMapping("/edit/{code}")
    public String showEditForm(@PathVariable("code") int code, Model model) {
        // System.out.println("code = " + code);
        MenuDTO menu = menuService.findMenuByCode(code);
        model.addAttribute("menu", menu);
        return "menu/edit";
    }

    @PostMapping("/edit")
    public String updateMenu(MenuDTO updateMenu, RedirectAttributes rttr) {
        menuService.updateMenu(updateMenu);
        rttr.addFlashAttribute("successMessage", "메뉴가 수정되었습니다.");
        return "redirect:/menu/detail/" + updateMenu.getCode();
    }

    @GetMapping("/delete/{code}")
    public String deleteMenu(@PathVariable("code") int code,RedirectAttributes rttr) {
        System.out.println("code = " + code);
        menuService.deleteMenuByCode(code);
        rttr.addFlashAttribute("successMessage", "메뉴 삭제가 완료되었습니다.");
        return "redirect:/menu/list";
    }

}
