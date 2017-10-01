package ec.edu.lexus.twitterapp.hashtags;

import org.greenrobot.eventbus.Subscribe;

import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.hashtags.events.HashtagEvent;
import ec.edu.lexus.twitterapp.hashtags.ui.HashtagsView;
import ec.edu.lexus.twitterapp.lib.base.EventBus;

/**
 * Created by Alexis on 30/09/2017.
 */

public class HashtagsPresenterImpl implements HashtagsPresenter {
    private EventBus eventBus;
    private HashtagsView view;
    private HashtagsInteractor interactor;

    public HashtagsPresenterImpl(EventBus eventBus, HashtagsView view, HashtagsInteractor interactor) {
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
    public void getHashtagTweets() {
        if (this.view != null){
            view.hideElements();
            view.showProgress();
        }
        this.interactor.execute();//this.interactor.getImageItemsList();
    }

    @Override
    @Subscribe
    public void onEventMainThread(HashtagEvent event) {
        String errorMsg=event.getError();
        if (this.view!=null){
            view.showElements();
            view.hideProgress();
            if (errorMsg != null) {
                this.view.onError(errorMsg);
            } else {
                this.view.setContent(event.getHashtags());

            }
        }

    }
}
