package org.example.backend_vet_in_house.shared.config.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.utils.JwtUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class JwtFilterValidator extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Es buena práctica validar que empiece con "Bearer " para evitar StringIndexOutOfBoundsException
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtil.validateToken(token);
                String username = jwtUtil.extractUsername(decodedJWT);
                String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();
                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            } catch (JWTVerificationException e) {
                // Construir la respuesta 401 Unauthorized directamente desde el filtro
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Token inválido o expirado\"}");
                return; // Detenemos la ejecución, no llamamos a filterChain.doFilter()
            }
        }

        filterChain.doFilter(request, response);
    }
}
