package com.jin.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;


@Data	// lombok
@Entity // JPA -> ORM
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	// 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id;								// 시퀀스
	private String username;		// ID
	private String password;			// 패스워드
	private String name;					// 사용자 이름
	private String website;			// 홈페이지 주소
	private String bio;						// 자기소개
	private String email;					// 이메일
	private String phone; 				// 휴대폰 번호
	private String gender; 				// 남,녀 유무
	private String profileImage;	// 포스팅 사진 경로 + 사진
	
	// (1) findById() 때만 동작
	// (2) findByUserInfo() 제외
	@OneToMany(mappedBy = "user")
	@JsonIgnoreProperties({"user", "tags", "likes"})
	private List<Image> images = new ArrayList<>();
	
	@CreationTimestamp	// 시간 자동 입력 어노테이션
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}
