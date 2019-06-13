package com.cgy.colorfulnews.module.news.interactor.impl;

import com.cgy.colorfulnews.common.ApiConstants;
import com.cgy.colorfulnews.common.HostType;
import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.news.interactor.NewsListInteract;
import com.cgy.colorfulnews.repository.net.RetrofitManager;
import com.cgy.colorfulnews.utils.MyUtils;
import com.cgy.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:27
 */
public class NewsListInteractImpl implements NewsListInteract<List<NewsSummary>> {

    @Inject
    public NewsListInteractImpl() {
    }

    @Override
    public Subscription loadNews(RequestCallback<List<NewsSummary>> listener, String type, String id, int startPage) {
        // mIsNetError = false;
        // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
        // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
        // 加一句unsubscribeOn(io)
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewsListObservable(type, id, startPage)
                .flatMap((Func1<Map<String, List<NewsSummary>>, Observable<NewsSummary>>) map -> {
                    if (id.endsWith(ApiConstants.HOUSE_ID)) {
                        //房产实际上针对地区的它的id与返回key不同
                        return Observable.from(map.get("北京"));
                    }
                    return Observable.from(map.get(id));
                })
                .map(newsSummary -> {
                    String pTime = MyUtils.formatDate(newsSummary.getPtime());
                    newsSummary.setPtime(pTime);
                    return newsSummary;
                })
                .distinct()
                .toSortedList((newsSummary, newsSummary2) -> newsSummary2.getPtime().compareTo(newsSummary.getPtime())).compose(TransformUtils.defaultSchedulers())
                .subscribe(new Subscriber<List<NewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<NewsSummary> newsSummaries) {
                        KLog.d();
                        listener.success(newsSummaries);
                    }
                });
    }
}
