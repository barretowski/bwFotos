package com.example.bwfotos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private Button btFoto;
    private FloatingActionButton fabGrayScale;
    private ImageView imageView;
    private static final int REQUEST_CODE_FOTO = 10000;
    private com.example.apptirafoto.PermissionsMarshmallow permissionsMashmallow = new com.example.apptirafoto.PermissionsMarshmallow(this);
    String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btFoto = findViewById(R.id.btFoto);
        fabGrayScale = findViewById(R.id.fabGrayScale);
        imageView = findViewById(R.id.imageView);

        btFoto.setOnClickListener(e->{
            tirarFoto();
        });
        fabGrayScale.setOnClickListener(f->{
            converterTonsCinza();
        });
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

    private void salvaFoto()
    {
        File file=getFilePublic("foto"+System.currentTimeMillis()+".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file,true);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush(); fos.close();
            // atualiza a galeria
            Intent novaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            sendBroadcast(novaIntent);
        } catch (Exception e)
        {  Toast.makeText(getApplicationContext(), "Erro " + e, Toast.LENGTH_LONG).show();  }
    }

    public File getFilePublic(String filename)
    {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), filename);
        return file;
    }


    private void converterTonsCinza() {
        Bitmap bitmap = (BitmapDrawable)imageView.getDrawable().getBitmap();
        Bitmap copiabitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int r,g,b,m,pixel,pixelcinza;
        for(int x=0;x<copiabitmap.getWidth();x++)
            for (int y = 0; y < copiabitmap.getHeight(); y++)
            {
                pixel = copiabitmap.getPixel(x,y);
                r = Color.red(pixel);
                g = Color.green(pixel);
                b = Color.blue(pixel);
                m = (int)(0.2989 * r + 0.5870 * g + 0.1140 * b);
                pixelcinza= Color.rgb(m,m,m);
                copiabitmap.setPixel(x,y,pixelcinza);
            }
        // insere a cópia transformada no imageview
        imageView.setImageBitmap(copiabitmap);

    }

    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, REQUEST_CODE_FOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {       super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_FOTO)  // Fechou a tela da câmera
        {
            if(resultCode == Activity.RESULT_OK) // Confirmou a foto
            {    imageBitmap = data.getParcelableExtra("data");
                imageView.setImageBitmap(imageBitmap);
                salvaFoto();
            }
            else  // Cancelou a foto
            {    imageView.setImageResource(R.mipmap.ic_launcher);  }
        }
    }

}