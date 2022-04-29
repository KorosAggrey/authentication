package com.kovatech.auth.component;

import com.kovatech.auth.config.MessagingConfig;
import com.kovatech.auth.datalayer.entities.User;
import com.kovatech.auth.models.EmailProperties;
import com.socketLabs.injectionApi.SendResponse;
import com.socketLabs.injectionApi.SocketLabsClient;
import com.socketLabs.injectionApi.message.BasicMessage;
import com.socketLabs.injectionApi.message.EmailAddress;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;*/

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessagingService {
    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);
    @Autowired
    private MessagingConfig config;

    @Autowired
    Configuration configuration;

    /*@SneakyThrows
    public String sendTextEmail(User createdUser) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email(config.getSendingEmail());
        String subject = "The GitBets";
        Email to = new Email("dev.aggrey@gmail.com");
        String emailContent = getEmailContent(createdUser);
        Content content = new Content("text/html", emailContent);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(config.getSendGridKey().trim());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }
    }*/

    public void prepareVerificationMessage(User createdUser){
        String emailContent = null;
        try {
            emailContent = getEmailContent(createdUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        EmailProperties properties = EmailProperties.builder()
                .emailTO(createdUser.getEmail()).Subject("Email Verification OTP for GitBets").htmlContent(emailContent)
                .plainTextContent(null).build();
        sendEmailViaSocketLabs(properties);
    }

    public void sendEmailViaSocketLabs(EmailProperties properties){
        SocketLabsClient client = new SocketLabsClient(config.getSendingId(), config.getSendGridKey());
        BasicMessage message = new BasicMessage();
        message.setSubject(properties.getSubject());
        message.setHtmlBody(properties.getHtmlContent());
        message.setPlainTextBody(properties.getPlainTextContent());
        message.setFrom(new EmailAddress("from@sandbox.socketlabs.dev"));
        message.getTo().add(new EmailAddress("aggreykoros04@gmail.com"));
        try {
            SendResponse response =  client.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getEmailContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("NAME", user.getFirstName() +" "+user.getLastName());
        model.put("CODE", user.getActivationCode());
        configuration.getTemplate("signUp.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
