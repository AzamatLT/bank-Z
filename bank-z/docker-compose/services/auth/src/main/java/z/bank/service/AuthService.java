package z.bank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import z.bank.dto.AuthResponse;
import z.bank.dto.RegisterRequest;
import z.bank.exception.BadRequestException;
import z.bank.model.User;
import z.bank.repository.UserRepository;
import z.bank.security.JwtUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Метод регистрации
    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());
    }

    // Метод авторизации
    public AuthResponse authenticateUser(String username, String password) {
        try {


            log.debug("=== Debug Authentication ===");
            log.debug("Input username: {}", username);
            log.debug("Input password (raw): {}", password);
         //   log.debug("DB stored password (hash): {}", user.getPassword());
         //   log.debug("Password matches: {}", passwordEncoder.matches(password, user.getPassword()));
            log.debug("===========================");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            return new AuthResponse(username, jwt, user.getRole());
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }

    }
}