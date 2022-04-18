package com.jigpud.snow.database.dao;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.jigpud.snow.database.SnowDatabase;
import com.jigpud.snow.util.logger.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author : jigpud
 */
@RunWith(JUnit4.class)
public class StoryDaoTest {
    private static final String TAG = "StoryDaoTest";

    @Test
    public void test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        StoryDao storyDao = SnowDatabase.getSnowDatabase(appContext).storyDao();
        Logger.d(TAG, storyDao.getStoryList().toString());
    }
}
