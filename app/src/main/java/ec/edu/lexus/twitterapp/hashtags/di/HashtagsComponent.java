package ec.edu.lexus.twitterapp.hashtags.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.edu.lexus.twitterapp.di.LibsModule;
import ec.edu.lexus.twitterapp.hashtags.ui.HashtagsFragment;
import ec.edu.lexus.twitterapp.images.ui.ImagesFragment;

/**
 * Created by Alexis on 30/09/2017.
 */
@Singleton
@Component(modules = {HashtagsModule.class,LibsModule.class})
public interface HashtagsComponent {
    void inject(HashtagsFragment hashtagsFragment);
}
