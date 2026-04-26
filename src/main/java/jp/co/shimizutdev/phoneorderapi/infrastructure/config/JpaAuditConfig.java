package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

/**
 * JPA監査設定
 */
@Configuration
public class JpaAuditConfig {

    /**
     * 監査ユーザーを解決するために参照するヘッダー名一覧
     */
    private static final List<String> AUDITOR_HEADER_NAMES = List.of(
        "X-User-Id",
        "X-User-Name",
        "X-Actor"
    );

    /**
     * 監査担当者を取得する
     *
     * @return 監査担当者
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> currentRequest()
            .map(this::resolveAuditor)
            .filter(StringUtils::hasText)
            .or(() -> Optional.of("system"));
    }

    /**
     * 現在の HTTP リクエストを取得する
     *
     * @return HTTP リクエスト
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
     * @param request HTTP リクエスト
     * @return 監査ユーザー
     */
    private String resolveAuditor(final HttpServletRequest request) {
        return AUDITOR_HEADER_NAMES.stream()
            .map(request::getHeader)
            .filter(StringUtils::hasText)
            .map(String::trim)
            .findFirst()
            .orElse(null);
    }
}
