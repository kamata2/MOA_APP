package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreInfoModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * [정의]
 * 외식 매장 지도 화면 Activity
 *
 * [구성]
 * MMI 정의 문서 필요/디자인 가이드 없음
 *
 * @author chan
 */
public class EatOutStoreDaumMapActivity extends AppCompatActivity implements MapView.MapViewEventListener{

    public static final String EXTRA_EATOUT_STORE_DAUM_MAP ="EXTRA_EATOUT_STORE_DAUM_MAP";
    public static final String EXTRA_DELOVERY_STORE_DAUM_MAP ="EXTRA_DELOVERY_STORE_DAUM_MAP";

    private final int defaultZoomLevel = 2;
    private CommonTitleView commonTitleView;
    private TextView tvEatOutStoreDaumMapStoreName;
    private TextView tvEatOutStoreDaumMapStoreAddr;
    private FrameLayout flEatOutStoreDaumMapContainter;
    private MapView mapView;

    private EatOutStoreInfoModel eatOutStoreInfoModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_out_store_daum_map);

        eatOutStoreInfoModel = new Gson().fromJson(getIntent().getStringExtra(EXTRA_EATOUT_STORE_DAUM_MAP), EatOutStoreInfoModel.class);

        initLayout();
        daumMapInit();
    }

    private void initLayout() {
        commonTitleView = findViewById(R.id.titleEatOutStoreDaumMap);
        commonTitleView.setBackButtonClickListener(v -> finish());
        tvEatOutStoreDaumMapStoreName = findViewById(R.id.tvEatOutStoreDaumMapStoreName);
        tvEatOutStoreDaumMapStoreAddr = findViewById(R.id.tvEatOutStoreDaumMapStoreAddr);
        flEatOutStoreDaumMapContainter = findViewById(R.id.flEatOutStoreDaumMapContainter);

        if(eatOutStoreInfoModel != null){
            if(eatOutStoreInfoModel.storNm != null){
                commonTitleView.setTitleName(eatOutStoreInfoModel.storNm);
            }

            if(eatOutStoreInfoModel.storNm != null){
                tvEatOutStoreDaumMapStoreName.setText(eatOutStoreInfoModel.storNm);
            }

            if(eatOutStoreInfoModel.roadNmDefltAddr != null){
                tvEatOutStoreDaumMapStoreAddr.setText(eatOutStoreInfoModel.roadNmDefltAddr);
            }
        }
    }

    /**
     * 다음 지도 세팅
     */
    private void daumMapInit() {
        try {
            mapView = new MapView(this);
            mapView.setZoomLevel(defaultZoomLevel, true);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(eatOutStoreInfoModel.latu), Double.parseDouble(eatOutStoreInfoModel.lont)), true);
            flEatOutStoreDaumMapContainter.addView(mapView);

            mapView.setMapViewEventListener(this);
            mapView.addPOIItem(makePoiItem(eatOutStoreInfoModel, 0));
        } catch (Exception e) {
            Logger.i(e.toString());
        }
    }

    /**
     * 지도에 마커 그리기
     * @param model
     * @param position
     * @return
     */
    private MapPOIItem makePoiItem(EatOutStoreInfoModel model, int position) {

        MapPointBounds mapPointBounds = new MapPointBounds();

        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName(model.storNm);
        poiItem.setTag(position);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(model.latu), Double.parseDouble(model.lont));
        poiItem.setUserObject(model);
        poiItem.setMapPoint(mapPoint);
        mapPointBounds.add(mapPoint);
        poiItem.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
//        poiItem.setCustomImageResourceId(R.drawable.icon_marker_n);
//        poiItem.setCustomSelectedImageResourceId(R.drawable.icon_marker_p);
        return poiItem;
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    protected void onDestroy() {
        if (flEatOutStoreDaumMapContainter != null) {
            flEatOutStoreDaumMapContainter.removeAllViews();
        }
        super.onDestroy();
    }
}