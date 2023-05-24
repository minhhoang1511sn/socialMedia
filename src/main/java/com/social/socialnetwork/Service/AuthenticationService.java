package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.AuthenticationReqest;
import com.social.socialnetwork.dto.AuthenticationResponse;
import com.social.socialnetwork.dto.PasswordDTO;
import com.social.socialnetwork.dto.RegisterReqest;

import com.social.socialnetwork.model.User;

public interface AuthenticationService {
     User register(RegisterReqest request);
     AuthenticationResponse authenticate(AuthenticationReqest reqest);
     User validateVerificationCodetoResetPassword(PasswordDTO passwordDTO);
     void saveVerificationCodeForUser(User users, String token);
     AuthenticationResponse validateVerificationCode(String code, String email);
}
