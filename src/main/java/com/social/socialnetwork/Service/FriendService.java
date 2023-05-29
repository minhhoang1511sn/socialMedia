package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.UserReq;
import com.social.socialnetwork.model.Friend;
import com.social.socialnetwork.model.User;

import java.util.List;

public interface FriendService {
     Boolean isFriend(User user1, User user2);
     List<User> getUserFriends(Long id);
     List<User> suggestFriend();

     void saveFriend(User userDto1, Long id) throws NullPointerException;

     List MutualFriends(Long id);
     boolean unFriend(Long id);
     User findById(Long id);
}
