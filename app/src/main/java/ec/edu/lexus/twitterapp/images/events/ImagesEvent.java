package ec.edu.lexus.twitterapp.images.events;

import java.util.List;

import ec.edu.lexus.twitterapp.entities.Image;

import static android.R.attr.version;

/**
 * Created by Alexis on 28/09/2017.
 */
public class ImagesEvent {
    private String error;
    private List<Image> images;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
