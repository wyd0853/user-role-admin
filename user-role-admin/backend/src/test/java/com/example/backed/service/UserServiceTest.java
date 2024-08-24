package com.example.backed.service;

import com.example.backed.entity.User;
import com.example.backed.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUsers() {
        String name = "John";
        LocalDateTime minCreateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime maxCreateTime = LocalDateTime.now();
        int pageNum = 1;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize);

        List<User> expectedUsers = List.of(new User(), new User());

        // Mocking the findUsers method in UserRepository
        when(userRepository.findUsers(name, minCreateTime, maxCreateTime, pageable)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsers(name, minCreateTime, maxCreateTime, pageNum, pageSize);

        // Verify that the findUsers method was called with the correct parameters
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findUsers(name, minCreateTime, maxCreateTime, pageable);
    }

    // 你可以添加更多的测试方法来覆盖 UserService 中的其他方法
}