package com.cgy.colorfulnews.module.photo.presenter.impl;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.PhotoRequestType;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.photo.interactor.impl.PhotoDetailInteractImpl;
import com.cgy.colorfulnews.module.photo.presenter.PhotoDetailPresenter;
import com.cgy.colorfulnews.module.photo.view.PhotoDetailView;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/17 13:37
 */
public class PhotoDetailPresenterImpl extends BasePresenterImpl<PhotoDetailView, Uri> implements
        PhotoDetailPresenter, RequestCallback<Uri> {

    private PhotoDetailInteractImpl mPhotoDetailInteract;
    private Activity mActivity;
    private int mRequestType = -1;

    @Inject
    public PhotoDetailPresenterImpl(PhotoDetailInteractImpl photoDetailInteract, Activity activity) {
        mPhotoDetailInteract = photoDetailInteract;
        mActivity = activity;
    }

    @Override
    public void success(Uri imageUri) {
        super.success(imageUri);
        switch (mRequestType) {
            case PhotoRequestType.TYPE_SHARE:
                share(imageUri);
                break;
            case PhotoRequestType.TYPE_SAVE:
                showSavePathImg(imageUri);
                break;
            case PhotoRequestType.TYPE_SET_WALLPAPER:
                setWallpaper(imageUri);
                break;
        }
    }

    private void share(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        mActivity.startActivity(Intent.createChooser(intent, App.getAppContext().getString(R.string.share)));
    }

    private void showSavePathImg(Uri imageUri) {
        mView.showMsg(mActivity.getString(R.string.picture_already_save_to, imageUri.getPath()));
    }

    private void setWallpaper(Uri imageUri) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File wallpaperFile = new File(imageUri.getPath());
            Uri contentURI = getImageContentUri(mActivity, wallpaperFile.getAbsolutePath());
            mActivity.startActivity(wallpaperManager.getCropAndSetWallpaperIntent(contentURI));
        } else {
            try {
                wallpaperManager.setStream(mActivity.getContentResolver().openInputStream(imageUri));
                mView.showMsg(App.getAppContext().getString(R.string.set_wallpaper_success));
            } catch (IOException e) {
                KLog.e(e.toString());
                mView.showMsg(e.getMessage());
            }
        }
    }

    // http://stackoverflow.com/questions/23207604/get-a-content-uri-from-a-file-uri
    private Uri getImageContentUri(Context context, String absPath) {
        KLog.d("getImageContentUri: " + absPath);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + " = ?"
                , new String[]{absPath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));
        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }


    @Override
    public void handlePicture(String imageUrl, @PhotoRequestType.PhotoRequestTypeChecker int type) {
        mRequestType = type;
        mPhotoDetailInteract.saveImageAndGetImageUri(this, imageUrl);
    }
}
