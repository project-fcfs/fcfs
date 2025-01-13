/*
package hanghae.user_service.infrastructure.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SystemPersonalDataEncryptorTest extends IntegrationInfraTestSupport {

    @Autowired
    private SystemPersonalDataEncryptor encryptor;

    @Test
    @DisplayName("같은 값을 입력하면 디코딩이 가능하다")
    void decodeSameValue() throws Exception {
        // given
        String value = "dafasdfewifhewf";
        String encodeData = encryptor.encodeData(value);
        System.out.println("encodeData = " + encodeData);

        // when
        String result = encryptor.decodeData(encodeData);

        // then
        assertThat(result).isEqualTo(value);
    }

}*/
