package ec.edu.lexus.twitterapp.hashtags.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.TwitterClientApp;
import ec.edu.lexus.twitterapp.entities.Hashtag;
import ec.edu.lexus.twitterapp.hashtags.HashtagsPresenter;
import ec.edu.lexus.twitterapp.hashtags.di.HashtagsComponent;
import ec.edu.lexus.twitterapp.hashtags.ui.adapters.HashtagsAdapter;
import ec.edu.lexus.twitterapp.hashtags.ui.adapters.OnItemClickListenerHashtags;
import ec.edu.lexus.twitterapp.images.di.ImagesComponent;

/**
 * A simple {@link Fragment} subclass.
 */
public class HashtagsFragment extends Fragment implements HashtagsView, OnItemClickListenerHashtags{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    FrameLayout container;

    @Inject
    HashtagsPresenter presenter;
    @Inject
    HashtagsAdapter adapter;

    public HashtagsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        setupInject();
        setupRecyclerView();
        presenter.getHashtagTweets();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setupInject() {
        TwitterClientApp app=(TwitterClientApp)getActivity().getApplication();
        HashtagsComponent hashtagsComponent=app.getHashtagsComponent(this,this);
//        presenter=imagesComponent.getPresenter();
        hashtagsComponent.inject(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showElements() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideElements() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String error) {
        Snackbar.make(container,error,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setContent(List<Hashtag> items) {
        adapter.setItems(items);
    }

    @Override
    public void onItemClick(Hashtag tweet) {
        Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(tweet.getTweetURL()));
        startActivity(i);
    }
}
