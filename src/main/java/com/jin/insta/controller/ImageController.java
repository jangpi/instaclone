package com.jin.insta.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jin.insta.model.Image;
import com.jin.insta.model.Likes;
import com.jin.insta.model.Tag;
import com.jin.insta.model.User;
import com.jin.insta.repository.ImageRepository;
import com.jin.insta.repository.LikeRepository;
import com.jin.insta.repository.TagRepository;
import com.jin.insta.servcie.MyUserDetail;
import com.jin.insta.util.Utils;

@Controller
public class ImageController {
	
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Value("${file.path}")
	private String fileRealPath;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@PostMapping("/image/like/{id}")
	public @ResponseBody String imageLike(@PathVariable("id") int id, @AuthenticationPrincipal MyUserDetail userDetail) {
		
		Likes oIdLike = likeRepository.findByUserIdAndImageId(userDetail.getUser().getId(), id);
		
		Optional<Image> oImage = imageRepository.findById(id);
		Image image = oImage.get();
		
		try {
			if(oIdLike == null) { // 좋아요 안한 상태(추가)
				Likes newLike = Likes.builder().image(image).user(userDetail.getUser()).build();
				
				likeRepository.save(newLike);
				
				return "like";
			}else {	// 좋아요 한 상태(삭제)
				likeRepository.delete(oIdLike);
				return "unLike";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "fall";
	}
	
	@GetMapping("/image/feed/scroll")
	public @ResponseBody List<Image> imageFeedScroll(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@PageableDefault(size = 3,  sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
			
		Page<Image> pageImages = imageRepository.findImage(userDetail.getUser().getId(), pageable);
		List<Image> images = pageImages.getContent();
		
		for(Image image: images) {
			Likes like = likeRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
		}
		
		return images;
	}
	
	@GetMapping({"/", "/image/feed"})
	// @AuthenticationPrincipal 로그인한 사용자의 정보를 파라메터로 받고 싶을때 기존에는 다음과 같이 Principal객체로 받아서 사용한다.
	// @PageableDefault size = 한페이지에 담을 모델의 수를 정한다.
	//										sort = 정렬의 기준이 되는 속성을 정한다.
	//										direction = 오름차순과 내림차순중 기준을 선택한다.
	// Pageable pageable = PageableDefault 값을 갖고 있는 변수를 선언
	public String imageFeed
	(
			@AuthenticationPrincipal MyUserDetail userDetail
			,@PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable
			,Model model
	)
	{	
		// log.info("username : " + userDetail.getUsername());
		
		// 내가 팔로우한 친구들의 사진
		Page<Image> pageImages = 
				imageRepository.findImage(userDetail.getUser().getId(), pageable);
		
		List<Image> images = pageImages.getContent();
		
		for(Image image: images) {
			Likes like = likeRepository.findByUserIdAndImageId(userDetail.getUser().getId(), image.getId());
			if(like != null) {
				image.setHeart(true);
			}
		}
		model.addAttribute("images", images);
		
		return "image/feed";
	}
	
	@GetMapping("/image/upload")
	public String imageUpload() {
		return "image/image_upload";
	}
	
	@PostMapping("/image/uploadProc")
	public String imageUploadProc
	(
			@AuthenticationPrincipal MyUserDetail userDetail,
			@RequestParam("file") MultipartFile file,
			@RequestParam("caption") String caption,
			@RequestParam("location") String location,
			@RequestParam("tags") String tags
	) {
		
		// 이미지 업로드 수행
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_" +file.getOriginalFilename();
		
		Path filePath = Paths.get(fileRealPath+uuidFilename);
		
		try {
			Files.write(filePath, file.getBytes());	// 하드디스크 기록
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User principal = userDetail.getUser();
		
		Image image = new Image();
		image.setCaption(caption);
		image.setLocation(location);
		image.setUser(principal);
		image.setPostImage(uuidFilename);
		
		// <img src="/upload/파일명" />
		
		imageRepository.save(image);
		
		// Tag 객체 생성
		List<String> tagList = Utils.tagParser(tags);
		
		for(String tag : tagList) {
		   Tag t = new Tag();
			t.setName(tag);
			t.setImage(image);
			tagRepository.save(t);
			image.getTags().add(t);
		}
		
		return"redirect:/";
	}
	
}
