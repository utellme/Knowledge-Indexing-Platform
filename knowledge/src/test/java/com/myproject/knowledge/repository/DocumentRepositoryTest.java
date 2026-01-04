package com.myproject.knowledge.repository;


import com.myproject.knowledge.entity.DocumentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import com.myproject.knowledge.model.DocumentResponse;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository repository;

    @Test
    void shouldSearchDocumentsForTenant() {
        // given
        save("tenant-a", "Platform Engineering", "Platform Scalability");
        save("tenant-a", "API Design", "RESTful APIs");
        save("tenant-b", "Platform Security", "JWT security");

        // when
        List<DocumentResponse> results =
                repository.searchByTenant("tenant-a", "platform", 10, 0);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).containsIgnoringCase("platform");

        // assert by count
        //tenant-a
        assertThat(repository.countByTenant("tenant-a")).isEqualTo(2);
       
        //tenant-b
        assertThat(repository.countByTenant("tenant-b")).isEqualTo(1);

    }

    @Test
    void shouldNotMixDocumentsAcrossTenants() {
        // given
        save("tenant-a", "Platform Engineering", "Internal platform docs");

        // when
        List<DocumentResponse> results =
                repository.searchByTenant("tenant-b", "platform", 10, 0);

        // then
        assertThat(results).isEmpty();
    }

    private void save(String tenantId, String title, String content) {
        DocumentEntity doc = new DocumentEntity();
        doc.setTenantId(tenantId);
        doc.setTitle(title);
        doc.setContent(content);
        doc.setCreatedAt(Instant.now());

        repository.save(doc);
    }
}




// import com.myproject.knowledge.entity.DocumentEntity;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
// //import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.context.junit4.SpringRunner;
// //import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// //import static org.hamcrest.MatcherAssert.assertThat;
// //import static org.hamcrest.Matchers.*;
// //import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

// import jakarta.persistence.EntityManager;

// import java.util.List;
// import java.util.UUID;


// //@ExtendWith(SpringExtension.class)
// @DataJpaTest
// //@SpringBootTest
// //@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// // @TestPropertySource(properties = {
// //     "spring.datasource.driver-class-name=org.h2.Driver",
// //     "spring.datasource.url=jdbc:h2:mem:my_test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
// //     "spring.datasource.username=sa",
// //     "spring.jpa.hibernate.ddl-auto=create-drop",
// //     "spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop",
// //            "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
// //     })
// class DocumentRepositoryTest {

//     // @InjectMocksc
//     // private DocumentRepository repository;

//     // @Mock
//     // EntityManager entityManager;

//     @Autowired
//     private TestEntityManager entityManager;

//     @Autowired
//     private DocumentRepository_1 repository;



//     @Test
//     void shouldSaveAndFindByTenant() {
//         DocumentEntity entity = new DocumentEntity();
    
//         entity.setId(UUID.randomUUID().toString());
//         entity.setTenantId("tenant-a");
//         entity.setTitle("Platform Engineering");
//         entity.setContent("Scalable platform architecture");
//         entity.setTags("platform engineering");

//         repository.save(entity);

//         //tenant-a
//         assertThat(repository.countByTenant("tenant-a")).isEqualTo(1);

//         //tenant-b
//         assertThat(repository.countByTenant("tenant-b")).isZero();
//     }
// }

