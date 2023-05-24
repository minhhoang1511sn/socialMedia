package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
@Repository
public interface  UserRepository  extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    @Query("select p from User p")
    List<User> getAllUser();
    @Query("select u from User u where u.email = :email")
    User findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE " +
            "u.firstName LIKE CONCAT('%',:query, '%')" +
            "Or u.lastName LIKE CONCAT('%', :query, '%')")
    List<User> searchByFirstAndOrLastName(String query);

    User findUserById(Long idCurrentUser);
}
