package hanghae.user_service.infrastructure.common;

import hanghae.user_service.service.port.MailSenderHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SystemMailSender implements MailSenderHolder {

    private static final String AUTH_CODE_SUBJECT = "[fcfs] 인증번호입니다.";
    private static final String AUTH_CODE_HEADER = "인증 코드는";
    private static final String FOOTER = "입니다";
    private final JavaMailSender mailSender;


    public SystemMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendAuthCode(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(AUTH_CODE_SUBJECT);
        message.setTo(email);
        message.setText(AUTH_CODE_HEADER + authCode + FOOTER);
        mailSender.send(message);
    }
}
