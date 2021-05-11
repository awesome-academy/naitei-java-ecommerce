package com.triplet.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "receiver_name", nullable = false)
	private String receiver_name;

	@Column(name = "receiver_address", nullable = false)
	private String receiver_address;

	@Column(name = "receiver_phone", nullable = false)
	private String receiver_phone;
	
	@Column(name = "price_total", nullable = false)
	private BigDecimal price_total;
	
	@Column(name = "quantity_total", nullable = false)
	private int quantity_total;

	public enum Status {
		PENDING, ACCEPTED, REJECTED, CANCELED
	};

	@Column(columnDefinition = "enum('PENDING','ACCEPTED','REJECTED','CANCELED')", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "create_time", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date create_time;

	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date update_time;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public BigDecimal getPrice_total() {
		return price_total;
	}

	public void setPrice_total(BigDecimal price_total) {
		this.price_total = price_total;
	}

	public int getQuantity_total() {
		return quantity_total;
	}

	public void setQuantity_total(int quantity_total) {
		this.quantity_total = quantity_total;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
}
