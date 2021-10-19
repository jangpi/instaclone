package com.jin.insta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jin.insta.model.Follow;
import com.jin.insta.model.User;
import com.jin.insta.repository.FollowRepository;
import com.jin.insta.repository.UserRepository;
import com.jin.insta.servcie.MyUserDetail;

@Controller
public class FollowController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@PostMapping("/follow/{id}")
	// @ResponseBody 자바객체를 HTTP요청의 바디내용으로 매핑하여 클라이언트로 전송한다.
	//@AuthenticationPrincipal 로그인한 사용자의 정보를 파라메터로 받고 싶을때 Principal에 객체로 담아서 전송한다.
	public @ResponseBody String follow
	(
			@AuthenticationPrincipal MyUserDetail userDetails,
			@PathVariable int id
	) 
	{
		User fromUser = userDetails.getUser();
		Optional<User> oToUser =
				userRepository.findById(id);
		User toUser = oToUser.get();
		
		Follow follow = new Follow();
		follow.setFromUser(fromUser);
		follow.setToUser(toUser);
		
		followRepository.save(follow);
		
		return "ok";
		
	}
	
	@DeleteMapping("/follow/{id}")
	// @ResponseBody 자바객체를 HTTP요청의 바디내용으로 매핑하여 클라이언트로 전송한다.
	//@AuthenticationPrincipal 로그인한 사용자의 정보를 파라메터로 받고 싶을때 Principal에 객체로 담아서 전송한다.
	public @ResponseBody String unfollow
	(
			@AuthenticationPrincipal MyUserDetail userDetails,
			@PathVariable int id
	) 
	{
		User fromUser = userDetails.getUser();
		Optional<User> oToUser =
				userRepository.findById(id);
		User toUser = oToUser.get();
		
		followRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
		
		List<Follow> follows = followRepository.findAll();
		return "ok";		// ResponseEntity로 수정
	}	
	

	@GetMapping("/follow/follower/{id}")
	public String followFollower(
			@PathVariable int id, 
			@AuthenticationPrincipal MyUserDetail userDetail,
			Model model) {
	
		// 팔로워 리스트
		List<Follow> followers = followRepository.findByToUserId(id);
	
		// 팔로우 리스트 (cos : 1) 2, 3
		List<Follow> principalFollows = followRepository.findByFromUserId(userDetail.getUser().getId());
	
		for (Follow f1 : followers) { // 3번 돈다.
			for (Follow f2 : principalFollows) {
				if (f1.getFromUser().getId() == f2.getToUser().getId()) {
					f1.setFollowState(true);
				}
			}
		}
	
		model.addAttribute("followers", followers);
		return "follow/follower";
	}
	// HttpServletRequest
	// http 프로토콜의 request정보를 서블릿에게 전달하기 위한 목적으로 사용.
	// 헤더정보, 파라미터, 쿠키, URI, URL 등의 정보를 읽어 들이는 메소드를 가지고 있다.
	@GetMapping("/follow/follow/{id}")
	public String followFollow(@PathVariable int id
			, @AuthenticationPrincipal MyUserDetail userDetail
			, Model model) {
		
		// 팔로우 리스트
		List<Follow> follows = followRepository.findByFromUserId(id);
		
		// 팔로우 리스트 
		List<Follow> principalFollows = followRepository.findByFromUserId(userDetail.getUser().getId());
		
		for(Follow f1 : follows) {	// 3번 돈다.
			for(Follow f2: principalFollows) {
				if(f1.getToUser().getId() == f2.getToUser().getId()) {
					f1.setFollowState(true);
				}
			}
		}
		
		model.addAttribute("follows", follows);
		return "follow/follow";
	}
}
