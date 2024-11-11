package com.java.studenthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.java.studenthandler.controller.StudentController;
import com.java.studenthandler.entity.Student;
import com.java.studenthandler.exception.IDNotFoundException;
import com.java.studenthandler.exception.MandatoryFieldMissingException;
import com.java.studenthandler.exception.StudentListEmptyException;
import com.java.studenthandler.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {

    Student STUDENT_1 = new Student(1, "DummyName 1", 20, "branchDummy 1");
    Student STUDENT_2 = new Student(2, "DummyName 2", 40, "branchDummy 2");
    Student STUDENT_3 = new Student(3, "DummyName 3", 45, "branchDummy 3");
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentController studentController;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void getAllStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(STUDENT_1, STUDENT_2, STUDENT_3));
        Mockito.when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("DummyName 1")))
                .andExpect(jsonPath("$[2].name", is("DummyName 3")));
    }

    @Test
    public void getAllStudents_failure()  {
        Mockito.when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        var exp = Assertions.assertThrows(StudentListEmptyException.class, () -> {
            studentController.getAllStudents();
        });

        Assertions.assertEquals("No student present in the system", exp.getErrMessage());
    }

    @Test
    public void getStudentByID_success() throws Exception {
        Mockito.when(studentRepository.findById(STUDENT_1.getRollNo())).thenReturn(java.util.Optional.of(STUDENT_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("DummyName 1")));
    }

    @Test
    public void getStudentByID_failure() throws Exception {
        Mockito.when(studentRepository.findById(1)).thenReturn(java.util.Optional.empty());
        var exp = Assertions.assertThrows(IDNotFoundException.class, () -> {
            studentController.getStudentByID(1);
        });
        Assertions.assertEquals("Student ID is not present in the system", exp.getErrMessage());
    }

    @Test
    public void createStudent_success() throws Exception {
        //Given
        Student STUDENT_4 = new Student(4, "DummyName 4", 25, "branchDummy 4");
        //Mock the calls
        Mockito.when(studentRepository.save(STUDENT_4)).thenReturn(STUDENT_2);

        mockMvc.perform(post("/students/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(STUDENT_4)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // Print the response body for debugging
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("DummyName 4")));

    }

    @Test
    public void createStudent_failure() throws Exception {
        Student newStudent = new Student(5, "", 20, "branchDummy 5");
        var exp = Assertions.assertThrows(MandatoryFieldMissingException.class, () -> {
            studentController.createStudent(newStudent);
        });

        Assertions.assertEquals("Name field is mandatory", exp.getErrMessage());


    }

    @Test
    public void updateStudent_success() throws Exception {
        Student updatedStudent = new Student(1, "DummyName Updated 1", 20, "branchDummy 1");

        Mockito.when(studentRepository.findById(updatedStudent.getRollNo())).thenReturn(java.util.Optional.of(updatedStudent));
        Mockito.when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);

        MockHttpServletRequestBuilder mockRequest = put("/students/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedStudent));
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("DummyName Updated 1")));

    }

    @Test
    public void updateStudent_failure() throws Exception {
        Mockito.when(studentRepository.findById(1)).thenReturn(java.util.Optional.empty());

        var exp = Assertions.assertThrows(IDNotFoundException.class, () -> {
            studentController.updateStudent(1);
        });

        Assertions.assertEquals("Student ID is not present in the system", exp.getErrMessage());
    }

    @Test
    public void deleteStudent_success() throws Exception {
        Mockito.when(studentRepository.findById(STUDENT_2.getRollNo())).thenReturn(Optional.of(STUDENT_2));
        int studentId = 2;
        mockMvc.perform(delete("/students/delete/{id}", studentId))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Deletion successfull"));

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(STUDENT_2);

    }

    @Test
    public void deleteStudent_failure() throws Exception {
        Mockito.when(studentRepository.findById(1)).thenReturn(java.util.Optional.empty());

        var exp = Assertions.assertThrows(IDNotFoundException.class, () -> {
            studentController.deleteStudent(1);
        });

        Assertions.assertEquals("Student ID is not present in the system", exp.getErrMessage());
    }

}
