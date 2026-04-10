package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * JPA監査設定
 */
@Configuration
public class JpaAuditConfig {

    /**
     * 監査担当者を取得する
     *
     * @return 監査担当者
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system");
    }
}
