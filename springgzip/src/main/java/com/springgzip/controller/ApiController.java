package com.springgzip.controller;

import com.springgzip.dto.UserDto;

import com.springgzip.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/v1/test")
    public ResponseEntity<List<UserDto>> test() {
        List<UserDto> userList = apiService.findUserAll();
        return ResponseEntity.ok(userList);
    }
}
