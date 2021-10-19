package com.jin.insta.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jin.insta.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	
	// unFollow
	@Transactional// DB 의 상태를 변경하는 작업 또는 한번에 수행되어야 하는 연산들을 의미
								// begin, commit 을 자동 수행
								// 예외 발생 시 rollback 처리 자동 수행
								// 원자성, 영속성, 격리성, 일관성 이 있다.
	int deleteByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	// 팔로우 유무
	int countByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	// 팔로우 리스트 (하얀 버튼)
	List<Follow> findByFromUserId(int fromUserId);
	
	// 팔로워 리스트 (맞팔 체크 후 버튼 색깔 결정)
	List<Follow> findByToUserId(int toUserId);
	
	// 팔로우 카운트
	int countByFromUserId(int fromUserId);
	
	// 팔로워 카운트
	int countByToUserId(int toUserId);
}
