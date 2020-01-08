package com.moaPlatform.moa.side_menu.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.intro.IntroActivity;
import com.moaPlatform.moa.intro.db.IntroCheckModel;
import com.moaPlatform.moa.side_menu.profile.model.ResUserProfile;
import com.moaPlatform.moa.side_menu.profile.model.UserProfile;
import com.moaPlatform.moa.util.ImageResizeUtils;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.db.RealmController;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.util.ImageResizeUtils.exifOrientationToDegrees;

/**
 * 프로필 수정하기 화면
 */
public class ProfileChangeActivity extends AppCompatActivity implements View.OnClickListener, ClassConnectInterface {
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    EditText inputMassage, inputNick;
    int exifDegree;
    File sourceFile;
    private ProfileChangeDialog profileChangeDialog;        //프로필 사진 변경하기 Dialog
    private ImageView iv_UserPhoto;
    private String imageFilePath;
    private Boolean isCamera = false;
    private Boolean isPermission = true;
    private File tempFile;
    private ScrollView svTop;
    private ConstraintLayout clBottom;
    private CheckBox cbOutCheck;
    private Boolean blPage = true;
    private TextView saveProfile, tvTitleName;
    private Button btnExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_profile);

        checkPermission();
        initView();
        initData();
    }

    private void initView() {
        iv_UserPhoto = findViewById(R.id.user_image);
        inputNick = findViewById(R.id.textnick);
        inputMassage = findViewById(R.id.textintr);

        svTop = findViewById(R.id.svmodifyview);
        clBottom = findViewById(R.id.checkexpireid);
        cbOutCheck = findViewById(R.id.memberout_checkbox);
        cbOutCheck.setOnClickListener(this);

        svTop.setVisibility(View.VISIBLE);
        clBottom.setVisibility(View.GONE);
        tvTitleName = findViewById(R.id.toolbarTitleName);

        inputNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    inputNick.setText(s.toString().replace(" ", ""));
                    inputNick.setSelection(inputNick.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 로그아웃
        TextView logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(this);
        // 뒤로가기
        RelativeLayout btnBack = findViewById(R.id.prBtnBack);
        btnBack.setOnClickListener(v -> finish());
        // 프로필 저장
        saveProfile = findViewById(R.id.profilesave);
        saveProfile.setOnClickListener(v -> sendData());

        TextView inputCount = findViewById(R.id.tvInputCount);
        inputMassage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputCount.setText(String.format(getResources().getString(R.string.iptStrSize), s.length()));
            }
        });

        TextView tvExpireId = findViewById(R.id.expireid);
        tvExpireId.setOnClickListener(this);

        btnExit = findViewById(R.id.btnMemberOut);
        btnExit.setOnClickListener(this);

//        Intent intent = new Intent(ProfileChangeActivity.this, MemberOutNotice.class);
//        tvExpireId.setOnClickListener(v -> finishDialog());
    }

    private void initData() {
        CommonModel commonModel = new CommonModel();

        RetrofitClient.getInstance().getMoaService().getProfile(commonModel).enqueue(new Callback<ResUserProfile>() {
            @Override
            public void onResponse(@NonNull Call<ResUserProfile> call, @NonNull Response<ResUserProfile> response) {
                ResUserProfile resUserProfile = response.body();
                if (resUserProfile == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resUserProfile)) {
                    initData();
                    return;
                }
                setData(resUserProfile.UserProfile);
            }

            @Override
            public void onFailure(@NonNull Call<ResUserProfile> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(ProfileChangeActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(UserProfile rUser) {
        if (rUser.imgUrl == null) {
            iv_UserPhoto.setBackground(getResources().getDrawable(R.drawable.profile_1));
        } else {
            glideImages(iv_UserPhoto, rUser.imgUrl);
        }
        inputNick.setText(rUser.nick);
        inputMassage.setText(rUser.intro);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:

                Drawable temp = iv_UserPhoto.getDrawable();
                Bitmap tmpBitmap = ((BitmapDrawable) temp).getBitmap();
                Drawable defaultDrawable = getResources().getDrawable(R.drawable.profile_1);
                Bitmap defaultBitmap = ((BitmapDrawable) defaultDrawable).getBitmap();

                boolean isPhoto;
                if (tmpBitmap.equals(defaultBitmap)) {
                    isPhoto = false;
                } else {
                    isPhoto = true;
                }
                Logger.d("is photo>>> " + isPhoto);

                profileChangeDialog = new ProfileChangeDialog(this, this);
                profileChangeDialog.setPhoto(isPhoto);
                profileChangeDialog.show();
                break;

            case R.id.logout:
                YesOrNoDialog logOutDialog = new YesOrNoDialog();
                logOutDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_logout));
                logOutDialog.show(getSupportFragmentManager(), "logOutDialog");
                logOutDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        logOutDialog.dismiss();
                    }

                    @Override
                    public void onDialogYes() {
                        logOutDialog.dismiss();
                        logout();
                    }
                });
                break;
            case R.id.expireid:
                svTop.setVisibility(View.GONE);
                clBottom.setVisibility(View.VISIBLE);
                saveProfile.setVisibility(View.GONE);
                tvTitleName.setText(getString(R.string.memberout_title));
                break;

            case R.id.btnMemberOut:
                if (cbOutCheck.isChecked()) {
                    finishDialog();
                }
