package com.omgproduction.dsport_application.fragments.helper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragment;

import java.util.List;

/**
 * Created by Strik on 08.03.2017.
 */

public abstract class UniversalListFragment<T, A extends RecyclerView.Adapter> extends AbstractFragment implements IRequestFuture<List<T>> {


    public enum Mode {
        GLOBAL,
        PRIVATE
    }

    private Mode mode = Mode.GLOBAL;

    private RecyclerView recyclerView;
    private A adapter;
    private RecyclerView.LayoutManager layoutManager;
    protected MenuManager menuManager;

    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    protected final void update() {
        switch (mode) {
            case GLOBAL:
                updateGlobal();
                break;
            case PRIVATE:
                updatePrivate();
                break;
        }

    }

    protected abstract void updatePrivate();

    protected abstract void updateGlobal();

    @Override
    public void onRefresh() {
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_fragment_social, container, false);
        setRefresher((SwipeRefreshLayout) view.findViewById(R.id.social_refresher));
        update();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStartQuery() {
        showProgressBar(true);
    }

    @Override
    public void onSuccess(List<T> values) {
        recyclerView = (RecyclerView) getView().findViewById(R.id.universal_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = getAdapter(values);
        recyclerView.setAdapter(adapter);
    }


    public abstract A getAdapter(List<T> values);

    @Override
    public void onFailure(String errorCode) {
        if(getView()!=null){
            printError(getView(), getView(), errorCode);
        }
    }



    @Override
    public void onFinishQuery() {
        showProgressBar(false);
    }

    public void onSetActive(boolean flag){

    }
}

