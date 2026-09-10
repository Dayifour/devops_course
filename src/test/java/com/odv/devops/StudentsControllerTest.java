package com.odv.devops;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.odv.devops.entitie.Students;
import com.odv.devops.repositorie.StudentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentsRepository studentsRepository;

    @BeforeEach
    void clearStudents() {
        studentsRepository.deleteAll();
    }

    @Test
    void createsStudentAndReturnsPersistedAttributes() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nom": "Bruno",
                                  "prenom": "Test",
                                  "dateN": "2000-05-15"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nom").value("Bruno"))
                .andExpect(jsonPath("$.prenom").value("Test"))
                .andExpect(jsonPath("$.dateN").value("2000-05-15"))
                .andExpect(jsonPath("$.createDate", notNullValue()))
                .andExpect(jsonPath("$.updateDate", notNullValue()));
    }

    @Test
    void listsCreatedStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void servesStudentsPageWithAddForm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("index.html"));
    }

    @Test
    void exposesOpenApiSpecification() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", notNullValue()))
                .andExpect(jsonPath("$.paths./api/students", notNullValue()));
    }

    @Test
    void updatesAnExistingStudent() throws Exception {
        Students student = createStudent();

        mockMvc.perform(put("/api/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nom": "Martin",
                                  "prenom": "Paul",
                                  "dateN": "1998-10-20"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.nom").value("Martin"))
                .andExpect(jsonPath("$.prenom").value("Paul"))
                .andExpect(jsonPath("$.dateN").value("1998-10-20"));
    }

    @Test
    void deletesAnExistingStudent() throws Exception {
        Students student = createStudent();

        mockMvc.perform(delete("/api/students/{id}", student.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private Students createStudent() {
        Students student = new Students();
        student.setNom("Dupont");
        student.setPrenom("Marie");
        return studentsRepository.save(student);
    }
}
