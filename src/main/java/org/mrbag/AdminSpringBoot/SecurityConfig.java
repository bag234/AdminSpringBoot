package org.mrbag.AdminSpringBoot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration
public class SecurityConfig {

    private final AdminServerProperties adminServer;

    public SecurityConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminServer.getContextPath() + "/");

        http
            .authorizeHttpRequests((req) -> req
                .requestMatchers(adminServer.getContextPath() + "/assets/**").permitAll()
                .requestMatchers(adminServer.getContextPath() + "/login").permitAll()
                .requestMatchers("/users/**").hasRole("USERS")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage(adminServer.getContextPath() + "/login")
                .successHandler(successHandler)
            )
            .logout((logout) -> logout.logoutUrl(adminServer.getContextPath() + "/logout"))
            .httpBasic(Customizer.withDefaults())
            .csrf((csrf) -> csrf.disable());

        return http.build();
    }
}
