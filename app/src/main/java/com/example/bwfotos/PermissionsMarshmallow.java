package com.example.bwfotos;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import androidx.core.app.ActivityCompat;

public class PermissionsMarshmallow {
    Context mContext;

    public PermissionsMarshmallow(Context context) {
        mContext = context;
    }
    public  boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext != null
                && permissions != null)
        {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public  void  requestPermissions( String[] PERMISSIONS,int PERMISSION_ALL) {
        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
    }

}

/*
public class PrincipalActivity extends AppCompatActivity {
   …
   private PermissionsMarshmallow permissionsMashmallow = new PermissionsMarshmallow(this);
   String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE};

   @Override
   protected void onCreate(Bundle savedInstanceState) {
         …
         CheckPermissionGranted();
   }

   private void CheckPermissionGranted()
   {    if (permissionsMashmallow.hasPermissions(PERMISSIONS)) {
            //  permission granted
        } else {
            // request permission
            permissionsMashmallow.requestPermissions(PERMISSIONS, 1);
        }
    }

    …
}
*/
