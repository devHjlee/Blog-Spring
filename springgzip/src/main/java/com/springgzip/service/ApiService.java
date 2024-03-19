package com.springgzip.service;

import com.springgzip.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ApiService {
    public List<UserDto> findUserAll() {
        List<UserDto> userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            userList.add(new UserDto(UUID.randomUUID().toString(),random.nextInt()));
        }
        return userList;
    }
}
