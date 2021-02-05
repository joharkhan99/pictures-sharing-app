package com.example.parserserverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SingleUserFeed extends AppCompatActivity {
  LinearLayout linearLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_user_feed);

    linearLayout = (LinearLayout) findViewById(R.id.imageContainerLayout);

    Intent intent = getIntent();
    String username = intent.getStringExtra("username");

    setTitle(username);

    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
    query.whereEqualTo("username", username);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null) {
          if (objects.size() > 0) {
            for (ParseObject object : objects) {
              ImageView imageView = new ImageView(SingleUserFeed.this);
              ParseFile image = (ParseFile) object.getParseFile("image");

              loadImages(image, imageView);

            }
          }
        }
      }
    });

  }

  //this methd will convert parsefile/bytes to image
  private void loadImages(ParseFile parseImage, final ImageView img) {

    if (parseImage != null) {
      parseImage.getDataInBackground(new GetDataCallback() {
        @Override
        public void done(byte[] data, ParseException e) {
          if (e == null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            img.setImageBitmap(bmp);
            linearLayout.addView(img);
          }
        }
      });
    } else {
      //else add default image
      img.setImageResource(R.drawable.insta_logo);
      linearLayout.addView(img);
    }
  }
}