package portfolio.project.hashtagqna.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import portfolio.project.hashtagqna.config.jwt.JwtAuthenticationFilter;
import portfolio.project.hashtagqna.repository.MemberRepository;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;

    @Qualifier("userAuthFailureHandler")
    private final AuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsConfig.corsFilter())
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl(corsConfig, memberRepository, failureHandler))
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/members/**").authenticated()
                .requestMatchers("/questions/**").authenticated()
                .requestMatchers("/", "/**").permitAll()
                .anyRequest().permitAll()
        ;
        return http.build();
    }
}
