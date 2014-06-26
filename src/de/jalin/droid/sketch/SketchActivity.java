package de.jalin.droid.sketch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class SketchActivity extends Activity {

	private Drawing drawing;
	private SketchCanvas view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey("drawing")) {
			drawing = (Drawing) savedInstanceState.get("drawing");
		} else {
			drawing = new Drawing();
		}
		setContentView(R.layout.activity_sketch);
		view = new SketchCanvas(this);
		final LinearLayout layout = (LinearLayout)findViewById(R.id.container);
		layout.addView(view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("drawing", drawing);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sketch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void reset(MenuItem item) {
		view.reset();
	}

	public void share(MenuItem item) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(0xFFE0E0E0);
		view.draw(canvas); 
		try {
			final String picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
			final File sketchTempDirectory = new File(picturesDirectory + "/sketch_temp");
			sketchTempDirectory.mkdirs();
			final File tempFile = new File(sketchTempDirectory.getAbsoluteFile() + "/sketch.jpg");
			final FileOutputStream outputStream = new FileOutputStream(tempFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); 
			outputStream.close();
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/jpeg");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "peter.hormanns@jalin.de" } );
			intent.putExtra(Intent.EXTRA_SUBJECT, "Sketch");
			intent.putExtra(Intent.EXTRA_TEXT, "Deine Notiz");
			startActivity(Intent.createChooser(intent, "Sende Notiz"));
		} catch (IOException e) {
			Log.e("FILE", e.getLocalizedMessage(), e);
		}
	}

	public Drawing getDrawing() {
		return drawing;
	}

}
