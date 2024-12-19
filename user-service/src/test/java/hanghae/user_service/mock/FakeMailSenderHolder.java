package hanghae.user_service.mock;

import hanghae.user_service.service.port.MailSenderHolder;
import java.util.HashMap;
import java.util.Map;

public class FakeMailSenderHolder implements MailSenderHolder {
    private Map<String, String> data = new HashMap<>();

    @Override
    public void sendAuthCode(String email, String authCode) {
        data.put(email, authCode);
    }

    public boolean isSendAuthCode(String email) {
        return data.containsKey(email);
    }
}
