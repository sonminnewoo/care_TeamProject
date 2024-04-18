package kr.spring.care.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .formLogin(form ->
        form
          .loginPage("/user/login")
          .defaultSuccessUrl("/", true)
          .usernameParameter("email")
          .failureUrl("/user/login/error")
      )
      .logout(logout ->
        logout
          .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
          .logoutSuccessUrl("/")
      );

    http.authorizeHttpRequests(authorize ->
      authorize
        .requestMatchers("/")
        .permitAll()
        .requestMatchers("/info")
        .permitAll()
        .requestMatchers("/user/**")
        .permitAll()
        .requestMatchers("/matching/findcaregiver", "/matching/findjob")
        .authenticated()
        .requestMatchers("/matching/**")
        .permitAll()
        .requestMatchers("/m/**")
        .permitAll()
        .requestMatchers("/item/**")
        .permitAll() //데이터베이스 권한
        .requestMatchers("/css/**")
        .permitAll()
        .requestMatchers("/img/**")
        .permitAll()
        .requestMatchers("/js/**")
        .permitAll() //js를 사용할경우 적용
        .requestMatchers("/notice/**")
        .permitAll() // 공지사항
        .requestMatchers("/admin/**")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated()
    );

    http.exceptionHandling(exceptionHandling ->
      exceptionHandling.authenticationEntryPoint(new CustomEntryPoint())
    );

    http.csrf(csrf ->
      csrf
        .ignoringRequestMatchers("/m/**")
    );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
