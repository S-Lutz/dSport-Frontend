package com.omgproduction.dsport_application.aaRefactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.ExerciseUnitAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.services.ExerciseUnitService;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class ExerciseUnitsActivity extends AppCompatActivity implements View.OnClickListener, onSocialItemClickedListener, onDialogFragmentClickListener {

    private ArrayList<ExerciseUnitNode> exerciseNodes;

    private RecyclerView exerciseUnitsRecyclerView;
    private ExerciseUnitAdapter exerciseUnitAdapter;

    private ExerciseUnitService exerciseUnitService;
    private LoadingView loadingView;
    private UserNode exerciseUnitOwner;

    private GeneralDialogFragment dialogFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_exercise_units_activity);

        this.exerciseUnitService = new ExerciseUnitService();
        exerciseUnitOwner = (UserNode) getIntent().getSerializableExtra("USER");

        dialogFragment = new GeneralDialogFragment(this, this, this);
        exerciseNodes = new ArrayList<>();

        loadingView = (LoadingView) findViewById(R.id.loading_exercise_units_result);
        loadingView.show();

        setupRecyclerView();
        getExercises(exerciseUnitOwner.getId());

        findViewById(R.id.create_exercise_units_fab).setOnClickListener(this);

        setUpToolbarTitle();

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.exercise_units_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setTitle("Exercise units");
    }

    private void updateView() {
        loadingView.show();
        getExercises(exerciseUnitOwner.getId());
    }

    private void setupRecyclerView() {
        exerciseUnitsRecyclerView = (RecyclerView) findViewById(R.id.exercise_units_recycler_view);
        exerciseUnitsRecyclerView.setHasFixedSize(true);
        exerciseUnitsRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exerciseUnitsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setExerciseUnitAdapter() {
        exerciseUnitAdapter = new ExerciseUnitAdapter(ExerciseUnitsActivity.this, exerciseNodes, this);
        exerciseUnitsRecyclerView.setAdapter(exerciseUnitAdapter);
    }

    private void getExercises(Long id) {
      if (!exerciseNodes.isEmpty()) exerciseNodes.clear();
      exerciseUnitService.getExerciseUnits(this, id, new BackendCallback<ArrayList<ExerciseUnitNode>>() {
          @Override
          public void onSuccess(ArrayList<ExerciseUnitNode> result, Map<String, String> responseHeader) {
              exerciseNodes = result;
              loadingView.hide();
              setExerciseUnitAdapter();
          }

          @Override
          public void onFailure(ErrorResponse error) {
              loadingView.hide();
              dialogFragment.createDialog().show();
          }

      });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_exercise_units_fab:
                startCreateExerciseUnitActivity();
        }
    }

    private void startCreateExerciseUnitActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", this.exerciseUnitOwner);
        startActivity(new Intent(this, CreateExerciseUnitActivity.class).putExtras(bundle));
    }

    @Override
    public void onClick(View v, int adapterPosition) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("EXERCISEUNIT", exerciseNodes.get(adapterPosition));
        startActivity(new Intent(this, ExerciseUnitDetailActivity.class).putExtras(bundle));
    }

    @Override
    public void onOkClicked() {
        updateView();
    }

    @Override
    public void onCancelClicked() {
        //do nothing
    }
}