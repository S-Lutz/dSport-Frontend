package com.omgproduction.dsport_application.fragments.helper;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.CreateEventActivity;


public class EventMenuFragment extends MenuFragment{

    private FloatingActionButton camera, gallery, event;
    @Override
    public int getLayout() {
        return R.layout.layout_fragment_floating_menu_event;
    }

    @Override
    protected void setMenuButtons(View v) {

        camera = (FloatingActionButton) v.findViewById(R.id.event_fab_picture);
        gallery = (FloatingActionButton) v.findViewById(R.id.event_fab_gallery);
        event = (FloatingActionButton) v.findViewById(R.id.event_fab_text);

        addMenuButton(camera);
        addMenuButton(gallery);
        addMenuButton(event);
    }

    @Override
    protected int getMenuContainer() {
        return R.id.event_menu_sub_button_holder;
    }

    @Override
    public void onMenuButtonClicked(View v) {
        switch (v.getId()){
            case R.id.event_fab_gallery: onGalleryClick();break;
            case R.id.event_fab_picture: onPictureClick();break;
            case R.id.event_fab_text: onEventClick();break;
        }
    }

    private void onPictureClick() {
        hide();
        Intent intent = new Intent(getContext(),CreateEventActivity.class);
        intent.putExtra(CREATE_EVENT_TYPE_KEY,CREATE_EVENT_PICTURE_VALUE);
        intent.putExtra(INTENT_EVENT_OWNER_ID,owner);
        startActivity(intent);
    }

    private void onGalleryClick() {
        hide();
        Intent intent = new Intent(getContext(),CreateEventActivity.class);
        intent.putExtra(CREATE_EVENT_TYPE_KEY,CREATE_EVENT_GALLERY_VALUE);
        intent.putExtra(INTENT_EVENT_OWNER_ID,owner);
        startActivity(intent);
    }

    private void onExerciseUnitClick() {

    }

    private void onEventClick() {
        hide();
        Intent intent = new Intent(getContext(), CreateEventActivity.class);
        intent.putExtra(CREATE_EVENT_TYPE_KEY,CREATE_EVENT_TEXT_VALUE);
        intent.putExtra(INTENT_POST_OWNER_ID,owner);
        startActivity(intent);
    }

    private void onTextClick() {
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

    public FloatingActionButton getEvent() {return event; }

    public void setEvent(FloatingActionButton text) {
        this.event = event;
    }
}
