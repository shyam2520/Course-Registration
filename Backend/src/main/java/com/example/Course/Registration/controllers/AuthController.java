package com.example.Course.Registration.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.Course.Registration.Util.Util;
import com.example.Course.Registration.payload.response.AbstractResponseFactory;
import com.example.Course.Registration.payload.response.JwtResponseFactory;
import com.example.Course.Registration.payload.response.MessageResponseFactory;
import com.example.Course.Registration.security.jwt.JwtUtils;
import com.example.Course.Registration.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Course.Registration.models.ERole;
import com.example.Course.Registration.models.Role;
import com.example.Course.Registration.models.User;
import com.example.Course.Registration.payload.request.LoginRequest;
import com.example.Course.Registration.payload.request.SignupRequest;
import com.example.Course.Registration.payload.response.MessageResponse;
import com.example.Course.Registration.repository.RoleRepository;
import com.example.Course.Registration.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	AuthenticationManager authenticationManager;

	UserRepository userRepository;

	RoleRepository roleRepository;

	PasswordEncoder encoder;

    JwtUtils jwtUtils;

	AbstractResponseFactory messageFactory = new MessageResponseFactory();

	AbstractResponseFactory jwtFactory = new JwtResponseFactory();
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		return ResponseEntity.ok(jwtFactory.getResponse(Util.makeCSVString(List.of(jwt, userDetails.getId(), userDetails.getFullName(),userDetails.getUsername(), roles.get(0)))));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		// if (userRepository.existsByUsername(signUpRequest.getUsername())) {
		// 	return ResponseEntity
		// 			.badRequest()
		// 			.body(new MessageResponse("Error: Username is already taken!"));
		// }

		// System.out.println("TESTING");
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(messageFactory.getResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getName(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
							 signUpRequest.getBranch(),
							 signUpRequest.getDegree());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		System.out.println("Details of User:");
		System.out.println("Name :"+user.getName());
		System.out.println("Email :"+user.getEmail());
		System.out.println("Password :"+user.getPassword());
		System.out.println("Branch :"+user.getBranch());
		System.out.println("Degree :"+user.getDegree());
		// System.out.println("\nRoles :"+user.getRoles());
		for (Role role2 : user.getRoles()) {
			System.out.println("Role :"+role2.getName());
		}
		userRepository.save(user);

		return ResponseEntity.ok(messageFactory.getResponse("User registered successfully!"));
	}

	
}
