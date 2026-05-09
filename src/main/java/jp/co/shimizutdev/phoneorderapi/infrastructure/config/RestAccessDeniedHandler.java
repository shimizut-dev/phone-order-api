package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/** 認可エラー時に JSON エラーレスポンスを返すハンドラー */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  /** セキュリティエラー応答の書き出し処理 */
  private final SecurityErrorResponseWriter errorResponseWriter;

  /**
   * コンストラクタ
   *
   * @param errorResponseWriter セキュリティエラー応答の書き出し処理
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "SecurityErrorResponseWriter は Spring のシングルトンBeanとして管理される。")
  public RestAccessDeniedHandler(final SecurityErrorResponseWriter errorResponseWriter) {
    this.errorResponseWriter = errorResponseWriter;
  }

  /**
   * 認可エラー時の応答を返す
   *
   * @param request HTTP リクエスト
   * @param response HTTP レスポンス
   * @param accessDeniedException 認可例外
   * @throws IOException レスポンス書き込みに失敗した場合
   * @throws ServletException サーブレット例外
   */
  @Override
  public void handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    errorResponseWriter.writeErrorResponse(
        request, response, HttpStatus.FORBIDDEN, "アクセスが拒否されました。");
  }
}
