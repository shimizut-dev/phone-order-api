package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** JPA監査設定 */
@Configuration
public class JpaAuditConfig {

  /** 監査ユーザー最大文字数 */
  private static final int AUDITOR_MAX_LENGTH = 50;

  /** 監査ユーザー最大文字数超過時の例外メッセージ */
  private static final String AUDITOR_TOO_LONG_MESSAGE = "監査ユーザーは50文字以内で指定してください。";

  /** 監査ユーザーを解決するために参照するヘッダー名一覧 */
  private static final List<String> AUDITOR_HEADER_NAMES =
      List.of("X-User-Id", "X-User-Name", "X-Actor");

  /**
   * 監査担当者を取得する。
   *
   * <p>現在の HTTP リクエストから監査ユーザーを解決し、リクエストが存在しない場合または 監査ユーザー用ヘッダーが未指定の場合は {@code system} を返す。
   *
   * @return 監査担当者
   * @throws InvalidAuditorException 監査ユーザーが最大文字数を超過している場合
   */
  @Bean
  public AuditorAware<String> auditorAware() {
    return () ->
        currentRequest()
            .map(this::resolveAuditor)
            .filter(StringUtils::hasText)
            .or(() -> Optional.of("system"));
  }

  /**
   * 現在の HTTP リクエストを取得する
   *
   * @return HTTP リクエスト。存在しない場合は空の Optional
   */
  private Optional<HttpServletRequest> currentRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest);
  }

  /**
   * リクエストヘッダーから監査ユーザーを解決する。
   *
   * <p>{@code X-User-Id}、{@code X-User-Name}、{@code X-Actor} の順に参照し、 最初に見つかった空でないヘッダー値を使用する。
   *
   * @param request HTTP リクエスト
   * @return 監査ユーザー。対象ヘッダーが存在しない場合は null
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
   * @return 検証済み監査ユーザー
   * @throws InvalidAuditorException 監査ユーザーが最大文字数を超過している場合
   */
  private String validateAuditorLength(final String auditor) {
    if (auditor.length() > AUDITOR_MAX_LENGTH) {
      throw new InvalidAuditorException(AUDITOR_TOO_LONG_MESSAGE);
    }

    return auditor;
  }
}
