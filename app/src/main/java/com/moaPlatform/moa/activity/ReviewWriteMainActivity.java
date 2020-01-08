package com.moaPlatform.moa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_FROM_VIEW;
import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_STORE_ID;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_AMOUNT_SCORE;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_CONTENT;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_DELIVERY_SCORE;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_ORDER_ID;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_SERVICE_SCORE;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_STORE_ID;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_TASTE_SCORE;
import static com.moaPlatform.moa.constants.MoaConstants.PARAM_USER_ID;
import static com.moaPlatform.moa.util.ImageResizeUtils.exifOrientationToDegrees;
import static com.moaPlatform.moa.util.ImageResizeUtils.rotate;

/**
 * 리뷰 작성하기 화면
 * [배달|외식]에서 공통으로 사용
 */
public class ReviewWriteMainActivity extends AppCompatActivity {

    private final String TAG = ReviewWriteMainActivity.class.getSimpleName();

    public static final String FROM_VIEW_EATOUT_STORE_REVIEW = "FROM_VIEW_EATOUT_STORE_REVIEW";
    public static final String FROM_VIEW_STORE_REVIEW = "FROM_VIEW_STORE_REVIEW";

    public static final String ORDERD_GOODS_LIST = "ORDERD_GOODS_LIST";     //주문된 상품 목록 : 배달만 사용

    private CommonTitleView commonTitleReviewWrite;
    private EditText etContent;
    private RatingBar rTastyStar, rMountStar, rDeliveryStar;
    private RelativeLayout rlSideReviewWriteImage1Container, rlSideReviewWriteImage2Container, rlSideReviewWriteImage3Container;
    private ImageView ivSideReviewWritePicture1, ivSideReviewWritePicture2, ivSideReviewWritePicture3;
    private ImageView ivSideReviewWritePicture1CameraIcon, ivSideReviewWritePicture2CameraIcon, ivSideReviewWritePicture3CameraIcon;
    private TextView tvSideReviewWritePicture1Delete, tvSideReviewWritePicture2Delete, tvSideReviewWritePicture3Delete;
    private TextView tvSideReviewWritePleaseEnterStar, tvSideMenuReviewWriteToolTip;
    private ConstraintLayout constraintSideReviewWriteComplete;                            //완료 > 전송하기

    private TextView tvSideReviewWriteStoreName;            //매장명
    private TextView tvSideReviewWriteMenuTitle;            //메뉴 타이틀
    private TextView tvSideReviewWriteMenuContent;          //메뉴 항목
    private TextView tvSideReviewWriteDeliveryScoreTitle;   //배달 평점 타이틀 문구

    private Integer type = 0;

    private File tempFile;
    private Uri photoUri;
    private int exifDegree;

    private String imageFilePath1;
    private String imageFilePath2;
    private String imageFilePath3;

    private File imageFile1;
    private File imageFile2;
    private File imageFile3;

    //progressDialog
    private ProgressDialog progressDialog;

    //request params
    private String requestStroeId;
    private String requestOrderId;
    private String requestTasteScore = "0.0";
    private String requestAmountScore = "0.0";
    private String requestDeliveryScore = "0.0";

    //text strings
    private String storeName;
    private String orderdGoodsList;

