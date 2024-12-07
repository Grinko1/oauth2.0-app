package com.example.oauthApp.config;


import com.example.oauthApp.exceptions.CustomAccessDeniedHandler;
import com.example.oauthApp.exceptions.CustomAuthenticationEntryPoint;
import com.example.oauthApp.service.SocialAppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final SocialAppService socialAppService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomErrorHandlingFilter customErrorHandlingFilter;
    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(SocialAppService socialAppService,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CustomErrorHandlingFilter customErrorHandlingFilter,
                          CustomLogoutHandler logoutHandler) {
        this.socialAppService = socialAppService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customErrorHandlingFilter = customErrorHandlingFilter;
        this.logoutHandler = logoutHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(customErrorHandlingFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Указывает, что сессия создается только при необходимости
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/login", "/error", "/webjars/**").permitAll()  // Разрешаем доступ всем
                        .requestMatchers("/h2-console/*").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Доступ только для администраторов
                        .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                )
                /*
                 *  Эти exceptionHandling так и не заработали
                 *  возвращает корректные JSON, но тогда "/login" - not found
                 */
//                  .exceptionHandling(exceptionHandling -> exceptionHandling
//                                        .authenticationEntryPoint(authenticationEntryPoint) // Для 401 Unauthorized
//                                        .accessDeniedHandler(accessDeniedHandler) // Для 403 Forbidden
//                                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(o -> o.baseUri("/login"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(socialAppService))  // Обработчик пользователя
                        .defaultSuccessUrl("/user", true)  // Переадресация на страницу пользователя после входа
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(logoutHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                );
        return http.build();
    }

}
