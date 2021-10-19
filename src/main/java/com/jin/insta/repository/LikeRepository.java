package com.jin.insta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jin.insta.model.Image;
import com.jin.insta.model.Likes;
import com.jin.insta.model.User;

public interface LikeRepository extends JpaRepository<Likes, Integer>{
	// 내가 좋아요 한 이미지 찾기 위해!!
	Likes findByUserIdAndImageId(int userId, int ImageId);
	
	int countByImageId(int imageId);
}
