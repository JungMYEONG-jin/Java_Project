package kakao.getCI.springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

    public void send(SimpleMailMessage simpleMessage) throws MailException {

    }

    public void send(SimpleMailMessage[] simpleMessages) throws MailException {

    }
}
