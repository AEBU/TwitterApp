package ec.edu.lexus.twitterapp.images.ui;

import java.util.List;

import ec.edu.lexus.twitterapp.entities.Image;

/**
 * Created by Alexis on 28/09/2017.
 */

public interface ImagesView {
    void showElements();
    void hideElements();
    void showProgress();
    void hideProgress();

    void onError(String error);
    void setContent(List<Image> items);
}
