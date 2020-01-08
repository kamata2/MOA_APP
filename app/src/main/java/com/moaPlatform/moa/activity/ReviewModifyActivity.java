package com.moaPlatform.moa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.controller.ReviewController;
import com.moaPlatform.moa.model.res_model.ResReviewModel;
import com.moaPlatform.moa.model.res_model.ReviewFile;
import com.moaPlatform.moa.model.res_model.ReviewHeaderInfoModel;
import com.moaPlatform.moa.util.ImageUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.moaPlatform.moa.util.ImageResizeUtils.exifOrientationToDegrees;
import static com.moaPlatform.moa.util.ImageResizeUtils.rotate;

/**
 * 리뷰 수정하기 화면
 */
public class ReviewModifyActivity extends AppCompatActivity implements RetrofitConnectionResult {

    public final static String EXTRA_USER_REVIEW_ID = "EXTRA_USER_REVIEW_ID";

    public static final String EXTRA_REVIEW_MODIFY_RESULT_MODEL = "EXTRA_REVIEW_MODIFY_RESULT_MODEL";       //수정된 리뷰 객체 모델 전달 Key
    public static final String EXTRA_REVIEW_ADAPTER_POSITION = "EXTRA_REVIEW_ADAPTER_POSITION";
    public static final String EXTRA_REVIEW_TYPE = "EXTRA_REVIEW_TYPE";
    public static final String REVIEW_TYPE_DELRIVERY = "REVIEW_TYPE_DELRIVERY";       //배달 리스트
    public static final String REVIEW_TYPE_EAT_OUT = "REVIEW_TYPE_EAT_OUT";           //외식 리스트

    private CommonTitleView commonTitleReviewWrite;
    private ConstraintLayout constraintSideReviewWriteComplete;
    private TextView tvSideReviewWriteComplete;

    private EditText etContent;
    private RatingBar ratingTastyStar, ratingMountStar, ratingDeliveryStar;
    private String ratingTastyStarText, ratingMountStarText, ratingDeliveryStarText;
    private RelativeLayout rlRelativePic1, rlRelativePic2, rlRelativePic3;
    private ImageView ivPicture1, ivPicture2, ivPicture3;
    private ImageView ivPictureBtn1, ivPictureBtn2, ivPictureBtn3;
    private TextView tvDelText1, tvDelText2, tvDelText3;
    private TextView tvSideReviewWritePleaseEnterStar, tvSideMenuReviewWriteToolTip;
    private TextView tvStoreName, tvSideReviewWriteMenuContent;
    private TextView tvSideReviewWriteMenuTitle;
    private TextView tvSideReviewWriteDeliveryScoreTitle;           //배달&서비스

    private File tempFile;
    private String imageFilePath1, imageFilePath2, imageFilePath3;

    private int storeId, exifDegree;
    private int selectedPicturePosition = -1;

    private String orderNum;
    private String userRevwId, reviewType;          //배달, 외식
    private int reviewAdapterPosition = -1;

    private ReviewController reviewController;
    private ProgressDialog progressDialog;

