package com.valeraci.kuzyasocialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;
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
        RegistrationDto registrationDto =
                ObjectCreator.createRegistrationDto("testEmail@gmail.com", "123456",
                        "lastname", "firstname", FamilyStatusTitle.SINGLE);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                registrationDto)))
                .andExpect(status().isOk())
                .andDo(print());


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
        RegistrationDto registrationDto =
                ObjectCreator.createRegistrationDto("test@gmail.com", "123456",
                        "lastname", "firstname", FamilyStatusTitle.SINGLE);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                registrationDto)))
                .andExpect(status().isOk())
                .andDo(print());


        LoginDto loginDto = ObjectCreator.createLoginDto("test@gmail.com", "123456");
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
