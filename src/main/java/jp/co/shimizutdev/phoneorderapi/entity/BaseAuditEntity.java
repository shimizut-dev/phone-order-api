package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * 監査項目
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseAuditEntity {

    /**
     * 作成日時
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * 作成者
     */
    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    /**
     * 更新日時
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    /**
     * 更新者
     */
    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

    /**
     * 削除日時
     */
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    /**
     * 削除者
     */
    @Column(name = "deleted_by", length = 50)
    private String deletedBy;
}
