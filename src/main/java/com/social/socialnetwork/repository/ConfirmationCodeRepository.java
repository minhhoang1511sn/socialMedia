package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode,Long> {
    @Query("select c from ConfirmationCode c where c.code=:code and c.user.email=:email")
    ConfirmationCode findVerificationCodeByCodeAndUser_Email(String code, String email);
    @Query("select c from ConfirmationCode c where c.user.email=:email")
    ConfirmationCode findVerificationCodeByUserEmail(String email);

}
