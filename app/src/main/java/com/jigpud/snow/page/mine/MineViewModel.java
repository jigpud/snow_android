package com.jigpud.snow.page.mine;

import androidx.lifecycle.LiveData;
import com.jigpud.snow.base.BaseViewModel;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.repository.user.UserRepository;

/**
 * @author jigpud
 */
public class MineViewModel extends BaseViewModel {
    private final UserRepository userRepository;

    public MineViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<UserEntity> getMyProfile() {
        return userRepository.getSelfInfo();
    }
}
