package com.example.camp.Configuration;


import com.example.camp.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NoArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {



    @Value("${auth.header}")
    private  String TOKEN_HEADER;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    public JwtTokenFilter(JwtTokenUtil tokenUtil, UserService userService) {
        this.jwtTokenUtil = tokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header =request.getHeader(TOKEN_HEADER);
        System.out.println(header);

        final SecurityContext securityContext= SecurityContextHolder.getContext();
        System.out.println(securityContext);

        if (header!=null && securityContext.getAuthentication() ==null){
            String token=header.substring("Bearer ".length());
            String username= jwtTokenUtil.getUsername(token);
            if (username!=null){
                UserDetails userDetails= userService.loadUserByUsername(username);
                if (jwtTokenUtil.isTokenValid(token, userDetails)){
                    UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }

            }



        }
        filterChain.doFilter(request,response);


    }


}
