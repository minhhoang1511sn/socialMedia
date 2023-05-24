package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.Cloudinary.CloudinaryUpload;
import com.social.socialnetwork.Service.FriendService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.*;
import com.social.socialnetwork.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceIplm implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final FriendRepository friendRepository;
    private final UserCommentRepository userCommentRepository;
    private final UserPostRepository userPostRepository;
    @Override
    public User findById(Long id) {
        User user = userRepository.findUserById(id);
        return user;
    }


    @Override
    public List<User> findAllUser() {
        List<User> users = userRepository.getAllUser();
        return users;
    }

    @Override
    public User updateUser(UserReq userReq){
        User userUpdate = findById(Utils.getIdCurrentUser());
        if (userUpdate != null) {
            if (userReq.getFirstName()!=null && !userReq.getFirstName().trim().equals(""))
                userUpdate.setFirstName(userReq.getFirstName().trim().replaceAll("  "," "));
            if (userReq.getLastName()!=null && !userReq.getLastName().trim().equals(""))
                userUpdate.setLastName(userReq.getLastName().trim().replaceAll("  "," "));
            if (userReq.getGender()!=null && !userReq.getGender().trim().equals(""))
                userUpdate.setGender(userReq.getGender().trim().replaceAll("  "," "));
            if (userReq.getAddress()!=null && !userReq.getAddress().trim().equals("")) {
                userUpdate.setAddress(userReq.getAddress());
            }
            if (userReq.getBirthday()!=null) {
                userUpdate.setBirthday(userReq.getBirthday());
            }
            if (userReq.getRole()!=null) {
                userUpdate.setRole(userReq.getRole());
            }
            userRepository.save(userUpdate);
            return userUpdate;
        } else return null;

    }
    @Override
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    @Override
    public List<User> findUserByUserName(String query){

        List<User> user = userRepository.searchByFirstAndOrLastName(query);
        List<User> result = new ArrayList<>();
        user.forEach(u->{
            if(u.getEnabled()==true)
            {
                result.add(u);
            }
        });
        return result;
    }
    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    @Override
    public ConfirmationCode SendVerifyCode(String email) {
        ConfirmationCode verificationCode = confirmationCodeRepository.findVerificationCodeByUserEmail(email);
        if (verificationCode != null) {
            String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            verificationCode.setCode(code);
            confirmationCodeRepository.save(verificationCode);
            return verificationCode;
        }
        return null;
    }
    @Override
    public boolean disabledUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getEnabled())
        {
            user.setEnabled(false);
            userRepository.save(user);
            return  true;
        }
        else
            throw new AppException(403, "User not found or can not disabled");
    }
    @Override
    public boolean enabledUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.getEnabled())
        {
            user.setEnabled(true);
            userRepository.save(user);
            return  true;
        }
        else
            throw new AppException(403, "User not found or can not enabled");
    }
    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    @Override
    public String upAvartar(MultipartFile file) throws IOException {
        Long id = Utils.getIdCurrentUser();
        User user = findById(id);
        if (user == null)
            throw new AppException(404, "User ID not found");
        Image imgUrl = new Image();
        if (user.getAvatarLink() != null && user.getAvatarLink().getImgLink().startsWith("https://res.cloudinary.com/minhhoang1511/image/upload")) {
            imgUrl = user.getAvatarLink();
            imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,imgUrl.getImgLink()));
        }else
            imgUrl.setImgLink(cloudinaryUpload.uploadImage(file,null));
        imgUrl.setUser(user);
        imgUrl.setPostType(PostType.PUBLIC);
        user.setAvatarLink(imgUrl);
        List<Image> imageUser = user.getImages();
        imageUser.add(imgUrl);
        user.setImages(imageUser);
        userRepository.save(user);
        List<UserComment> userComments = userCommentRepository.findAllByUserId(user.getId());
        String uAvatar = imgUrl.getImgLink();
        userComments.forEach(u -> u.setAvatar(uAvatar));
        userCommentRepository.saveAll(userComments);
        List<UserPost> userPosts = userPostRepository.findAllByUserId(user.getId());
        userPosts.forEach(u -> u.setAvatar(uAvatar));
        userPostRepository.saveAll(userPosts);
        return imgUrl.getImgLink();
    }
    @Override
    public User getCurrentUser() {
        User user = userRepository.findUserById(Utils.getIdCurrentUser());

        return user;
    }
    @Override
    public List<Image> getAllImageByUser(Long userId) {
        User user = userRepository.findUserById(userId);
        List<Image> images = imageRepository.getAllImageByUser(user);
        return images;
    }
    @Override
    public List<Image> getimgByUser(Long userId) {
        User userfriend = userRepository.findUserById(userId);
        User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
        List<Image> images = getAllImageByUser(userId);
        List<Image> imgByFriend = new ArrayList<>();
            images.forEach(p ->{
                if(friendRepository.existsByFirstUserAndSecondUser(curUser,userfriend))
                {
                    if(p.getPostType() == PostType.PUBLIC || p.getPostType() == PostType.FRIEND)
                    {
                        imgByFriend.add(p);
                    }
                }
                else{
                    if(p.getPostType() == PostType.PUBLIC)
                    {
                        imgByFriend.add(p);
                    }
                }
            });
        return imgByFriend;
    }
    @Override
    public List<Video> getAllVideoByUser(Long userId) {
        User user = userRepository.findUserById(userId);
        List<Video> videos = videoRepository.getAllVideoByUser(user);
        return videos;
    }
    @Override
    public List<Video> getvideoByUser(Long userId) {
        User userfriend = userRepository.findUserById(userId);
        User curUser = userRepository.findUserById(Utils.getIdCurrentUser());
        List<Video> videos = getAllVideoByUser(userId);
        List<Video> videoByFriend = new ArrayList<>();
            videos.forEach(p ->{
                if(friendRepository.existsByFirstUserAndSecondUser(curUser,userfriend))
                {
                    if(p.getPostType() == PostType.PUBLIC || p.getPostType() == PostType.FRIEND)
                    {
                        videoByFriend.add(p);
                    }
                }
                else{
                    if(p.getPostType() == PostType.PUBLIC)
                    {
                        videoByFriend.add(p);
                    }
                }
            });
        return videoByFriend;
    }

}
