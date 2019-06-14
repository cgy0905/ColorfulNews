package com.cgy.colorfulnews.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.HostType;
import com.cgy.colorfulnews.repository.net.RetrofitManager;
import com.cgy.colorfulnews.utils.MyUtils;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 10:11
 */
public class URLImageGetter implements Html.ImageGetter {
    private TextView mTextView;
    private int mPicWidth;
    private String mNewsBody;
    private int mPicCount;
    private int mPicTotal;
    private static final String mFilePath = App.getAppContext().getCacheDir().getAbsolutePath();
    public Subscription mSubscription;

    public URLImageGetter(TextView textView, String newsBody, int picTotal) {
        mTextView = textView;
        mPicWidth = mTextView.getWidth();
        mNewsBody = newsBody;
        mPicTotal = picTotal;
    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable;
        File file = new File(mFilePath, source.hashCode() + "");
        if (file.exists()) {
            mPicCount++;
            drawable = getDrawableFromDisk(file);
        } else {
            drawable = getDrawableFromNet(source);
        }
        return drawable;
    }

    @Nullable
    private Drawable getDrawableFromDisk(File file) {
        Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
        if (drawable != null) {
            int picHeight = calculatePicHeight(drawable);
            drawable.setBounds(0, 0, mPicWidth, picHeight);
        }
        return null;
    }

    private int calculatePicHeight(Drawable drawable) {
        float imgWidth = drawable.getIntrinsicWidth();
        float imgHeight = drawable.getIntrinsicHeight();
        float rate = imgHeight / imgWidth;
        return (int) (mPicWidth * rate);
    }

    @Nullable
    private Drawable getDrawableFromNet(String source) {
        mSubscription = RetrofitManager.getInstance(HostType.NEWS_DETAIL_HTML_PHOTO)
                .getNewsBodyHtmlPhoto(source)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> writePicToDisk(response, source))
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        KLog.i();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                    }

                    @Override
                    public void onNext(Boolean isLoadSuccess) {
                        KLog.i();
                        mPicCount++;
                        if (isLoadSuccess && (mPicCount == mPicTotal - 1)) {
                            mTextView.setText(Html.fromHtml(mNewsBody, URLImageGetter.this, null));
                        }
                    }
                });
        return createPicPlaceholder();
    }

    @Nullable
    private Boolean writePicToDisk(ResponseBody response, String source) {
        File file = new File(mFilePath, source.hashCode() + "");
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            in = response.byteStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            KLog.e(e.toString());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                KLog.e();
            }
        }
    }

    @NonNull
    private Drawable createPicPlaceholder() {
        Drawable drawable;
        int color = MyUtils.getColor(R.color.image_place_holder, R.color.image_place_holder_night);
        drawable = new ColorDrawable(App.getAppContext().getResources().getColor(color));
        drawable.setBounds(0, 0, mPicWidth, mPicWidth / 3);
        return drawable;
    }
}
