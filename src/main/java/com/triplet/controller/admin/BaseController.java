package com.triplet.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triplet.bean.ProductInfo;
import com.triplet.model.Product;
import com.triplet.model.Rate;
import com.triplet.service.CategoryService;
import com.triplet.service.EmailService;
import com.triplet.service.OrderItemService;
import com.triplet.service.OrderService;
import com.triplet.service.ProductService;
import com.triplet.service.RateService;
import com.triplet.service.UserService;
import com.triplet.service.impl.MyUser;

public abstract class BaseController {

	@Autowired
	protected UserService userService;

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected OrderItemService orderItemService;

	@Autowired
	protected ProductService productService;

	@Autowired
	protected RateService rateService;

	@Autowired
	protected CategoryService categoryService;
	
	@Autowired
	protected EmailService emailService;
	
	protected MyUser loadCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken)
			return null;
		MyUser user = (MyUser) authentication.getPrincipal();
		return user;
	}

	protected String handleRedirect(final RedirectAttributes redirectAttributes, String css, String msg,
			String redirectEndpoint) {
		redirectAttributes.addFlashAttribute("css", css);
		redirectAttributes.addFlashAttribute("msg", msg);
		return "redirect:" + redirectEndpoint;
	}

	protected List<ProductInfo> generateProductInfos(List<Product> products) {
		List<ProductInfo> productInfos = new ArrayList<>();
		for (Product product : products) {
			List<Rate> rates = rateService.loadRatings(product.getId());
			product.setRates(rates);
			ProductInfo proInfo = new ProductInfo(product);
			productInfos.add(proInfo);
		}
		return productInfos;
	}
}
