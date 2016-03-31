package com.extra.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.extra.R;
import com.extra.view.ExtraView;

public class MainActivity extends Activity {

	private ExtraView mExtraView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mExtraView = (ExtraView) findViewById(R.id.extraView);
		mExtraView.setMaxExtraNumber(5);
		mExtraView.setMaxPictureNumber(3);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		mExtraView.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mExtraView.onDestroy();
		mExtraView.clearDiskCache();
	}

}
