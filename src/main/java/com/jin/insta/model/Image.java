package com.jin.insta.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // primarykey 역할
	private int id;
	private String location; // 사진 찍은 위치(로마)
	private String caption; // 사진 설명
	private String postImage; // 포스팅 사진 경로+이름
	
	@ManyToOne // 단방향 예를들어 N:1 회원과 휴대폰의 관계, 연관관계
	@JoinColumn(name = "userId") // 외래키를 매핑
	@JsonIgnoreProperties({"password", "images"}) // 무시할 속성을 나타날 떄 사용
	private User user;
	
	// (1) Like List
	@OneToMany(mappedBy = "image") // 단방향 , 회원 한명이 여러개의 휴대폰을 가지고있는것
	private List<Likes> likes = new ArrayList<>();
	
	// (2) Tag List
	// @OneToMany(mappedBy = "image", cascade = CasadeType.
	@OneToMany(mappedBy = "image")
	@JsonManagedReference // 양방향 관계에서 정방향 참조할 변수에 어노테이션을 추가하면 직렬화에 포함
	private List<Tag> tags = new ArrayList<>();
	
	@Transient // DB에 영향x
	private int likeCount;
	
	@Transient
	private boolean heart;
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}
