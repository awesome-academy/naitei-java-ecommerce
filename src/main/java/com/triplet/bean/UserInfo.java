package com.triplet.bean;

import java.util.Arrays;
import java.util.List;

import com.triplet.model.User;

public class UserInfo {

	private int id;
	private String username;
	private String fullname;
	private String email;
	private String password;
	private String confirmPassword;
	private String avatar;
	private String phone;
	private String address;
	private List<String> addresses;

	public UserInfo() {
		super();
	}

	public UserInfo(User user) {
		this.id = user.getId();
		this.fullname = user.getFullname();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.address = user.getAddress();
		if (user.getAddress() != null)
			this.addresses = Arrays.asList(user.getAddress().split("/"));
		this.phone = user.getPhone();
		this.avatar = user.getAvatar();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User convertToUser() {
		User user = new User();
		user.setFullname(this.getFullname());
		user.setUsername(this.getUsername());
		user.setEmail(this.getEmail());
		user.setPassword(this.getPassword().trim());
		user.setAvatar(this.getAvatar());
		user.setPhone(this.getPhone());
		user.setAddress(this.getAddress());

		// Addresses's List to String
		if (this.addresses != null) {
			StringBuilder strbld = new StringBuilder();
			for (String address : this.addresses) {
				strbld.append(address);
				strbld.append('/');
			}
			user.setAddress(strbld.toString());
		}
		return user;
	}
}
