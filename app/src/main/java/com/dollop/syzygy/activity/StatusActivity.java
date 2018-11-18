package com.dollop.syzygy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.sohel.S;

public class StatusActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_status);

		Intent mainIntent = getIntent();
		TextView tv4 = (TextView) findViewById(R.id.textView1);
		tv4.setText(mainIntent.getStringExtra("transStatus"));
		S.E("transStatus"+mainIntent.getStringExtra("transStatus"));

		if(mainIntent.getStringExtra("transStatus").equals("Transaction Successful!"))
		{
		Intent intent=new Intent();
		setResult(101,intent);
		finish();
		}else{
			S.T(StatusActivity.this,"Failed");
			finish();
		}

	}
	/*
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}*/

} 