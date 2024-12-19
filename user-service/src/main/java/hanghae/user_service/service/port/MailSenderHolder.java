package hanghae.user_service.service.port;

public interface MailSenderHolder {

    void sendAuthCode(String email, String authCode);
}
