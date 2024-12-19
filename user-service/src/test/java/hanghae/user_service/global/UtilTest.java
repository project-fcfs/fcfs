package hanghae.user_service.global;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    @DisplayName("O")
    void 이름_test() throws Exception {
        // given
        Object input = null;

        // when
        String result = (String) input;

        // then
        System.out.println("result = " + result);
    }
}
