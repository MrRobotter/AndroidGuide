package com.joinyon.androidguide.WordPressEditor.activity;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;

import com.joinyon.androidguide.R;
import com.joinyon.androidguide.WordPressEditor.view.EditorFragmentAbstract;
import com.joinyon.androidguide.WordPressEditor.view.ImageSettingsDialogFragment;

import org.wordpress.android.util.helpers.MediaFile;

import java.util.ArrayList;

/**
 * 这里可以进行媒体文件上传操作
 * 监听上传进度
 */
public class MyEditActivity extends AppCompatActivity implements EditorFragmentAbstract.EditorFragmentListener,
        EditorFragmentAbstract.EditorDragAndDropListener {
    public static final String CONTENT_PARAM = "CONTENT_PARAM";
    public static final String PARAM = "PARAM";
    private EditorFragmentAbstract mEditorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_edit);
    }

    @Override
    public void onEditorFragmentInitialized() {
        // arbitrary setup
        mEditorFragment.setFeaturedImageSupported(true);
        mEditorFragment.setBlogSettingMaxImageWidth("600");
        mEditorFragment.setDebugModeEnabled(true);
        // get title and content and draft switch
        String content = getIntent().getStringExtra(CONTENT_PARAM);
        mEditorFragment.setContent(content);
        Log.e("AAA", "onEditorFragmentInitialized");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e("AAA", "执行了几次？？");
        if (fragment instanceof EditorFragmentAbstract) {
            mEditorFragment = (EditorFragmentAbstract) fragment;
            Log.e("AAA", "fragment");
        } else {
            Log.e("AAA", "没有");
        }
    }

    @Override
    public void onSettingsClicked() {

    }

    @Override
    public void onAddMediaClicked() {

    }

    @Override
    public void onMediaRetryClicked(String mediaId) {

    }

    @Override
    public void onMediaUploadCancelClicked(String mediaId, boolean delete) {

    }

    @Override
    public void onFeaturedImageChanged(long mediaId) {

    }

    @Override
    public void onVideoPressInfoRequested(String videoId) {

    }

    @Override
    public String onAuthHeaderRequested(String url) {
        return "";
    }

    @Override
    public void saveMediaFile(MediaFile mediaFile) {

    }

    @Override
    public void onTrackableEvent(EditorFragmentAbstract.TrackableEvent event) {

    }

    @Override
    public void onMediaDropped(ArrayList<Uri> mediaUri) {

    }

    @Override
    public void onRequestDragAndDropPermissions(DragEvent dragEvent) {

    }
}
