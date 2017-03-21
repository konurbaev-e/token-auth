package org.konurbaev.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.konurbaev.auth.security.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IntegrationSecurityTest {

    private static final Logger logger = LogManager.getLogger(IntegrationSecurityTest.class);
    
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private final MediaType plainContentType = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("utf8"));

    @Before
    public void localSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void testSecurityWithAuthAndHello() throws Exception {
        // declare user auth request
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("user");
        authenticationRequest.setPassword("password");
        // convert user auth request to json
        String strAuthenticationRequest = asJsonString(authenticationRequest);
        logger.info(strAuthenticationRequest);
        // perform authentication
        logger.info("Calling /api/login");
        MvcResult response = mockMvc.perform(post("/api/login").content(strAuthenticationRequest).contentType(jsonContentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContentType))
                .andExpect(jsonPath("token", isA(String.class)))
                .andReturn()
        ;
        // get token from response to use it in hello call
        String strResponse = response.getResponse().getContentAsString();
        logger.info(strResponse);
        String token = strResponse.substring(10, strResponse.length() - 2);
        logger.info(token);
        // call hello endpoint
        logger.info("Calling /api/hello");
        response = mockMvc.perform(get("/api/hello").with(user("user")).header("token", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(plainContentType))
                .andExpect(content().string("Hello, user"))
                .andReturn()
                ;
        // print hello call response
        strResponse = response.getResponse().getContentAsString();
        logger.info(strResponse);
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}
