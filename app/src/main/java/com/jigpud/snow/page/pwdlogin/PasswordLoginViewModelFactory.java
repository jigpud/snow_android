package com.jigpud.snow.page.pwdlogin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author jigpud
 */
public class PasswordLoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private PasswordLoginViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        TokenDao tokenDao = database.tokenDao();
        UserDao userDao = database.userDao();
        UserRepository userRepository = UserRepositoryImpl.getInstance(tokenDao, userDao);
        return (T) new PasswordLoginViewModel(userRepository);
    }

    public static PasswordLoginViewModelFactory create() {
        return new PasswordLoginViewModelFactory();
    }
}
