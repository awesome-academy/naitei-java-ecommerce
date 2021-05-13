package com.triplet.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triplet.bean.Feedback;
import com.triplet.bean.UserInfo;
import com.triplet.model.Category;
import com.triplet.model.User;
import com.triplet.utils.AuthFacebookUtils;
import com.triplet.validate.UserValidation;

@PropertySource("classpath:messages.properties")
@Controller
public class HomeController extends BaseController {
	private static final Logger logger = Logger.getLogger(HomeController.class);

	@Value("${msg_error_username_or_email}")
	private String msg_error_username_or_email;

	@Value("${msg_sucess_register}")
	private String msg_sucess_register;

	@GetMapping(value = { "/", "/welcome" })
	public String index(Model model, HttpSession session) {
		logger.info("home page");

		List<Category> roots = categoryService.getRoots();
		session.setAttribute("roots", roots);
		return "views/web/home/index";
	}

	@GetMapping("/login")
	public String login() {
		logger.info("login ne");
		return "views/signin/signin";
	}

	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/welcome";
	}

	@GetMapping(value = "/accessDenied")
	public String accessDenied() {
		return "redirect:/login?accessDenied";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new UserInfo());
		return "views/signup/signup";
	}

	@PostMapping("/register-process")
	public String register(@ModelAttribute("user") UserInfo userInfo, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.info("register");
		UserValidation userVali = new UserValidation();
		userVali.validate(userInfo, result);
		if (result.hasErrors()) {
			return "views/signup/signup";
		} else if (userService.createUser(userInfo.convertToUser()) == false) {
			model.addAttribute("error", msg_error_username_or_email);
			return "views/signup/signup";
		}
		userService.createUser(userInfo.convertToUser());
		redirectAttributes.addFlashAttribute("registersuccess", msg_sucess_register);
		logger.info(redirectAttributes.getAttribute("registersuccess"));
		return "views/signin/signin";
	}

	@PostMapping("/login-facebook")
	public String loginFB(@RequestParam("id") String id, @RequestParam("fullname") String fullname,
			@RequestParam("email") String email, @RequestParam("avatar") String avatar) {
		User user = null;
		// If user not exist, register processing
		if (!userService.checkUsernameExist(id) && !userService.checkEmailExist(email)) {
			user = new User();
			user.setUsername(id);
			user.setFullname(fullname);
			user.setEmail(email);
			user.setAvatar(avatar);
			userService.createUser(user);
		} else {
			// If user exist, load user
			user = userService.findByUsername(id);
		}
		// Login processing
		AuthFacebookUtils authFacebookUtils = new AuthFacebookUtils();
		authFacebookUtils.SetAuthentication(user);

		return "views/web/home/index";
	}

	@GetMapping("/contact")
	public String showContact(Model model) {
		Feedback feedback = new Feedback();
		model.addAttribute("feedback", feedback);
		return "views/web/contacts/contact";
	}

}
