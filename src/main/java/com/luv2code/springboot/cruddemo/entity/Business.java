package com.luv2code.springboot.cruddemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="business")
public class Business {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="business_id")
	private int businessId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	@JsonProperty("foundationDate")
	@Column(name="foundation_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date foundationDate;
	@JsonProperty("registrationDate")
	@Column(name="registration_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date registrationDate;
	
	@Column(name="business_number")
	private int businessNumber;

	@Column(name = "status")
	private int status;

	@Column(name="comment")
	private String comment;
	
	public Business() {}


	public Business(String name, String email, Date foundationDate, Date registrationDate, int businessNumber, int status, String comment) {
		this.name = name;
		this.email = email;
		this.foundationDate = foundationDate;
		this.registrationDate = registrationDate;
		this.businessNumber = businessNumber;
		this.status=status;
		this.comment=comment;
	}

	// define getters and setters


	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFoundationDate() {
		return foundationDate;
	}

	public void setFoundationDate(Date foundationDate) {
		this.foundationDate = foundationDate;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(int businessNumber) {
		this.businessNumber = businessNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	// toString method


	@Override
	public String toString() {
		return "Business{" +
				"businessId=" + businessId +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", foundationDate=" + foundationDate +
				", registrationDate=" + registrationDate +
				", businessNumber=" + businessNumber +
				", status=" + status +
				", comment='" + comment + '\'' +
				'}';
	}
}
