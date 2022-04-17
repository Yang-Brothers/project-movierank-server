package yangbrothers.movierank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import yangbrothers.movierank.config.MovieApiConfig;
import yangbrothers.movierank.dto.request.DailyBoxOfficeSearchDTO;
import yangbrothers.movierank.dto.response.DailyBoxOfficeApiDTO;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {MovieApiConfig.class, MovieApiService.class, ObjectMapper.class})
class MovieApiServiceTest {

    @Autowired
    MovieApiService movieApiService;

    @Autowired
    KobisOpenAPIRestService kobisOpenAPIRestService;

//    @Test
//    @DisplayName("dailyTest")
//    public void dailyTest() throws Exception {
//        DailyBoxOfficeSearchDTO dailyBoxOfficeSearchDTO = new DailyBoxOfficeSearchDTO();
//        dailyBoxOfficeSearchDTO.setDate(LocalDate.of(2022, 01, 14));
//        ResponseEntity<DailyBoxOfficeApiDTO> response = movieApiService.searchDailyBoxOfficeList(dailyBoxOfficeSearchDTO);
//        ArrayList<DailyBoxOfficeApiDTO.DailyBoxOfficeDTO> dailyBoxOfficeList = (ArrayList<DailyBoxOfficeApiDTO.DailyBoxOfficeDTO>) response.getBody().getData().get("dailyBoxOfficeList");
//
//        assertThat(dailyBoxOfficeList.size()).isNotZero();
//    }
}