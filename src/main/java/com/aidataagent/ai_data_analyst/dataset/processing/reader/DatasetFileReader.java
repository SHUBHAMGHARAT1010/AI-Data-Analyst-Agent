package com.aidataagent.ai_data_analyst.dataset.processing.reader;

import java.nio.file.Path;
import java.util.List;

public interface DatasetFileReader {

    List<List<String>> read(String filePath);
}
