package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * メソッド開始/終了ログ出力アスペクト
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MethodLogAspect {

    /**
     * ログ最大文字数
     */
    private static final int MAX_LOG_LENGTH = 5000;

    /**
     * ログマスキング
     */
    private final LogMasker logMasker;

    /**
     * メソッド開始 / 終了ログを出力する。
     *
     * @param joinPoint JoinPoint
     * @return 戻り値
     * @throws Throwable 例外
     */
    @Around(
        "execution(* jp.co.shimizutdev.phoneorderapi.presentation..*Controller.*(..)) || "
            + "execution(* jp.co.shimizutdev.phoneorderapi.application..*Service.*(..)) || "
            + "execution(* jp.co.shimizutdev.phoneorderapi.infrastructure.repository..*RepositoryImpl.*(..))"
    )
    @SuppressWarnings("unused")
    public Object logMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getMethod().getName();
        String methodDisplayName = className + "#" + methodName;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[method start] methodName: {}", methodDisplayName);

        if (log.isDebugEnabled()) {
            log.debug(
                "[method start] arguments: [{}] {}",
                getRuntimeTypeName(joinPoint.getArgs()),
                abbreviate(logMasker.maskObject(joinPoint.getArgs()))
            );
        }

        try {
            Object returnValue = joinPoint.proceed();

            stopWatch.stop();

            log.info("[method end] methodName: {}", methodDisplayName);
            log.info("[method end] duration(ms): {}", stopWatch.getTotalTimeMillis());

            if (log.isDebugEnabled()) {
                log.debug(
                    "[method end] return value: [{}] {}",
                    getDeclaredReturnTypeName(signature, returnValue),
                    abbreviate(logMasker.maskObject(returnValue))
                );
            }

            return returnValue;
        } catch (Throwable ex) {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            log.warn("[method exception] {}", methodDisplayName);
            log.warn("[method exception] class: {}", ex.getClass().getName());
            log.warn("[method exception] message: {}", nullToEmpty(ex.getMessage()));
            log.warn("[method exception] duration(ms): {}", stopWatch.getTotalTimeMillis());

            throw ex;
        }
    }

    /**
     * 宣言上の戻り値型を取得する。
     *
     * @param signature   メソッドシグネチャ
     * @param returnValue 戻り値
     * @return 戻り値型
     */
    private String getDeclaredReturnTypeName(final MethodSignature signature, final Object returnValue) {
        String typeName = formatType(signature.getMethod().getGenericReturnType());

        if (returnValue instanceof Collection<?> collection) {
            return typeName + "(size=" + collection.size() + ")";
        }

        if (returnValue != null && returnValue.getClass().isArray()) {
            return typeName + "(length=" + Array.getLength(returnValue) + ")";
        }

        return typeName;
    }

    /**
     * 実行時の型名を取得する。
     *
     * @param value 値
     * @return 型名
     */
    private String getRuntimeTypeName(final Object value) {
        if (value == null) {
            return "null";
        }

        return value.getClass().getSimpleName();
    }

    /**
     * Typeを文字列へ整形する。
     *
     * @param type 型
     * @return 型名
     */
    private String formatType(final Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            String rawTypeName = simpleTypeName(parameterizedType.getRawType());
            String actualTypeNames = Stream.of(parameterizedType.getActualTypeArguments())
                .map(this::formatType)
                .collect(Collectors.joining(", "));
            return rawTypeName + "<" + actualTypeNames + ">";
        }

        return simpleTypeName(type);
    }

    /**
     * 型名をシンプル名へ変換する。
     *
     * @param type 型
     * @return シンプル名
     */
    private String simpleTypeName(final Type type) {
        String typeName = type.getTypeName();
        int lastDotIndex = typeName.lastIndexOf('.');
        return lastDotIndex >= 0 ? typeName.substring(lastDotIndex + 1) : typeName;
    }

    /**
     * ログ文字列を最大文字数に省略する。
     *
     * @param value 値
     * @return 省略後文字列
     */
    private String abbreviate(final String value) {
        if (value == null) {
            return "";
        }

        if (value.length() <= MAX_LOG_LENGTH) {
            return value;
        }

        return value.substring(0, MAX_LOG_LENGTH) + "...(truncated)";
    }

    /**
     * null を空文字へ変換する。
     *
     * @param value 値
     * @return 変換後文字列
     */
    private String nullToEmpty(final String value) {
        return value == null ? "" : value;
    }
}
