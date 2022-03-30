package com.jigpud.snow.page.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.database.dao.TokenDao;
import com.jigpud.snow.database.dao.UserDao;
import com.jigpud.snow.repository.user.UserRepository;
import com.jigpud.snow.repository.user.UserRepositoryImpl;
import com.jigpud.snow.repository.vc.VerificationCodeRepository;
import com.jigpud.snow.repository.vc.VerificationCodeRepositoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author jigpud
 */
public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private RegisterViewModelFactory() {}

    @SuppressWarnings("unchecked")
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        SnowDatabase database = SnowDatabase.getSnowDatabase(SnowApplication.getAppContext());
        UserDao userDao = database.userDao();
        TokenDao tokenDao = database.tokenDao();
        UserRepository userRepository = UserRepositoryImpl.getInstance(tokenDao, userDao);
        VerificationCodeRepository verificationCodeRepository = VerificationCodeRepositoryImpl.getInstance();
        return (T) new RegisterViewModel(userRepository, verificationCodeRepository);
    }

    public static RegisterViewModelFactory create() {
        return new RegisterViewModelFactory();
    }
}