package ec.edu.lexus.twitterapp.images;


import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;

import ec.edu.lexus.twitterapp.images.events.ImagesEvent;
import ec.edu.lexus.twitterapp.images.ui.ImagesView;
import ec.edu.lexus.twitterapp.lib.base.EventBus;

/**
 * Created by Alexis on 28/09/2017.
 */

public class ImagesPresenterImpl implements ImagesPresenter {

    private EventBus eventBus;
    private ImagesView view;
    private ImagesInteractor interactor;

    public ImagesPresenterImpl(EventBus eventBus, ImagesView view, ImagesInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        view=null;
    }

    @Override
    public void getImageTweets() {
        if (this.view != null){
            view.hideElements();
            view.showProgress();
        }
        this.interactor.execute();//this.interactor.getImageItemsList();
    }

    @Override
    @Subscribe
    public void onEventMainThread(ImagesEvent event) {
        String errorMsg = event.getError();
        if (this.view != null) {
            view.showElements();
            view.hideProgress();
            if (errorMsg != null) {
                this.view.onError(errorMsg);
            } else {
//                List<Image> items = event.getImages();
//                if (items != null && !items.isEmpty()) {
//                    this.view.setImages(items);
//                }
                this.view.setContent(event.getImages());
            }
        }
    }
}
