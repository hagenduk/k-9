package com.fsck.k9.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fsck.k9.R;

import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ShowAttachmentActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getIntent();
        Log.i("Cached File deleted - ", intent.getData().getPath());
        File f = new File(intent.getData().getPath());
        f.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ShowAttachmentActivity - ", "ShowAttachmentActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attachment);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        Intent intent = getIntent();
        InputStream stream = null;
        try {
            if (intent.getType().equals("text/plain")) {
                TextView t = new TextView(com.fsck.k9.activity.ShowAttachmentActivity.this);
                stream = this.getContentResolver().openInputStream(intent.getData());
                t.setText(IOUtils.toString(stream));
                linearLayout1.addView(t);
            } else if ((intent.getType().equals("image/*")) || (intent.getType().equals("image/jpg")) || (intent.getType().equals("image/jpeg"))) {
                imageView = new ImageView(com.fsck.k9.activity.ShowAttachmentActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                linearLayout1.addView(imageView);
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = this.getContentResolver().openInputStream(
                        intent.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                imageView.setImageBitmap(bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