    private List<ReviewFile> userReviewFileList;
    private boolean[] imageDeleteStatus = {false, false, false};        //첨부된 이미지를 삭제하였는지 체크여부

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.side_review_write);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initDefaultData(savedInstanceState);
                initController();
                initView();
                getReviewInfo();
                initListener();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ReviewModifyActivity.this, getString(R.string.msg_permission_denied), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        TedPermission.with(ReviewModifyActivity.this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getString(R.string.msg_guide_permission_approve))
                .setDeniedMessage(getString(R.string.msg_detail_permission_denied))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Logger.d("TEST >>>>>>>>>> onSaveInstanceState");
        outState.putString(EXTRA_USER_REVIEW_ID, userRevwId);
        outState.putString(EXTRA_REVIEW_TYPE, reviewType);
        outState.putInt(EXTRA_REVIEW_ADAPTER_POSITION, reviewAdapterPosition);
    }

    private void initDefaultData(Bundle savedInstanceState) {

        if(savedInstanceState != null){
            userRevwId = savedInstanceState.getString(EXTRA_USER_REVIEW_ID);
            reviewType = savedInstanceState.getString(EXTRA_REVIEW_TYPE);
            reviewAdapterPosition = savedInstanceState.getInt(EXTRA_REVIEW_ADAPTER_POSITION);
        }else{
            Intent intent = getIntent();
            if (intent.getExtras() != null){
                userRevwId = intent.getStringExtra(EXTRA_USER_REVIEW_ID);
                reviewType = intent.getStringExtra(EXTRA_REVIEW_TYPE);
                reviewAdapterPosition = intent.getIntExtra(EXTRA_REVIEW_ADAPTER_POSITION, -1);
            }
        }
    }

    private void initController(){
        reviewController = new ReviewController(this);
        reviewController.setRetrofitConnectionResult(this);
    }

    private void initView() {

        commonTitleReviewWrite = findViewById(R.id.commonTitleReviewWrite);
        commonTitleReviewWrite.setBackButtonClickListener(v -> finish());
        commonTitleReviewWrite.setTitleName(getString(R.string.common_title_review_modify));

        constraintSideReviewWriteComplete = findViewById(R.id.constraintSideReviewWriteComplete);
        tvSideReviewWriteComplete = findViewById(R.id.tvSideReviewWriteComplete);

        ratingTastyStar = findViewById(R.id.ratingSideReviewWriteTasty);
        ratingMountStar = findViewById(R.id.ratingSideReviewWriteAmount);
        ratingDeliveryStar = findViewById(R.id.ratingSideReviewWriteDelivery);

        etContent = findViewById(R.id.etSideReviewWriteContent);

        rlRelativePic1 = findViewById(R.id.rlSideReviewWriteImage1Container);
        rlRelativePic2 = findViewById(R.id.rlSideReviewWriteImage2Container);
        rlRelativePic3 = findViewById(R.id.rlSideReviewWriteImage3Container);

        ivPicture1 = findViewById(R.id.ivSideReviewWritePicture1);
        ivPicture2 = findViewById(R.id.ivSideReviewWritePicture2);
        ivPicture3 = findViewById(R.id.ivSideReviewWritePicture3);

        tvDelText1 = findViewById(R.id.tvSideReviewWritePicture1Delete);
        tvDelText2 = findViewById(R.id.tvSideReviewWritePicture2Delete);
        tvDelText3 = findViewById(R.id.tvSideReviewWritePicture3Delete);

        ivPictureBtn1 = findViewById(R.id.ivSideReviewWritePicture1CameraIcon);
        ivPictureBtn2 = findViewById(R.id.ivSideReviewWritePicture2CameraIcon);
        ivPictureBtn3 = findViewById(R.id.ivSideReviewWritePicture3CameraIcon);

        tvStoreName = findViewById(R.id.tvSideReviewWriteStoreName);
        tvSideReviewWriteMenuTitle = findViewById(R.id.tvSideReviewWriteMenuTitle);
        tvSideReviewWriteMenuContent = findViewById(R.id.tvSideReviewWriteMenuContent);
        tvSideReviewWriteDeliveryScoreTitle = findViewById(R.id.tvSideReviewWriteDeliveryScoreTitle);

        tvDelText1.setVisibility(View.GONE);
        tvDelText2.setVisibility(View.GONE);
        tvDelText3.setVisibility(View.GONE);

        tvSideReviewWritePleaseEnterStar = findViewById(R.id.tvSideReviewWritePleaseEnterStar);
        tvSideMenuReviewWriteToolTip = findViewById(R.id.tvSideMenuReviewWriteToolTip);

        tvSideReviewWriteComplete.setText(getString(R.string.common_title_review_modify_complete_button));

        if(reviewType != null){
            if (reviewType.equals(REVIEW_TYPE_DELRIVERY)) {
                tvSideReviewWriteDeliveryScoreTitle.setText(getString(R.string.review_delivery_score));
            } else if(reviewType.equals(REVIEW_TYPE_EAT_OUT)){
                tvSideReviewWriteDeliveryScoreTitle.setText(getString(R.string.review_service_score));
            }
        }
    }

    private void initListener() {
        rlRelativePic1.setOnClickListener(v -> {
            selectedPicturePosition = 1;
            if (tvDelText1.getVisibility() == View.VISIBLE) {
                deletePicture(selectedPicturePosition);
            } else {
                showImageSelectDialog();
            }
        });

        rlRelativePic2.setOnClickListener(v -> {
            selectedPicturePosition = 2;
            if (tvDelText2.getVisibility() == View.VISIBLE) {
                deletePicture(selectedPicturePosition);
            } else {
                showImageSelectDialog();
            }
        });

        rlRelativePic3.setOnClickListener(v -> {
            selectedPicturePosition = 3;
            if (tvDelText3.getVisibility() == View.VISIBLE) {
                deletePicture(selectedPicturePosition);
            } else {
                showImageSelectDialog();
            }
        });

        constraintSideReviewWriteComplete.setOnClickListener(v -> {
            requestReviewModify();
        });

        ratingTastyStar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingTastyStarText = String.valueOf(rating));
        ratingMountStar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingMountStarText = String.valueOf(rating));
        ratingDeliveryStar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingDeliveryStarText = String.valueOf(rating));
    }


    //리뷰 수정 요청
    private void requestReviewModify() {

        Logger.d("rating >>>> ratingTastyStarText " + ratingTastyStarText);
        Logger.d("rating >>>> ratingMountStarText " + ratingMountStarText);
        Logger.d("rating >>>> ratingDeliveryStarText " + ratingDeliveryStarText);

        if (ratingTastyStarText == null || ratingMountStarText == null || ratingDeliveryStarText == null
                || ratingTastyStarText.equals("0.0") || ratingMountStarText.equals("0.0") || ratingDeliveryStarText.equals("0.0")) {
            tvSideReviewWritePleaseEnterStar.setVisibility(View.VISIBLE);
            return;
        } else {
            tvSideReviewWritePleaseEnterStar.setVisibility(View.GONE);
        }
        if (etContent.length() == 0) {
            tvSideMenuReviewWriteToolTip.setVisibility(View.VISIBLE);
            return;
        } else {
            tvSideMenuReviewWriteToolTip.setVisibility(View.GONE);
        }

        showProgressDialog();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (reviewType.equals(REVIEW_TYPE_DELRIVERY)) {
            builder.addFormDataPart(MoaConstants.PARAM_SEPAVALUE, MoaConstants.PARAM_VALUE_DELIVERY);
            builder.addFormDataPart(MoaConstants.PARAM_DELIVERY_SCORE, String.valueOf(ratingDeliveryStarText));

        } else if (reviewType.equals(REVIEW_TYPE_EAT_OUT)) {
            builder.addFormDataPart(MoaConstants.PARAM_SEPAVALUE, MoaConstants.PARAM_VALUE_EATOUT);
            builder.addFormDataPart(MoaConstants.PARAM_SERVICE_SCORE, String.valueOf(ratingDeliveryStarText));
        }

        builder.addFormDataPart(MoaConstants.PARAM_STORE_ID, String.valueOf(storeId));
        builder.addFormDataPart(MoaConstants.PARAM_USER_ID, MoaAuthHelper.getInstance().getBasePrimaryInfo());
        builder.addFormDataPart(MoaConstants.PARAM_USERREVWID, userRevwId);
        builder.addFormDataPart(MoaConstants.PARAM_ORDER_ID, orderNum);
        builder.addFormDataPart(MoaConstants.PARAM_TASTE_SCORE, String.valueOf(ratingTastyStarText));
        builder.addFormDataPart(MoaConstants.PARAM_AMOUNT_SCORE, String.valueOf(ratingMountStarText));
        builder.addFormDataPart(MoaConstants.PARAM_CONTENT, etContent.getText().toString().trim());

        try {
            String[] imageFilePathList = {imageFilePath1, imageFilePath2, imageFilePath3};
            String[] mutipartNameList = {MoaConstants.PARAM_FILE_1, MoaConstants.PARAM_FILE_2, MoaConstants.PARAM_FILE_3};
            String[] mutipartUpdateNameList = {MoaConstants.PARAM_FILE_1_UPDATE, MoaConstants.PARAM_FILE_2_UPDATE, MoaConstants.PARAM_FILE_3_UPDATE};
            List<String> imageFileKeyList = new ArrayList<>();
            imageFileKeyList.clear();

            for (int i = 0; i < userReviewFileList.size(); i++) {
                if (userReviewFileList.get(i).userRevwAttchFileId != null) {
                    imageFileKeyList.add(userReviewFileList.get(i).userRevwAttchFileId);
                }
            }

            for (int i = 0; i < imageFilePathList.length; i++) {
                //파일 키값이 존재하고(기존 업로드된 사진이 있는 경우)
                if (imageFileKeyList.size() > i && ObjectUtil.checkNotNull(imageFileKeyList.get(i))) {
                    if (ObjectUtil.checkNotNull(imageFilePathList[i])) {
                        //imageFilePath가 있으면 새로운 이미지를 올렸다고 판단
                        File imageFile = new File(imageFilePathList[i]);
                        builder.addFormDataPart(mutipartUpdateNameList[i], imageFileKeyList.get(i));       //수정
                        builder.addFormDataPart(mutipartNameList[i], imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));

                        Logger.d("리뷰수정 테스트 >>>> position "+ i + " 기존 이미지를 대체합니다. "+ " 파일 path " + imageFilePathList[i] + " 파일 key값 " + imageFileKeyList.get(i));
                    } else {
                        //삭제한 상태값이 존재하면 삭제처리
                        if(imageDeleteStatus[i]){
                            builder.addFormDataPart(mutipartNameList[i], imageFileKeyList.get(i));
                            Logger.d("리뷰수정 테스트 >>>> position "+ i + " 등록된 이미지를 삭제합니다. ");
                        }else{
                            Logger.d("리뷰수정 테스트 >>>> position "+ i + " 등록된 이미지가 있었고 삭제하거나 변경한 이미지가 없습니다. ");
                        }
                    }
                } else {
                    //기존 업로드된 사진이 없는 경우로 신규 업로드 처리
                    if(ObjectUtil.checkNotNull(imageFilePathList[i])){
                        File imageFile = new File(imageFilePathList[i]);
                        builder.addFormDataPart(mutipartNameList[i], imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
                        Logger.d("리뷰수정 테스트 >>>> position "+ i + " 이미지를 새로 등록합니다. " + imageFilePathList[i]);
                    }else{
                        Logger.d("리뷰수정 테스트 >>>> position "+ i + " 기존 이미지가 없었고 새로 등록한 이미지도 없습니다 ");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reviewController != null) {
            reviewController.postReviewModify(builder.build());
        }
    }

    private void showImageSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.review_modify_activity_add_picture_dialog_title));
        builder.setMessage(getString(R.string.review_modify_activity_add_picture_dialog_content));
        builder.setPositiveButton(getString(R.string.review_modify_activity_add_picture_dialog_btn_camera), (dialog1, which) -> goCamera());
        builder.setNegativeButton(getString(R.string.review_modify_activity_add_picture_dialog_btn_gallery), (dialog1, which) -> goGallery());
        builder.show();
    }

    private void goCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = ImageUtil.createImageFile();

            if (tempFile != null) {

                if (selectedPicturePosition == 1) {
                    imageFilePath1 = tempFile.getAbsolutePath();
                } else if (selectedPicturePosition == 2) {
                    imageFilePath2 = tempFile.getAbsolutePath();
                } else if (selectedPicturePosition == 3) {
                    imageFilePath3 = tempFile.getAbsolutePath();
                }

                Uri photoUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", tempFile);
                } else {
                    photoUri = Uri.fromFile(tempFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, MoaConstants.REQUEST_CAMERA);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //앨범에서 이미지 가져오기
    public void goGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, MoaConstants.REQUEST_GALLELY);
    }

    private void deletePicture(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.one_button_dialog_title));
        builder.setTitle(getString(R.string.review_modify_activity_remove_picture_dialog_content));
        builder.setPositiveButton(getString(R.string.yes_or_no_dialog_yes),
                (dialog1, which) -> {

                    imageDeleteStatus[position - 1] = true;

                    if (position == 1) {
                        imageFilePath1 = null;
                        ivPicture1.setImageBitmap(null);
                        ivPictureBtn1.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        tvDelText1.setVisibility(View.GONE);
                    } else if (position == 2) {
                        imageFilePath2 = null;
                        ivPicture2.setImageBitmap(null);
                        ivPictureBtn2.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        tvDelText2.setVisibility(View.GONE);
                    } else if (position == 3) {
                        imageFilePath3 = null;
                        ivPicture3.setImageBitmap(null);
                        ivPictureBtn3.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                        tvDelText3.setVisibility(View.GONE);
                    }
                });
        builder.setNegativeButton(getString(R.string.yes_or_no_dialog_no),(dialog1, which) -> {});
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            /**
             * 갤러리 or 카메라 로 이동하여 사진 미선택 또는 촬영을 하지않고 뒤로 돌아온경우 빈썸네일을 삭제한다.
             */
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Logger.d(tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        } else {
            if (requestCode == MoaConstants.REQUEST_GALLELY) {
                if (data == null) {
                    return;
                }
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
                    Logger.d("imageFile1 Uri : " + Uri.fromFile(tempFile));

                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                setTempFileToBitmapImage(selectedPicturePosition);

            } else if (requestCode == MoaConstants.REQUEST_CAMERA) {
                ExifInterface exif = null;
                try {
                    if (selectedPicturePosition == 1) {
                        exif = new ExifInterface(imageFilePath1);
                    } else if (selectedPicturePosition == 2) {
                        exif = new ExifInterface(imageFilePath2);
                    } else if (selectedPicturePosition == 3) {
                        exif = new ExifInterface(imageFilePath3);
                    }
                    int exifOrientation;
                    if (exif != null) {
                        exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientationToDegrees(exifOrientation);
                    } else {
                        exifDegree = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setTempFileToBitmapImage(selectedPicturePosition);
            }
        }
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setTempFileToBitmapImage(int selectedPicturePosition) {

        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), new BitmapFactory.Options());
        Logger.d("setTempFileToBitmapImage : " + tempFile.getAbsolutePath());

        imageDeleteStatus[selectedPicturePosition - 1] = false;

        if (selectedPicturePosition == 1) {
            imageFilePath1 = tempFile.getAbsolutePath();

            ivPicture1.setImageBitmap(rotate(originalBm, exifDegree));
            ivPictureBtn1.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvDelText1.setVisibility(View.VISIBLE);

        } else if (selectedPicturePosition == 2) {
            imageFilePath2 = tempFile.getAbsolutePath();

            ivPicture2.setImageBitmap(rotate(originalBm, exifDegree));
            ivPictureBtn2.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvDelText2.setVisibility(View.VISIBLE);

        } else if (selectedPicturePosition == 3) {
            imageFilePath3 = tempFile.getAbsolutePath();

            ivPicture3.setImageBitmap(rotate(originalBm, exifDegree));
            ivPictureBtn3.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvDelText3.setVisibility(View.VISIBLE);
        }

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.common_dialog_msg_review));
        progressDialog.setCancelable(false);
        progressDialog.show();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void dismissProgressDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    /**
     * 리뷰 Data Set
     * @param model
     */
    private void initReviewInfo(ReviewHeaderInfoModel model) {
        if (model == null)
            return;

        //매장명
        tvStoreName.setText(model.storNm);

        if (reviewType.equals(REVIEW_TYPE_DELRIVERY)) {
            if(ObjectUtil.checkNotNull(model.orderedGoodsList)){
                tvSideReviewWriteMenuTitle.setVisibility(View.VISIBLE);
                tvSideReviewWriteMenuContent.setVisibility(View.VISIBLE);

                tvSideReviewWriteMenuContent.setText(model.getOrderedGoodsList());
            }else{
                tvSideReviewWriteMenuTitle.setVisibility(View.GONE);
                tvSideReviewWriteMenuContent.setVisibility(View.GONE);
            }

        } else if(reviewType.equals(REVIEW_TYPE_EAT_OUT)){
            tvSideReviewWriteMenuTitle.setVisibility(View.GONE);
            tvSideReviewWriteMenuContent.setVisibility(View.GONE);
        }

        //별점들
        ratingTastyStar.setRating(Float.parseFloat(model.tastEvalScor));
        ratingMountStar.setRating(Float.parseFloat(model.volumEvalScor));
        ratingDeliveryStar.setRating(Float.parseFloat(model.dlvryEvalScor));

        ratingTastyStarText = model.tastEvalScor;
        ratingMountStarText = model.volumEvalScor;
        ratingDeliveryStarText = model.dlvryEvalScor;

        //작성 내용
        etContent.setText(model.cntnt);

        ImageView[] pictureImageList = {ivPicture1, ivPicture2, ivPicture3};

        ImageView[] pictureImageButtonList = {ivPictureBtn1, ivPictureBtn2, ivPictureBtn3};
        TextView[] pictureDeleteTextViewList = {tvDelText1, tvDelText2, tvDelText3};

        if(model.userRevwFileList != null){
            this.userReviewFileList = model.userRevwFileList;
        }

        //사진이 4개이상 등록된경우가 있기때문에 보완..
        int reviewImageListCount;
        if (model.userRevwFileList.size() > 3) {
            reviewImageListCount = 3;
        }else{
            reviewImageListCount = model.userRevwFileList.size();
        }

        //사진이 있는 항목만 set
        for (int i = 0; i < reviewImageListCount; i++) {

            pictureImageButtonList[i].setBackground(getResources().getDrawable(R.drawable.delete_w));
            pictureDeleteTextViewList[i].setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(BuildConfig.FILE_SERVER_BASE_URL + model.userRevwFileList.get(i).imageUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(pictureImageList[i]);
        }

        storeId = model.storId;
        orderNum = model.orderId;

    }

    /**
     * 리뷰 정보 조회
     */
    private void getReviewInfo() {
        if(reviewType.equals(REVIEW_TYPE_DELRIVERY)){
            if(reviewController != null){
                reviewController.getDeliveryReviewInfo(userRevwId);
            }
        }else if(reviewType.equals(REVIEW_TYPE_EAT_OUT)){
            if(reviewController != null){
                reviewController.getPlaceReviewInfo(userRevwId);
            }
        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (!isFinishing()) {
            if (type == reviewController.REQUEST_REVIEW_DETAIL_INFO) {
                initReviewInfo((ReviewHeaderInfoModel) baseModel);
            } else if (type == reviewController.REQUEST_REVIEW_MODIFY) {
                ResReviewModel model = (ResReviewModel) baseModel;
                Logger.d("onRetrofitSuccess >>>>>>>>> " + model.toString());
                Toast.makeText(this, getString(R.string.common_toast_msg_review_modify_success), Toast.LENGTH_SHORT).show();

                if (reviewAdapterPosition != -1) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_REVIEW_ADAPTER_POSITION, reviewAdapterPosition);
                    intent.putExtra(EXTRA_REVIEW_MODIFY_RESULT_MODEL, new Gson().toJson(model.reviewModel));
                    setResult(MoaConstants.RESULT_REVIEW_MODIFY_SUCCESS, intent);
                    finish();
                }
            }
            dismissProgressDialog();
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        if (!isFinishing()) {
            dismissProgressDialog();
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            setResult(MoaConstants.RESULT_REVIEW_MODIFY_FAIL);
            finish();
        }
    }
}


