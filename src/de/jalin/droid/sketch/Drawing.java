package de.jalin.droid.sketch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Drawing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final List<Line> lineList;
	private Line line;
	
	public Drawing() {
		lineList = new ArrayList<>();
		line = new Line();
	}
	
	public void startPath(float x, float y) {
		line = new Line();
		line.add(x, y);
		lineList.add(line);
	}

	public void lineTo(float x, float y) {
		line.add(x, y);
	}

	public void draw(final Canvas canvas, final Paint paint) {
		for (final Line l : lineList) {
			canvas.drawPath(l.asPath(), paint);
		}
	}

	public void reset() {
		line = new Line();
		lineList.clear();
	}
	
	class Pt implements Serializable {
		private static final long serialVersionUID = 1L;
		float x, y;
		Pt(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	class Line implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<Pt> ptList = new ArrayList<>();
		void add(float x, float y) {
			ptList.add(new Pt(x, y));
		}
		void clear() {
			ptList.clear();
		}
		Path asPath() {
			final Path p = new Path();
			if (ptList.size() > 0) {
				final Pt pt = ptList.get(0);
				p.moveTo(pt.x, pt.y);
			}
			for (Pt pt : ptList) {
				p.lineTo(pt.x, pt.y);
			}
			return p;
		}
	}
	
}
