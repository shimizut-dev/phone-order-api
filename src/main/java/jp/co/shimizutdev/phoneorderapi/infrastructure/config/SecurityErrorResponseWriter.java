package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import jp.co.shimizutdev.phoneorderapi.presentation.error.ApiErrorResponse;
import jp.co.shimizutdev.phoneorderapi.presentation.error.ApiValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/** セキュリティ関連のエラーレスポンスを JSON で返すユーティリティ */
@Component
public class SecurityErrorResponseWriter {

  /** JSON 変換に使用する ObjectMapper */
  private final ObjectMapper objectMapper;

  /**
   * コンストラクタ
   *
   * @param objectMapper JSON 変換に使用する ObjectMapper
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "ObjectMapper は Spring のシングルトンBeanとして管理される。")
  public SecurityErrorResponseWriter(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * 標準形式のセキュリティエラーレスポンスを書き出す
   *
   * @param request HTTP リクエスト
   * @param response HTTP レスポンス
   * @param status HTTP ステータス
   * @param message エラーメッセージ
   * @throws IOException レスポンス書き込みに失敗した場合
   */
  public void writeErrorResponse(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final HttpStatus status,
      final String message)
      throws IOException {

    response.setStatus(status.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ApiErrorResponse body =
        new ApiErrorResponse(
            OffsetDateTime.now(),
            status.value(),
            status.name(),
            message,
            request.getRequestURI(),
            List.of(new ApiValidationError("auth", message)));

    objectMapper.writeValue(response.getOutputStream(), body);
  }
}
