package ec.edu.lexus.twitterapp.hashtags;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ec.edu.lexus.twitterapp.api.CustomTwitterApiClient;
import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.hashtags.events.HashtagEvent;
import ec.edu.lexus.twitterapp.lib.base.EventBus;

/**
 * Created by Alexis on 30/09/2017.
 */

public class HashtagsRepositoryImpl implements HashtagsRepository{

    private EventBus eventBus;
    private CustomTwitterApiClient client;
    private final static int TWEET_COUNT=100;

    public HashtagsRepositoryImpl(EventBus eventBus, CustomTwitterApiClient client) {
        this.eventBus = eventBus;
        this.client = client;
    }

    @Override
    public void getHashtags() {
        Callback<List<Tweet>> callback=new Callback<List<Tweet>>() {

            @Override
            public void success(Result<List<Tweet>> result) {
                List<Hashtag> items=new ArrayList<>();
                for (Tweet tweet:result.data){
                    if (containsHashtags(tweet)){
                        Hashtag hashtagModel=new Hashtag();
                        hashtagModel.setId(tweet.idStr);
                        hashtagModel.setFavoriteCount(tweet.favoriteCount);
                        hashtagModel.setTweetText(tweet.text);

                        List<String> hashtags=new ArrayList<String>();
                        for (HashtagEntity hashtag:tweet.entities.hashtags){
                            hashtags.add(hashtag.text   );
                        }
                        hashtagModel.setHashtags(hashtags);
                        items.add(hashtagModel);
                    }
                }
                Collections.sort(items, new Comparator<Hashtag>() {
                    public int compare(Hashtag t1, Hashtag t2) {
                        return t2.getFavoriteCount() - t1.getFavoriteCount();
                    }
                });
                postHashtags(items);

            }

            @Override
            public void failure(TwitterException exception) {
                postError(exception.getLocalizedMessage());

            }
        };
        client.getTimeLineService().homeTimeline(TWEET_COUNT,true,true,true,true).enqueue(callback);

    }

    private boolean containsHashtags(Tweet tweet) {
        return  tweet.entities != null && //tiene entities
                tweet.entities.hashtags != null && //estos entities tienen hashtags
                !tweet.entities.hashtags.isEmpty();//y estos hashtags no sean vacíos
    }

    private void postError(String error) {
        postEvent(null,error);
    }

    private void postHashtags(List<Hashtag> items) {
        postEvent(items,null);
    }

    private void postEvent(List<Hashtag> items,String error){
        HashtagEvent event = new HashtagEvent();
        event.setHashtags(items);
        eventBus.post(event);
    }
}
