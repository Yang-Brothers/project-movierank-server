package yangbrothers.movierank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import yangbrothers.movierank.dto.request.DailyBoxOfficeSearchDTO;
import yangbrothers.movierank.jwt.JwtAccessDeniedHandler;
import yangbrothers.movierank.jwt.JwtAuthenticationEntryPoint;
import yangbrothers.movierank.jwt.TokenProvider;
import yangbrothers.movierank.service.MovieApiService;
import yangbrothers.movierank.service.MovieService;

import java.time.LocalDateTime;

import static org.springframework.data.redis.connection.ReactiveStreamCommands.AddStreamRecord.body;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@Import({ObjectMapper.class, JwtAuthenticationEntryPoint.class, HttpEncodingAutoConfiguration.class})
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    private MovieApiService movieApiService;

    @Test
    @DisplayName("movieFailTest")
    public void movieFailTest() throws Exception {
        mockMvc.perform(get("/api/movie/searchMovieList").queryParam("len", "200"))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("DailyBoxOfficeTest")
    public void dailyBoxOfficeTest() throws Exception {
        mockMvc.perform(get("/api/movie/searchDailyBoxOfficeList").queryParam("len", "4").queryParam("date", "2022-01-15"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("DailyBocOfficeFailTest")
    public void dailyBocOfficeFailTest() throws Exception {
        mockMvc.perform(get("/api/movie/searchDailyBoxOfficeList").queryParam("len", "4").queryParam("date", "20220115"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationMsg.date").value("형식을 지켜주세요."))
                .andDo(print());
    }

}