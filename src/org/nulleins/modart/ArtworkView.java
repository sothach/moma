package org.nulleins.modart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import static android.os.SystemClock.sleep;

public class ArtworkView extends View {

  private Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
  private int sparsity = 2;

  public ArtworkView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onDraw(final Canvas canvas) {
    createArtwork(canvas, getMeasuredWidth(), getMeasuredHeight());
    canvas.save ();
  }

  /** draw randomly colored and sized rectangles, randomly on a spiral path */
  private void createArtwork(final Canvas canvas, final int width, final int height) {
    float r = 1;
    float theta = 0.0f;
    while(theta < 360.0f) {
      final Point point = new Point( // convert polar to cartesian coordinates
          scale(r * (float)Math.cos(theta), 200, width),
          scale(r * (float)Math.sin(theta), 200, height));
      theta += randFloat(0.01f, (float) sparsity);
      r += randFloat(0.01f, (float) sparsity);

      drawRandomDoor(canvas, width, height, point);
    }
    drawWhiteDoor(canvas, width, height);
  }

  private void drawRandomDoor(Canvas canvas, int width, int height, final Point point) {
    brush.setARGB(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    paintRectangle(canvas, point);
  }

  private void drawWhiteDoor(Canvas canvas, int width, int height) {
    final Point whiteDoor = new Point(randInt(10, width), randInt(10, height));
    final int size = randInt(1, 50);
    brush.setARGB(255, 255, 255, 255);
    final Rect door = new Rect(whiteDoor.x, whiteDoor.y, whiteDoor.x + (int) (size * 0.8), whiteDoor.y + (int) (size * 1.2));
    canvas.drawRect(door, brush);
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
    final int size = randInt(1,50);
    canvas.drawRect(point.x, point.y, point.x + (int)(size*0.8), point.y + (int)(size*1.2), brush);
  }

  public void setSparsity(final int value) {
    sparsity = value+1;
  }
}
