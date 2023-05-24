package com.social.socialnetwork.repository;

import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode,Long> {

    ConfirmationCode findVerificationCodeByCodeAndUser_Email(String code, String email);

    ConfirmationCode findVerificationCodeByUserEmail(String email);

}
