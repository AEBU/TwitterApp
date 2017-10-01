package ec.edu.lexus.twitterapp.hashtags.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.images.ui.adapters.ImagesAdapter;
import ec.edu.lexus.twitterapp.images.ui.adapters.OnItemClickListener;
import ec.edu.lexus.twitterapp.lib.base.ImageLoader;

/**
 * Created by Alexis on 30/09/2017.
 */

public class HashtagsAdapter extends RecyclerView.Adapter<HashtagsAdapter.ViewHolder> {

    private List<Hashtag> dataset;
    private OnItemClickListenerHashtags listenerHashtags;

    public HashtagsAdapter(List<Hashtag> dataset, OnItemClickListenerHashtags listenerHashtags) {
        this.dataset = dataset;
        this.listenerHashtags = listenerHashtags;
    }

    @Override
    public HashtagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_images, parent, false);
        return new HashtagsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HashtagsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Hashtag> newItems) {
        dataset.addAll(newItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
        }

        public void setOnClickListener(final Hashtag hashtag, final OnItemClickListenerHashtags listenerHashtags){
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listenerHashtags.onItemClick(hashtag);
                }
            });
        }
    }
}
