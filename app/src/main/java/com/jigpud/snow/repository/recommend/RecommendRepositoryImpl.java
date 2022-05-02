package com.jigpud.snow.repository.recommend;

import androidx.lifecycle.LiveData;
import com.jigpud.snow.bean.ApiResponse;
import com.jigpud.snow.bean.AttractionResponse;
import com.jigpud.snow.bean.PageData;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.database.dao.AttractionDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.http.RecommendService;
import com.jigpud.snow.repository.base.BaseRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class RecommendRepositoryImpl extends BaseRepository implements RecommendRepository {
    private static volatile RecommendRepositoryImpl instance;

    private final RecommendService recommendService;
    private final AttractionDao attractionDao;
    private final UserDao userDao;

    private RecommendRepositoryImpl(
            RecommendService recommendService,
            AttractionDao attractionDao,
            UserDao userDao
    ) {
        this.recommendService = recommendService;
        this.attractionDao = attractionDao;
        this.userDao = userDao;
    }

    @Override
    public LiveData<List<AttractionEntity>> getHotAttractionListFromLocal() {
        return null;
    }

    @Override
    public Observable<List<AttractionEntity>> getHotAttractionList() {
        return withIO(recommendService.getHotAttractionList())
                .map(this::handleAttractionListResponse);
    }

    @Override
    public LiveData<List<AttractionEntity>> getRecommendAttractionListFromLocal(long pageSize, long currentPage) {
        return null;
    }

    @Override
    public Observable<List<AttractionEntity>> getRecommendAttractionList(long pageSize, long currentPage) {
        return withIO(recommendService.getRecommendAttractionList(pageSize, currentPage))
                .map(this::handleAttractionPageDataResponse);
    }

    @Override
    public LiveData<List<UserEntity>> getRecommendUserListFromLocal(long pageSize, long currentPage) {
        return null;
    }

    @Override
    public Observable<List<UserEntity>> getRecommendUserList(long pageSize, long currentPage) {
        return withIO(recommendService.getRecommendUserList(pageSize, currentPage))
                .map(this::handleUserListResponse);
    }

    private List<AttractionEntity> handleAttractionPageDataResponse(ApiResponse<PageData<AttractionResponse>> apiResponse) {
        List<AttractionEntity> attractionEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (AttractionResponse attractionResponse : apiResponse.getData().getRecords()) {
                AttractionEntity attractionEntity = AttractionEntity.create(attractionResponse);
                attractionEntityList.add(attractionEntity);
            }
            attractionDao.insertAll(attractionEntityList);
        }
        return attractionEntityList;
    }

    private List<AttractionEntity> handleAttractionListResponse(ApiResponse<List<AttractionResponse>> apiResponse) {
        List<AttractionEntity> attractionEntityList = new ArrayList<>();
        if (apiResponse.isSuccess()) {
            for (AttractionResponse attractionResponse : apiResponse.getData()) {
                AttractionEntity attractionEntity = AttractionEntity.create(attractionResponse);
                attractionEntityList.add(attractionEntity);
            }
            attractionDao.insertAll(attractionEntityList);
        }
        return attractionEntityList;
    }

    private List<UserEntity> handleUserListResponse(ApiResponse<PageData<UserInformationResponse>> apiResponse) {
        List<UserEntity> userEntityList = new ArrayList<>();
        if (apiResponse.isSuccess() && apiResponse.getData().getRecords() != null) {
            for (UserInformationResponse userInformationResponse : apiResponse.getData().getRecords()) {
                UserEntity userEntity = UserEntity.create(userInformationResponse);
                userEntityList.add(userEntity);
            }
            userDao.insertAll(userEntityList);
        }
        return userEntityList;
    }

    public static RecommendRepositoryImpl getInstance(
            RecommendService recommendService,
            AttractionDao attractionDao,
            UserDao userDao
    ) {
        if (instance == null) {
            synchronized (RecommendRepositoryImpl.class) {
                if (instance == null) {
                    instance = new RecommendRepositoryImpl(recommendService, attractionDao, userDao);
                }
            }
        }
        return instance;
    }
}
