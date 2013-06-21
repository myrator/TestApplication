package com.andruha.android.myapplication;

import java.io.FileNotFoundException;
import java.util.Locale;

import com.andruha.android.myapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;


public class ScalingActivity extends Activity implements OnClickListener{
	Button btnCamera, btnGallery;
	ImageView ivPicture;
	ZoomControls zcZoom;
	Bitmap bmp;
	private static final int CAMERA_REQUEST = 1888;
	private static final int GALLERY_REQUEST = 0;
	float scaleWidth = 1;
	float scaleHeight = 1;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scaling_activity);
		

		
            
            
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
		btnGallery = (Button) findViewById(R.id.btnGallery);
		btnGallery.setOnClickListener(this);
		ivPicture = (ImageView) findViewById(R.id.ivPicture);
		zcZoom = (ZoomControls) findViewById(R.id.zcZoom); 
		zcZoom.setVisibility(View.GONE);
		
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		
		zcZoom.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();
				double scale = 1.3;

				scaleWidth = (float) (scaleWidth * scale);
				scaleHeight = (float) (scaleHeight * scale);

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,bmpHeight, matrix, true);
				ivPicture.setImageBitmap(resizeBmp);				
			}		
		});
		
		zcZoom.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();

				double scale = 0.8;

				scaleWidth = (float) (scaleWidth * scale);
				scaleHeight = (float) (scaleHeight * scale);

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,bmpHeight, matrix, true);
				ivPicture.setImageBitmap(resizeBmp);
			}		
		});
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btnCamera:
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
			break;
		case R.id.btnGallery:
			Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent, GALLERY_REQUEST);
			break;
		
		}
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);

	if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST){
	 Uri targetUri = data.getData();
	 Bitmap bitmap;
	 try {
	  bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
	  ivPicture.setImageBitmap(bitmap);
	 } catch (FileNotFoundException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	 }
	}
	
	if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST){
        Bitmap photo = (Bitmap) data.getExtras().get("data"); 
        ivPicture.setImageBitmap(photo);		
	}
	
	btnCamera.setVisibility(View.GONE);
	btnGallery.setVisibility(View.GONE);
	ivPicture.setVisibility(View.VISIBLE);
	zcZoom.setVisibility(View.VISIBLE);
	bmp = ((BitmapDrawable) ivPicture.getDrawable()).getBitmap();	
	}	
	
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, getResources().getString(R.string.pref));
        mi.setIntent(new Intent(this, MyPreferenceActivity.class));
        return super.onCreateOptionsMenu(menu);
      }
	
}
