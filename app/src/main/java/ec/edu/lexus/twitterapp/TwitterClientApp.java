package ec.edu.lexus.twitterapp;

import android.app.Application;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import ec.edu.lexus.twitterapp.di.LibsModule;
import ec.edu.lexus.twitterapp.images.di.DaggerImagesComponent;
import ec.edu.lexus.twitterapp.images.di.ImagesComponent;
import ec.edu.lexus.twitterapp.images.di.ImagesModule;
import ec.edu.lexus.twitterapp.images.ui.ImagesView;
import ec.edu.lexus.twitterapp.images.ui.adapters.OnItemClickListener;

/**
 * Created by Alexis on 26/09/2017.
 */

public class TwitterClientApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initTwitter();
    }

    private void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public ImagesComponent getImagesComponent(Fragment fragment, ImagesView view, OnItemClickListener listener){
        return DaggerImagesComponent
                .builder()
                .libsModule(new LibsModule(fragment))
                .imagesModule(new ImagesModule(view, listener))
                .build();
    }

}
