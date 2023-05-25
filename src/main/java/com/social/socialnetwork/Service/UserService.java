package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.Image;
import com.social.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {
     User findById(Long id);
     List<User> findAllUser();
     User updateUser(UserReq userReq);
     User findUserByEmail(String email);
     List<User> findUserByUserName(String query);
     void changePassword(User user, String newPassword);
     ConfirmationCode SendVerifyCode(String email);
     boolean disabledUser(Long id);
     boolean enabledUser(Long id);
     boolean checkIfValidOldPassword(User user, String oldPassword);
     String upAvartar(MultipartFile file)  throws IOException ;
     User getCurrentUser();
     List<Image> getimgByUser(Long userId);
     List<Image> getAllImageByUser(Long userId);
}
