package ssotom.clone.reddit.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssotom.clone.reddit.demo.dto.request.RefreshTokenRequest;
import ssotom.clone.reddit.demo.exception.NotFoundException;
import ssotom.clone.reddit.demo.model.RefreshToken;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.model.VerificationToken;
import ssotom.clone.reddit.demo.repository.UserRepository;
import ssotom.clone.reddit.demo.repository.VerificationTokenRepository;
import ssotom.clone.reddit.demo.dto.request.LoginRequest;
import ssotom.clone.reddit.demo.dto.request.SingUpRequest;
import ssotom.clone.reddit.demo.dto.response.AuthenticationResponse;
import ssotom.clone.reddit.demo.security.JwtProvider;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {

    private final static String EMAIL_ACTIVATION_URL = "http://localhost:8080/api/auth/account-verification";
    private final static String EMAIL_ACTIVATION_SUBJECT = "Please Activate your account";
    private final static String EMAIL_ACTIVATION_MESSAGE = "Thank you for signing up to Spring Reddit Clone, " +
            "please click on the below url to activate your account : " + EMAIL_ACTIVATION_URL + "/";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

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
                .enabled(false)
                .build();
        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message =  generateEmailActivationMessage(token);
        mailService.sendMail(user.getEmail(), EMAIL_ACTIVATION_SUBJECT, message);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new NotFoundException("Invalid Token: " + token));
        fetchUserAndEnable(verificationToken.get());
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return  AuthenticationResponse.builder()
                .type("Bearer")
                .token(token)
                .refreshToken(refreshTokenService.generateRefreshToken(getCurrentUser()).getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User no found with username: " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshToken.getUser().getUsername());
        return AuthenticationResponse.builder()
                .type("Bearer")
                .token(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshToken.getUser().getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
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

}
