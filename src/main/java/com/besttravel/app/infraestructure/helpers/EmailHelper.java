package com.besttravel.app.infraestructure.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class EmailHelper {
	private final JavaMailSender mailSender;

	public void sendEmail(String to, String name, String product) {
		MimeMessage message = mailSender
			.createMimeMessage();

		String htmlContent = this.readHtmlTemplate(name, product);

		try {
			message.setFrom(new InternetAddress("cgalindorivera@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, to);
			message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
			mailSender.send(message);
		} catch (MessagingException e) {
			log.error("Error to send email. " + e.getMessage());
		}
	}

	private String readHtmlTemplate(String name, String product) {
		try(var lines = Files.lines(TEMPLATE_PATH)) {
			String html = lines.collect(Collectors.joining());
			return html.replace("{name}", name).replace("{product}", product);
		} catch (IOException e) {
			log.error("Cant read html template. " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	 private final Path TEMPLATE_PATH = Paths.get("src/main/resources/email/email_template.html");
}
