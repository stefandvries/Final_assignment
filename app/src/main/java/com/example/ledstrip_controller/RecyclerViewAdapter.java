package com.example.ledstrip_controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ledstrip_controller.database.Remote;
import com.example.ledstrip_controller.database.RemoteDao;
import com.example.ledstrip_controller.database.RepoDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    RemoteDao mRemoteDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    ToggleButton mPreviousFavorite = null;
    private List<Remote> mData;

    Remote mPreviousFavoriteRemote = null;


    public RecyclerViewAdapter(List<Remote> data) {
        this.mData = data;

    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_list_item, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);


        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        final Remote remote = mData.get(position);
        holder.titleTextView.setText((remote.mName));

        holder.titleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(v.getContext(), EditRemoteActivity.class);
                intent.putExtra("remoteID", remote.id);
                v.getContext().startActivity(intent);
                return false;
            }
        });

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Nieuw scherm aanroepen
                Intent intent = new Intent(v.getContext(), RemoteActivity.class);
                intent.putExtra("remoteID", String.valueOf(remote.id));

                v.getContext().startActivity(intent);
            }
        });

        final ScaleAnimation scaleAnimation;
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        if (remote.mIsFavorite) {


            if (mPreviousFavoriteRemote == null) {
                holder.buttonFavorite.setChecked(true);
                mPreviousFavorite = holder.buttonFavorite;
                mPreviousFavoriteRemote = remote;
            }
        }


        holder.buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //animation
                compoundButton.startAnimation(scaleAnimation);


                if (mPreviousFavoriteRemote != null) {
                    mPreviousFavorite.setChecked(false);
                    mPreviousFavorite = holder.buttonFavorite;
                    setFavorite(remote, true, compoundButton);

                } else if (mPreviousFavoriteRemote == null) {
                    mPreviousFavorite = holder.buttonFavorite;
                    setFavorite(remote, false, compoundButton);
                }
           }
        });


    }


    private void setFavorite(final Remote remote, final boolean thereWasAPreviousFav, final CompoundButton compoundButton) {


        mExecutor.execute(new Runnable() {

            @Override

            public void run() {


                mRemoteDao = RepoDatabase
                        .getInstance(compoundButton.getContext())
                        .getRemoteDao();

                if (thereWasAPreviousFav) {
                    Remote mOldFavorite = new Remote(mPreviousFavoriteRemote.id, mPreviousFavoriteRemote.mName, mPreviousFavoriteRemote.mBaseLink, false);
                    mRemoteDao.update(mOldFavorite);
                }
                Remote mNewFavorite = new Remote(remote.id, remote.mName, remote.mBaseLink, true);
                mRemoteDao.update(mNewFavorite);
                mPreviousFavoriteRemote = mNewFavorite;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ToggleButton buttonFavorite;


        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            buttonFavorite = itemView.findViewById(R.id.button_favorite);

        }
    }

    public void swapList(List<Remote> newList) {
        mData = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}
