package com.aidataagent.ai_data_analyst.dataset.processing.worker;

import com.aidataagent.ai_data_analyst.dataset.entity.DataSet;
import com.aidataagent.ai_data_analyst.dataset.processing.entity.DatasetProcessingJob;
import com.aidataagent.ai_data_analyst.dataset.processing.model.ProcessingStatus;
import com.aidataagent.ai_data_analyst.dataset.processing.repository.DatasetProcessingJobRepository;
import com.aidataagent.ai_data_analyst.dataset.processing.service.DatasetProcessingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatasetProcessingWorker {

    private final Logger log= LoggerFactory.getLogger(DatasetProcessingWorker.class);
    private final DatasetProcessingJobRepository jobRepository;
    private final DatasetProcessingService processingService;

    public DatasetProcessingWorker(DatasetProcessingJobRepository jobRepository,DatasetProcessingService processingService) {

        this.jobRepository = jobRepository;
        this.processingService=processingService;
    }

    @Scheduled(fixedDelay = 5000)
    public void processPendingJobs() {

        List<DatasetProcessingJob> jobs = jobRepository.findByStatus(ProcessingStatus.PENDING);
        DataSet dataSet= new DataSet();

        if (jobs.isEmpty()) {
            return;
        }

        for (DatasetProcessingJob job : jobs) {

            try {
                dataSet= job.getDataSet();
                job.setStatus(ProcessingStatus.PROCESSING);
                job.setStartedAt(LocalDateTime.now());

                jobRepository.save(job);
                log.info("Started processing job. jobId={}, datasetId={}",
                        job.getJobId(), dataSet.getDatasetId());


                processingService.process(job.getId());
                job.setStatus((ProcessingStatus.COMPLETED));
                job.setCompletedAt(LocalDateTime.now());

                jobRepository.save(job);
                log.info("Completed processing job. jobId={}", job.getJobId());

            } catch (Exception e) {

                int currentRetryCount=job.getRetryCount()==null?0:job.getRetryCount();

                int maxRetryCount=dataSet.getMaxRetryCount()==null?3:dataSet.getMaxRetryCount();

                currentRetryCount++;

                job.setRetryCount(currentRetryCount);
                job.setErrorMessage(e.getMessage());
                job.setFailedAt(LocalDateTime.now());

                if(currentRetryCount<maxRetryCount){

                    //Retry is available

                    job.setStatus(ProcessingStatus.PENDING);

                    log.info(
                            "Dataset processing failed. Retrying job. jobId={}, retryCount={}, maxRetryCount={}",
                            job.getJobId(),
                            currentRetryCount,
                            maxRetryCount
                    );

                }else {
                    // Retry exhausted

                    job.setStatus(ProcessingStatus.FAILED);

                    log.info(
                            "Dataset Processing permanently Failed. jobId={}, retryCount={}, maxRetryCount={}",
                            job.getJobId(),currentRetryCount, maxRetryCount
                    );
                }
                jobRepository.save(job);

            }
        }

        log.info("-----------------------------");

    }


}
