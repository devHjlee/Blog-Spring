package com.springcache.repository;

import com.springcache.domain.FavoriteMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteMenu,Long> {
    List<FavoriteMenu> findByUserId(String userId);
}
