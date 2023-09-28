package com.valeraci.kuzyasocialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestEntityManager entityManager;
    private String token;

    private UserCredential userCredentialAdmin;

    private UserCredential userCredentialUser;


    @BeforeAll
    public void setup() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LoginDto("admin@gmail.com", "admin1"));

        MvcResult result = mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andReturn();

        token = "Bearer " + result.getResponse()
                .getContentAsString()
                .split(":")[2]
                .replace('\"', ' ')
                .replace('}', ' ')
                .trim();
    }

    @BeforeEach
    public void readUsers(){
        userCredentialAdmin = entityManager.find(UserCredential.class, 1L);
        userCredentialUser = entityManager.find(UserCredential.class, 2L);
    }

    @Test
    public void lockUserNotFoundExceptionTest() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void lockAccessDeniedExceptionTest() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/" + userCredentialAdmin.getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(403, result.getResponse().getStatus());
    }

    @Test
    public void lockUserAlreadyLockedExceptionTest() throws Exception {
        Lock lock = ObjectCreator.createLock();
        lock.setUserCredential(userCredentialUser);

        entityManager.persistAndFlush(lock);

        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/" + userCredentialUser.getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    public void lockTest() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/" + userCredentialUser.getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void lockNoTokenTest() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/" + userCredentialUser.getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andReturn();

        assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    public void unlockUserNotFoundExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/users/lock/-1")
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void unlockUserNotBlockedExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/users/lock/" + userCredentialAdmin.getUser().getId())
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    public void unlockTest() throws Exception {
        Lock lock = ObjectCreator.createLock();

        lock.setUserCredential(userCredentialUser);
        entityManager.persistAndFlush(userCredentialUser);
        entityManager.persistAndFlush(lock);

        MvcResult result = mockMvc.perform(delete("/users/lock/" + userCredentialUser.getUser().getId())
                        .header("Authorization", token))
                .andDo(print())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void unlockNoTokenTest() throws Exception {
        Lock lock = ObjectCreator.createLock();

        lock.setUserCredential(userCredentialUser);

        entityManager.persistAndFlush(lock);

        MvcResult result = mockMvc.perform(delete("/users/lock/" + userCredentialUser.getUser().getId()))
                .andDo(print())
                .andReturn();

        assertEquals(401, result.getResponse().getStatus());
    }
}
