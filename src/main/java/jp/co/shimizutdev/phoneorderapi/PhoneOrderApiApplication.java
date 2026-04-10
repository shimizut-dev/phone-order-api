package jp.co.shimizutdev.phoneorderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * アプリケーション起動クラス
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PhoneOrderApiApplication {

    /**
     * アプリケーションを起動する
     *
     * @param args 起動引数
     */
    public static void main(final String[] args) {
        SpringApplication.run(PhoneOrderApiApplication.class, args);
    }
}
