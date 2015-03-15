package org.nulleins.modart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import org.nulleins.Modart.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModartActivity extends Activity {

  private SeekBar seekBar;
  private SensorManager mSensorManager;
  private float mAccel; // acceleration apart from gravity
  private float mAccelCurrent; // current acceleration including gravity
  private float mAccelLast; // last acceleration including gravity
  private ArtworkView artCanvas;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mSensorManager.registerListener(mSensorListener,
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
    mAccel = 0.00f;
    mAccelCurrent = SensorManager.GRAVITY_EARTH;
    mAccelLast = SensorManager.GRAVITY_EARTH;

    artCanvas = (ArtworkView)findViewById(R.id.artcanvas);
    seekBar = (SeekBar) findViewById(R.id.seekBar);
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(final SeekBar seekBar, final int value, final boolean fromUser) {
        artCanvas.setComplexity(value);
        artCanvas.invalidate();
      }
      @Override
      public void onStartTrackingTouch(final SeekBar seekBar) {
      }
      @Override
      public void onStopTrackingTouch(final SeekBar seekBar) {
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_info:
        moreInfo();
        return true;
      case R.id.menu_item_share:
        shareArtwork();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void moreInfo() {
    final Dialog dialog = new Dialog(this);

    dialog.setContentView(R.layout.information);
    dialog.setTitle("More Information");

    final Button come2moma=(Button)dialog.findViewById(R.id.come2moma);
    come2moma.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View view) {
        gotoMoma();
      }
    });
    final Button procrastinate=(Button)dialog.findViewById(R.id.procrastinate);
    procrastinate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View view) {
          dialog.cancel();
      }
    });
    dialog.show();
  }

  /** invoke the 'share' chooser to share the current art canvas */
  private void shareArtwork() {
    final File imageFile = screenshotOf(artCanvas);
    if(imageFile != null) {
      final Intent shareIntent = new Intent();
      shareIntent.setAction(Intent.ACTION_SEND);
      shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
      shareIntent.setType("image/png");
      startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }
  }

  /** @return a temporary file object referencing a PNG screen-shot of <code>view</code>,
    * or <code>null</code> if creation of bitmap failed */
  private File screenshotOf(final View view) {
    view.setDrawingCacheEnabled(true);
    final Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
    try {
      final File imageFile = File.createTempFile("artshare", ".png", getApplicationContext().getCacheDir());
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(imageFile));
      return imageFile;
    } catch (IOException e) {
      Log.e("MOD", "Failed to store image to share: " + e.getMessage());
      return null;
    }
  }

  private void gotoMoma() {
    final Intent browserIntent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(getResources().getString(R.string.moma_landing_url)));
    startActivity(browserIntent);
  }

  private final SensorEventListener mSensorListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(final SensorEvent se) {
      final float x = se.values[0];
      final float y = se.values[1];
      final float z = se.values[2];
      mAccelLast = mAccelCurrent;
      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
      final float delta = mAccelCurrent - mAccelLast;
      mAccel = mAccel * 0.9f + delta * 0.1f; // low-cut filter
      if(Math.abs(delta) > 20.0) {
        findViewById(R.id.artcanvas).invalidate();
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(mSensorListener,
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    mSensorManager.unregisterListener(mSensorListener);
    super.onPause();
  }
}
