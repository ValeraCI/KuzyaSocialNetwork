package com.valeraci.kuzyasocialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void register() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        ObjectCreator.createRegistrationDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void registerFail() throws Exception {
        RegistrationDto registrationDto =
                ObjectCreator.createRegistrationDto("test@gmail.com", "1234",
                        "lastname", "firstname", FamilyStatusTitle.SINGLE);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                registrationDto)))
                .andExpect(status().isOk())
                .andDo(print());


        mockMvc.perform(post("/users")
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
                        "lastname", "firstname" , FamilyStatusTitle.SINGLE);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
}
