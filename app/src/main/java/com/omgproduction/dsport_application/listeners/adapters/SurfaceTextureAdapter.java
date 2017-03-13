package com.omgproduction.dsport_application.listeners.adapters;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

/**
 * Created by Florian on 08.03.2017.
 */

public class SurfaceTextureAdapter implements TextureView.SurfaceTextureListener {
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
