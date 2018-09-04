package com.joinyon.androidguide.WordPressEditor.inter;

import org.json.JSONObject;

import java.util.Map;

import static com.joinyon.androidguide.WordPressEditor.view.EditorFragmentAbstract.MediaType;

/**
 * 编辑器状态变化监听
 */
public interface OnJsEditorStateChangedListener {
    /**
     * Dom下载
     */
    void onDomLoaded();

    /**
     * 选择变化
     *
     * @param selectionArgs
     */
    void onSelectionChanged(Map<String, String> selectionArgs);

    /**
     * 选中样式变化
     *
     * @param changeSet
     */
    void onSelectionStyleChanged(Map<String, Boolean> changeSet);

    /**
     * 媒体文件选择
     *
     * @param mediaId
     * @param mediaType
     * @param meta
     * @param uploadStatus
     */
    void onMediaTapped(String mediaId, MediaType mediaType, JSONObject meta, String uploadStatus);

    /**
     * 链接选择
     *
     * @param url
     * @param title
     */
    void onLinkTapped(String url, String title);

    /**
     * 媒体文件移除
     *
     * @param mediaId
     */
    void onMediaRemoved(String mediaId);

    /**
     * 媒体文件替换
     *
     * @param mediaId
     */
    void onMediaReplaced(String mediaId);

    void onVideoPressInfoRequested(String videoId);

    /**
     * @param responseArgs
     */
    void onGetHtmlResponse(Map<String, String> responseArgs);

    /**
     *
     */
    void onActionFinished();
}
