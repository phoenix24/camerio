package org.fictious.camerio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Preview extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

	private Camera camera;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		SurfaceView surface = (SurfaceView) findViewById(R.id.camera_surface);
		SurfaceHolder holder = surface.getHolder();
		
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		surface.setClickable(true);
		surface.setOnClickListener(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Camera.Parameters parameters = camera.getParameters();
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optsize = getoptimalsize(sizes, width, height);
		parameters.setPreviewSize(optsize.width, optsize.height);

		camera.setParameters(parameters);
		camera.startPreview();
	}

	private Size getoptimalsize(List<Size> sizes, int width, int height) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) width / height;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = height;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			camera.release();
			camera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		camera.takePicture(shutter, raw, jpeg);
	}
	
	ShutterCallback shutter = new ShutterCallback() {
		public void onShutter() {
			//TODO: do something here.
		}
	};
	
	PictureCallback raw = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
	};
	
	PictureCallback jpeg = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream output = null;
			try {
				output = new FileOutputStream("/sdcard/test.jpg");
				output.write(data);
				output.close();
				camera.startPreview();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
