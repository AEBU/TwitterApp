package ec.edu.lexus.twitterapp.hashtags;

/**
 * Created by Alexis on 30/09/2017.
 */

public class HashtagsInteractorImpl implements HashtagsInteractor{
    HashtagsRepository repository;

    public HashtagsInteractorImpl(HashtagsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getHashtags();
    }
}
