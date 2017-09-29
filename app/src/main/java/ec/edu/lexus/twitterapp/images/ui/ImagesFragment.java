package ec.edu.lexus.twitterapp.images.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.entities.Image;
import ec.edu.lexus.twitterapp.images.ImagesPresenter;
import ec.edu.lexus.twitterapp.images.ui.adapters.ImagesAdapter;
import ec.edu.lexus.twitterapp.images.ui.adapters.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment implements ImagesView, OnItemClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    FrameLayout container;

    ImagesPresenter presenter;
    ImagesAdapter adapter;
    public ImagesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        return view;
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
    public void setContent(List<Image> items) {
        adapter.setItems(items);
    }

    @Override
    public void onItemClick(Image tweet) {
        Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(tweet.getTweetURL()));
        startActivity(i);
    }


}
