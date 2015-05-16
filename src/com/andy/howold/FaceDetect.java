package com.andy.howold;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class FaceDetect
{

	public interface Callback
	{
		public void success(JSONObject jsonObject);

		public void fail(FaceppParseException exception);
	}

	public void detect(final Bitmap bitmap, final Callback callback)
	{

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				HttpRequests httpRequests = new HttpRequests(CONSTANTS.apikey,
						CONSTANTS.apisecret, true, true);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				float scale = Math.min(
						1,
						Math.min(600f / bitmap.getWidth(),
								600f / bitmap.getHeight()));
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);

				Bitmap imgSmall = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, false);
				// Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " +
				// imgSmall.getHeight());

				imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				byte[] array = bos.toByteArray();
				PostParameters parameters = new PostParameters();
				parameters.setImg(array);
				try
				{
					JSONObject result = httpRequests
							.detectionDetect(parameters);
					if (callback != null)
					{
						callback.success(result);
						Log.i("json success", result.toString());
					}
				}
				catch (FaceppParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (callback != null)
					{
						callback.fail(new FaceppParseException("fail"));
					}
				}

			}
		}).start();

	}
}
