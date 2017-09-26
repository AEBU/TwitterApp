package ec.edu.lexus.twitterapp.lib;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import ec.edu.lexus.twitterapp.lib.base.ImageLoader;

/**
 * Created by Alexis on 26/09/2017.
 */

public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;

    public GlideImageLoader(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    @Override
    public void load(ImageView imgAvatar, String url) {
        RequestOptions requestOptions = new RequestOptions();
            requestOptions
                    .centerCrop()
                    .override(600, 400)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        glideRequestManager
                .load(url)
                .apply(requestOptions)
                .into(imgAvatar);

    }
}
