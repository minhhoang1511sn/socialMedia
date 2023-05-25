package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Post;
import com.social.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;



public interface PostService {
     Post  createPost(PostReq postReq,  MultipartFile images);
     Post findById(Long id);
     boolean deletePost(Long id);
     List<Post> getAllPost(Long id);
     Post updatePost(PostReq postReq,  MultipartFile images);
     String uploadImage(Long postId, MultipartFile images) ;
     List<Post> gettingPostByFriend();
     List<Post> getAllPostByUser(Long userId);

}
