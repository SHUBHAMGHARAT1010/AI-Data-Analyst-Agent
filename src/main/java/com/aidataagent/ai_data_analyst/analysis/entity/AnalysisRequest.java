package com.aidataagent.ai_data_analyst.analysis.entity;

import com.aidataagent.ai_data_analyst.analysis.model.AnalysisStatus;
import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_requests")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "analysis_request_id",
            nullable = false,
            unique = true,
            length = 50
    )
    private String analysisRequestId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "dataset_id",
            nullable = false
    )
    private DataSet dataset;

    @Column(
            name = "user_instruction",
            nullable = false
    )
    private String userInstruction;

    @Column(
            name = "analysis_type",
            length = 100
    )
    private String analysisType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 50
    )
    private AnalysisStatus status;

    @Column(
            name = "retry_count",
            nullable = false
    )
    private Integer retryCount;

    @Column(
            name = "max_retry_count",
            nullable = false
    )
    private Integer maxRetryCount;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.retryCount == null) {
            this.retryCount = 0;
        }

        if (this.maxRetryCount == null) {
            this.maxRetryCount = 3;
        }

        if (this.status == null) {
            this.status = AnalysisStatus.CREATED;
        }
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}
