package ec.edu.lexus.twitterapp.api;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Acceso a el API de Twitter, para mas entendimiento a través de la interfaz voy a exponer cuál es la forma
 * de acceder a este API (TimeLIneService), y a través del Cliente voy a poder obtener esta Interfaz (CustomTwitterApiClient)
 * Created by Alexis on 26/09/2017.
 */

public class CustomTwitterApiClient extends TwitterApiClient {

    public CustomTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public TimeLineService getTimeLineService(){
        return getService(TimeLineService.class);
    }
}
