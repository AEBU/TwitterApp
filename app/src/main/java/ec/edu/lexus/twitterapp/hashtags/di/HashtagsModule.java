package ec.edu.lexus.twitterapp.hashtags.di;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.edu.lexus.twitterapp.api.CustomTwitterApiClient;
import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.hashtags.HashtagsInteractor;
import ec.edu.lexus.twitterapp.hashtags.HashtagsInteractorImpl;
import ec.edu.lexus.twitterapp.hashtags.HashtagsPresenter;
import ec.edu.lexus.twitterapp.hashtags.HashtagsPresenterImpl;
import ec.edu.lexus.twitterapp.hashtags.HashtagsRepository;
import ec.edu.lexus.twitterapp.hashtags.HashtagsRepositoryImpl;
import ec.edu.lexus.twitterapp.hashtags.ui.HashtagsView;
import ec.edu.lexus.twitterapp.hashtags.ui.adapters.HashtagsAdapter;
import ec.edu.lexus.twitterapp.hashtags.ui.adapters.OnItemClickListenerHashtags;
import ec.edu.lexus.twitterapp.images.ImagesInteractor;
import ec.edu.lexus.twitterapp.images.ImagesPresenter;
import ec.edu.lexus.twitterapp.images.ImagesPresenterImpl;
import ec.edu.lexus.twitterapp.images.ui.ImagesView;
import ec.edu.lexus.twitterapp.images.ui.adapters.OnItemClickListener;
import ec.edu.lexus.twitterapp.lib.base.EventBus;

/**
 * Created by Alexis on 30/09/2017.
 */

@Module
public class HashtagsModule {

    private HashtagsView view;
    private OnItemClickListenerHashtags listenerHashtags;

    public HashtagsModule(HashtagsView view, OnItemClickListenerHashtags listenerHashtags) {
        this.view = view;
        this.listenerHashtags = listenerHashtags;
    }


    @Provides
    @Singleton
    HashtagsAdapter providesHashtagsAdapter(List<Hashtag> dataset, OnItemClickListenerHashtags listenerHashtags){
        return new HashtagsAdapter(dataset,listenerHashtags);
    }

    @Provides
    @Singleton
    List<Hashtag> providesItemsList(){
        return new ArrayList<Hashtag>();
    }

    @Provides
    @Singleton
    OnItemClickListenerHashtags providesOnItemClickListenerHashtags(){
        return this.listenerHashtags;
    }


    //..............Para proveer el PresenterImpl

    @Provides
    @Singleton
    HashtagsPresenter provideImagesPresenter(EventBus eventBus, HashtagsView view, HashtagsInteractor interactor) {
        return new HashtagsPresenterImpl(eventBus, view, interactor);
    }

        //seguimos con la cascada y en este caso nos toca proveer el hashtagView, que necesitamos ya que usamos anteriormente
        //como tenemos la vista definida en el constructor le podemos enviar como this.view


    @Provides
    @Singleton
    HashtagsView provideHashtagsView() {
        return this.view;
    }

        //necesito un Interactor para proveer devido a ImagesPresenter

    @Provides
    @Singleton
    HashtagsInteractor providesHashtagsInteractor(HashtagsRepository repository){
        return new HashtagsInteractorImpl(repository);
    }
        //para esto necesito un repositorio que deseo inyectar dentro

    @Provides
    @Singleton
    HashtagsRepository providesHashtagsRepository(EventBus eventBus, CustomTwitterApiClient client){
        return new HashtagsRepositoryImpl(eventBus, client);
    }
        //para proveer la sesion usamos TwitterSession

    @Provides
    @Singleton
    CustomTwitterApiClient providesCustomTwitterApiClient(TwitterSession session){
        return new CustomTwitterApiClient(session);
    }
        //solo me falta la sesión de Twitter

    @Provides
    @Singleton
    TwitterSession providesTwitterSession(){
        return  TwitterCore.getInstance().getSessionManager().getActiveSession();
    }

    //terminó de la llamada




}
