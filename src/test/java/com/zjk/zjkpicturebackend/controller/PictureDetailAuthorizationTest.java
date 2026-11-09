package com.zjk.zjkpicturebackend.controller;

import com.zjk.zjkpicturebackend.exception.ErrorCode;
import com.zjk.zjkpicturebackend.model.entity.Picture;
import com.zjk.zjkpicturebackend.model.entity.User;
import com.zjk.zjkpicturebackend.model.vo.PictureVO;
import com.zjk.zjkpicturebackend.service.PictureService;
import com.zjk.zjkpicturebackend.service.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PictureController.class, properties = "spring.profiles.active=test")
class PictureDetailAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PictureService pictureService;

    @ParameterizedTest
    @CsvSource({
            "1, guest, true",
            "0, guest, false",
            "2, other, false",
            "0, owner, true",
            "2, admin, true",
            ", guest, false"
    })
    void detailFollowsReviewVisibility(Integer reviewStatus, String viewer, boolean visible) throws Exception {
        long pictureId = 1987654321098765432L;
        Picture picture = new Picture();
        picture.setId(pictureId);
        picture.setUserId(10L);
        picture.setReviewStatus(reviewStatus);
        PictureVO detail = new PictureVO();
        detail.setName("测试图片");
        when(pictureService.getById(pictureId)).thenReturn(picture);
        when(pictureService.getPictureVO(eq(picture), any(HttpServletRequest.class))).thenReturn(detail);

        if (!"guest".equals(viewer)) {
            User user = new User();
            user.setId("owner".equals(viewer) ? 10L : 20L);
            when(userService.getLoginUserPermitNull(any(HttpServletRequest.class))).thenReturn(user);
            when(userService.isAdmin(user)).thenReturn("admin".equals(viewer));
        }

        ResultActions result = mockMvc.perform(get("/picture/get/vo").param("id", String.valueOf(pictureId)));
        if (visible) {
            result.andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.name").value("测试图片"));
        } else {
            result.andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_ERROR.getCode()))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }
}
