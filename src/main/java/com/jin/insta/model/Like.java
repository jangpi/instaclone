package com.jin.insta.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="userId")
	@JsonIgnoreProperties({"images", "password", "name", "website", "bio", "email", "phone", "gender", "createDate", "updateDate"})		// 무한참조 방지
	private User user;		// id, username, profileImage
	
	@ManyToOne
	@JoinColumn(name="imageId")
	@JsonIgnoreProperties({"tags", "user", "likes"})
	private Image image;		// 기본 image_id

	@CreationTimestamp	// 시간 자동 입력 어노테이션
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}
