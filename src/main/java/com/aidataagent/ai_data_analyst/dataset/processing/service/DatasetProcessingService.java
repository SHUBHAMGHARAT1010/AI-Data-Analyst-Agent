package com.aidataagent.ai_data_analyst.dataset.processing.service;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetColumnProfileEntity;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProcessingJob;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProfileEntity;
import com.aidataagent.ai_data_analyst.dataset.processing.mapper.DatasetProfileMapper;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ColumnQualityResult;
import com.aidataagent.ai_data_analyst.dataset.processing.model.DatasetProfile;
import com.aidataagent.ai_data_analyst.dataset.processing.profiler.DatasetProfiler;
import com.aidataagent.ai_data_analyst.dataset.processing.reader.DatasetFileReader;
import com.aidataagent.ai_data_analyst.dataset.processing.repository.DatasetColumnProfileRepository;
import com.aidataagent.ai_data_analyst.dataset.processing.repository.DatasetProcessingJobRepository;
import com.aidataagent.ai_data_analyst.dataset.processing.repository.DatasetProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class DatasetProcessingService {

    private final DatasetProfiler profiler;
    private static final Logger log = LoggerFactory.getLogger(DatasetProcessingService.class);
    private final DatasetFileReader datasetFileReader;
    private final DatasetProfilingService profilingService;
    private final DatasetProfileMapper profileMapper;
    private final DatasetProfileRepository profileRepository;
    private final DatasetColumnProfileRepository columnProfileRepository;
    private final DatasetProcessingJobRepository jobRepository;
    private final DataQualityService dataQualityService;


    public DatasetProcessingService(DatasetFileReader datasetFileReader,
                                    DatasetProfiler profiler,
                                    DatasetProfilingService profilingService,
                                    DatasetProfileMapper profileMapper,
                                    DatasetProfileRepository profileRepository,
                                    DatasetColumnProfileRepository columnProfileRepository,
                                    DatasetProcessingJobRepository jobRepository,
                                    DataQualityService dataQualityService) {
        this.datasetFileReader = datasetFileReader;
        this.profiler = profiler;
        this.profilingService = profilingService;
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
        this.columnProfileRepository = columnProfileRepository;
        this.jobRepository = jobRepository;
        this.dataQualityService = dataQualityService;
    }


    @Transactional
    public void process(Long jobId) {


        DatasetProcessingJob job = jobRepository.findById(jobId).orElseThrow(() ->
                new IllegalStateException("Processing job not found:" + jobId));
        DataSet dataSet = job.getDataSet();

        String datasetId = dataSet.getDatasetId();
        String filePath = dataSet.getFilePath();
        String fileType = dataSet.getFileType();

        log.info("Starting dataset processing. datasetId={}, fileType={},filePath={}",
                datasetId, fileType, filePath);

//if(true){
//        Only for the Testing perspective.
//    throw  new RuntimeException("Testing retry mechanism");
//
//}

        List<List<String>> rows = datasetFileReader.read(filePath);

        List<String> headers = rows.getFirst();

        IntStream.range(0, headers.size())
                .forEach(columnIndex -> {
                    String columnName = headers.get(columnIndex);

                    List<String> values = rows.stream()
                            .skip(1)
                            .filter(row -> columnIndex < row.size())
                            .map(row -> row.get(columnIndex))
                            .toList();
//                           log.info("Column Name :{} ,Values :{}",columnName,values);
                });



        DatasetProfile profilingService1 = profilingService.profile(rows);

        profilingService1.columns().forEach(column -> {
            ColumnQualityResult qualityResult = dataQualityService.assess(column);

            log.info(
                    "Column Quality : name={}, missingPercentage={}%,status={}",
                    qualityResult.columnName(),
                    qualityResult.missingPercentage(),
                    qualityResult.status()
            );
        });


        log.info(
                "Dataset profiling completed, datasetId={}, rows={}, columns={}, duplicates={}",
                dataSet.getDatasetId(),
                profilingService1.totalRows(),
                profilingService1.totalColumns(),
                profilingService1.duplicateRows()
        );

        DatasetProfileEntity profile = profileMapper.toEntity(profilingService1, dataSet);

        profileRepository.save(profile);

        List<DatasetColumnProfileEntity> columnProfiles =
                profilingService1.columns()
                        .stream()
                        .map(column ->
                                new DatasetColumnProfileEntity(profile, column)
                        )
                        .toList();

        columnProfileRepository.saveAll(columnProfiles);

        if (rows == null || rows.isEmpty()) {
            throw new IllegalStateException(
                    "Dataset contains no readable data. datasetId=" + datasetId
            );
        }
        profilingService1.columns().forEach(column -> log.info(

                "Column profile : name={}, type={}, totalValues={}, nullValues={}, emptyValues={}",
                column.columnName(),
                column.dataType(),
                column.totalValues(),
                column.nullValues(),
                column.emptyValues()
        ));
        log.info("Dataset file read successfully. datasetId={}, rowCount={}",
                datasetId, rows.size());
    }


}
