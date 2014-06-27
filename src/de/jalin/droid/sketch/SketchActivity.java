package de.jalin.droid.sketch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class SketchActivity extends Activity {

	public static final String BUNDLE_KEY_DRAWING = "drawing";
	
	private Drawing drawing;
	private SketchCanvas view;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_DRAWING)) {
			drawing = (Drawing) savedInstanceState.get(BUNDLE_KEY_DRAWING);
		} else {
			drawing = new Drawing();
		}
		setContentView(R.layout.activity_sketch);
		view = new SketchCanvas(this);
		final LinearLayout layout = (LinearLayout)findViewById(R.id.container);
		layout.addView(view);
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(BUNDLE_KEY_DRAWING, drawing);
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.sketch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		final int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void settings(final MenuItem item) {
		startActivity(new Intent(this, SettingActivity.class));;
	}

	public void about(final MenuItem item) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(("http://www.lug-kr.de/wiki/SketchDroid"))));
	}

	public void reset(final MenuItem item) {
		view.reset();
	}

	public void share(final MenuItem item) {
		final int width = view.getWidth();
		final int height = view.getHeight();
		final int max = width > height ? width : height;
		final Bitmap bitmap = Bitmap.createBitmap(max, max, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas); 
		try {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			final String mailto = preferences.getString("prefMailto", "hello@example.org");
			final String subject = preferences.getString("prefSubject", "SketchDroid");
			final String text = preferences.getString("prefText", "");
			final String picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
			final File sketchTempDirectory = new File(picturesDirectory + "/sketch_temp");
			sketchTempDirectory.mkdirs();
			final File tempFile = new File(sketchTempDirectory.getAbsoluteFile() + "/sketch.png");
			final FileOutputStream outputStream = new FileOutputStream(tempFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); 
			outputStream.close();
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/png");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mailto } );
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
		} catch (IOException e) {
			Log.e("FILE", e.getLocalizedMessage(), e);
		}
	}

	public Drawing getDrawing() {
		return drawing;
	}

}
