package com.jigpud.snow.page.newstory;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.img.ImageRepository;
import com.jigpud.snow.repository.story.StoryRepository;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class NewStoryViewModel extends BaseViewModel {
    private final StoryRepository storyRepository;
    private final ImageRepository imageRepository;

    NewStoryViewModel(StoryRepository storyRepository, ImageRepository imageRepository) {
        this.storyRepository = storyRepository;
        this.imageRepository = imageRepository;
    }

    public LiveData<Pair<Boolean, String>> postStory(String title, String content, List<String> pictureList, String attractionId) {
        MutableLiveData<Pair<Boolean, String>> postStoryStatusLiveData = new MutableLiveData<>();

        Disposable disposable = Observable.fromIterable(pictureList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(imageRepository::uploadStoryImage)
                .collectInto(new ArrayList<String>(), (list, url) -> {
                    if (url != null && !url.isEmpty()) {
                        list.add(url);
                    }
                })
                .map(urlList -> {
                    lifecycle(storyRepository.release(title, content, urlList, attractionId)
                            .observeOn(Schedulers.io())
                            .doOnError(throwable -> postStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                            .subscribe(postStoryStatusLiveData::postValue));
                    return urlList;
                })
                .doOnError(throwable -> postStoryStatusLiveData.postValue(new Pair<>(false, "出错啦！")))
                .subscribe();
        lifecycle(disposable);
        return postStoryStatusLiveData;
    }
}
