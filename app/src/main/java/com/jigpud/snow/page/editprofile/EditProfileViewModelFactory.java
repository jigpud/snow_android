package com.jigpud.snow.page.editprofile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.http.QiniuService;
import com.jigpud.snow.http.UserService;
import com.jigpud.snow.repository.img.ImageRepository;
import com.jigpud.snow.repository.img.ImageRepositoryImpl;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import com.jigpud.snow.util.network.ApiGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class EditProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private EditProfileViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        UserDao userDao = database.userDao();
        TokenDao tokenDao = database.tokenDao();

        UserService userService = ApiGenerator.create(UserService.class);
        QiniuService qiniuService = ApiGenerator.create(QiniuService.class);

        UserRepository userRepository = UserRepositoryImpl.getInstance(userService, tokenDao, userDao);
        ImageRepository imageRepository = ImageRepositoryImpl.getInstance(qiniuService);
        return (T) new EditProfileViewModel(userRepository, imageRepository);
    }

    public static EditProfileViewModelFactory create() {
        return new EditProfileViewModelFactory();
    }
}
