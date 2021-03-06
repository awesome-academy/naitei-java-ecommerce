package com.triplet.service.impl;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.triplet.bean.Feedback;
import com.triplet.model.User;
import com.triplet.service.EmailService;

public class EmailServiceImpl extends BaseServiceImpl implements EmailService {

	private static final Logger logger = Logger.getLogger(EmailServiceImpl.class);

	private static final String FEEDBACK_MAIL_CENTER = "nguyen.thanh.huyen@sun-asterisk.com";

	@Override
	@Async
	public void sendFeedbackMail(Feedback feedback) {

		Locale locale = LocaleContextHolder.getLocale();
		Context ctx = new Context(locale);
		ctx = prepareContext(ctx, feedback);

		MimeMessage mimeMessage = getMailSender().createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			mimeMessageHelper.setTo(FEEDBACK_MAIL_CENTER);
			mimeMessageHelper.setSubject(feedback.getSubject());

			// Create the HTML body using Thymeleaf
			String htmlContent = "";
			SpringTemplateEngine templateEngine = getTemplateEngine();
			htmlContent = templateEngine.process("views/web/contacts/email-contact.html", ctx);
			mimeMessageHelper.setText(htmlContent, true);

			// Send email
			getMailSender().send(mimeMessage);

		} catch (Exception e) {
			logger.error("Error sending email!" + e);
		}
	}

	private Context prepareContext(Context ctx, Feedback feedback) {
		// Prepare the context and variable
		User user = feedback.getUser();
		ctx.setVariable("id", user.getId());
		ctx.setVariable("username", user.getUsername());
		ctx.setVariable("fullname", user.getFullname());
		ctx.setVariable("address", user.getAddress());
		ctx.setVariable("phone", user.getPhone());
		ctx.setVariable("email", user.getEmail());
		ctx.setVariable("content", feedback.getContent());
		return ctx;
	}
}