package com.andy.howold;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.howold.FaceDetect.Callback;
import com.facepp.error.FaceppParseException;

public class MainActivity extends Activity implements OnClickListener
{

	private static final int PICK_PHOTO_CODE = 0X110;
	protected static final int DETECT_SUCCESS = 0X111;
	protected static final int DETECT_FAIL = 0X112;
	private Button btn_getImage;
	private Button btn_detect;
	private TextView tv_state;
	private ImageView mPhoto;//
	private Bitmap mPhotoImage;// 图片
	private String mCurrentPhotoString;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		intiView();

		initEvents();
	}

	/**
	 * 初始化事件
	 */
	private void initEvents()
	{
		btn_getImage.setOnClickListener(this);
		btn_detect.setOnClickListener(this);

	}

	/**
	 * 初始化控件
	 */
	private void intiView()
	{
		btn_getImage = (Button) findViewById(R.id.getImage);
		btn_detect = (Button) findViewById(R.id.detect);
		tv_state = (TextView) findViewById(R.id.tv_state);
		mPhoto = (ImageView) findViewById(R.id.imageView1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK)
		{
			// 是选择照片的code
			if (requestCode == PICK_PHOTO_CODE)
			{
				if (data != null)
				{
					Uri uri = data.getData();

					try
					{
						mPhotoImage = MediaStore.Images.Media.getBitmap(
								this.getContentResolver(), uri);
					}
					catch (FileNotFoundException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// ContentResolver cr = this.getContentResolver();
					// try
					// {
					// mPhotoImage = BitmapFactory.decodeStream(cr
					// .openInputStream(uri));
					//
					// }
					// catch (FileNotFoundException e)
					// {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }

					// Cursor cursor = getContentResolver().query(uri, null,
					// null,
					// null, null);
					//
					// cursor.moveToFirst();
					//
					// int index = cursor
					// .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					//
					// mCurrentPhotoString = cursor.getString(index);
					//
					// cursor.close();

					// resizePhoto();

					mPhoto.setImageBitmap(mPhotoImage);
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void resizePhoto()
	{
		// TODO Auto-generated method stub
		mPhotoImage = BitmapFactory.decodeFile(mCurrentPhotoString);
	}

	/**
	 * 按键响应
	 * 
	 * @param arg0
	 */
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.getImage:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, PICK_PHOTO_CODE);
			break;
		case R.id.detect:
			FaceDetect faceDetect = new FaceDetect();
			faceDetect.detect(mPhotoImage, new Callback()
			{

				// success fail 仍然是在子线程中执行的 所以这里如果更新UI
				// 一定还是要借助handler
				@Override
				public void success(JSONObject jsonObject)
				{
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = DETECT_SUCCESS;
					msg.obj = jsonObject;
					handler.sendMessage(msg);
				}

				@Override
				public void fail(FaceppParseException exception)
				{
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = DETECT_SUCCESS;
					handler.sendMessage(msg);
				}
			});
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == DETECT_SUCCESS)
			{

			}
			else if (msg.what == DETECT_FAIL)
			{

			}
		};
	};

}
