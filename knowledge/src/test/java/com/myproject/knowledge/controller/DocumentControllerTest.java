package com.myproject.knowledge.controller;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.knowledge.context.SecurityContext;
import com.myproject.knowledge.model.DocumentRequest;
import com.myproject.knowledge.model.DocumentResponse;
import com.myproject.knowledge.service.DocumentService;
import com.myproject.knowledge.support.TestSecurityConfig;


@WebMvcTest(DocumentController.class)
@Import(TestSecurityConfig.class)
class DocumentControllerTest {

    private static final String TENANT_ID = "tenant-a";
    private static final String API_KEY = "tenant-a-key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DocumentService documentService;


   @BeforeEach
    public void beforeMethod() {
        SecurityContext.setTenantId(TENANT_ID);

    }

    // -----------------------------
    // POST /documents
    // -----------------------------
    @Test
    //@Disabled
    void shouldCreateDocumentSuccessfully() throws Exception {

        SecurityContext.setTenantId(TENANT_ID);
        DocumentRequest request = new DocumentRequest();
        request.setTitle("Platform Engineering");
        request.setContent("Scalable platform architecture");
        request.setTags(List.of("platform", "engineering"));

        when(documentService.save(eq("tenant-a"), any(DocumentRequest.class))).thenReturn(new Random().nextLong());

        // Act + Assert
        mockMvc.perform(post("/api/v1/tenants/tenant-a/documents")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentId").exists());
    }

    // -----------------------------
    // GET /documents/search
    // -----------------------------
    @Test
   // @Disabled
    void shouldSearchDocumentsForTenant() throws Exception {

        SecurityContext.setTenantId(TENANT_ID);
        List<DocumentResponse> responses = List.of(
                new DocumentResponse( new Random().nextLong(), "Platform Engineering", 100.0),
                new DocumentResponse( new Random().nextLong(), "Platform Scalability", 98.0));

        when(documentService.search(eq("tenant-a"), eq("platform"),eq(10), eq(0))).thenReturn(responses);

        mockMvc.perform(
                get("/api/v1/tenants/tenant-a/documents/search")
                    .header("X-API-Key", API_KEY)
                    .param("q", "platform")
                    .param("limit", "10")
                    .param("offset", "0")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Platform Engineering"))
            .andExpect(jsonPath("$[1].title").value("Platform Scalability"));
    }

    // -----------------------------
    // Tenant Isolation
    // -----------------------------
    @Test
    //@Disabled
void shouldNotReturnDocumentsFromAnotherTenant() throws Exception {
    // Arrange
    String API_KEY_A = "tenant-a-key";
    String API_KEY_B = "tenant-b-key";
    String tenantA = "tenant-a";
    String tenantB = "tenant-b";
    String query = "platform";

    // Document exists for tenant-a
    List<DocumentResponse> tenantADocs = List.of(
            new DocumentResponse( new Random().nextLong(), "Platform Scalability", 98.0));

    // Mock service behavior
    when(documentService.search(eq(tenantA), eq(query), eq(10), eq(0)))
            .thenReturn(tenantADocs);

    when(documentService.search(eq(tenantB), eq(query), eq(10), eq(0)))
            .thenReturn(List.of()); // tenant-b must see nothing

    // Act + Assert: tenant-a sees the document
    mockMvc.perform(get("/api/v1/tenants/{tenantId}/documents/search", tenantA)
                    .header("X-API-Key", API_KEY_A)
                    .param("q", query)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("Platform Scalability"));

    // Act + Assert: tenant-b sees NOTHING
    mockMvc.perform(get("/api/v1/tenants/{tenantId}/documents/search", tenantB)
                     .header("X-API-Key", API_KEY_B)
                    .param("q", query)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
}


    // -----------------------------
    // Security: Missing API Key
    // -----------------------------
    @Test
    //@Disabled
    void shouldRejectRequestWithoutApiKey() throws Exception {

        mockMvc.perform(
                get("/api/v1/tenants/{tenantId}/documents/search", TENANT_ID)
                    .param("q", "platform")
            )
            .andExpect(status().isUnauthorized());
    }

    // -----------------------------
    // Validation: Bad Request
    // -----------------------------
    @Test
    //@Disabled
    void shouldRejectInvalidRequestBody() throws Exception {

       //DocumentRequest invalidRequest = new DocumentRequest(); // missing fields

       String invalidRequest = """
            {
              "title": "",
              "content": "",
              "tags": ""
            }
            """;

        mockMvc.perform(
                post("/api/v1/tenants/{tenantId}/documents", TENANT_ID)
                    .header("X-API-Key", API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
            .andExpect(status().isBadRequest());
    }
}
