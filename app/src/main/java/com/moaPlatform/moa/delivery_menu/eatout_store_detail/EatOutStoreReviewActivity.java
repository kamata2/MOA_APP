package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.os.Bundle;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.fragment.ReviewFragment;
import com.moaPlatform.moa.util.Logger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_STORE_ID;

/**
 * 외식 리뷰 리스트 보기 화면
 */
public class EatOutStoreReviewActivity extends AppCompatActivity {

    private int requestStoreId;
    private String storeName;

    private ReviewFragment reviewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_out_store_review);
        initData();
        initFragment();
    }

    private void initData(){
        requestStoreId = getIntent().getIntExtra(EXTRA_STORE_ID, 0);
        storeName = getIntent().getStringExtra(MoaConstants.EXTRA_TITLE_NAME);
    }

    private void initFragment() {

        reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MoaConstants.EXTRA_STORE_ID, requestStoreId);
        bundle.putString(MoaConstants.EXTRA_STORE_NAME, storeName);
        bundle.putString(ReviewFragment.EXTRA_REVIEW_LIST_TYPE, ReviewFragment.REVIEW_LIST_TYPE_EAT_OUT);
        bundle.putBoolean(ReviewFragment.EXTRA_TITLE_VISIBILITY, true);
        reviewFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flEatOutStoreReviewContainer, reviewFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Logger.d("onBackPressed");
        if(reviewFragment != null){
            if(reviewFragment.isReviewListItemChanged()){
                setResult(MoaConstants.RESULT_REVIEW_LIST_NOTIFYCHANGED);
            }
        }
        super.onBackPressed();
    }
}
