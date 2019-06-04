package com.example.ledstrip_controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ledstrip_controller.database.CommandDao;
import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.List;

public class RemotesFragment extends Fragment {


    private MainViewModel mMainViewModel;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private RemoteDao mRemoteDao;
    private CommandDao mCommandDao;
    private List<Remote> mRemoteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_remotes, container, false);

        mCommandDao = RepoDatabase
                .getInstance(getContext())
                .getCommandDao();

        mRemoteDao = RepoDatabase
                .getInstance(getContext())
                .getRemoteDao();

        // Create recylerView
        recyclerView = myFragmentView.findViewById(R.id.recyclerv_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create MainModelView
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getReminders().observe(this, new Observer<List<Remote>>() {

            @Override
            public void onChanged(@Nullable List<Remote> remotes) {
                mRemoteList = remotes;
                updateUI();
            }
        });

        // Create Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Nieuw scherm aanroepen
                Intent intent = new Intent(getActivity(), AddRemoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return myFragmentView;
    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new RecyclerViewAdapter(mRemoteList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapList(mRemoteList);
        }
    }

}







