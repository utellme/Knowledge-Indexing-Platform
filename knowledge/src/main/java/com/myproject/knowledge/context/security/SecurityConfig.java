package com.myproject.knowledge.context.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.myproject.knowledge.config.ApiKeyRequestFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyRequestFilter apiKeyFilter;

    public SecurityConfig(ApiKeyRequestFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable default security features you are not using
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .httpBasic(httpBasic->httpBasic.disable()) // disable basic auth and removes wwww-authenticate header
            .formLogin(form -> form.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/health",
                    "/api/v1/metrics",
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .requestMatchers("/api/v1/tenants/**").hasRole("TENANT")
                .requestMatchers("/h2-console/**").hasRole("TENANT")
                .anyRequest().denyAll()
                //.anyRequest().authenticated()
            )

            // Register API Key filter
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)

            // Needed for H2 console
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}

// import java.util.Arrays;
// import java.util.List;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;





/**
 * Configuration class for security config.
 */
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class WebSecurityConfiguration{

   

//    // private static final String POLICY_DIRECTIVES = "default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none';";

//     @Bean
//     protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
//        // configure(httpSecurity);
//         return httpSecurity.build();
//     }

    // protected void configure(final HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity.cors(withDefaults())
    //             .headers(headers -> headers.xssProtection(HeadersConfigurer.XXssConfig::disable))
    //             .headers(headers -> headers.contentSecurityPolicy(p -> p.policyDirectives(POLICY_DIRECTIVES)))
    //             .csrf(AbstractHttpConfigurer::disable)
    //             .authorizeHttpRequests((requests) -> requests
    //             .requestMatchers("/hello").permitAll()
    //             .requestMatchers("/probe/**").permitAll()
    //             .requestMatchers("/hello", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui/index.html**", "/webjars/**").permitAll()
    //             .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
    //             .requestMatchers("/**")
    //             .authenticated())
    //             .oauth2ResourceServer((oauth2) -> oauth2
    //             .jwt((jwt) -> jwt
    //             .authenticationManager(new RBACJWTAuthenticationManager(jwtDecoder(), this.userPermissionsTableStorage, this.userRepository))));
    // }


    // protected void configure(HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
    //             .authorizeRequests().antMatchers("/console/**").permitAll();
    //     httpSecurity.csrf().disable();
    //     httpSecurity.headers().frameOptions().disable();
    // }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     final CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.addAllowedOriginPattern(CorsConfiguration.ALL);
    //     configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
    //     configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
    //     final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

//}