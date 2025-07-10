
// src/main/java/EmailUtil.java
import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import java.util.Properties;

public class EmailUtil {
    private static final String FROM_EMAIL = "2981443172@qq.com"; // 发件人邮箱
    private static final String PASSWORD = "oocqvhwaqfvkdead"; // 发件人密码或应用专用密码

    public static void sendVerificationCode(String toEmail, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.qq.com"); // 邮箱服务器，如smtp.gmail.com
        props.put("mail.smtp.port", "587"); // 邮箱服务器端口

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("电子商城注册验证码");
        message.setText("您的验证码是: " + code + "，有效期为5分钟，请勿告知他人。");

        Transport.send(message);
    }
}
