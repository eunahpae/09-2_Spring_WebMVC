package com.ohgiraffers.crud.menu.model.service;

import com.ohgiraffers.crud.menu.model.dao.MenuMapper;
import com.ohgiraffers.crud.menu.model.dto.CategoryDTO;
import com.ohgiraffers.crud.menu.model.dto.MenuDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuMapper menuMapper;

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    public List<MenuDTO> findAllMenu() {
        return menuMapper.findAllMenu();
    }


    public List<CategoryDTO> findAllCategory() {
        return menuMapper.findAllCategory();
    }

    @Transactional
    public void registNewMenu(MenuDTO newMenu) {
        menuMapper.registNewMenu(newMenu);
    }

    public MenuDTO findMenuByCode(int code) {
        return menuMapper.findMenuByCode(code);
    }

    public void updateMenu(MenuDTO menu) {
        menuMapper.updateMenu(menu);
    }

    public void deleteMenuByCode(int code) {
        menuMapper.deleteMenuByCode(code);
    }
}
