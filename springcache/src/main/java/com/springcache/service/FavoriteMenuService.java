package com.springcache.service;

import com.springcache.domain.FavoriteMenu;
import com.springcache.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteMenuService {
    private final FavoriteRepository menuRepository;

    @Transactional
    @CacheEvict(value = "FavoriteMenu", key = "#userId")
    public void saveMenu(String userId, String favoriteMenu) {
        menuRepository.save(FavoriteMenu.builder().userId(userId).menuName(favoriteMenu).build());
    }

    @Transactional
    @Cacheable(value = "FavoriteMenu", key = "#userId")
    public List<FavoriteMenu> findByUserId(String userId) {
        return menuRepository.findByUserId(userId);
    }
}
