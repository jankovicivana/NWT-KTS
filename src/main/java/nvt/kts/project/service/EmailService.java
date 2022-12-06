package nvt.kts.project.service;

import nvt.kts.project.model.User;
import nvt.kts.project.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailService {

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("rentalappteam6@gmail.com");
        mailSender.setPassword("qxoizxexuemjfgow");
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

    JavaMailSenderImpl javaMailSender = mailSender();

    @Autowired
    private Environment env;

    @Autowired
    private TokenUtils tokenUtils;

    @Async
    public void sendAccountActivation(User u) throws MessagingException {
        MimeMessageHelper mail = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "UTF-8");
        mail.setTo(u.getEmail());
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Uber account activation");

        String token = tokenUtils.generateToken(u.getUsername());
        String link = "https://localhost:4200/activate/" + token;
        mail.setText("<html>\n" +
                "    <body>\n" +
                "        <div style=\"margin: 50px;\">\n" +
                "            <div style=\"background-color: rgb(99, 216, 99);height: 55px;\">\n" +
                "                    <h1 style=\"margin-left:15px; color: white;\">Activate account</h1>\n" +
                "            </div>\n" +
                "            <div style=\"margin-top: 10px;\">\n" +
                "                <div style=\"margin: 25px;\">\n" +
                "                Dear "+u.getName()+",\n" +
                "                <br/>\n" +
                "                Click the link below to activate your account: \n" +
                "                <br/>\n" +
                "                "+ link + " \n" +
                "                <br/>\n" +
                "                Regards,\n" +
                "                <br/>\n" +
                "                <span >Uber app team</span>\n" +
                "            </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "    </body>\n" +
                "</html>",true);
        javaMailSender.send(mail.getMimeMessage());
    }

    @Async
    public void sendPasswordChange(User u) throws MessagingException {
        MimeMessageHelper mail = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "UTF-8");
        mail.setTo(u.getEmail());
        mail.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        mail.setSubject("Uber account password forgotten");

        String token = tokenUtils.generateToken(u.getUsername());
        String link = "https://localhost:4200/changePassword/" + token;
        mail.setText("<html>\n" +
                "    <body>\n" +
                "        <div style=\"margin: 50px;\">\n" +
                "            <div style=\"background-color: rgb(99, 216, 99);height: 55px;\">\n" +
                "                    <h1 style=\"margin-left:15px; color: white;\">Change your password</h1>\n" +
                "            </div>\n" +
                "            <div style=\"margin-top: 10px;\">\n" +
                "                <div style=\"margin: 25px;\">\n" +
                "                Dear "+u.getName()+",\n" +
                "                <br/>\n" +
                "                Click the link below to change your password: \n" +
                "                <br/>\n" +
                "                "+ link + " \n" +
                "                <br/>\n" +
                "                Regards,\n" +
                "                <br/>\n" +
                "                <span >Uber app team</span>\n" +
                "            </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "    </body>\n" +
                "</html>",true);
        javaMailSender.send(mail.getMimeMessage());
    }


}