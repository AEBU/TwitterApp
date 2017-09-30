package ec.edu.lexus.twitterapp.images.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.edu.lexus.twitterapp.di.LibsModule;
import ec.edu.lexus.twitterapp.images.ImagesPresenter;
import ec.edu.lexus.twitterapp.images.ui.ImagesFragment;

import static android.R.attr.y;

/**
 * Created by Alexis on 29/09/2017.
 */

@Singleton
@Component(modules = {ImagesModule.class,LibsModule.class})
public interface ImagesComponent {

    //este es el target que podemos usar para inyectar, ya sea como Actividad , fragmento etc
    void inject(ImagesFragment imagesFragment);

    /*
     *Segunda opción sería devolver un objeto y ponerle algún nombre al método, es decir en el módulo voy a crear los provides
     *  y este método lo voy a llamar en base al componente.
    */
    ImagesPresenter getPresenter();
}
