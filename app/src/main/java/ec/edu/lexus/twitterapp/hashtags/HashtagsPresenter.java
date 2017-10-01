package ec.edu.lexus.twitterapp.hashtags;

import ec.edu.lexus.twitterapp.hashtags.events.HashtagEvent;
import ec.edu.lexus.twitterapp.images.events.ImagesEvent;

/**
 * Created by Alexis on 30/09/2017.
 */

public interface HashtagsPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    void getHashtagTweets();
    void onEventMainThread(HashtagEvent event);
}
