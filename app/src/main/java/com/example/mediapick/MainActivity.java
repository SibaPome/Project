package com.example.mediapick;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_MEDIA_PICK = 1000;   // request code
    private static MainActivity ins = null;
    Context context;
    TextView textView;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ins = this;
        textView = findViewById(R.id.tv);
        button = findViewById(R.id.bt);
        imageView = findViewById(R.id.image_view);
        context = MainActivity.getInstance().getApplicationContext();

        button.setOnClickListener(buttonListener);
    }

    public static MainActivity getInstance() {
        return ins;
    }


    // ボタン押したとき
    View.OnClickListener buttonListener = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View view) {
            // ボタン押した後の処理（画像を選ばせ，選ばれた画像を表示する）
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);   // 内部ストレージへアクセス
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(intent, RESULT_MEDIA_PICK);
                textView.setText("画像を表示しています。");
            } else {
                textView.setText("失敗でーす");
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == RESULT_MEDIA_PICK) {
            if (resultCode == RESULT_OK) {
                Uri uri;
                if (resultData != null) {
                        uri = resultData.getData();
                        try {
                            Bitmap bmp = getBitmapFromUri(uri);
                            imageView.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                    }
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
