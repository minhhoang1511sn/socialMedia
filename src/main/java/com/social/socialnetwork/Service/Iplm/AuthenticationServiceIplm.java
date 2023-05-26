package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.AuthenticationService;
import com.social.socialnetwork.config.JwtService;
import com.social.socialnetwork.dto.*;
import com.social.socialnetwork.exception.AppException;
import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.Role;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.ConfirmationCodeRepository;
import com.social.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceIplm implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Override
    public User register(RegisterReqest request) {
        if (!GenericValidator.isEmail(request.getEmail()))
            throw new AppException(400, "Wrong email");
        boolean check = userRepository.existsByEmail(request.getEmail());
        if (check) {
            throw new AppException(400,"Email already exits");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .Enabled(request.getEnabled())
                .build();
        return userRepository.save(user);
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationReqest reqest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        reqest.getEmail(),
                        reqest.getPassword()
                )
        );
        var user = userRepository.findByEmail(reqest.getEmail())
                .orElseThrow();
        if(!user.getEnabled())
        {
            throw new AppException(400,"User not authenticate");
        }
        else {
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        }
    }
    @Override
    public void saveVerificationCodeForUser(User users, String token) {
        ConfirmationCode verificationToken = new ConfirmationCode(users, token);
        System.out.println(verificationToken.getExpirationTime());
        confirmationCodeRepository.save(verificationToken);
    }
    @Override
    public AuthenticationResponse validateVerificationCode(String code, String email) {
        ConfirmationCode verificationCode
                = confirmationCodeRepository.findVerificationCodeByCodeAndUser_Email(code, email);

        if (verificationCode == null) {
            throw new AppException(400,"User not validated");
        }

        User user = verificationCode.getUser();

        verificationCode.setToken(null);
        confirmationCodeRepository.save(verificationCode);
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();

    }
    @Override
    public User validateVerificationCodetoResetPassword(PasswordDTO passwordDTO) {
        ConfirmationCode verificationCode
                = confirmationCodeRepository.findVerificationCodeByCodeAndUser_Email(passwordDTO.getVerifyCode(), passwordDTO.getEmail());

        if (verificationCode == null) {
            throw new AppException(400,"User not validated");
        }

        User user = userRepository.findUserByEmail(passwordDTO.getEmail());
            verificationCode.setToken(null);
            confirmationCodeRepository.save(verificationCode);
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(user);
            return user;
    }
}
