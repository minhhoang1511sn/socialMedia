package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.CommentService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.CommentReq;
import com.social.socialnetwork.dto.PostResp;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.CommentRepository;
import com.social.socialnetwork.repository.PostRepository;
import com.social.socialnetwork.repository.UserCommentRepository;
import com.social.socialnetwork.repository.UserRepository;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserCommentRepository userCommentRepository;

    @Override
    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }
    @Override
    public Comment postComment(CommentReq commentReq) {
        Long userId = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(userId) && postRepository.existsById(commentReq.getPostId());
        UserComment userComment = new UserComment();
        if(check){
                Comment comment = new Comment();
                comment.setRate(0.0);
                comment.setContent(commentReq.getContent());
                User user = userService.findById(userId);
                Post post = postRepository.findById(commentReq.getPostId()).orElse(null);
                comment.setPost(post);
                comment.setCreateTime(new Date());
                if(user.getAvatarLink()!=null)
                userComment.setAvatar(user.getAvatarLink().getImgLink());
                userComment.setFirstName(user.getFirstName());
                userComment.setLastName(user.getLastName());
                userComment.setUserId(user.getId());
                userCommentRepository.save(userComment);
                comment.setUserComment(userComment);
                commentRepository.save(comment);
                return comment;
        } else {
            throw new AppException(404, "Post or Comment not exits.");
        }
    }


    @Override
    public List<Comment> getAllCommentByPost(Long postid) {
        Post post = postRepository.getReferenceById(postid);
        List<Comment> commentList = commentRepository.getCommentByPost(post);
        return commentList;
    }

    @Override
    public Comment updateComment(CommentReq commentReq) {
        Long userId = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(userId);
        if(check){
            Comment commentUpdate = findById(commentReq.getId());
            UserComment uc = userCommentRepository.getReferenceById(commentReq.getId());
            if(commentUpdate.getUserComment().getUserId() == userId)
            {
                if(commentUpdate != null) {
                   commentUpdate.setContent(commentReq.getContent());
                   commentUpdate.setUserComment(uc);
                   commentUpdate.setRate(commentReq.getRate());
                    return commentUpdate;
                } else throw new AppException(404, "Comment ID not found");
            }
            else  throw new AppException(500, "User don't have permission");
        }
        else throw new AppException(500, "User not found");

    }

    @Override
    public boolean deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        Long currentUser = Utils.getIdCurrentUser();
        if(currentUser == comment.getUserComment().getUserId())
        {
            if(comment != null){
                commentRepository.deleteById(id);
                Post post = comment.getPost();
                List<Comment> commentList = post.getCommentList();
                commentList.remove(comment);
                post.setCommentList(commentList);
                return  true;
            }
            else{
                throw new AppException(404, "Comment ID not found");}
        }
        else{
            throw new AppException(500, "User don't have permission to delete this comment");
        }
    }

}
