package com.cgy.colorfulnews.module.photo.interactor.impl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.photo.interactor.PhotoDetailInteract;
import com.cgy.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/17 11:23
 */
public class PhotoDetailInteractImpl implements PhotoDetailInteract<Uri> {

    @Inject
    public PhotoDetailInteractImpl() {}

    @Override
    public Subscription saveImageAndGetImageUri(RequestCallback<Uri> listener, String url) {
        return Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {
            KLog.d(Thread.currentThread().getName());

            Bitmap bitmap = null;
            try {
                bitmap = Picasso.with(App.getAppContext())
                        .load(url)
                        .get();
            } catch (IOException e) {
                subscriber.onError(e);
            }
            if (bitmap == null) {
                subscriber.onError(new Exception("下载图片失败"));
            }
            subscriber.onNext(bitmap);
            subscriber.onCompleted();
        })
//                .observeOn(Schedulers.io())
                .flatMap((Func1<Bitmap, Observable<Uri>>) bitmap -> {
                    KLog.d(Thread.currentThread().getName());

                    return getUriObservable(bitmap, url);
                })
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(App.getAppContext().getString(R.string.error_try_again));
                    }

                    @Override
                    public void onNext(Uri uri) {
                        listener.success(uri);
                    }
                });
    }

    @NonNull
    private Observable<Uri> getUriObservable(Bitmap bitmap, String url) {
        File file = getImageFile(bitmap, url);
        if (file == null) {
            return Observable.error(new NullPointerException("Save image file failed!"));
        }
        Uri uri = Uri.fromFile(file);
        // 通知图库更新 //Update the System --> MediaStore.Images.Media --> 更新ContentUri
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        App.getAppContext().sendBroadcast(scannerIntent);
        return Observable.just(uri);
    }

    private File getImageFile(Bitmap bitmap, String url) {
        String fileName = "/colorful_news/photo/" + url.hashCode() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory(), fileName); // getFilesDir()等只能在程序内部访问不能用作分享路径
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
