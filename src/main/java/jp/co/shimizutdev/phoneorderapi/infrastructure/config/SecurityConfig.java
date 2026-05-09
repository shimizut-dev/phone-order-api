package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/** API 向け Spring Security の設定 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  /** Basic 認証ユーザー名 */
  @Value("${app.security.basic.username}")
  private String username;

  /** Basic 認証パスワード */
  @Value("${app.security.basic.password}")
  private String password;

  /**
   * セキュリティフィルタチェーンを構成する
   *
   * @param http HTTP セキュリティ設定
   * @param authenticationEntryPoint 未認証時の応答処理
   * @param accessDeniedHandler 認可エラー時の応答処理
   * @return 構成済みのフィルタチェーン
   * @throws Exception 設定に失敗した場合
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      final HttpSecurity http,
      final RestAuthenticationEntryPoint authenticationEntryPoint,
      final RestAccessDeniedHandler accessDeniedHandler)
      throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/orders/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/orders")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/orders/*/cancel")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  /**
   * Basic 認証用のインメモリユーザーを作成する
   *
   * @param passwordEncoder パスワードエンコーダー
   * @return ユーザー情報サービス
   */
  @Bean
  public UserDetailsService userDetailsService(final PasswordEncoder passwordEncoder) {
    UserDetails apiUser =
        User.withUsername(username)
            .password(passwordEncoder.encode(password))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(apiUser);
  }

  /**
   * パスワードエンコーダーを作成する
   *
   * @return パスワードエンコーダー
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
