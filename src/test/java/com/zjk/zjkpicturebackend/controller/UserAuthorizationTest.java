package com.zjk.zjkpicturebackend.controller;

import com.zjk.zjkpicturebackend.aop.AuthInterceptor;
import com.zjk.zjkpicturebackend.constant.UserConstant;
import com.zjk.zjkpicturebackend.exception.BusinessException;
import com.zjk.zjkpicturebackend.exception.ErrorCode;
import com.zjk.zjkpicturebackend.model.entity.User;
import com.zjk.zjkpicturebackend.model.vo.UserVO;
import com.zjk.zjkpicturebackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class, properties = "spring.profiles.active=test")
@Import({AuthInterceptor.class, AopAutoConfiguration.class})
class UserAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void anonymousUserCannotAccessAdminEndpoint() throws Exception {
        when(userService.getLoginUser(any(HttpServletRequest.class)))
                .thenThrow(new BusinessException(ErrorCode.NOT_LOGIN_ERROR));

        mockMvc.perform(get("/user/get").param("id", "1"))
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_LOGIN_ERROR.getCode()));
        verify(userService, never()).getById(1L);
    }

    @Test
    void regularUserCannotAccessAdminEndpoint() throws Exception {
        User loginUser = new User();
        loginUser.setUserRole(UserConstant.DEFAULT_ROLE);
        when(userService.getLoginUser(any(HttpServletRequest.class))).thenReturn(loginUser);

        mockMvc.perform(get("/user/get").param("id", "1"))
                .andExpect(jsonPath("$.code").value(ErrorCode.NO_AUTH_ERROR.getCode()));
        verify(userService, never()).getById(1L);
    }

    @Test
    void adminCanAccessUserDetailsWithoutPasswordHash() throws Exception {
        User loginUser = new User();
        loginUser.setUserRole(UserConstant.ADMIN_ROLE);
        User user = new User();
        user.setUserName("示例用户");
        user.setUserPassword("example-password-hash");
        when(userService.getLoginUser(any(HttpServletRequest.class))).thenReturn(loginUser);
        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/get").param("id", "1"))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.userName").value("示例用户"))
                .andExpect(jsonPath("$.data.userPassword").doesNotHaveJsonPath());
    }

    @Test
    void publicEndpointReturnsOnlyUserView() throws Exception {
        User user = new User();
        user.setUserPassword("example-password-hash");
        UserVO userVO = new UserVO();
        userVO.setUserName("示例用户");
        when(userService.getById(1L)).thenReturn(user);
        when(userService.getUserVO(user)).thenReturn(userVO);

        mockMvc.perform(get("/user/get/vo").param("id", "1"))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.userName").value("示例用户"))
                .andExpect(jsonPath("$.data.userPassword").doesNotHaveJsonPath());
        verify(userService, never()).getLoginUser(any(HttpServletRequest.class));
    }
}
