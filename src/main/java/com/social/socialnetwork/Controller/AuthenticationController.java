package com.social.socialnetwork.Controller;

import com.social.socialnetwork.Service.AuthenticationService;
import com.social.socialnetwork.Service.EmailService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.*;
import com.social.socialnetwork.model.ConfirmationCode;
import com.social.socialnetwork.model.User;
import com.social.socialnetwork.repository.UserRepository;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins ="http://localhost:3000")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;
    final String TITLE_SUBJECT_EMAIL = "Nhom 2 Register TOKEN";
    final String RESET_PASSWORD_TOKEN = "Reset Password Token";
    final String RESEND_VERIFYCATION_CODE = "Resend verifycation code";
    @GetMapping("/resendConfirmationCode")
    public ResponseEntity<?> resendConfirmationCode(@RequestParam("email") String email
    ) throws MessagingException, TemplateException, IOException {
        String confirmationCode
                = userService.SendVerifyCode(email).getCode();;
        Map<String,Object> model = new HashMap<>();
        model.put("code",confirmationCode);
        model.put("title",RESEND_VERIFYCATION_CODE);
        model.put("subject", RESEND_VERIFYCATION_CODE);
        emailService.sendEmail(email, model);

        return ResponseEntity.ok(new ResponseDTO(true,"Verification TOKEN Sent",
                null));
    }

    @PostMapping("/register-email")
    public ResponseEntity<?> register(
            @RequestBody RegisterReqest request
    ) throws TemplateException, MessagingException, IOException {
         authenticationService.register(request);
        User users = userRepository.findUserByEmail(request.getEmail());
        String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        authenticationService.saveVerificationCodeForUser(users,code);

        Map<String,Object> model = new HashMap<>();
        model.put("code",code);
        model.put("title", TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailService.sendEmail(request.getEmail(), model);

        return ResponseEntity.ok(new ResponseDTO(true,"Sending email",
                null));
    }
    @RequestMapping(value = "/verifyRegistration", method = RequestMethod.POST)
    public ResponseEntity<?> verifyRegistration(@RequestParam String code,
                                                @RequestParam String email) {
        AuthenticationResponse result = authenticationService.validateVerificationCode(code,email);
        if(result==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO) {
        User result = authenticationService.validateVerificationCodetoResetPassword(passwordDTO);
        if(result==null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("/resetPasswordRequest")
    public ResponseEntity<?> resetPasswordReq(@RequestBody PasswordDTO passwordDTO)
            throws MessagingException, TemplateException, IOException {
        User user = userService.findUserByEmail(passwordDTO.getEmail());
        if (user!= null && user.getEnabled()){
            String code = "";
            code = userService.SendVerifyCode(passwordDTO.getEmail()).getCode();
            Map<String,Object> model = new HashMap<>();
            model.put("code",code);
            model.put("title", RESET_PASSWORD_TOKEN);
            model.put("subject", RESET_PASSWORD_TOKEN);
            //Send email
            emailService.sendEmail(user.getEmail(), model);

            return ResponseEntity.ok(new ResponseDTO(true,"Sent email reset verify code",
                    null));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(false,"Not found email",
                null));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationReqest request
    )
    {
        if(userRepository.findByEmail(request.getEmail()).isPresent())
        {   if(authenticationService.authenticate(request) != null)
            return ResponseEntity.ok(authenticationService.authenticate(request));
            else return  ResponseEntity.ok("Email is not authenticate");
        }
        else return  ResponseEntity.ok("Email is not Exists");
    }
    @GetMapping(value="/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
