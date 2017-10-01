package ec.edu.lexus.twitterapp.hashtags.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.entities.Hashtag;

import static android.os.Build.VERSION_CODES.M;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_hashtags, parent, false);
        return new ViewHolder(view,parent.getContext());

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hashtag tweet=dataset.get(position);
        holder.setOnClickListener(tweet,listenerHashtags);
        holder.txtTweet.setText(tweet.getTweetText());
        holder.setItems(tweet.getHashtags());
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
        @BindView(R.id.txtTweet)
        TextView txtTweet;
        @BindView(R.id.recyclerViewHashtags)
        RecyclerView recyclerViewHashtags;

        private View view;
        private HashtagListAdapter adapter;
        private ArrayList<String> items;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.view = itemView;

            items = new ArrayList<String>();
            adapter= new HashtagListAdapter(items);

            CustomGridLayoutManager layoutManager=new CustomGridLayoutManager(context,3);
            recyclerViewHashtags.setLayoutManager(null);
            recyclerViewHashtags.setAdapter(adapter);

        }

        public void setOnClickListener(final Hashtag hashtag, final OnItemClickListenerHashtags listenerHashtags) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerHashtags.onItemClick(hashtag);
                }
            });
        }
        public void setItems(List<String> newItems){
            items.clear();
            items.addAll(newItems);
            adapter.notifyDataSetChanged();
        }
    }
}
