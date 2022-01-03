package yangbrothers.movierank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import yangbrothers.movierank.entity.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MovieApiService {

    private String key;
    private String curPage;
    private String itemPerPage;

    public MovieApiService(String key, String curPage, String itemPerPage) {
        this.key = key;
        this.curPage = curPage;
        this.itemPerPage = itemPerPage;
    }

    public List<Movie> movieList() {
        ArrayList<Movie> result = new ArrayList<>();

        try {
            KobisOpenAPIRestService kobisOpenAPIRestService = new KobisOpenAPIRestService(key);

            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(kobisOpenAPIRestService.getMovieList(true, curPage, itemPerPage,
                    null, null, null, null, null, null, null, null));

            JSONObject movieListResult = (JSONObject) obj.get("movieListResult");
            JSONArray movieList = (JSONArray) movieListResult.get("movieList");

            ObjectMapper objectMapper = new ObjectMapper();

            for (Object o : movieList) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                JSONObject object = (JSONObject) o;
                object.remove("companys");
                JSONArray directors = (JSONArray) object.get("directors");
                for (Object director : directors) {
                    JSONObject JSON_director = (JSONObject) director;
                    stringJoiner.add(JSON_director.get("peopleNm").toString());
                }

                object.remove("directors");
                Movie Movie = objectMapper.readValue(object.toString(), Movie.class);
                Movie.setDirectors(stringJoiner.toString());
                result.add(Movie);
            }
        } catch (Exception e) {
            e.getMessage();
        }



        return result;
    }
}
