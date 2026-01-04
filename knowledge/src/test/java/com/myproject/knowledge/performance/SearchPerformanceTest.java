package com.myproject.knowledge.performance;


// import com.myproject.knowledge.model.DocumentResponse;
// import com.myproject.knowledge.repository.DocumentRepository;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.junit.jupiter.api.Test;

// import java.util.List;
// import java.util.concurrent.TimeUnit;

// import org.junit.jupiter.api.Disabled;


//import static org.junit.jupiter.api.Assertions.assertTrue;

// @SpringBootTest
// @AutoConfigureMockMvc
// class SearchPerformanceTest {

//     private static final int TOTAL_DOCS = 10000;
//     private static final String TENANT_ID = "tenant-benchmark";
//     private static final Logger LOGGER = LoggerFactory.getLogger(SearchPerformanceTest.class);

//     @Autowired
//     private DocumentRepository repository;

//     @Test
//     @Disabled
//     void searchShouldCompleteUnder100msFor10kDocs() {

//         int runs = 20;
//         int numberOfDocumentsPerRun = 50;
//         int numberOfRuns = TOTAL_DOCS / numberOfDocumentsPerRun;
//         long totalTime = 0;
//         int offset = 0;

//         for (int i = 0; i < numberOfRuns; i++) {
//             long start = System.nanoTime();
       
//             List<DocumentResponse> results = repository.searchByTenant("platform", TENANT_ID, 50, 0);
//             offset += results.size();
//             long end = System.nanoTime() - start;
//             totalTime += end;
//             LOGGER.info("Iteration: " + i + " search size: " + results.size() + " limit: 50" + " offset: " + offset + " time: " + end);
//         }

//         LOGGER.info("Total time to proceess 10k documents " + totalTime + " ms");
//         long avgMs = TimeUnit.NANOSECONDS.toMillis(totalTime / runs);
//         LOGGER.info("Average search latency = " + avgMs + " ms");
//     }
// }
