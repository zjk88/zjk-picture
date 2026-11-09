package com.zjk.zjkpicturebackend;

import com.qcloud.cos.COSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.profiles.active=test",
        "spring.datasource.url=jdbc:mysql://localhost:3306/zjk_picture",
        "spring.datasource.username=test",
        "spring.datasource.password="
})
class ZjkPictureBackendApplicationTests {

    @MockBean
    private COSClient cosClient;

    @Test
    void contextLoads() {
    }

}
