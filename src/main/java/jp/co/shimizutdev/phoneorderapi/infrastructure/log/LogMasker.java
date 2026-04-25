package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * ログマスキング
 */
@Component
@RequiredArgsConstructor
public class LogMasker {

    /**
     * マスク文字列
     */
    private static final String MASKED_VALUE = "****";

    /**
     * マスク対象ヘッダー名
     */
    private static final Set<String> MASK_TARGET_HEADER_NAMES = Set.of(
        "authorization",
        "cookie",
        "set-cookie",
        "x-api-key",
        "proxy-authorization"
    );

    /**
     * マスク対象項目名
     */
    private static final Set<String> MASK_TARGET_FIELD_NAMES = Set.of(
        "password",
        "passwd",
        "token",
        "access_token",
        "refresh_token",
        "authorization",
        "secret",
        "api_key",
        "apikey",
        "card_number",
        "phone_number",
        "email",
        "mail"
    );

    /**
     * オブジェクトマッパー
     */
    private final ObjectMapper objectMapper;

    /**
     * ヘッダー値をマスキングする。
     *
     * @param headerName  ヘッダー名
     * @param headerValue ヘッダー値
     * @return マスキング後ヘッダー値
     */
    public String maskHeader(final String headerName, final String headerValue) {
        if (headerValue == null) {
            return "";
        }

        if (headerName == null) {
            return headerValue;
        }

        if (MASK_TARGET_HEADER_NAMES.contains(normalizeName(headerName))) {
            return MASKED_VALUE;
        }

        return headerValue;
    }

    /**
     * テキストをマスキングする。
     *
     * @param text テキスト
     * @return マスキング後テキスト
     */
    public String maskText(final String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        try {
            JsonNode root = objectMapper.readTree(text);
            maskJsonNode(root);
            return objectMapper.writeValueAsString(root);
        } catch (Exception ex) {
            return normalizeWhitespace(text);
        }
    }

    /**
     * オブジェクトをマスキングする。
     *
     * @param value 値
     * @return マスキング後文字列
     */
    public String maskObject(final Object value) {
        if (value == null) {
            return "";
        }

        try {
            JsonNode root = objectMapper.valueToTree(value);
            maskJsonNode(root);
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException | IllegalArgumentException ex) {
            return normalizeWhitespace(String.valueOf(value));
        }
    }

    /**
     * JSONノードを再帰的にマスキングする。
     *
     * @param node JSONノード
     */
    private void maskJsonNode(final JsonNode node) {
        switch (node) {
            case ObjectNode objectNode -> maskObjectNode(objectNode);
            case ArrayNode arrayNode -> maskArrayNode(arrayNode);
            default -> {
                // ObjectNode / ArrayNode 以外の値ノードは再帰的に走査する項目がないため、マスキングしない
            }
        }
    }

    /**
     * JSONオブジェクトをマスキングする。
     *
     * @param objectNode JSONオブジェクト
     */
    private void maskObjectNode(final ObjectNode objectNode) {
        objectNode.properties().forEach(entry -> maskObjectNodeEntry(objectNode, entry));
    }

    /**
     * JSONオブジェクトの項目をマスキングする。
     *
     * @param objectNode JSONオブジェクト
     * @param entry      項目
     */
    private void maskObjectNodeEntry(
        final ObjectNode objectNode,
        final Map.Entry<String, JsonNode> entry) {

        String fieldName = entry.getKey();
        JsonNode childNode = entry.getValue();

        if (isMaskTargetField(fieldName)) {
            objectNode.put(fieldName, MASKED_VALUE);
            return;
        }

        maskJsonNode(childNode);
    }

    /**
     * JSON配列をマスキングする。
     *
     * @param arrayNode JSON配列
     */
    private void maskArrayNode(final ArrayNode arrayNode) {
        arrayNode.forEach(this::maskJsonNode);
    }

    /**
     * マスク対象項目か判定する。
     *
     * @param fieldName 項目名
     * @return 判定結果
     */
    private boolean isMaskTargetField(final String fieldName) {
        return MASK_TARGET_FIELD_NAMES.contains(normalizeName(fieldName));
    }

    /**
     * 項目名を比較用へ正規化する。
     *
     * @param name 名称
     * @return 正規化後名称
     */
    private String normalizeName(final String name) {
        return name.replace("-", "_")
            .toLowerCase(Locale.ROOT);
    }

    /**
     * 文字列を1行へ正規化する。
     *
     * @param text テキスト
     * @return 正規化後テキスト
     */
    private String normalizeWhitespace(final String text) {
        return text.replaceAll("\\R", " ")
            .replaceAll("\\s{2,}", " ")
            .trim();
    }
}
