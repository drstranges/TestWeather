package com.testapp.weather.util;

import android.Manifest;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.testapp.weather.R;
import com.testapp.weather.sync.StatusReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Use this class to find ser location.
 * Write location in preferences if found.
 * Created on 25.12.2015.
 */
public class LocationLoader extends AsyncTaskLoader<String> {

    public static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    public static void launch(Context _context) throws PermissionHelper.PermissionSecurityException {
        new LocationLoader(_context).forceLoad();
    }

    public LocationLoader(Context context) throws PermissionHelper.PermissionSecurityException {
        super(context);
        PermissionHelper.assertPermissions(getContext(), REQUIRED_PERMISSIONS);
    }

    @Override
    public String loadInBackground() {
        String locationAddress = null;
        try {
            PermissionHelper.assertPermissions(getContext(), REQUIRED_PERMISSIONS);

            Location myLocation = getLocation();
            if (myLocation != null) {
                locationAddress = getAddress(myLocation);
            }
        } catch (PermissionHelper.PermissionSecurityException _e) {
            _e.printStackTrace();
            StatusReceiver.sendSyncError(getContext(), 0, getContext().getString(R.string.error_location_not_found));
        }
        return locationAddress;
    }

    @Nullable
    private Location getLocation() {
        LocationManager locationManager =
                (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        //noinspection ResourceType
        return provider == null ? null : locationManager.getLastKnownLocation(provider);
    }

    private String getAddress(Location _location) {
        String locationAddress = "";
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(_location.getLatitude(), _location.getLongitude(), 1);
            // Handle case where no address was found.
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                locationAddress = address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationAddress;
    }

    @Override
    public void deliverResult(String _location) {
        super.deliverResult(_location);
        if (!TextUtils.isEmpty(_location)) {
            PrefUtils.setPreferredLocation(getContext(), _location);
        }
    }
}
