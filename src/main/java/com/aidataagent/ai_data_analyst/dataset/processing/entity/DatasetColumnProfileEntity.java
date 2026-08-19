package com.aidataagent.ai_data_analyst.dataset.processing.entity;


import com.aidataagent.ai_data_analyst.dataset.processing.model.ColumnProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "dataset_column_profiles",
indexes = {
        @Index(name = "idx_column_profile_dataset_profile_id",
        columnList = "dataset_profile_id")
})
@Getter
@AllArgsConstructor
@Builder
public class DatasetColumnProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "dataset_profile_id",nullable = false)
    private DatasetProfileEntity datasetProfile;

    @Column(name = "column_name",nullable = false)
    private String columnName;

    @Column(name = "data_type",nullable = false)
    private String dataType;

    @Column(name = "total_values",nullable = false)
    private Long totalValues;

    @Column(name = "null_values",nullable = false)
    private Long nullvalues;

    @Column(name = "empty_values",nullable = false)
    private Long emptyValues;

protected DatasetColumnProfileEntity(){

}

public DatasetColumnProfileEntity(
        DatasetProfileEntity datasetProfile,String columnName,
                                  String dataType,
                                  long totalValues,
                                  long nullvalues,
                                  long emptyValues

                                  ){
    this.columnName=columnName;
    this.dataType=dataType;
    this.totalValues=totalValues;
    this.nullvalues=nullvalues;
    this.emptyValues=emptyValues;
    this.datasetProfile=datasetProfile;

}

public DatasetColumnProfileEntity(DatasetProfileEntity datasetProfile, ColumnProfile columnProfile){
    this.datasetProfile=datasetProfile;
    this.columnName=columnProfile.columnName();
    this.dataType=columnProfile.dataType().toString();
    this.totalValues=columnProfile.totalValues();
    this.nullvalues=columnProfile.nullValues();
    this.emptyValues=columnProfile.emptyValues();

}


    public void setDatasetProfile(DatasetProfileEntity datasetProfile){
        this.datasetProfile=datasetProfile;
    }
}
