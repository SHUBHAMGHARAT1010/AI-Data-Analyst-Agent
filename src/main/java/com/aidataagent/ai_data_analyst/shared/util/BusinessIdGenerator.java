package com.aidataagent.ai_data_analyst.shared.util;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class BusinessIdGenerator {

    private static final DateTimeFormatter DATE_FORMATTER= DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateDatasetId(){
        return "DS-"+ LocalDate.now().format(DATE_FORMATTER)
                +"_"
                + generateRandomPart();
    }

    public String generateAnalysisRequestId(){
        return "AR_"
                +LocalDate.now().format(DATE_FORMATTER)
                +"_"
                +generateRandomPart();
    }

    public String generateProcessingJobId(){
        return "JOB-"+generateRandomPart();
    }
    private String generateRandomPart(){
        return UUID.randomUUID()
                .toString()
                .replace("_","")
                .substring(0,8)
                .toUpperCase();
    }


}
