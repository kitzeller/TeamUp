package wpi.jtkaplan.teamup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wpi.jtkaplan.teamup.model.Class;

public class RecyclerViewFragment extends Fragment {

    private List<Class> classes;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData() {
        // TODO: Need to get this data from Google Cloud for each user.
        classes = new ArrayList<>();
        classes.add(new Class("Intro to Psy", "PSY 1402","Mr Wilson"));
        classes.add(new Class("Intro to Computing", "CS 4518","Tian Guo"));
        classes.add(new Class("Fun and Games", "FUN 101","Mr Jake"));
        classes.add(new Class("Intro to Games", "IMGD 1001","Mr Baker"));
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(classes);
        adapter.getItemId(classes.size() - 1);
        rv.setAdapter(adapter);
    }
}