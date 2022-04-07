package com.jigpud.snow.page.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.jigpud.snow.bean.StoryListResponse;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.page.base.BaseViewModel;
import com.jigpud.snow.repository.story.StoryRepository;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.util.logger.Logger;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jigpud
 */
public class MineViewModel extends BaseViewModel {
    private static final String TAG = "MineViewModel";

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final long pageSize = 10;
    private long currentPage = 1;

    public MineViewModel(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    public LiveData<UserEntity> getMyProfile() {
        return userRepository.getSelfInfo();
    }

    public LiveData<List<StoryListResponse>> refreshMyStoryList() {
        Logger.d(TAG, "refresh my story list");
        currentPage = 1;
        return getMyStoryList(pageSize, currentPage);
    }

    public LiveData<List<StoryListResponse>> loadMoreStoryList() {
        long loadPage = currentPage + 1;
        Logger.d(TAG, "load page %d", loadPage);
        return Transformations.map(getMyStoryList(pageSize, loadPage), storyList -> {
           if (storyList != null && !storyList.isEmpty()) {
               currentPage++;
           }
           return storyList;
        });
    }

    public LiveData<List<StoryListResponse>> getMyStoryList(long pageCount, long page) {
        MutableLiveData<List<StoryListResponse>> myStoryListLiveData = new MutableLiveData<>();
        Disposable disposable = storyRepository.getMyStoryList(pageCount, page)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> myStoryListLiveData.postValue(new ArrayList<>()))
                .subscribe(myStoryListLiveData::postValue);
        lifecycle(disposable);
        return myStoryListLiveData;
    }

    public LiveData<List<StoryListResponse>> getUserStoryList(String userid, long pageCount, long page) {
        MutableLiveData<List<StoryListResponse>> userStoryList = new MutableLiveData<>();
        Disposable disposable = storyRepository.getUserStoryList(userid, pageCount, page)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> userStoryList.postValue(new ArrayList<>()))
                .subscribe(userStoryList::postValue);
        lifecycle(disposable);
        return userStoryList;
    }
}
