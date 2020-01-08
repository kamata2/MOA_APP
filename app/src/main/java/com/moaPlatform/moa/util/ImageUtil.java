package com.moaPlatform.moa.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    public static File createImageFile() throws IOException {
        // 이미지 파일 이름 ( moaPlanet_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "moaPlanet_" + timeStamp + "_";
        // 이미지가 저장될 폴더 이름 ( moaPlanet )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/moaPlanet/");
        if (!storageDir.exists()) storageDir.mkdirs();
        // 파일 생성
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        Logger.d("createImageFile : " + file.getAbsolutePath());
        return file;
    }
}
