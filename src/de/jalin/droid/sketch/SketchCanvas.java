package de.jalin.droid.sketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class SketchCanvas extends View {

	private final Drawing drawing;
	private final Paint paint;

	public SketchCanvas(final Context context) {
		super(context);
		this.drawing = ((SketchActivity)context).getDrawing();
		paint = initPaint();
	}

	private Paint initPaint() {
		final Paint paint = new Paint();
		paint.setDither(true);
		paint.setColor(0xFF4040C0);
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
			if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
				drawing.startPath(x, y);
			}
			drawing.lineTo(x, y);
			invalidate();
		}
		return pointerCount > 0;
	}

	@Override
	public void draw(final Canvas canvas) {
		canvas.drawColor(0xFFE0E0E0);
		drawing.draw(canvas, paint);
	}

	public void reset() {
		drawing.reset();
		invalidate();
	}

}
