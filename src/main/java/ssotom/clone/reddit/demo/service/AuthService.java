package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.model.VerificationToken;
import ssotom.clone.reddit.demo.repository.UserRepository;
import ssotom.clone.reddit.demo.repository.VerificationTokenRepository;
import ssotom.clone.reddit.demo.exception.SpringRedditException;
import ssotom.clone.reddit.demo.request.LoginRequest;
import ssotom.clone.reddit.demo.request.SingUpRequest;
import ssotom.clone.reddit.demo.response.AuthenticationResponse;
import ssotom.clone.reddit.demo.security.JwtProvider;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {

    private final static String EMAIL_ACTIVATION_URL = "http://localhost:8080/api/auth/account_verification";
    private final static String EMAIL_ACTIVATION_SUBJECT = "Please Activate your account";
    private final static String EMAIL_ACTIVATION_MESSAGE = "Thank you for signing up to Spring Reddit Clone, " +
            "please click on the below url to activate your account : " + EMAIL_ACTIVATION_URL + "/";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void signup(SingUpRequest singUpRequest) {

        User user = User.builder()
                .username(singUpRequest.getUsername())
                .email(singUpRequest.getEmail())
                .password(passwordEncoder.encode(singUpRequest.getPassword()))
                .createdAt(Instant.now())
                .enabled(false)
                .build();
        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message =  generateEmailActivationMessage(token);
        mailService.sendMail(user.getEmail(), EMAIL_ACTIVATION_SUBJECT, message);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token: " + token));
        fetchUserAndEnable(verificationToken.get());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String generateEmailActivationMessage(String token) {
        return EMAIL_ACTIVATION_MESSAGE + token;
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

}
