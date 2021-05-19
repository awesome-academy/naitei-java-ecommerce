package com.triplet.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triplet.bean.UserInfo;
import com.triplet.model.User;
import com.triplet.service.impl.MyUser;
import com.triplet.utils.ExcelFileUtils;
import com.triplet.utils.ExcelReportView;
import com.triplet.validate.UserValidation;

@PropertySource("classpath:messages.properties")
@Controller(value = "admin-user")
@RequestMapping("/admin/users")
public class UserController extends BaseController {

	@Value("${msg_error_username_or_email}")
	private String msg_error_username_or_email;

	@Value("${msg_success_add_user}")
	private String msg_success_add_user;

	@Value("${msg_error_file}")
	private String msg_error_file;
	
	@Value("${msg_processing_file}")
	private String msg_processing_file;

	Logger logger = Logger.getLogger(UserController.class);

	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<User> users = userService.loadUsers();
		model.addAttribute("users", users);
		return "views/admin/user/users";
	}

	@GetMapping("/new")
	public String register(Model model) {
		model.addAttribute("user", new UserInfo());
		model.addAttribute("status", "add");
		return "views/admin/user/user-form";
	}

	@PostMapping(value = "/create")
	public String addOneUser(@ModelAttribute("user") UserInfo userInfo, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "Input error";
		UserValidation userVali = new UserValidation();
		userVali.validate(userInfo, result);
		if (result.hasErrors()) {
			return handleRedirect(redirectAttributes, typeCss, message, "/admin/users/new");
		}
		if (userService.createUser(userInfo.convertToUser()) == false) {
			message = msg_error_username_or_email;
			return handleRedirect(redirectAttributes, typeCss, message, "/admin/users/new");
		}
		userService.createUser(userInfo.convertToUser());
		typeCss = "success";
		message = msg_success_add_user;
		return handleRedirect(redirectAttributes, typeCss, message, "/admin/users");
	}

	@PostMapping(value = "/upload-excel")
	public String uploadExcel(@RequestParam("file") MultipartFile file, final RedirectAttributes redirectAttributes) {
		String typeCss = "success";
		String message = msg_processing_file;
		if (!ExcelFileUtils.hasExcelFormat(file)) {
			message = msg_error_file;
			return handleRedirect(redirectAttributes, typeCss, message, "/admin/users");
		}
		ImportExcelFile(file);
		return handleRedirect(redirectAttributes, typeCss, message, "/admin/users");
	}

	@Async
	private void ImportExcelFile(MultipartFile file) {
		List<User> users = new ArrayList<User>();
		List<UserInfo> listUserInfo = new ArrayList<>();
		ExcelFileUtils excelFileUtil = new ExcelFileUtils();
		List<Integer> linesError = new ArrayList<Integer>();
		MyUser user = loadCurrentUser();
		String message ="Invalid information in email, password or username";
		
		try {
			listUserInfo = excelFileUtil.convertToUserInfos(file);
			users = userService.convertToUsers(listUserInfo);
		}
		catch(Exception e){
			emailService.sendImportExcelError(message, linesError, user.getFullname(), file.getOriginalFilename());
			logger.error("Error in save file excel"+e);
			return;
		}
		
		linesError = userService.saveBatch(users);

		if(linesError.size()>0) {
			message = "Email or username is existed";
			emailService.sendImportExcelError(message, linesError, user.getFullname(), file.getOriginalFilename());
		}
	}

	@GetMapping("/export-excel")
	public ModelAndView exportExcel() {
		List<User> users = userService.loadUsers();
		return new ModelAndView(new ExcelReportView(), "users", users);
	}

}