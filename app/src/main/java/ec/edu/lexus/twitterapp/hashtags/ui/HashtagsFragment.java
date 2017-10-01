package ec.edu.lexus.twitterapp.hashtags.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ec.edu.lexus.twitterapp.R;
import ec.edu.lexus.twitterapp.entities.Hashtag;

/**
 * A simple {@link Fragment} subclass.
 */
public class HashtagsFragment extends Fragment implements HashtagsView{


    public HashtagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hashtags, container, false);
    }

    @Override
    public void showElements() {

    }

    @Override
    public void hideElements() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void setContent(List<Hashtag> items) {

    }
}
