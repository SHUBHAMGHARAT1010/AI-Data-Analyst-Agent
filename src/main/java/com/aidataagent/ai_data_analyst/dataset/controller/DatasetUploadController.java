package com.aidataagent.ai_data_analyst.dataset.controller;

import com.aidataagent.ai_data_analyst.dataset.dto.DatasetUploadResponse;
import com.aidataagent.ai_data_analyst.dataset.service.DataSetUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DatasetUploadController {

    private final DataSetUploadService dataSetUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DatasetUploadResponse> uploadDataset(
            @RequestParam("file")MultipartFile file,
            @RequestParam("domain") String domain,
            @RequestParam(value = "analysisType",required = false)
            String analysisType,
            @RequestParam(value = "userInstruction", required = false)
            String userInstruction
            ){
                DatasetUploadResponse datasetUploadResponse= dataSetUploadService.uploadDataset(
                        file,
                        domain,
                        userInstruction,
                        analysisType
                );

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(datasetUploadResponse);
    }


}

