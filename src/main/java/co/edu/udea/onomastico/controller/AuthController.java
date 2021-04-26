package co.edu.udea.onomastico.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.CredentialsException;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.payload.JwtAuthenticationResponse;
import co.edu.udea.onomastico.payload.LoginRequest;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.EmailService;
import co.edu.udea.onomastico.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Value("${app.resetpwd}")
	private String RESET_SERVER;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
	private UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
	private EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Throwable {
    	Usuario user = (Usuario) usuarioService.findUserByEmail(loginRequest.getUserEmail()).orElseThrow(CredentialsException::new);
    	if(user.getEstado().contains("INACTIVO"))return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
 	@GetMapping("/forgotpwd")
 	public ResponseEntity<?> sendEmailforgotPasswordForm(@RequestParam("email") String userEmail) {
 		Optional<Usuario> optional = usuarioService.findUserByEmail(userEmail);
 		if (optional.isPresent()) {
 			Usuario user = optional.get();
 			user.setResetToken(UUID.randomUUID().toString());
 			usuarioService.save(user);
 			String asunto = "Solicitud restablecimiento de contraseña en onomastico";
 			String message = "Para restablecer su contrasena, diríjase a :\n" + RESET_SERVER
 					+ "?token=" + user.getResetToken();
 			
 			emailService.sendEmail(user.getEmail(),asunto, message);
 			return ResponseEntity.ok().build();
 		}
		return ResponseEntity.notFound().build();
 	}

    @PostMapping("/resetpwd")
 	public ResponseEntity<?>displayResetPasswordPage(@RequestParam("token") String token, @RequestParam("password") String password) {
 		Optional<Usuario> user = usuarioService.findUserByResetToken(token);
 		if (user.isPresent()) {
 			Usuario resetUser = user.get(); 
            resetUser.setPassword(passwordEncoder.encode(password));
 			resetUser.setResetToken(null);
 			usuarioService.save(resetUser);
 			return ResponseEntity.ok().build();
 			
 		}
 		return ResponseEntity.unprocessableEntity().build();
    }
}