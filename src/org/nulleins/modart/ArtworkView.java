package org.nulleins.modart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import org.nulleins.Modart.R;

import java.util.Random;

public class ArtworkView extends View {

  private Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Toast toast = Toast.makeText(getContext(),
      getResources().getString(R.string.shake_tip), Toast.LENGTH_SHORT);
  private int complexity = 2;

  public ArtworkView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onDraw(final Canvas canvas) {
    createArtwork(canvas, getMeasuredWidth(), getMeasuredHeight());
    toast.show();
    canvas.save ();
  }

  /** draw randomly colored and sized rectangles, randomly on a spiral path */
  private void createArtwork(Canvas canvas, final int width, final int height) {
    float r = 1;
    float theta = 0;
    while(theta < 360.0f) {
      // convert polar to cartesian coordinates
      Point point = new Point(
          scale(r * (float)Math.cos(theta), 200, width),
          scale(r * (float)Math.sin(theta), 200, height));
      paintRectangle(canvas, point);
      theta += randFloat(0.01f, (float)complexity); // was: 1.2f);
      r += randFloat(0.01f, (float)complexity); // was: 2.0f
    }
  }

  private static int scale ( final float value, final float baseMax, final int bound) {
    return (int)(bound * value / baseMax);
  }

  private static Random rand = new Random();
  private static int randInt(int min, int max) {
    return rand.nextInt((max - min) + 1) + min;
  }
  private static float randFloat(final float min, final float max) {
    return rand.nextFloat() * (max - min) + min;
  }

  /** paint one rectangle, use random color */
  private void paintRectangle(final Canvas canvas, final Point point) {
    brush.setARGB(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    int size = randInt(1,50);
    canvas.drawRect(point.x, point.y, point.x + (int)(size*0.8), point.y + (int)(size*1.2), brush);
  }

  public void setComplexity(final int value) {
    complexity = value+1;
  }
}
