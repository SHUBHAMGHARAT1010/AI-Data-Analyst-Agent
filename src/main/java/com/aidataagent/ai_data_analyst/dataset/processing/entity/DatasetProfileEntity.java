package com.aidataagent.ai_data_analyst.dataset.processing.entity;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "dataset_profiles",
        indexes = {
                @Index(name = "idx_dataset_profile_datset_id", columnList = "dataset_id")
        }
)
@Getter
public class DatasetProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "dataset_id",
            nullable = false,
            unique = true
    )
    private DataSet dataSet;

    @Column(name = "total_rows",
            nullable = false)
    private long totalRows;

    @Column(name = "total_columns", nullable = false)
    private int totalColumns;

    @Column(name = "duplicate_rows", nullable = false)
    private long duplicateRows;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "datasetProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DatasetColumnProfileEntity> columnProfiles= new ArrayList<>();

    protected DatasetProfileEntity() {

    }

    public DatasetProfileEntity(DataSet dataSet, long totalRows, int totalColumns, long duplicateRows) {

        this.dataSet = dataSet;
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.duplicateRows = duplicateRows;
        this.createdAt = LocalDateTime.now();

    }

    public void addColumnProfile(DatasetColumnProfileEntity columnProfileEntity){
        columnProfiles.add(columnProfileEntity);
        columnProfileEntity.setDatasetProfile(this);
    }

}


