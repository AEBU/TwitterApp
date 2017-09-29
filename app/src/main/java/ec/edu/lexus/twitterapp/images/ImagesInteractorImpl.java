package ec.edu.lexus.twitterapp.images;

/**
 * Created by Alexis on 28/09/2017.
 */

public class ImagesInteractorImpl implements ImagesInteractor {
    ImagesRepository repository;

    public ImagesInteractorImpl(ImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getImages();
    }
}
