package com.jin.insta.model;

import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
public class Follow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// 중간 테이블 생성됨.
	// FromUser가 toUser를 following함.
	// toUser를 fromUsern가 follow함.
	
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	@JsonIgnoreProperties({"images"})
	private User fromUser;
	
	@ManyToOne
	@JoinColumn(name = "toUserId")
	@JsonIgnoreProperties({"images"})
	private User toUser;
	
	@Transient	// DB에 영향 X
	private boolean fromUserMatpal;
	
	@Transient
	private boolean principalMatpal;
	
	@Transient
	private boolean followState;
	
	@CreationTimestamp	// 시간 자동 입력 어노테이션
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;
}
