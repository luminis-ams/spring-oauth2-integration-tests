package eu.luminis.amsterdam.rest;

import eu.luminis.amsterdam.OAuthHelper;
import eu.luminis.amsterdam.SpringOuath2IntegrationTestsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringOuath2IntegrationTestsApplication.class)
@WebAppConfiguration
@IntegrationTest
public class HelloControllerTest {

    @Autowired
    private WebApplicationContext webapp;

    @Autowired
    private OAuthHelper authHelper;

    private MockMvc restMvc;

    @Before
    public void setup() {
        restMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
    }

    @Test
    public void testHelloAnonymous() throws Exception {
        ResultActions resultActions = restMvc.perform(post("/hello")).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));

    }

    @Test
    public void testHelloAuthenticated() throws Exception {
        RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_USER");
        ResultActions resultActions = restMvc.perform(post("/hello").with(bearerToken)).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));

    }

    @Test
    public void testHelloAgainAnonymous() throws Exception {
        ResultActions resultActions = restMvc.perform(post("/hello-again")).andDo(print());

        resultActions
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testHelloAgainAuthenticated() throws Exception {
        RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_USER");
        ResultActions resultActions = restMvc.perform(post("/hello-again").with(bearerToken)).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("hello again"));
    }
}