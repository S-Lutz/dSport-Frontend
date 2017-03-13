package com.omgproduction.dsport_application.fragments.helper;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.CreatePostActivity;


public class SocialMenuFragment extends MenuFragment{

    private FloatingActionButton camera, gallery, exercise_unit, event, text;
    @Override
    public int getLayout() {
        return R.layout.layout_fragment_floating_menu_social;
    }

    @Override
    protected void setMenuButtons(View v) {

        camera = (FloatingActionButton) v.findViewById(R.id.social_fab_picture);
        gallery = (FloatingActionButton) v.findViewById(R.id.social_fab_gallery);
        exercise_unit = (FloatingActionButton) v.findViewById(R.id.social_fab_exercise_unit);
        event = (FloatingActionButton) v.findViewById(R.id.social_fab_event);
        text = (FloatingActionButton) v.findViewById(R.id.social_fab_text);

        addMenuButton(camera);
        addMenuButton(gallery);
        addMenuButton(exercise_unit);
        addMenuButton(event);
        addMenuButton(text);
    }

    @Override
    protected int getMenuContainer() {
        return R.id.social_menu_sub_button_holder;
    }

    @Override
    public void onMenuButtonClicked(View v) {
        switch (v.getId()){
            case R.id.social_fab_event: onEventClick(); break;
            case R.id.social_fab_exercise_unit: onExerciseUnitClick();break;
            case R.id.social_fab_gallery: onGalleryClick();break;
            case R.id.social_fab_picture: onPictureClick();break;
            case R.id.social_fab_text: onTextClick(); break;
        }
    }

    private void onPictureClick() {
        hide();
        Intent intent = new Intent(getContext(),CreatePostActivity.class);
        intent.putExtra(CREATE_POST_TYPE_KEY,CREATE_POST_PICTURE_VALUE);
        intent.putExtra(INTENT_POST_OWNER_ID,owner);
        startActivity(intent);
    }

    private void onGalleryClick() {
        hide();
        Intent intent = new Intent(getContext(),CreatePostActivity.class);
        intent.putExtra(CREATE_POST_TYPE_KEY,CREATE_POST_GALLERY_VALUE);
        intent.putExtra(INTENT_POST_OWNER_ID,owner);
        startActivity(intent);
    }

    private void onExerciseUnitClick() {

    }

    private void onEventClick() {
    }

    private void onTextClick() {
        hide();
        Intent intent = new Intent(getContext(),CreatePostActivity.class);
        intent.putExtra(CREATE_POST_TYPE_KEY,CREATE_POST_TEXT_VALUE);
        intent.putExtra(INTENT_POST_OWNER_ID,owner);
        startActivity(intent);
    }

    public FloatingActionButton getCamera() {
        return camera;
    }

    public void setCamera(FloatingActionButton camera) {
        this.camera = camera;
    }

    public FloatingActionButton getGallery() {
        return gallery;
    }

    public void setGallery(FloatingActionButton gallery) {
        this.gallery = gallery;
    }

    public FloatingActionButton getExercise_unit() {
        return exercise_unit;
    }

    public void setExercise_unit(FloatingActionButton exercise_unit) {
        this.exercise_unit = exercise_unit;
    }

    public FloatingActionButton getEvent() {
        return event;
    }

    public void setEvent(FloatingActionButton event) {
        this.event = event;
    }

    public FloatingActionButton getText() {
        return text;
    }

    public void setText(FloatingActionButton text) {
        this.text = text;
    }
}
