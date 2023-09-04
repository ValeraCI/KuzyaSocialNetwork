package com.valeraci.kuzyasocialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeraci.kuzyasocialnetwork.dto.locks.LockDto;
import com.valeraci.kuzyasocialnetwork.dto.users.LoginDto;
import com.valeraci.kuzyasocialnetwork.models.Lock;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import com.valeraci.kuzyasocialnetwork.utils.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestEntityManager entityManager;
    private String token;

    private final UserCredential userCredentialAdmin =
            ObjectCreator.createUserCredential("admin@gmail.com",
                    "$2a$10$RkFea3usuUV27pYUPyCSQOAD.EdZNPN23qkXA3QrnBF6.I/7wCnQK", "Admin",
                    "Admin", FamilyStatusTitle.SINGLE, RoleTitle.ROLE_ADMINISTRATOR);

    private final UserCredential userCredentialUser =
            ObjectCreator.createUserCredential("user@gmail.com",
                    "$2a$10$1doJAftGDeLsphyULJGkRuUx8zyevBerfv5.eDC5qBFN0kuE.7D5a", "User",
                    "User", FamilyStatusTitle.SINGLE, RoleTitle.ROLE_USER);

    @BeforeEach
    public void setup() throws Exception {
        entityManager.persistAndFlush(userCredentialAdmin);
        entityManager.persistAndFlush(userCredentialUser);

        entityManager.clear();

        String json = objectMapper.writeValueAsString(
                new LoginDto("admin@gmail.com", "admin1"));

        MvcResult result = mockMvc.perform(get("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        token = "Bearer " + result.getResponse()
                .getContentAsString()
                .split(":")[2]
                .replace('\"', ' ')
                .replace('}', ' ')
                .trim();
    }

    @Test
    public void lockUserNotFoundExceptionTest() throws Exception {
        String json = objectMapper.writeValueAsString(
                new LockDto(1, "testReason"));

        MvcResult result = mockMvc.perform(patch("/users/lock/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
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

        MvcResult result = mockMvc.perform(patch("/users/lock/" + userCredentialUser.getUser().getId()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
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
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void unlockUserNotFoundExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/users/lock/-1")
                        .header("Authorization", token))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void unlockUserNotBlockedExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(delete("/users/lock/" + userCredentialAdmin.getUser().getId())
                        .header("Authorization", token))
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    public void unlockTest() throws Exception {
        Lock lock = ObjectCreator.createLock();

        lock.setUserCredential(userCredentialUser);

        entityManager.persistAndFlush(lock);

        MvcResult result = mockMvc.perform(delete("/users/lock/" + userCredentialUser.getUser().getId())
                        .header("Authorization", token))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }
}