//                else {
//                    Toast.makeText(this, getString(R.string.member_check_button), Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.memberout_checkbox:
                if (cbOutCheck.isChecked()) {
                    btnExit.setBackgroundColor(getResources().getColor(R.color.coral));
                } else {
                    btnExit.setBackgroundColor(getResources().getColor(R.color.grayb1));
                }
                break;
        }
    }

    private void logout() {
        savePref();
        UserUseHelper userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);
        userUseHelper.userLogout();
    }

    private void sendData() {
        if (imageFilePath != null) {
            sourceFile = new File(imageFilePath);
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("userId", MoaAuthHelper.getInstance().getBasePrimaryInfo());
        builder.addFormDataPart("nick", inputNick.getText().toString().trim());
        builder.addFormDataPart("intro", inputMassage.getText().toString().trim());

        Drawable temp = iv_UserPhoto.getDrawable();
        Drawable temp1 = ProfileChangeActivity.this.getResources().getDrawable(R.drawable.profile_1);

        Bitmap tmpBitmap = ((BitmapDrawable) temp).getBitmap();
        Bitmap tmpBitmap1 = ((BitmapDrawable) temp1).getBitmap();

        if (tmpBitmap.equals(tmpBitmap1)) {
            builder.addFormDataPart("imageDeleteYn", "Y");
        } else {
            builder.addFormDataPart("imageDeleteYn", "N");
        }

        if (sourceFile != null) {
            builder.addFormDataPart("file", sourceFile.getName(), RequestBody.create(MediaType.parse("image/*"), sourceFile));
        }
        MultipartBody requestBody = builder.build();
        RetrofitClient.getInstance().getMoaService().setProfile(requestBody).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    sendData();
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Toast.makeText(ProfileChangeActivity.this, getString(R.string.common_toast_msg_success_profile_change), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileChangeActivity.this, commonModel.resultMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(ProfileChangeActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void closeEvent() {
        profileChangeDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, getString(R.string.common_toast_msg_canceled), Toast.LENGTH_SHORT).show();
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
                imageFilePath = tempFile.getAbsolutePath();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFilePath);
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

    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    protected void takeAlbum() {
        if (isPermission) goToAlbum();
        else
            Toast.makeText(this, getString(R.string.msg_permission_need_for_save_file), Toast.LENGTH_SHORT).show();
    }

    protected void takePhoto() {
        if (isPermission) goToCamera();
        else
            Toast.makeText(this, getString(R.string.msg_permission_need_for_save_file), Toast.LENGTH_SHORT).show();
    }

    public void goToCamera() {
        isCamera = true;
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
                photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", tempFile);
            } else {
                photoUri = Uri.fromFile(tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "moaPlanet_" + timeStamp + "_";

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/moaPlanet/");
        if (!storageDir.exists()) {
            boolean isMkDir = storageDir.mkdirs();
            if (!isMkDir)
                Logger.d("Directory creation failed");
        }

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        Bitmap tempBitmap;
        if (isCamera) {
            tempBitmap = ImageResizeUtils.rotate(originalBm, exifDegree);
        } else {
            tempBitmap = originalBm;
        }

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(tempBitmap)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_UserPhoto);

        tempFile = null;
    }

    protected void emptyImage() {
        iv_UserPhoto.setImageBitmap(null);
        iv_UserPhoto.setImageResource(R.drawable.profile_1);
    }

    private void glideImages(ImageView imageview, String subUrl) {
        try {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterMember() {
        UserUseHelper userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);
        userUseHelper.unregisterMember(MoaAuthHelper.getInstance().getCurrentMemberID());
        userUseHelper.setClassConnectInterface(type -> {
            if (!isFinishing()) {
                switch (type) {
                    //회원탈퇴 완료가 된이후에 사용자 정보를 삭제하고 앱을 종료하도록 한다.
                    case MEMBERSHIP_WITHDRAWAL_SUCCESS:
                        Realm.init(this);
                        RealmController realmController = new RealmController();
                        Realm realm = realmController.realmInstance();

                        realm.executeTransactionAsync(realm1 -> {
                            IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
                            if (introCheckModel != null)
                                introCheckModel.type = CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode();
                        }, () -> {
                            realm.close();
                            MoaAuthHelper.getInstance().removeControlInfo();
                            MoaAuthHelper.getInstance().setAutoLoginInfo(null);

                            Handler delayHandler = new Handler();
                            delayHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ActivityCompat.finishAffinity(ProfileChangeActivity.this);
                                }
                            }, 1000);
                        });
                        break;
                }
            }
        });
    }

    private void finishDialog() {
        YesOrNoDialog cardDeleteYesOrNoDialog = new YesOrNoDialog();
        cardDeleteYesOrNoDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_expire));
        cardDeleteYesOrNoDialog.show(getSupportFragmentManager(), "expireYesOrNoDialog");
        cardDeleteYesOrNoDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                cardDeleteYesOrNoDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                unregisterMember();
                cardDeleteYesOrNoDialog.dismiss();
            }
        });
    }

    private void savePref() {
        SharedPreferences sf = getSharedPreferences("haveiduser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("memid", MoaAuthHelper.getInstance().getBasePrimaryInfo());
        editor.commit();
    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {

        Logger.d("onActType >>>>>>>>>>>>> " + type);

        if (type == CodeTypeManager.ClassCodeManager.USER_LOGOUT) {
            //추후 원복 대비 일부러 남김.
//            Realm.init(this);
//            RealmController realmController = new RealmController();
//            Realm realm = realmController.realmInstance();
//
//            realm.executeTransactionAsync(realm1 -> {
//                IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
//                if (introCheckModel != null)
//                    introCheckModel.type = CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode();
//            }, () -> {
//                realm.close();
//                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            });


            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}


