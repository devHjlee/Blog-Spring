package com.springcache.service;

import com.springcache.domain.FavoriteMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FavoriteMenuServiceTest {
    @Autowired
    private FavoriteMenuService favoriteMenuService;

    @Test
    void test() {
        String userId = "user123";
        for(int i = 0; i < 1000; i++) {
            favoriteMenuService.saveMenu(userId,"TestMenu"+i);
        }
        List<FavoriteMenu> dbList = favoriteMenuService.findByUserId(userId);
        List<FavoriteMenu> cacheList = favoriteMenuService.findByUserId(userId);

        Assertions.assertEquals(dbList.size(),cacheList.size());

    }
}