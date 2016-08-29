package com.blissapp.a123puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button easy;
	private Button moderate;
	private Button difficult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		easy = (Button)findViewById(R.id.button1);
		moderate = (Button) findViewById(R.id.button2);
		difficult = (Button) findViewById(R.id.button3);
		easy.setOnClickListener(this);
		moderate.setOnClickListener(this);
		difficult.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button2:
			startActivityWithIntent("easy");
			break;
		case R.id.button1:
			startActivityWithIntent("moderate");
			break;
		case R.id.button3:
			startActivityWithIntent("difficult");
			break;
		default:
			break;
		}

	}
	
	private void startActivityWithIntent(String mode) {
		Intent intent = new Intent(this,ABCGameActivity.class);
		intent.putExtra("mode", mode);
		startActivity(intent);
	}

}
