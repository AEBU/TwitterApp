package ec.edu.lexus.twitterapp.hashtags;

import ec.edu.lexus.twitterapp.api.CustomTwitterApiClient;
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

    }
}
