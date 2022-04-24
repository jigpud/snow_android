package com.jigpud.snow.util.img;

import android.content.Context;
import android.net.Uri;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

import java.io.File;
import java.util.ArrayList;

/**
 * @author : jigpud
 */
public class LubanCompressEngine implements CompressFileEngine {
    private static volatile LubanCompressEngine instance;

    private LubanCompressEngine() {}

    @Override
    public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
        Luban.with(context)
                .load(source)
                .ignoreBy(100)
                .setCompressListener(new OnNewCompressListener() {
                    @Override
                    public void onStart() {}

                    @Override
                    public void onSuccess(String source, File compressFile) {
                        if (call != null) {
                            call.onCallback(source, compressFile.getAbsolutePath());
                        }
                    }

                    @Override
                    public void onError(String source, Throwable e) {
                        if (call != null) {
                            call.onCallback(source, null);
                        }
                    }
                })
                .launch();
    }

    public static LubanCompressEngine createCompressEngin() {
        if (instance == null) {
            synchronized (LubanCompressEngine.class) {
                if (instance == null) {
                    instance = new LubanCompressEngine();
                }
            }
        }
        return instance;
    }
}
