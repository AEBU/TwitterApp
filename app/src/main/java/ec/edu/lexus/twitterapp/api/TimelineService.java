package ec.edu.lexus.twitterapp.api;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * EndPoint Que necesitamos, desarrollado con Retrofit
 * Created by Alexis on 26/09/2017.
 */

public interface TimeLineService {

    /**
     * Peticion hacia el api de Twitter para usarlo dentro de nuestros tweets con los parametros
     * @param count numero de tweets a recibir
     * @param trim_user establecido den True, devuelve un ID numérico de los autores de estado
     * @param exclude_replies evita que las respuestas aparezcan, excluyendo replies(replicas)
     * @param contributor_details detalles del que manda el tweet
     * @param include_entities este incluye las entidades para imágenes y hashtags
     * @param callback listado de tweets que me provee retrofit
    */

    @GET("/1.1/statuses/home_timeline.json")
    void homeTimeline(@Query("count") Integer count,
                      @Query("trim_user") Boolean trim_user,
                      @Query("exclude_replies") Boolean exclude_replies,
                      @Query("contributor_details") Boolean contributor_details,
                      @Query("include_entities") Boolean include_entities,
                      Callback<List<Tweet>> callback);
}
