package app.config;

import app.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       http
               .cors(cors -> {})
               .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(matchers -> matchers
                       .requestMatchers("/api/v1/auth/**").permitAll()
                       .anyRequest().authenticated()
               )
               .httpBasic(Customizer.withDefaults())
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .logout((logout) -> logout
                       .logoutUrl("/api/v1/auth/logout")
                       .addLogoutHandler(logoutHandler)
                       .logoutSuccessHandler(
                               ((request, response, authentication) ->
                                       SecurityContextHolder.clearContext()
                               )
                       )
               );


       return http.build();
   }
}
