package com.springcache.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Entity
@Getter
public class FavoriteMenu implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userId;
    private String menuName;

    @Builder
    public FavoriteMenu(String userId, String menuName) {
        this.userId = userId;
        this.menuName = menuName;
    }

}