    private String fromView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_review_write);

        initDefautData();
        initView();
        initListener();
    }

    private void initDefautData() {

        Intent intent = getIntent();
        fromView = intent.getStringExtra(EXTRA_FROM_VIEW);
        requestStroeId = String.valueOf(intent.getIntExtra(EXTRA_STORE_ID, 0));
        storeName = intent.getExtras().getString(MoaConstants.EXTRA_STORE_NAME);

        if (ObjectUtil.checkNotNull(fromView) && fromView.equals(FROM_VIEW_EATOUT_STORE_REVIEW)) {

        } else {
            requestOrderId = intent.getExtras().getString(MoaConstants.EXTRA_ORDER_ID);
            orderdGoodsList = intent.getExtras().getString(ORDERD_GOODS_LIST);
        }
    }

    private void initView() {

        rTastyStar = findViewById(R.id.ratingSideReviewWriteTasty);
        rMountStar = findViewById(R.id.ratingSideReviewWriteAmount);
        rDeliveryStar = findViewById(R.id.ratingSideReviewWriteDelivery);

        commonTitleReviewWrite = findViewById(R.id.commonTitleReviewWrite);
        commonTitleReviewWrite.setBackButtonClickListener(v -> finish());
        commonTitleReviewWrite.setTitleName("리뷰쓰기");

        etContent = findViewById(R.id.etSideReviewWriteContent);

        rlSideReviewWriteImage1Container = findViewById(R.id.rlSideReviewWriteImage1Container);
        rlSideReviewWriteImage2Container = findViewById(R.id.rlSideReviewWriteImage2Container);
        rlSideReviewWriteImage3Container = findViewById(R.id.rlSideReviewWriteImage3Container);

        ivSideReviewWritePicture1 = findViewById(R.id.ivSideReviewWritePicture1);
        ivSideReviewWritePicture2 = findViewById(R.id.ivSideReviewWritePicture2);
        ivSideReviewWritePicture3 = findViewById(R.id.ivSideReviewWritePicture3);

        tvSideReviewWritePicture1Delete = findViewById(R.id.tvSideReviewWritePicture1Delete);
        tvSideReviewWritePicture2Delete = findViewById(R.id.tvSideReviewWritePicture2Delete);
        tvSideReviewWritePicture3Delete = findViewById(R.id.tvSideReviewWritePicture3Delete);

        ivSideReviewWritePicture1CameraIcon = findViewById(R.id.ivSideReviewWritePicture1CameraIcon);
        ivSideReviewWritePicture2CameraIcon = findViewById(R.id.ivSideReviewWritePicture2CameraIcon);
        ivSideReviewWritePicture3CameraIcon = findViewById(R.id.ivSideReviewWritePicture3CameraIcon);

        tvSideReviewWriteStoreName = findViewById(R.id.tvSideReviewWriteStoreName);
        tvSideReviewWriteMenuTitle = findViewById(R.id.tvSideReviewWriteMenuTitle);
        tvSideReviewWriteMenuContent = findViewById(R.id.tvSideReviewWriteMenuContent);

        tvSideReviewWriteDeliveryScoreTitle = findViewById(R.id.tvSideReviewWriteDeliveryScoreTitle);

        tvSideReviewWritePleaseEnterStar = findViewById(R.id.tvSideReviewWritePleaseEnterStar);
        tvSideMenuReviewWriteToolTip = findViewById(R.id.tvSideMenuReviewWriteToolTip);
        constraintSideReviewWriteComplete = findViewById(R.id.constraintSideReviewWriteComplete);

        tvSideReviewWriteStoreName.setText(storeName);

        tvSideReviewWritePicture1Delete.setVisibility(View.GONE);
        tvSideReviewWritePicture2Delete.setVisibility(View.GONE);
        tvSideReviewWritePicture3Delete.setVisibility(View.GONE);

        if (ObjectUtil.checkNotNull(fromView)) {
            if (fromView.equals(FROM_VIEW_EATOUT_STORE_REVIEW)) {

                tvSideReviewWriteDeliveryScoreTitle.setText(getString(R.string.review_service_score));

                tvSideReviewWriteMenuTitle.setVisibility(View.GONE);
                tvSideReviewWriteMenuContent.setVisibility(View.GONE);
            } else if (fromView.equals(FROM_VIEW_STORE_REVIEW)) {

                tvSideReviewWriteDeliveryScoreTitle.setText(getString(R.string.review_delivery_score));

                if (ObjectUtil.checkNotNull(orderdGoodsList)) {
                    tvSideReviewWriteMenuContent.setText(orderdGoodsList);
                    tvSideReviewWriteMenuTitle.setVisibility(View.VISIBLE);
                    tvSideReviewWriteMenuContent.setVisibility(View.VISIBLE);
                } else {
                    tvSideReviewWriteMenuTitle.setVisibility(View.GONE);
                    tvSideReviewWriteMenuContent.setVisibility(View.GONE);
                }
            }
        }

    }

    private void initListener() {
        rlSideReviewWriteImage1Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                if (tvSideReviewWritePicture1Delete.getVisibility() == View.VISIBLE) {
                    deletePictureDialog();
                } else {
                    addPictureDialog();
                }
            }
        });
        rlSideReviewWriteImage2Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                if (tvSideReviewWritePicture2Delete.getVisibility() == View.VISIBLE) {
                    deletePictureDialog();
                } else {
                    addPictureDialog();
                }
            }
        });
        rlSideReviewWriteImage3Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                if (tvSideReviewWritePicture3Delete.getVisibility() == View.VISIBLE) {
                    deletePictureDialog();
                } else {
                    addPictureDialog();
                }
            }
        });
        constraintSideReviewWriteComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultipart();
            }
        });

        rTastyStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Logger.d("Star Test Taste >>>>" + rating);
                requestTasteScore = String.valueOf(rating);
            }
        });

        rMountStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Logger.d("Star Test Amount >>>>" + rating);
                requestAmountScore = String.valueOf(rating);
            }
        });

        rDeliveryStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Logger.d("Star Test Delivery >>>>" + rating);
                requestDeliveryScore = String.valueOf(rating);
            }
        });
    }

    /**
     * 사진 추가 다이얼로그
     */
    void addPictureDialog() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewWriteMainActivity.this);
                builder.setTitle("파일첨부");
                builder.setMessage("촬영 혹은 앨범에서 선택하세요");
                builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goCamera();
                    }
                });
                builder.setNegativeButton("사진앨범",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goGallery();
                            }
                        });
                builder.show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ReviewWriteMainActivity.this, "사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다")
                .setDeniedMessage("사진권한")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    /**
     * 선택된 사진 삭제 다이얼로그
     */
    private void deletePictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setTitle("첨부 파일을 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 1) {
                            ivSideReviewWritePicture1.setImageBitmap(null);
                            ivSideReviewWritePicture1CameraIcon.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                            tvSideReviewWritePicture1Delete.setVisibility(View.GONE);
                            imageFilePath1 = null;
                        } else if (type == 2) {
                            ivSideReviewWritePicture2.setImageBitmap(null);
                            ivSideReviewWritePicture2CameraIcon.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                            tvSideReviewWritePicture2Delete.setVisibility(View.GONE);
                            imageFilePath2 = null;
                        } else if (type == 3) {
                            ivSideReviewWritePicture3.setImageBitmap(null);
                            ivSideReviewWritePicture3CameraIcon.setBackground(getResources().getDrawable(R.drawable.addphoto_l));
                            tvSideReviewWritePicture3Delete.setVisibility(View.GONE);
                            imageFile3 = null;
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void goCamera() // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(ReviewWriteMainActivity.this, getApplicationContext().getPackageName() + ".fileprovider", tempFile);
            } else {
                photoUri = Uri.fromFile(tempFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, MoaConstants.REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // 이미지 파일 이름 ( moaPlanet_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "moaPlanet_" + timeStamp + "_";
        // 이미지가 저장될 폴더 이름 ( moaPlanet )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/moaPlanet/");
        if (!storageDir.exists()) storageDir.mkdirs();
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

    // 앨범에서 이미지 가져오기
    public void goGallery() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, MoaConstants.REQUEST_GALLELY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(ReviewWriteMainActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
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
                Logger.d("tempFile Uri : " + Uri.fromFile(tempFile));
                if (type == 1) {
                    imageFilePath1 = tempFile.getAbsolutePath();
                } else if (type == 2) {
                    imageFilePath2 = tempFile.getAbsolutePath();
                } else if (type == 3) {
                    imageFilePath3 = tempFile.getAbsolutePath();
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            }
            setTempFileToBitmapImage();
        } else if (requestCode == MoaConstants.REQUEST_CAMERA) {
            ExifInterface exif = null;
            try {
                if (type == 1) {
                    exif = new ExifInterface(imageFilePath1);
                } else if (type == 2) {
                    exif = new ExifInterface(imageFilePath2);
                } else if (type == 3) {
                    exif = new ExifInterface(imageFilePath3);
                }
                int exifOrientation;
                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegrees(exifOrientation);
                } else {
                    exifDegree = 0;
                }
                setTempFileToBitmapImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setTempFileToBitmapImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Logger.d("setTempFileToBitmapImage : " + tempFile.getAbsolutePath());
        if (type == 1) {
            ivSideReviewWritePicture1.setImageBitmap(rotate(originalBm, exifDegree));
            ivSideReviewWritePicture1CameraIcon.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvSideReviewWritePicture1Delete.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ivSideReviewWritePicture2.setImageBitmap(rotate(originalBm, exifDegree));
            ivSideReviewWritePicture2CameraIcon.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvSideReviewWritePicture2Delete.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            ivSideReviewWritePicture3.setImageBitmap(rotate(originalBm, exifDegree));
            ivSideReviewWritePicture3CameraIcon.setBackground(getResources().getDrawable(R.drawable.delete_w));
            tvSideReviewWritePicture3Delete.setVisibility(View.VISIBLE);
        }

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;
    }

    /**
     * 리뷰 등록정보 전송
     */
    private void requestMultipart() {

        Logger.d("rating >>>> ratingTastyStarText " + requestTasteScore);
        Logger.d("rating >>>> ratingMountStarText " + requestAmountScore);
        Logger.d("rating >>>> ratingDeliveryStarText " + requestDeliveryScore );

        if (requestTasteScore == null || requestAmountScore == null || requestDeliveryScore == null
                || requestTasteScore.equals("0.0") || requestAmountScore.equals("0.0") || requestDeliveryScore.equals("0.0")) {
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
        builder.addFormDataPart(PARAM_STORE_ID, requestStroeId);
        builder.addFormDataPart(PARAM_USER_ID, MoaAuthHelper.getInstance().getBasePrimaryInfo());

        if (ObjectUtil.checkNotNull(fromView) && fromView.equals(FROM_VIEW_EATOUT_STORE_REVIEW)) {
            //외식 리뷰 쓰기
            builder.addFormDataPart(PARAM_SERVICE_SCORE, String.valueOf(requestDeliveryScore));
        } else {
            //배달 리뷰 쓰기
            builder.addFormDataPart(PARAM_ORDER_ID, requestOrderId);
            builder.addFormDataPart(PARAM_DELIVERY_SCORE, String.valueOf(requestDeliveryScore));
        }
        builder.addFormDataPart(PARAM_TASTE_SCORE, String.valueOf(requestTasteScore));
        builder.addFormDataPart(PARAM_AMOUNT_SCORE, String.valueOf(requestAmountScore));
        builder.addFormDataPart(PARAM_CONTENT, etContent.getText().toString().trim());

        try {
            if (ObjectUtil.checkNotNull(imageFilePath1)) {
                imageFile1 = new File(imageFilePath1);
                builder.addFormDataPart("file", imageFile1.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile1));
            }
            if (ObjectUtil.checkNotNull(imageFilePath2)) {
                imageFile2 = new File(imageFilePath2);
                builder.addFormDataPart("file2", imageFile2.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile2));
            }
            if (ObjectUtil.checkNotNull(imageFilePath3)) {
                imageFile3 = new File(imageFilePath3);
                builder.addFormDataPart("file3", imageFile3.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultipartBody requestBody = builder.build();
        if (ObjectUtil.checkNotNull(fromView) && fromView.equals(FROM_VIEW_EATOUT_STORE_REVIEW))
            postEatOutReview(requestBody);
        else
            reviewRegisterList(requestBody);
    }

    private void postEatOutReview(MultipartBody requestBody) {
        //외식 리뷰 쓰기
        RetrofitClient.getInstance().getMoaService().postEatOutReview(requestBody).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    postEatOutReview(requestBody);
                    return;
                }
                if (!isFinishing()) {
                    dismissProgressDialog();
                    if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                        Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_review_write_success), Toast.LENGTH_SHORT).show();
                        setResult(MoaConstants.RESULT_REVIEW_WRITE_SUCCESS, null);
                    } else if (commonModel.resultValue.equals(ServerSideMsg.FAIL)) {
                        Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_review_write_fail), Toast.LENGTH_SHORT).show();
                        setResult(MoaConstants.RESULT_REVIEW_WRITE_FAIL, null);
                    } else {
                        Toast.makeText(ReviewWriteMainActivity.this, commonModel.resultMessage, Toast.LENGTH_SHORT).show();
                        setResult(MoaConstants.RESULT_REVIEW_WRITE_FAIL, null);
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                dismissProgressDialog();
                Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reviewRegisterList(MultipartBody requestBody) {
        RetrofitClient.getInstance().getMoaService().reviewRegisterList(requestBody).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel model = response.body();
                if (model == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(model)) {
                    reviewRegisterList(requestBody);
                    return;
                }
                dismissProgressDialog();
                if (model.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_review_write_success), Toast.LENGTH_SHORT).show();
                    setResult(MoaConstants.RESULT_REVIEW_WRITE_SUCCESS, null);
                } else {
                    Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_review_write_fail), Toast.LENGTH_SHORT).show();
                    setResult(MoaConstants.RESULT_REVIEW_WRITE_FAIL, null);
                }
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Toast.makeText(ReviewWriteMainActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
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
}
