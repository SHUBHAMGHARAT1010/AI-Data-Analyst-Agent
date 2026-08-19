package com.aidataagent.ai_data_analyst.dataset.service;

import com.aidataagent.ai_data_analyst.analysis.entity.AnalysisRequest;
import com.aidataagent.ai_data_analyst.analysis.model.AnalysisStatus;
import com.aidataagent.ai_data_analyst.analysis.repository.AnalysisRequestRepository;
import com.aidataagent.ai_data_analyst.dataset.dto.DatasetUploadResponse;
import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.model.DatasetStatus;
import com.aidataagent.ai_data_analyst.dataset.repository.DataSetRepository;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProcessingJob;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ProcessingStatus;
import com.aidataagent.ai_data_analyst.dataset.processing.repository.DatasetProcessingJobRepository;
import com.aidataagent.ai_data_analyst.shared.util.BusinessIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Set;

@Service
public class DataSetUploadService {

    private final DatasetProcessingJobRepository datasetProcessingJobRepository;
    private final DataSetRepository dataSetRepository;
    private final LocalFileStorageService localFileStorageService;
    private final AnalysisRequestRepository analysisRequestRepository;
    private final BusinessIdGenerator businessIdGenerator;
    private static final Set<String> SUPPORTED_TYPES =
            Set.of("xlsx","xls","csv");

    public DataSetUploadService(
            DataSetRepository dataSetRepository,
            AnalysisRequestRepository analysisRequestRepository,
            LocalFileStorageService localFileStorageService,
            BusinessIdGenerator businessIdGenerator,
            DatasetProcessingJobRepository datasetProcessingJobRepository
    ) {
        this.dataSetRepository = dataSetRepository;
        this.analysisRequestRepository = analysisRequestRepository;
        this.localFileStorageService = localFileStorageService;
        this.businessIdGenerator = businessIdGenerator;
        this.datasetProcessingJobRepository=datasetProcessingJobRepository;
    }

    @Transactional
    public DatasetUploadResponse uploadDataset(
            MultipartFile multipartFile,
            String domain,
            String userInstruction,
            String analysisType
    ) {

        validateFile(multipartFile);

        String datasetId = businessIdGenerator.generateDatasetId();
        String analysisRequestId = businessIdGenerator.generateAnalysisRequestId();
        String storedPath = localFileStorageService.store(multipartFile, datasetId);

        DataSet dataSet = new DataSet();

        dataSet.setDatasetId(datasetId);
        dataSet.setOriginalFileName(multipartFile.getOriginalFilename());
        dataSet.setFilePath(storedPath);
        dataSet.setFileType(getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        dataSet.setDomain(domain);
        dataSet.setStatus(DatasetStatus.UPLOADED);

        DataSet savedDataSet = dataSetRepository.save(dataSet);

        AnalysisRequest analysisRequest = new AnalysisRequest();

        analysisRequest.setAnalysisRequestId(analysisRequestId);
        analysisRequest.setDataset(savedDataSet);
        analysisRequest.setUserInstruction(userInstruction);
        analysisRequest.setAnalysisType(analysisType);
        analysisRequest.setStatus(AnalysisStatus.CREATED);

        AnalysisRequest savedAnalysisRequest = analysisRequestRepository.save(analysisRequest);

        String processingJobId= businessIdGenerator.generateProcessingJobId();

        DatasetProcessingJob processingJob = new DatasetProcessingJob();

            processingJob.setJobId(processingJobId);
            processingJob.setDataSet(savedDataSet);
            processingJob.setStatus(ProcessingStatus.PENDING);

            datasetProcessingJobRepository.save(processingJob);

        return DatasetUploadResponse.builder()
                .datasetId(savedDataSet.getDatasetId())
                .datasetStatus(savedDataSet.getStatus())
                .analysisRequestId(savedAnalysisRequest.getAnalysisRequestId())
                .analysisStatus(savedAnalysisRequest.getStatus())
                .message("Dataset uploaded Successfully")
                .build();


    }


    private void validateFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Please upload non-empty file");
        }

        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file name is invalid");
        }

        String extension = StringUtils.getFilenameExtension(fileName);

        if (!SUPPORTED_TYPES.contains(extension)) {
            throw new IllegalArgumentException(
                    "Only XLSX,XLS and CSV files are supported"
            );
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");

        if (lastIndex < 0 || lastIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("Uploaded file has no extension");
        }

        return fileName.substring(lastIndex + 1).toLowerCase();
    }
}
