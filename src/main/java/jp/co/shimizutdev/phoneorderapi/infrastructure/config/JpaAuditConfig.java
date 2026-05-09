package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** JPA監査の設定クラス */
@Configuration
public class JpaAuditConfig {

  /** 監査ユーザーの最大文字数 */
  private static final int AUDITOR_MAX_LENGTH = 50;

  /** 監査ユーザーが最大文字数を超過した場合の例外メッセージ */
  private static final String AUDITOR_TOO_LONG_MESSAGE = "監査ユーザーは50文字以内で指定してください。";

  /** 監査ユーザーを解決するために参照するヘッダー名の優先順 */
  private static final List<String> AUDITOR_HEADER_NAMES =
      List.of("X-User-Id", "X-User-Name", "X-Actor");

  /**
   * 監査担当者を取得する
   *
   * <pre>
   * 解決順は次の通り。
   * 1. SecurityContext の認証ユーザー名
   * 2. リクエストヘッダー（X-User-Id, X-User-Name, X-Actor）
   * 3. 固定値 {@code system}
   * </pre>
   *
   * @return 監査担当者
   * @throws InvalidAuditorException 監査ユーザーが最大文字数を超過している場合
   */
  @Bean
  public AuditorAware<String> auditorAware() {
    return () ->
        currentAuthenticationAuditor()
            .or(() -> currentRequest().map(this::resolveAuditor).filter(StringUtils::hasText))
            .or(() -> Optional.of("system"));
  }

  /**
   * SecurityContext から認証ユーザー名を取得する
   *
   * @return 認証ユーザー名（取得できない場合は空）
   */
  private Optional<String> currentAuthenticationAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    if (authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    if (!StringUtils.hasText(authentication.getName())) {
      return Optional.empty();
    }

    return Optional.of(validateAuditorLength(authentication.getName().trim()));
  }

  /**
   * 現在の HTTP リクエストを取得する
   *
   * @return HTTP リクエスト。取得できない場合は空の Optional
   */
  private Optional<HttpServletRequest> currentRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest);
  }

  /**
   * リクエストヘッダーから監査ユーザーを解決する
   *
   * <p>{@code X-User-Id}、{@code X-User-Name}、{@code X-Actor} の順に参照し、最初に見つかった空でないヘッダー値を使用する。
   *
   * @param request HTTP リクエスト
   * @return 監査ユーザー。対象ヘッダーが未指定の場合は null
   * @throws InvalidAuditorException 監査ユーザーが最大文字数を超過している場合
   */
  private String resolveAuditor(final HttpServletRequest request) {
    return AUDITOR_HEADER_NAMES.stream()
        .map(request::getHeader)
        .filter(StringUtils::hasText)
        .map(String::trim)
        .map(this::validateAuditorLength)
        .findFirst()
        .orElse(null);
  }

  /**
   * 監査ユーザーの文字数を検証する
   *
   * @param auditor 監査ユーザー
   * @return 検証済みの監査ユーザー
   * @throws InvalidAuditorException 監査ユーザーが最大文字数を超過している場合
   */
  private String validateAuditorLength(final String auditor) {
    if (auditor.length() > AUDITOR_MAX_LENGTH) {
      throw new InvalidAuditorException(AUDITOR_TOO_LONG_MESSAGE);
    }

    return auditor;
  }
}
