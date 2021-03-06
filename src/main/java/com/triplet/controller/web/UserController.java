package com.triplet.controller.web;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triplet.bean.Feedback;
import com.triplet.bean.UserInfo;
import com.triplet.model.User;
import com.triplet.service.impl.MyUser;

@PropertySource("classpath:messages.properties")
@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

	@Value("${msg_success_update}")
	private String msg_success_update;

	@Value("${msg_error_update}")
	private String msg_error_update;

	@Value("${msg_success_mail}")
	private String msg_success_mail;

	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		MyUser currentUser = loadCurrentUser();
		if (currentUser == null || currentUser.getId() != id) {
			return "redirect:/login?accessDenied";
		}
		User user = userService.findById(currentUser.getId());
		UserInfo userInfo = new UserInfo(user);
		model.addAttribute("userInfo", userInfo);
		return "views/web/users/user";
	}

	@PostMapping("/{id}/update")
	public String saveOrUpdate(@PathVariable("id") int id, Model model, UserInfo userInfo, BindingResult result,
			final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = msg_error_update;

		User user = userService.findById(userInfo.getId());

		User userUpdate = userInfo.convertToUser();

		user.setAddress(userUpdate.getAddress());
		user.setAvatar(userUpdate.getAvatar());
		user.setPhone(userUpdate.getPhone());
		user.setFullname(userUpdate.getFullname());

		if (userService.saveOrUpdate(user) == null) {
			return handleRedirect(redirectAttributes, typeCss, message, "/users/" + user.getId());
		}

		typeCss = "success";
		message = msg_success_update;
		return handleRedirect(redirectAttributes, typeCss, message, "/users/" + user.getId());
	}

	@RequestMapping(value = "/{id}/update", params = { "addAddress" })
	public String addAddress(UserInfo userInfo, final BindingResult bindingResult) {
		if (userInfo.getAddresses() == null)
			userInfo.setAddresses(new ArrayList<String>());
		userInfo.getAddresses().add(new String());
		return "views/web/users/user";
	}

	@RequestMapping(value = "/{id}/update", params = { "removeAddress" })
	public String removeAddress(UserInfo userInfo, final BindingResult bindingResult, final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("removeAddress"));
		userInfo.getAddresses().remove(rowId.intValue());
		return "views/web/users/user";
	}

	@PostMapping("/{id}/send-feedback")
	public String sendFeedback(@ModelAttribute("feedback") Feedback feedback,
			final RedirectAttributes redirectAttributes) {

		MyUser currentUser = loadCurrentUser();
		User user = userService.findById(currentUser.getId());
		feedback.setUser(user);

		emailService.sendFeedbackMail(feedback);

		String typeCss = "success";
		String message = msg_success_mail;
		return handleRedirect(redirectAttributes, typeCss, message, "/contact");
	}
}
