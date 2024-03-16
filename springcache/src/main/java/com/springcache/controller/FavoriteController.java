package com.springcache.controller;

import com.springcache.domain.FavoriteMenu;
import com.springcache.service.FavoriteMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteMenuService favoriteMenuService;

    @GetMapping("/favorite")
    public List<FavoriteMenu> getFavoriteMenu() {
        return favoriteMenuService.findByUserId("user123");
    }

    @GetMapping("/saveFavorite")
    public void saveFavorite() {
        favoriteMenuService.saveMenu("user123","test");
    }
}
