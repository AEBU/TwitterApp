package ec.edu.lexus.twitterapp.hashtags.ui;

import java.util.List;

import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.entities.Image;

/**
 * Created by Alexis on 30/09/2017.
 */

public interface HashtagsView {
    void showElements();
    void hideElements();
    void showProgress();
    void hideProgress();

    void onError(String error);
    void setContent(List<Hashtag> items);
}
