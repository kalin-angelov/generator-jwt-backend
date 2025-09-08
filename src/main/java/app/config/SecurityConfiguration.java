package app.config;

import app.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       http
               .cors(cors -> {})
               .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(matchers -> matchers
                       .requestMatchers("/api/v1/auth/**").permitAll()
                       .anyRequest().authenticated()
               )
//               .formLogin(Customizer.withDefaults())
               .httpBasic(Customizer.withDefaults())
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }
}
