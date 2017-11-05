package com.omgproduction.dsport_application.aaRefactored.interfaces;

import android.view.View;

import com.omgproduction.dsport_application.aaRefactored.adapter.SearchUserAdapter;

public interface onItemClickListener<T>{
    void onClick(View v, T value, SearchUserAdapter.relationshipType status, int adapterPosition);
}