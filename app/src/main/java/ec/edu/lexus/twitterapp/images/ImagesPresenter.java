package ec.edu.lexus.twitterapp.images;

import ec.edu.lexus.twitterapp.images.events.ImagesEvent;

/**
 * Created by Alexis on 28/09/2017.
 */

public interface ImagesPresenter {
    void onResume();
    void onPause();
    void onDestroy();
    void getImageTweets();
    void onEventMainThread(ImagesEvent event);
}
