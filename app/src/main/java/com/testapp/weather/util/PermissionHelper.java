package com.testapp.weather.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for simplify using Android M permission system
 * Created by romka on 25.12.15.
 */
public class PermissionHelper {

    @NonNull
    public static String[] checkPermissions(@NonNull Context _context, String[] _permissions) {
        if (_permissions == null) return new String[0];
        List<String> missingPermissions = new ArrayList<>(_permissions.length);
        for (String permission : _permissions) {
            if (ContextCompat.checkSelfPermission(_context, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        return missingPermissions.toArray(new String[missingPermissions.size()]);
    }

    @NonNull
    public static void assertPermissions(@NonNull Context _context, String[] _permissions) throws PermissionSecurityException {
        if (_permissions != null) {
            List<String> missingPermissions = new ArrayList<>(_permissions.length);
            for (String permission : _permissions) {
                if (ContextCompat.checkSelfPermission(_context, permission) != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission);
                }
            }
            int count = missingPermissions.size();
            if (count > 0){
                throw new PermissionSecurityException(missingPermissions.toArray(new String[count]),
                        "Permission is required: " + missingPermissions.toString());
            }
        }

    }

    public static boolean isSomePermissionGranted(int[] _grantResults) {
        for (int res : _grantResults){
            if (res == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static class PermissionSecurityException extends Throwable {
        private final String[] mRequiredPermissions;

        public PermissionSecurityException(String[] _requiredPermissions, String _message) {
            super(_message);
            mRequiredPermissions = _requiredPermissions == null ? new String[]{} : _requiredPermissions;
        }

        @NonNull
        public String[] getRequiredPermissions() {
            return mRequiredPermissions;
        }
    }
}
