package yangbrothers.movierank.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import yangbrothers.movierank.dto.request.DailyBoxOfficeSearchDTO;
import yangbrothers.movierank.dto.response.DailyBoxOfficeApiDTO;
import yangbrothers.movierank.entity.Movie;
import yangbrothers.movierank.util.ApiUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Service
public class MovieApiService {

    private final ObjectMapper objectMapper;
    private final KobisOpenAPIRestService kobisOpenAPIRestService;
    private final JSONParser jsonParser = new JSONParser();
    @Value("${kmdb.key}")
    private String key;

    public MovieApiService(ObjectMapper objectMapper, KobisOpenAPIRestService kobisOpenAPIRestService) {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
        this.kobisOpenAPIRestService = kobisOpenAPIRestService;
    }

    public List<Movie> searchMovieList(Integer startCount) {
        URI uri = UriComponentsBuilder.fromUriString("http://api.koreafilm.or.kr")
                .path("/openapi-data2/wisenut/search_api/search_json2.jsp")
                .queryParam("collection", "kmdb_new2")
                .queryParam("ServiceKey", key)
                .queryParam("listCount", 500)
                .queryParam("detail", "Y")
                .queryParam("startCount", startCount)
                .encode()
                .build()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        String json = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody().toString();

        ArrayList<Movie> result = new ArrayList<>();

        try {
            JSONObject obj = (JSONObject) jsonParser.parse(json);
            JSONObject target = (JSONObject) ((JSONArray) obj.get("Data")).get(0);
            JSONArray movieList = (JSONArray) target.get("Result");

            for (Object movie : movieList) {
                JSONObject jsonObject = (JSONObject) movie;

                JSONObject directors = (JSONObject) jsonObject.get("directors");
                JSONArray director = (JSONArray) directors.get("director");
                StringJoiner directorJoiner = new StringJoiner(", ");
                for (Object o : director) {
                    JSONObject object = (JSONObject) o;
                    String directorNm = object.get("directorNm").toString();
                    directorJoiner.add(directorNm);
                }

                JSONObject actors = (JSONObject) jsonObject.get("actors");
                JSONArray actor = (JSONArray) actors.get("actor");
                StringJoiner actorJoiner = new StringJoiner(", ");
                for (Object o : actor) {
                    JSONObject object = (JSONObject) o;
                    String actorNm = object.get("actorNm").toString();
                    actorJoiner.add(actorNm);
                }

                JSONObject plots = (JSONObject) jsonObject.get("plots");
                JSONArray plot = (JSONArray) plots.get("plot");
                String plotJoiner = null;
                for (Object o : plot) {
                    JSONObject object = (JSONObject) o;
                    if (object.get("plotLang").toString().equals("한국어")) {
                        plotJoiner = object.get("plotText").toString();
                        break;
                    }
                }

                jsonObject.remove("directors");
                jsonObject.remove("actors");
                jsonObject.remove("plots");

                Movie movieEntity = objectMapper.readValue(jsonObject.toString(), Movie.class);
                movieEntity.createMovie(directorJoiner, actorJoiner, plotJoiner);
                result.add(movieEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ResponseEntity<DailyBoxOfficeApiDTO> searchDailyBoxOfficeList(DailyBoxOfficeSearchDTO dailyBoxOfficeSearchDTO) {
        LocalDate localDateTime = dailyBoxOfficeSearchDTO.getDate();
        String date = String.format("%04d%02d%02d", localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(kobisOpenAPIRestService.getDailyBoxOffice(true, date, String.valueOf(dailyBoxOfficeSearchDTO.getLen()),
                    null, null, null));

            JSONObject boxOfficeResult = (JSONObject) obj.get("boxOfficeResult");
            JSONArray jsonArray = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");
            ArrayList<DailyBoxOfficeApiDTO.DailyBoxOfficeDTO> dailyBoxOfficeList = new ArrayList<>();

            for (Object movie : jsonArray) {
                JSONObject jsonObject = (JSONObject) movie;
                DailyBoxOfficeApiDTO.DailyBoxOfficeDTO dailyBoxOfficeDTO = objectMapper.readValue(jsonObject.toString(), DailyBoxOfficeApiDTO.DailyBoxOfficeDTO.class);
                dailyBoxOfficeList.add(dailyBoxOfficeDTO);
            }

            DailyBoxOfficeApiDTO dailyBoxOfficeApiDTO = new DailyBoxOfficeApiDTO();
            dailyBoxOfficeApiDTO.getData().put("dailyBoxOfficeList", dailyBoxOfficeList);
            ApiUtil.makeSuccessResult(dailyBoxOfficeApiDTO, ApiUtil.SUCCESS_OK);

            return new ResponseEntity<>(dailyBoxOfficeApiDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}