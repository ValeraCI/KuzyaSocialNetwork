package com.valeraci.kuzyasocialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void register() throws Exception {
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                ObjectCreator.createRegistrationDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void registerFail() throws Exception {
        UserCredential userCredentialAdmin =
                ObjectCreator.createUserCredential("admin@gmail.com",
                        "$2a$10$RkFea3usuUV27pYUPyCSQOAD.EdZNPN23qkXA3QrnBF6.I/7wCnQK", "Admin",
                        "Admin", FamilyStatusTitle.SINGLE, RoleTitle.ROLE_ADMINISTRATOR);

        entityManager.persist(userCredentialAdmin);
        entityManager.flush();

        RegistrationDto registrationDto =
                ObjectCreator.createRegistrationDto("admin@gmail.com", "admin1",
                        "Admin", "Admin", FamilyStatusTitle.SINGLE);


        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                registrationDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void registerError() throws Exception {
        RegistrationDto registrationDto =
                ObjectCreator.createRegistrationDto(null, "1234",
                        "lastname", "firstname", FamilyStatusTitle.SINGLE);


        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        UserCredential userCredentialAdmin =
                ObjectCreator.createUserCredential("admin@gmail.com",
                        "$2a$10$RkFea3usuUV27pYUPyCSQOAD.EdZNPN23qkXA3QrnBF6.I/7wCnQK", "Admin",
                        "Admin", FamilyStatusTitle.SINGLE, RoleTitle.ROLE_ADMINISTRATOR);

        entityManager.persist(userCredentialAdmin);
        entityManager.flush();


        LoginDto loginDto = ObjectCreator.createLoginDto("admin@gmail.com", "admin1");
        mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void loginFail() throws Exception {
        LoginDto loginDto = ObjectCreator.createLoginDto("BadEmail@gmail.com", "GoodPassword");
        mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void loginError() throws Exception {
        LoginDto loginDto = ObjectCreator.createLoginDto("notEmail", null);
        mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
