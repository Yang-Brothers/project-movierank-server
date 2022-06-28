package yangbrothers.movierank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookMarkControllerTest {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SpyBookMarkService spyBookMarkService = new SpyBookMarkService();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new BookMarkController(spyBookMarkService))
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    public void list_isOk() throws Exception {
        mockMvc.perform(get("/api/v1/bookmark/list").param("username", "test"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test name")
    public void list_NoQueryParam() throws Exception {
        mockMvc.perform(get("/api/v1/bookmark/list").param("username", ""))
                .andExpect(status().isBadRequest());
    }
}