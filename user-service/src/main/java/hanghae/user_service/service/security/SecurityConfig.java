package hanghae.user_service.service.security;

import static org.springframework.http.HttpMethod.*;

import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.TokenStoreRepository;
import hanghae.user_service.service.port.UserRepository;
import hanghae.user_service.service.security.handler.FormAccessDeniedHandler;
import hanghae.user_service.service.security.handler.FormEntryPointHandler;
import hanghae.user_service.service.security.jwt.JwtAuthenticationFilter;
import hanghae.user_service.service.security.jwt.JwtAuthorizationFilter;
import hanghae.user_service.service.security.jwt.JwtProcess;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProcess jwtProcess;
    private final AuthenticationConfiguration configuration;
    private final PersonalDataEncryptor encryptor;
    private final UserRepository userRepository;
    private final FormUserDetailService userDetailService;
    private final TokenStoreRepository tokenStoreRepository;
    private final Environment env;

    public SecurityConfig(JwtProcess jwtProcess, AuthenticationConfiguration configuration,
                          PersonalDataEncryptor encryptor, UserRepository userRepository,
                          FormUserDetailService userDetailService, TokenStoreRepository tokenStoreRepository,
                          Environment env) {
        this.jwtProcess = jwtProcess;
        this.configuration = configuration;
        this.encryptor = encryptor;
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
        this.tokenStoreRepository = tokenStoreRepository;
        this.env = env;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                """
                        ROLE_ADMIN > ROLE_USER
                        """);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())

                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailService)
                .logout(logout -> {
                    logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", POST.name()))
                            .logoutSuccessUrl("/")
                            .addLogoutHandler(new CustomLogoutHandler(tokenStoreRepository))
                            .permitAll();

                })
                .addFilterAt(
                        new JwtAuthenticationFilter(authenticationManager(configuration), jwtProcess, encryptor, env,
                                tokenStoreRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        new JwtAuthorizationFilter(authenticationManager(configuration), jwtProcess, userRepository),
                        JwtAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new FormEntryPointHandler())
                        .accessDeniedHandler(new FormAccessDeniedHandler()))

        ;

        return http.build();
    }
}
