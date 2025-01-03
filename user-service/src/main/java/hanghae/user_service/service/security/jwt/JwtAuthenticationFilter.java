package hanghae.user_service.service.security.jwt;

import static hanghae.user_service.service.security.jwt.JwtVO.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.user_service.controller.req.UserLoginReqDto;
import hanghae.user_service.service.common.util.CustomResponseUtil;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.TokenStoreRepository;
import hanghae.user_service.service.security.model.LoginUser;
import hanghae.user_service.service.security.model.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String LOGIN_SUCCESS = "Login Success";
    private static final String LOGIN_FAIL = "Login Fail";

    private final AuthenticationManager authenticationManager;
    private final JwtProcess jwtProcess;
    private final PersonalDataEncryptor encryptor;
    private final Environment env;
    private final TokenStoreRepository tokenStoreRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess,
                                   PersonalDataEncryptor encryptor, Environment env,
                                   TokenStoreRepository tokenStoreRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtProcess = jwtProcess;
        this.encryptor = encryptor;
        this.env = env;
        this.tokenStoreRepository = tokenStoreRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginReqDto loginDto = objectMapper.readValue(request.getInputStream(), UserLoginReqDto.class);
            String encodedEmail = encryptor.encodeData(loginDto.email());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    encodedEmail, loginDto.password()
            );
            return authenticationManager.authenticate(authToken);

        } catch (Exception e) {
            throw new AuthenticationServiceException(LOGIN_FAIL, e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        LoginUser loginUser = principal.getLoginUser();
        Long expirationTime = Long.valueOf(env.getProperty("login.user.expiration-time"));

        String accessToken = jwtProcess.createAccessToken(loginUser.userId(), expirationTime);
        String key = env.getProperty("redis.user.token");
        tokenStoreRepository.save(key, accessToken, expirationTime);

        response.addHeader(AUTHORIZATION, TOKEN_PREFIX + accessToken);
        CustomResponseUtil.success(response, LOGIN_SUCCESS);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, LOGIN_FAIL, HttpStatus.UNAUTHORIZED);
    }
}
