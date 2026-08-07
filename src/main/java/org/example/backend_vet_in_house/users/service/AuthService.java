package org.example.backend_vet_in_house.users.service;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.shared.exception.user.UserAlreadyExistsException;
import org.example.backend_vet_in_house.users.dto.res.RoleResDTO;
import org.example.backend_vet_in_house.utils.JwtUtil;
import org.example.backend_vet_in_house.users.dto.req.LoginReqDTO;
import org.example.backend_vet_in_house.users.dto.req.RegisterReqDTO;
import org.example.backend_vet_in_house.users.dto.res.LoginResDTO;
import org.example.backend_vet_in_house.users.model.RoleEntity;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.RoleEntityRepository;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final JwtUtil jwtUtil;

    public String registerUser(RegisterReqDTO req) throws RoleNotFoundException {

        if(userEntityRepository.existsByUsername(req.username())) {
            throw new UserAlreadyExistsException("Usuario: " + req.username() + " ya existe");
        }

        Set<RoleEntity> roles = new HashSet<>();

        RoleEntity roleDefault = roleEntityRepository.findById(1L)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        roles.add(roleDefault);

        userEntityRepository
                .save(UserEntity.builder()
                        .firstName(req.firstName())
                        .lastName(req.lastName())
                        .username(req.username())
                        .password(passwordEncoder.encode(req.password()))
                        .isEnabled(true)
                        .isAccountNonExpired(true)
                        .isAccountNonLocked(true)
                        .isCredentialsNonExpired(true)
                        .roles(roles)
                        .build()
                );
        return "User registered with successfully";
    }

    public LoginResDTO loginUser(LoginReqDTO req) {
        String username = req.username();
        String password = req.password();

        Authentication authentication = authenticated(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.createToken(authentication);

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username" + username + " not found"));

        List<RoleResDTO> roles = user.getRoles().stream()
                .map(r -> new RoleResDTO(
                        r.getRoleEnum().name()
                )).toList();

        return new LoginResDTO(
                user.getFirstName(),
                user.getLastName(),
                username,
                "User logued successfuly.",
                token,
                roles,
                true);
    }

    public Authentication authenticated(String username, String password) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
