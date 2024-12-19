package hanghae.user_service.service.port;

import org.springframework.scheduling.annotation.Async;

public interface MailSenderHolder {
    @Async("customTaskExecutor")
    void sendAuthCode(String email, String authCode);
}
