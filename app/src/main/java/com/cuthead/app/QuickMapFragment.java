package com.cuthead.app;


import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cuthead.controller.ProgressWheel;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class QuickMapFragment extends Fragment implements AMapLocationListener, LocationSource
        , AMap.OnMarkerDragListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,GeocodeSearch.OnGeocodeSearchListener{
    @InjectView(R.id.map)
    MapView mapView;
    @InjectView(R.id.tv_title)
    TextView mTitle;
    @InjectView(R.id.pw_location)
    ProgressWheel mProgressWheel;
    @InjectView(R.id.btn_confirm)
    Button mBtnConfirm;

    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private GeocodeSearch geocoderSearch;
    private Marker MyMarker;
    private LatLng location;

    private final int QUICKBOOK_FLAG = 0;

    public QuickMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_map, container, false);
        ButterKnife.inject(this, view);
        mapView.onCreate(savedInstanceState);
        init();

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new SubmitFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", QUICKBOOK_FLAG);
                bundle.putDouble("latitude",location.latitude);
                bundle.putDouble("longtidue",location.longitude);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.qb_container, fragment).commit();
            }
        });

        return view;
    }

    // 初始化AMap对象
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        // geocode search intial
        geocoderSearch = new GeocodeSearch(getActivity());
        geocoderSearch.setOnGeocodeSearchListener(this);

        // let the progress wheel spin
        mProgressWheel.spin();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setOnMarkerDragListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, -1, 10, this);
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
                MarkerOptions options = new MarkerOptions();
                options.draggable(true);
                options.snippet("你的位置");
                location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                options.position(location);
                MyMarker = aMap.addMarker(options);

                // zoom the map
                CameraUpdate update = CameraUpdateFactory.zoomTo(16.5f);
                aMap.animateCamera(update);

                // update the title text
                mTitle.setText("请确认您的位置");

                // update the visibilty
                mProgressWheel.setVisibility(View.GONE);
                mBtnConfirm.setVisibility(View.VISIBLE);
                MyMarker.showInfoWindow();

                // geoCode Request
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.latitude,location.longitude),200,GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        location = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);


        CameraUpdate update = CameraUpdateFactory.changeLatLng(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
        aMap.animateCamera(update);

        MyMarker.showInfoWindow();
        MyMarker.setTitle("你的新位置");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int code) {
            if (code == 0){
                if (result != null && result.getRegeocodeAddress()!= null) {
                    String str = result.getRegeocodeAddress().getFormatAddress()
                            + "附近";
                    MyMarker.setSnippet(str);
                    MyMarker.setTitle("你的位置");

                }
            }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
