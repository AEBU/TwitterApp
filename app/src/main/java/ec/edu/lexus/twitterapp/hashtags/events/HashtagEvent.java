package ec.edu.lexus.twitterapp.hashtags.events;

import java.util.List;

import ec.edu.lexus.twitterapp.entities.Hashtag;

/**
 * Created by Alexis on 30/09/2017.
 */
public class HashtagEvent {
    private String error;
    private List<Hashtag> hashtags;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
}
