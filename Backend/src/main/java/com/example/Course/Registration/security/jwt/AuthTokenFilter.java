package com.example.Course.Registration.security.jwt;

import java.io.IOException;

import com.example.Course.Registration.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      System.out.println("JWT: "+jwtUtils.validateJwtToken(jwt));
      if (jwt != null && jwtUtils.validateJwtToken(jwt) == 0) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      else if(jwtUtils.validateJwtToken(jwt)==2){
        response.setStatus(501);
        // response.sendRedirect("/api/auth/signin");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT token is expired");
        return;
      }
      else if(jwtUtils.validateJwtToken(jwt)==1){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
        return;
      }
      else if(jwtUtils.validateJwtToken(jwt)==3){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT claims string is empty");
        return;
      }
      else if(jwtUtils.validateJwtToken(jwt)==4){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT token is unsupported");
        return;
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
