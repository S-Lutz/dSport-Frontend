package com.omgproduction.dsport_application.refactored.interfaces;

import android.view.View;

import com.omgproduction.dsport_application.refactored.adapter.SearchUserAdapter;

public interface onItemClickListener<T>{
    void onClick(View v, T value, SearchUserAdapter.relationshipType status, int adapterPosition);
}