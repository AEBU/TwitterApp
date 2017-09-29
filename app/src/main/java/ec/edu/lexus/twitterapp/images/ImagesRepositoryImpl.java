package ec.edu.lexus.twitterapp.images;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ec.edu.lexus.twitterapp.api.CustomTwitterApiClient;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.images.events.ImagesEvent;
import ec.edu.lexus.twitterapp.lib.base.EventBus;

import static android.R.attr.fillAfter;
import static android.R.attr.version;

/**
 * Created by Alexis on 28/09/2017.
 */

public class ImagesRepositoryImpl implements ImagesRepository {
    private EventBus eventBus;
    private CustomTwitterApiClient client;
    private final static int TWEET_COUNT = 100;


    public ImagesRepositoryImpl(EventBus eventBus, CustomTwitterApiClient client) {
        this.eventBus = eventBus;
        this.client = client;
    }


    @Override
    public void getImages() {
        Callback<List<Tweet>> callback=new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                List<Image> items=new ArrayList<>();
                for (Tweet tweet:result.data){
                    if (containsImages(tweet)){
                        Image tweetModel = new Image();
                        tweetModel.setId(tweet.idStr);
                        tweetModel.setFavoriteCount(tweet.favoriteCount);

                        //esta parte lo podemos omitar, pero va si queremos quitar los enlaces dentro del texto
                        String tweetText=tweet.text;//asignamos el texto
                        int index=tweetText.indexOf("http");//veo si hay un valor de retorno para mi índice
                        if (index>0){//si acaso hay el índice entonces quito todo desde la priemra posición hasta que aparezca ese índice
                            tweetText=tweetText.substring(0,index);
                        }
                        tweetModel.setTweetText(tweetText);

                        //este es para la foto, la piermoa únicamente(0), porque ya esta validado que tweetentites no este vacía
                        MediaEntity currentPhoto = tweet.entities.media.get(0);
                        String imageURL = currentPhoto.mediaUrl;
                        tweetModel.setImageURL(imageURL);
                        items.add(tweetModel);

                    }
                }

                Collections.sort(items, new Comparator<Image>() {
                    public int compare(Image t1, Image t2) {
                        return t2.getFavoriteCount() - t1.getFavoriteCount();
                    }
                });
                postImages(items);

            }

            @Override
            public void failure(TwitterException exception) {
                postError(exception.getLocalizedMessage());
            }
        };

        client.getTimeLineService().homeTimeline(TWEET_COUNT,true,true,true,true,callback);
    }


    private boolean containsImages(Tweet tweet) {
        return  tweet.entities != null && //tiene entities
                tweet.entities.media != null && //estos entities tienen media(fotos)
                !tweet.entities.media.isEmpty();//y estos media no son vacías
    }

    private void postError(String error) {
        postEvent(null,error);
    }

    private void postImages(List<Image> items) {
        postEvent(items,null);
    }

    private void postEvent(List<Image> items,String error){
        ImagesEvent event = new ImagesEvent();
        event.setImages(items);
        eventBus.post(event);
    }
}
