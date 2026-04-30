package jp.co.shimizutdev.phoneorderapi.support;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.logging.LoggingSystem;

/**
 * テストクラスで変更したログレベルをテスト後に戻す拡張
 */
public final class ResetLogLevelExtension implements AfterAllCallback {

    @Override
    public void afterAll(final ExtensionContext context) {
        final ResetLogLevel resetLogLevel = context.getRequiredTestClass().getAnnotation(ResetLogLevel.class);
        if (resetLogLevel == null) {
            return;
        }

        final LoggingSystem loggingSystem = LoggingSystem.get(context.getRequiredTestClass().getClassLoader());
        for (final Class<?> loggerClass : resetLogLevel.value()) {
            loggingSystem.setLogLevel(loggerClass.getName(), null);
        }
    }
}
