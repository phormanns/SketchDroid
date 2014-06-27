package de.jalin.droid.sketch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

public class SketchCanvas extends View {

	private final Drawing drawing;
	private final Paint paint;
	private final int bgColor;
	private final int fgColor;

	public SketchCanvas(final Context context) {
		super(context);
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final String bgColorString = preferences.getString("prefBGColor", "#E0E0E0");
		final String fgColorString = preferences.getString("prefFGColor", "#4040C0");
		bgColor = Color.parseColor(bgColorString);
		fgColor = Color.parseColor(fgColorString);
		drawing = ((SketchActivity)context).getDrawing();
		paint = initPaint();
	}

	private Paint initPaint() {
		final Paint paint = new Paint();
		paint.setDither(true);
		paint.setColor(fgColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(3);
		return paint;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		final int pointerCount = event.getPointerCount();
		for (int ptr = 0; ptr < pointerCount; ptr++) {
			final float x = event.getX(ptr);
			final float y = event.getY(ptr);
			if (ptr == 0 && MotionEvent.ACTION_DOWN == event.getActionMasked()) {
				drawing.startPath(x, y);
			}
			drawing.lineTo(x, y);
			invalidate();
		}
		return pointerCount > 0;
	}

	@Override
	public void draw(final Canvas canvas) {
		canvas.drawColor(bgColor);
		drawing.draw(canvas, paint);
	}

	public void reset() {
		drawing.reset();
		invalidate();
	}

}
