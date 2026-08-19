package com.aidataagent.ai_data_analyst.dataset.processing.entity;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ProcessingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dataset_processing_job")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatasetProcessingJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false, unique = true)
    private String jobId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = false)
    private DataSet dataSet;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @PrePersist
    public void prePersist() {

        if (retryCount == null) {
            this.retryCount = 0;
        }
        if (status == null) {
            this.status = ProcessingStatus.PENDING;
        }
    }
}
