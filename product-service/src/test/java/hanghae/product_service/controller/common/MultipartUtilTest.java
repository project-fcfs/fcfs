package hanghae.product_service.controller.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class MultipartUtilTest {

    @Test
    @DisplayName("유효하지 않은 파일 확장자라면 에러를 반환한다")
    void InvalidFileExtension() throws Exception {
        // given
        String file = "test.123";
        MockMultipartFile input = createFile(file);

        // then
        assertThatThrownBy(() -> MultipartUtil.validateImageFile(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 파일 확장자입니다.");

    }

    @Test
    @DisplayName("확장자가 없다면 에러를 반환한다")
    void NoExtensionError() throws Exception {
        // given
        String file = "test";
        MockMultipartFile input = createFile(file);

        // then
        assertThatThrownBy(() -> MultipartUtil.validateImageFile(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 파일 확장자입니다.");
    }

    private MockMultipartFile createFile(String originalFilename){
        return new MockMultipartFile("name", originalFilename, MediaType.IMAGE_PNG_VALUE,
                originalFilename.getBytes(StandardCharsets.UTF_8));
    }

}