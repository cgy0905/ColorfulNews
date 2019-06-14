package com.cgy.colorfulnews.module.news.interactor.impl;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.common.HostType;
import com.cgy.colorfulnews.entity.NewsDetail;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.news.interactor.NewsDetailInteract;
import com.cgy.colorfulnews.repository.net.RetrofitManager;
import com.cgy.colorfulnews.utils.MyUtils;
import com.cgy.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 18:24
 */
public class NewsDetailInteractImpl implements NewsDetailInteract<NewsDetail> {

    @Inject
    public NewsDetailInteractImpl() {}


    @Override
    public Subscription loadNewsDetail(RequestCallback<NewsDetail> callback, String postId) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewsDetailObservable(postId)
                .map(map -> {
                    KLog.d(Thread.currentThread().getName());

                    NewsDetail newsDetail = map.get(postId);
                    changeNewsDetail(newsDetail);
                    return newsDetail;
                })
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(new Observer<NewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callback.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        callback.success(newsDetail);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgBeans = newsDetail.getImg();
        if (isChange(imgBeans)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgBeans, newsBody);
            newsDetail.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetail.ImgBean> imgBeans) {
        return imgBeans != null && imgBeans.size() >= 2 && App.isHavePhoto();
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgBeans, String newsBody) {
        for (int i = 0; i < imgBeans.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgBeans.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);
        }
        return newsBody;
    }
}
