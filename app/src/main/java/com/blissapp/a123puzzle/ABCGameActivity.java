package com.blissapp.a123puzzle;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.animoto.android.views.DraggableGridView;
import com.animoto.android.views.OnRearrangeListener;

public class ABCGameActivity extends Activity {
	static Random random = new Random();
	static String results="1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";
	//static String[] words="A B C D E F G H I J K L M N O P Q R S T U V W X Y Z K".split(" ");
	String[] words;
	DraggableGridView dgv;
	Button button1, button2;
	TextView myText;
	private String word;
	ArrayList<String> poem = new ArrayList<String>();
	private CountDownTimer countDownTimer; // built in android class
	// CountDownTimer
	private long totalTimeCountInMilliseconds; // total count down time in
	// milliseconds
	private long timeBlinkInMilliseconds; // start time of start blinking
	private boolean blink; // controls the blinking .. on and off
	DisplayMetrics metrics;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		dgv = ((DraggableGridView)findViewById(R.id.vgv));
		myText = ((TextView)findViewById(R.id.txt_timer));
		Intent intent = getIntent();
		String mode = intent.getStringExtra("mode");
		// to shuffle the number 
		//http://www.amdz.com/shuffler.php
		if (mode != null) {
			if (mode.equals("easy")) {
				words = "1 9 4 8 6 10 2 7 3 11 5 16 20 13 18 12 17 15 19 14".split(" ");
				setTimer(5);
			} else if (mode.equals("moderate")) {
				words = "1 20 8 13 5 10 6 3 9 15 18 12 17 4 2 19 11 16 14 7".split(" ");
				setTimer(2);
			} else if (mode.equals("difficult")) {
				words = "6 16 18 8 20 11 9 5 12 2 19 14 4 3 10 15 1 7 13 17".split(" ");
				setTimer(1);
			}
		}
		addLetterInScreen();
		startTimer();
		setListeners();
		checkForResult();
	}


	private void setTimer(int value) {
		int time = value;
		totalTimeCountInMilliseconds = 60 * time * 1000;
		timeBlinkInMilliseconds = 30 * 1000;
	}


	private void startTimer() {
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
			// 500 means, onTick function will be called at every 500
			// milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				long seconds = leftTimeInMilliseconds / 1000;

				if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
					myText.setTextAppearance(getApplicationContext(),
							R.style.blinkText);
					// change the style of the textview .. giving a red
					// alert style

					if (blink) {
						myText.setVisibility(View.VISIBLE);
						// if blink is true, textview will be visible
					} else {
						myText.setVisibility(View.INVISIBLE);
					}

					blink = !blink; // toggle the value of blink
				}

				myText.setText(String.format("%02d", seconds / 60)
						+ ":" + String.format("%02d", seconds % 60));
				// format the textview to show the easily readable format

			}

			@Override
			public void onFinish() {
				// this function will be called when the timecount is finished
				myText.setText("Time up!");
				checkResult();
			}

		}.start();

	}


	private void checkResult() {
		String finishedPoem = "";
		String result;
		for (String s : poem)
			finishedPoem += s + " ";

		if(finishedPoem.trim().equals(results)){
			result = "Success";
		} else {
			result = "Failure";
		}

		final Dialog dialog = new Dialog(ABCGameActivity.this);
		dialog.setContentView(R.layout.custom);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout(metrics.widthPixels / 2, metrics.heightPixels / 2);
		
		TextView status = (TextView) dialog.findViewById(R.id.status);
		status.setText(result);
		
		TextView coupon = (TextView) dialog.findViewById(R.id.coupon);
		
		if (result.equals("Success")) {
			coupon.setText("2JS7ZR2N");
		} else {
			coupon.setText("Lost !!!");
		}

		Button dialogButton = (Button) dialog.findViewById(R.id.trybutton);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});

		dialog.show();
	}

	private void checkForResult() {
		String finishedPoem = "";
		String result;
		for (String s : poem)
			finishedPoem += s + " ";

		if(finishedPoem.trim().equals(results)){
			result = "Success";
			final Dialog dialog = new Dialog(ABCGameActivity.this);
			dialog.setContentView(R.layout.custom);
			dialog.getWindow().setLayout(metrics.widthPixels / 2, metrics.heightPixels / 2);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			TextView text = (TextView) dialog.findViewById(R.id.coupon);
			text.setText("2JS7ZR2N");

			TextView status = (TextView) dialog.findViewById(R.id.status);
			status.setText(result);

			Button dialogButton = (Button) dialog.findViewById(R.id.trybutton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});

			dialog.show();
		} else {
			result = "Failure";
		}

	}

	private void shareIt() {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Congrats, won the play. - Funny Apps";
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Info");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	} 

	private void addLetterInScreen() {
		for(int i=0; i<words.length ;i++){
			word = words[i];
			ImageView view = new ImageView(ABCGameActivity.this);
			view.setImageBitmap(getThumb(word));
			dgv.addView(view);
			poem.add(word);
		}
	}

	private void setListeners()
	{
		dgv.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				String word = poem.remove(oldIndex);
				if (oldIndex < newIndex)
					poem.add(newIndex, word);
				else
					poem.add(newIndex, word);
				checkForResult();
			}
		});
	}

	private Bitmap getThumb(String s)
	{
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();

		paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		paint.setTextSize(50);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);

		return bmp;
	}

	@Override
	protected void onDestroy() {
		countDownTimer.cancel();
		super.onDestroy();
	}
}