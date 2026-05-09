package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** 未認証アクセス時に JSON エラーレスポンスを返すエントリポイント */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

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
  public RestAuthenticationEntryPoint(final SecurityErrorResponseWriter errorResponseWriter) {
    this.errorResponseWriter = errorResponseWriter;
  }

  /**
   * 未認証アクセス時の応答を返す
   *
   * @param request HTTP リクエスト
   * @param response HTTP レスポンス
   * @param authException 認証例外
   * @throws IOException レスポンス書き込みに失敗した場合
   * @throws ServletException サーブレット例外
   */
  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException)
      throws IOException, ServletException {
    errorResponseWriter.writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "認証が必要です。");
  }
}
