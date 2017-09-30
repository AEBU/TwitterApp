package ec.edu.lexus.twitterapp.images.di;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import ec.edu.lexus.twitterapp.api.CustomTwitterApiClient;
import ec.edu.lexus.twitterapp.di.LibsModule;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.images.ImagesInteractor;
import ec.edu.lexus.twitterapp.images.ImagesInteractorImpl;
import ec.edu.lexus.twitterapp.images.ImagesPresenter;
import ec.edu.lexus.twitterapp.images.ImagesPresenterImpl;
import ec.edu.lexus.twitterapp.images.ImagesRepository;
import ec.edu.lexus.twitterapp.images.ImagesRepositoryImpl;
import ec.edu.lexus.twitterapp.images.ui.ImagesView;
import ec.edu.lexus.twitterapp.images.ui.adapters.ImagesAdapter;
import ec.edu.lexus.twitterapp.images.ui.adapters.OnItemClickListener;
import ec.edu.lexus.twitterapp.lib.base.EventBus;
import ec.edu.lexus.twitterapp.lib.base.ImageLoader;

/**
 * Created by Alexis on 29/09/2017.
 */
@Module
public class ImagesModule {
    private ImagesView view;
    private OnItemClickListener clickListener;

    public ImagesModule(ImagesView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

    //para Adapter Images, con esto ya tengo lista mi inyección del adaptador, necesito devolver todo lo demás
    @Provides
    @Singleton
    ImagesAdapter provideAdapter(List<Image> items,ImageLoader imageLoader, OnItemClickListener clickListener) {
        return new ImagesAdapter(items,imageLoader, clickListener);
    }

    @Provides
    @Singleton
    List<Image> provideItemsList() {
        return new ArrayList<Image>();
    }

    @Provides
    @Singleton
    OnItemClickListener provideClickListener() {
        return this.clickListener;
    }

    //para devolver el objeto del target(Fragmento), además del adapter es un presenter
    @Provides
    @Singleton
    ImagesPresenter provideImagesPresenter(EventBus eventBus,ImagesView view, ImagesInteractor interactor) {
        return new ImagesPresenterImpl(eventBus, view, interactor);
    }
    //como  EventBus viene de la librería voy a necesitar un provides para ImageView
    @Provides
    @Singleton
    ImagesView provideImagesView() {
        return this.view;
    }

    @Provides
    @Singleton
    ImagesInteractor provideImagesInteractor(ImagesRepository repository) {
        return new ImagesInteractorImpl(repository);
    }

    //como en providesImages Interactor recibo un repocitorio, debo dar mi Repositorio como los provides

    @Provides
    @Singleton
    ImagesRepository provideImagesRepository(EventBus eventBus,CustomTwitterApiClient client) {
        return new ImagesRepositoryImpl(eventBus,client);
    }

    //seguimos en esta cascada al CustomTwitterApiClient., y ver que no mas necesita este CustomApiClient

    @Provides
    @Singleton
    CustomTwitterApiClient providesCustomTwitterApiClient(TwitterSession session){
        return new CustomTwitterApiClient(session);
    }
    //ahora necesitamos un Session, pero no necesita un parámetro porque este lo puedo obtender desde el Core de twitter

    @Provides
    @Singleton
    TwitterSession providesTwitterSession(){
        return  TwitterCore.getInstance().getSessionManager().getActiveSession();
    }
}
