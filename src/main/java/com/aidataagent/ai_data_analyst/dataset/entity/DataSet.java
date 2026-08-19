package com.aidataagent.ai_data_analyst.dataset.entity;

import com.aidataagent.ai_data_analyst.dataset.model.DatasetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "datasets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name= "dataset_id",
            nullable = false,
            unique = true,
            length = 50
    )
    private String datasetId;

    @Column(
            name = "original_file_name",
            nullable = false,
            length = 50
    )
    private String originalFileName;

    @Column(
            name= "file_type",
            nullable = false,
            length = 20
    )
    private String fileType;

    @Column(
            name = "file_path",
            nullable = false,
            length = 1000
    )
    private String filePath;

    @Column(
            name = "domain",
            length = 100
    )
    private String domain;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 50
    )
    private DatasetStatus status;

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

    @Column(name= "error_message")
    private String errorMessage;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name= "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

@PrePersist
    public void prePersist(){
        LocalDateTime now=LocalDateTime.now();
        this.createdAt=now;
        this.updatedAt=now;

        if(this.retryCount==null){
            this.retryCount=0;
        }
        if(this.maxRetryCount==null){
            this.maxRetryCount=3;
        }
        if(this.status==null){
            this.status=DatasetStatus.UPLOADED;
        }
    }

    @PreUpdate
    public void preUpdate(){
    this.updatedAt=LocalDateTime.now();
    }

}
