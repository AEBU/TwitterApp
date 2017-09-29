package ec.edu.lexus.twitterapp.images.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.lib.base.ImageLoader;

import static java.util.Collections.addAll;

/**
 * Created by Alexis on 29/09/2017.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {


    private List<Image> dataset;
    private ImageLoader imageLoader;
    private OnItemClickListener clickListener;

    public ImagesAdapter(List<Image> dataset, ImageLoader imageLoader, OnItemClickListener clickListener) {
        this.dataset = dataset;
        this.imageLoader = imageLoader;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image tweet = dataset.get(position);
        holder.setOnclickListener(tweet, clickListener);
        holder.txtTweet.setText(tweet.getTweetText());
        imageLoader.load(holder.imgMedia, tweet.getImageURL());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Image> newItems) {
        dataset.addAll(newItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgMedia)
        ImageView imgMedia;
        @BindView(R.id.txtTweet)
        TextView txtTweet;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.view=itemView;
        }

        public void setOnclickListener(final Image image, final OnItemClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(image);
                }
            });
        }
    }
}
