package com.moaPlatform.moa.side_menu.customercenter.myquestion.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.util.ImageResizeUtils.exifOrientationToDegrees;
import static com.moaPlatform.moa.util.ImageResizeUtils.rotate;

/**
 * 1:1 문의 하기 화면
 */
public class OneOnOneQuestionMainActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private CommonTitleView commonTitleView;
    EditText eEmail, eName, ePhone, eTitle, eContent, ePassword;
    private CommonLoadingView viewQuestionOneAndOneLoading;
    Spinner sQuestionType;
    RelativeLayout pRelativePic1, pRelativePic2, pRelativePic3;
    ImageView iPic, iPic2, iPic3;
    ImageView iDelPhoto, iDelPhoto2, iDelPhoto3;
    TextView delText1, delText2, delText3;
    Button btnQuestion;
    LinearLayout guest_only;
    View nomember_line;
    String[] sTypeList;
    int exifDegree;
    File imageFile1, imageFIle2, imageFile3;

    private Boolean isPermission = true;
    private File tempFile;
    private String imageFilePath1, imageFilePath2, imageFilePath3;
    private Integer type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_customercenter_question_oneonone);

        checkPermission();
        initDefaultData();
        initView();
        initSpinner();
        initAction();
    }

    private void checkPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getString(R.string.msg_guide_permission_approve))
                .setDeniedMessage(getString(R.string.permission_picture))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void initDefaultData(){
        sTypeList = getResources().getStringArray(R.array.oneOnOneQuestionTypes);
    }

    private void initView() {
        eEmail = findViewById(R.id.side_oneonone_email);
        eName = findViewById(R.id.side_oneonone_name);
        ePhone = findViewById(R.id.side_oneonone_phone);
        sQuestionType = findViewById(R.id.side_oneonone_spinner);
        eTitle = findViewById(R.id.side_oneonone_title);
        eContent = findViewById(R.id.side_oneonone_content);


        pRelativePic1 = findViewById(R.id.side_relative_img1);
        pRelativePic2 = findViewById(R.id.side_relative_img2);
        pRelativePic3 = findViewById(R.id.side_relative_img3);

        iPic = findViewById(R.id.side_oneonone_pic);
        iPic2 = findViewById(R.id.side_oneonone_pic2);
        iPic3 = findViewById(R.id.side_oneonone_pic3);

        delText1 = findViewById(R.id.side_img_del);
        delText2 = findViewById(R.id.side_img_del2);
        delText3 = findViewById(R.id.side_img_del3);

        iDelPhoto = findViewById(R.id.side_img1);
        iDelPhoto2 = findViewById(R.id.side_img2);
        iDelPhoto3 = findViewById(R.id.side_img3);

        delText1.setVisibility(View.GONE);
        delText2.setVisibility(View.GONE);
        delText3.setVisibility(View.GONE);

        ePassword = findViewById(R.id.side_oneonone_password);
        btnQuestion = findViewById(R.id.ask_question_btn);

        guest_only = findViewById(R.id.guest_linear);
        guest_only.setVisibility(View.GONE);

        commonTitleView = findViewById(R.id.commonTitleMyReview);
        commonTitleView.setBackButtonClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.side_one_one_question));

        viewQuestionOneAndOneLoading = findViewById(R.id.viewQuestionOneAndOneLoading);

        /**
         * 비회원이 아닐시 비밀번호 입력창이 사라지고 그위의 상단 라인도 함꼐 사라짐.
         */
        nomember_line = findViewById(R.id.side_fix_underline);
        nomember_line.setVisibility(View.GONE);

    }

    /**
     * 프로그래스바 숨김
     */
    private void dismissLoadingBar() {
        if(viewQuestionOneAndOneLoading != null){
            viewQuestionOneAndOneLoading.animationStop(this);
        }
    }

    /**
     * 프로그래스바 보여짐
     */
    private void showLoadingBar() {
        if(viewQuestionOneAndOneLoading != null){
            viewQuestionOneAndOneLoading.loadingAnimationPlay(this);
        }
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item, sTypeList);
        sQuestionType.setAdapter(adapter);
        sQuestionType.setSelection(0);
    }

    private void initAction() {
        pRelativePic1.setOnClickListener(v -> {
            //Todo 공통 다이얼로그 변경
            type = 1;
            if (delText1.getVisibility() == View.VISIBLE) {
                delDialog();
            } else {
                showDialog();
            }
        });
        pRelativePic2.setOnClickListener(v -> {
            type = 2;
            if (delText2.getVisibility() == View.VISIBLE) {
                delDialog();
            } else {
                showDialog();
            }
        });
        pRelativePic3.setOnClickListener(v -> {
            type = 3;
            if (delText3.getVisibility() == View.VISIBLE) {
                delDialog();
            } else {
                showDialog();
            }
        });
        btnQuestion.setOnClickListener(v -> {

            if(!ObjectUtil.checkNotNull(eEmail.getText().toString())){
                Toast.makeText(this, getString(R.string.common_toast_msg_please_enter_email), Toast.LENGTH_SHORT).show();
                return;
            }

            //이메일 유효성 체크
            if(!StringUtil.isEmail(eEmail.getText().toString())){
                Toast.makeText(this, getString(R.string.common_toast_msg_email_format_not_available), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!ObjectUtil.checkNotNull(ePhone.getText().toString())){
                Toast.makeText(this, getString(R.string.common_toast_msg_please_enter_phone_number), Toast.LENGTH_SHORT).show();
                return;
            }

            //전화번호 유효성 체크
            if(StringUtil.isPhoneNumber(ePhone.getText().toString()) || StringUtil.isCellPhoneNumber(ePhone.getText().toString())){
            }else{
                Toast.makeText(this, getString(R.string.common_toast_msg_phone_number_format_not_available), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!ObjectUtil.checkNotNull(eTitle.getText().toString())){
                Toast.makeText(this, getString(R.string.common_toast_msg_please_enter_title), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!ObjectUtil.checkNotNull(eContent.getText().toString())){
                Toast.makeText(this, getString(R.string.common_toast_msg_please_enter_content), Toast.LENGTH_SHORT).show();
                return;
            }

            showLoadingBar();
            requestMultipartData();
        });
    }

    private void requestMultipartData() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("userId", MoaAuthHelper.getInstance().getBasePrimaryInfo());
        builder.addFormDataPart("mmbrSepa", "1");
        builder.addFormDataPart("email", eEmail.getText().toString().trim());
        builder.addFormDataPart("nm", eName.getText().toString().trim());
        builder.addFormDataPart("mobiNum", ePhone.getText().toString().trim());
        builder.addFormDataPart("inqryTp", "0" + (sQuestionType.getSelectedItemPosition() + 1));
        builder.addFormDataPart("inqryTitle", eTitle.getText().toString().trim());
        builder.addFormDataPart("inqryCntnt", eContent.getText().toString().trim());
        builder.addFormDataPart("pswd", ePassword.getText().toString().trim());

        try{
            if (ObjectUtil.checkNotNull(imageFilePath1)) {
                imageFile1 = new File(imageFilePath1);
                builder.addFormDataPart("file", imageFile1.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile1));
            }
            if (ObjectUtil.checkNotNull(imageFilePath2)) {
                imageFIle2 = new File(imageFilePath2);
                builder.addFormDataPart("file2", imageFIle2.getName(), RequestBody.create(MediaType.parse("image/*"), imageFIle2));
            }
            if (ObjectUtil.checkNotNull(imageFilePath3)) {
                imageFile3 = new File(imageFilePath3);
                builder.addFormDataPart("file3", imageFile3.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile3));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        MultipartBody requestBody = builder.build();

        // show it
        RetrofitClient.getInstance().getMoaService().uploadImage(requestBody).enqueue(new Callback<CommonModel>() {

            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                dismissLoadingBar();
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    requestMultipartData();
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Toast.makeText(OneOnOneQuestionMainActivity.this, getString(R.string.msg_question_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OneOnOneQuestionMainActivity.this, getString(R.string.msg_question_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                dismissLoadingBar();
                Toast.makeText(OneOnOneQuestionMainActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_file_attach));
        builder.setMessage(getString(R.string.msg_dialog_select_camera_or_gallery));
        builder.setPositiveButton(getString(R.string.title_capture), (dialog, which) -> {
            if (isPermission) goCamera();
            else
                Toast.makeText(OneOnOneQuestionMainActivity.this, getString(R.string.msg_permission_need_for_save_file), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(getString(R.string.title_gallery),
                (dialog, which) -> {
                    if (isPermission) goGallery();
                    else
                        Toast.makeText(OneOnOneQuestionMainActivity.this, getString(R.string.msg_permission_need_for_save_file), Toast.LENGTH_SHORT).show();
                });
        builder.show();
    }

    void delDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.yes_or_no_dialog_title));
        builder.setMessage(getString(R.string.msg_remove_attached_file));
        builder.setPositiveButton(getString(R.string.yes_or_no_dialog_yes),
                (dialog, which) -> {
                    if (type == 1) {
                        iPic.setImageBitmap(null);
                        iDelPhoto.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        delText1.setVisibility(View.GONE);
                    } else if (type == 2) {
                        iPic2.setImageBitmap(null);
                        iDelPhoto2.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        delText2.setVisibility(View.GONE);
                    } else if (type == 3) {
                        iPic3.setImageBitmap(null);
                        iDelPhoto3.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        delText3.setVisibility(View.GONE);
                    }
                });
        builder.setNegativeButton(getString(R.string.yes_or_no_dialog_no),
                null);
        builder.show();
    }

    public void goCamera() // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.msg_retire_because_of_img_err), Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            Uri photoUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(OneOnOneQuestionMainActivity.this, getApplicationContext().getPackageName() + ".fileprovider", tempFile);
            } else {
                photoUri = Uri.fromFile(tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // 이미지 파일 이름 ( moaPlanet_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "moaPlanet_" + timeStamp + "_";
        // 이미지가 저장될 폴더 이름 ( moaPlanet )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/moaPlanet/");
        if (!storageDir.exists()) {
            boolean isMkDir = storageDir.mkdirs();
            if (!isMkDir)
                Logger.d("Directory creation failed");
        }
        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        if (type == 1) {
            imageFilePath1 = image.getAbsolutePath();
        } else if (type == 2) {
            imageFilePath2 = image.getAbsolutePath();
        } else if (type == 3) {
            imageFilePath3 = image.getAbsolutePath();
        }
        Logger.d("createImageFile : " + image.getAbsolutePath());
        return image;
    }

    public void goGallery() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, getString(R.string.common_toast_msg_canceled), Toast.LENGTH_SHORT).show();
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Logger.e(tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            if (type == 1) {
                imageFilePath1 = "";
            } else if (type == 2) {
                imageFilePath2 = "";
            } else if (type == 3) {
                imageFilePath3 = "";
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Logger.d("PICK_FROM_ALBUM photoUri : " + photoUri);
            Cursor cursor = null;
            try {
                /* Uri 스키마를 content:/// 에서 file:/// 로  변경한다. */
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
                Logger.d("tempFile Uri : " + Uri.fromFile(tempFile));
                if (type == 1) {
                    imageFilePath1 = tempFile.getAbsolutePath();
                } else if (type == 2) {
                    imageFilePath2 = tempFile.getAbsolutePath();
                } else if (type == 3) {
                    imageFilePath3 = tempFile.getAbsolutePath();
                }

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            ExifInterface exif = null;
            try {
                if (type == 1) {
                    exif = new ExifInterface(imageFilePath1);
                } else if (type == 2) {
                    exif = new ExifInterface(imageFilePath2);
                } else if (type == 3) {
                    exif = new ExifInterface(imageFilePath3);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            setImage();
        }
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Logger.d("setImage : " + tempFile.getAbsolutePath());
        if (type == 1) {
            iPic.setImageBitmap(rotate(originalBm, exifDegree));
            iDelPhoto.setBackground(getResources().getDrawable(R.drawable.delete_w));
            delText1.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            iPic2.setImageBitmap(rotate(originalBm, exifDegree));
            iDelPhoto2.setBackground(getResources().getDrawable(R.drawable.delete_w));
            delText2.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            iPic3.setImageBitmap(rotate(originalBm, exifDegree));
            iDelPhoto3.setBackground(getResources().getDrawable(R.drawable.delete_w));
            delText3.setVisibility(View.VISIBLE);
        }

        // tempFile 사용 후 null 처리를 해줘야 합니다.
        // (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
        // 기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
        tempFile = null;
    }
}