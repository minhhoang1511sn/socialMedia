package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.PostService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.PostReq;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.*;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceIplm implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final UserPostRepository userPostRepository;
    private final UserService userService;
    @Override
    public Post createPost(PostReq postReq,  MultipartFile images)
    {
        Long idCurrentUser = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(idCurrentUser);
        if(check){
            Post post = new Post();
            User user = userService.findById(idCurrentUser);
            UserPost userPost = new UserPost();
            userPost.setUserId(user.getId());
            if(user.getAvatarLink()!=null)
            userPost.setAvatar(user.getAvatarLink().getImgLink());
            userPost.setFirstName(user.getFirstName());
            userPost.setLastName(user.getLastName());
            userPostRepository.save(userPost);
            post.setContent(postReq.getContent());
            post.setCountLike(0L);
            post.setPostType(PostType.PUBLIC);
            post.setUser(user);
            post.setUserPost(userPost);
            post.setCreateDate(new Date());
            postRepository.save(post);
            if(images!=null){
                uploadImage(post.getId(), images);
            }

            List<Post> postbyUser = user.getPosts();
            postbyUser.add(post);
            user.setPosts(postbyUser);
            return postRepository.save(post);
        } else {
            throw new AppException(404, "Product or Comment not exits.");
        }
    }

    @Override
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);

    }
    @Override
    public boolean deletePost(Long id){
        Long idCurrentUser = Utils.getIdCurrentUser();
        Post postDelete = postRepository.findById(id).orElse(null);
        if(postDelete !=null && postDelete.getUser().getId() == idCurrentUser)
        {
            Image image = postDelete.getImages();
            List<Comment> commentList = postDelete.getCommentList();
            if(image!=null){
                image.setUser(null);
                imageRepository.deleteById(image.getId());
            }

            if(commentList!=null){
                for (Comment comment : commentList) {
                    comment.setUserComment(null);
                    commentRepository.deleteById(comment.getId());
                }
            }
            postDelete.setPostType(null);
            postDelete.setUser(null);
            postRepository.deleteById(id);
            return true;
        }
        else throw new AppException(404, "Post does not exits.");
    }
    @Override
    public List<Post> getAllPost(Long id)
    {
        User user = userRepository.findUserById(id);
        List<Post> posts = postRepository.findPostOrderByCreateDate(user);
        return posts;
    }

    @Override
    public Post updatePost(PostReq postReq,  MultipartFile images){
        Post postUpdate = postRepository.findById(postReq.getId()).orElse(null);
        Long idCurrentUser = Utils.getIdCurrentUser();
        if(postUpdate != null && idCurrentUser == postUpdate.getUser().getId())
        {
            uploadImage(postUpdate.getId(), images);
            List<Comment> commentList = postUpdate.getCommentList();
            User curUser = userRepository.findUserById(idCurrentUser);
            UserPost uc = postUpdate.getUserPost();
            PostType postType = postUpdate.getPostType();
            postUpdate.setContent(postReq.getContent());
            postUpdate.setUser(curUser);
            postUpdate.setUserPost(uc);
            postUpdate.setCommentList(commentList);
            postUpdate.setCreateDate(new Date());
            postUpdate.setPostType(postType);
            return postRepository.save(postUpdate);
        }
        else
            throw new AppException(400,"Post does not exists");
    }
    @Override
    public String uploadImage(Long postId, MultipartFile images) {
        Long idCurrentUser = Utils.getIdCurrentUser();
        boolean check = userRepository.existsById(idCurrentUser);
        User user = userRepository.findUserById(idCurrentUser);
        Image image = new Image();
        Post post = postRepository.getReferenceById(postId);
        if(check && post!=null){
                try {
                    String url = cloudinaryUpload.uploadImage(images,null);
                    image.setImgLink(url);
                    image.setPost(post);
                    image.setUser(user);
                    image.setPostType(post.getPostType());
                } catch (IOException e) {
                    throw new AppException(400,"Failed");
                }
            ;
            imageRepository.save(image);
            post.setImages(image);
            List<Image> imageUser = user.getImages();
            imageUser.add(image);
            user.setImages(imageUser);
            String url =image.getImgLink();

            return url;
        }
        else
            return null;

    }

    @Override
    public List<Post> gettingPostByFriend() {
        List<User> users = userRepository.getAllUser();

        List<Post> newfeeds = new ArrayList<>();
        users.forEach(u->{
            newfeeds.addAll(u.getPosts());
        });
   return  newfeeds;
    }
    @Override
    public List<Post> getAllPostByUser(Long userId) {
        User user = userRepository.findUserById(userId);
        List<Post> posts = postRepository.findPostOrderByCreateDate(user);
        return posts;
    }


}
