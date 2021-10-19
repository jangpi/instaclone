package com.jin.insta.servcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jin.insta.model.User;
import com.jin.insta.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{		//UserDetailsService DB에서 유저를 불러오는 중요한 역할
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		MyUserDetail userDetails = null;
		if(user != null) {
			userDetails = new MyUserDetail();
			userDetails.setUser(user);
		} else {
			throw new UsernameNotFoundException("Not Found 'username'");
		}
		return userDetails;
	}
	
	
}
