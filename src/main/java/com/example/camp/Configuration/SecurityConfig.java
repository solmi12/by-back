package com.example.camp.Configuration;


import com.example.camp.entity.Role;
import com.example.camp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    private final JwtTokenFilter jwtTokenFilter;
    @Autowired
    private  CorsFilterConfig corsFilterConfig;
    private final JwtTokenUtil jwtTokenUtil;

    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, JwtTokenUtil jwtTokenUtil, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jwtTokenUtil = jwtTokenUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;

    }
    @Bean

    public JwtTokenFilter authFilter(){
        return new JwtTokenFilter();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userService.loadUserByUsername(username));
    }

    @Bean
    public SecurityFilterChain filtreAdminChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/tools/add").permitAll()
                            .requestMatchers(HttpMethod.GET,"/tools/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/invoices/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/invoices/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/invoices/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/invoices/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/cart/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/cart/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/cart/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/cart/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/tools/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/tools/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/tools/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider());
        httpSecurity.addFilterBefore(corsFilterConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
