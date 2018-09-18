package cn.corner.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mvc;

    @Before
    public void setMvc(){
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        this.mvc = mockMvc;
    }

    @Test
    public void whenQuerySucess() throws Exception {
        String result = mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("username","testname"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andReturn().getResponse().getContentAsString();
        System.out.print(result);
    }

    @Test
    public void whenGenInfoSucess() throws Exception {
        String result = mvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        System.out.print(result);
    }

    @Test
    public void whenGenInfoSucess2() throws Exception {
        mvc.perform(get("/user/a")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreatSucess() throws Exception {
        Date date = new Date();
        String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":" + date.getTime() + "}";
        String result = mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void whenValidate() throws Exception {
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String content = "{\"id\":\"1\",\"username\":\"tom\",\"password\":null,\"birthday\":"+date.getTime()+"}";
        String result = mvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void whenDeleteSuccess() throws Exception {
        mvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
