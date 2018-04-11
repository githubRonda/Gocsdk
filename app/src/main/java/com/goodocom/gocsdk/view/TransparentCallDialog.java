package com.goodocom.gocsdk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.goodocom.gocsdk.R;

public class TransparentCallDialog extends AlertDialog {
	private View view;

	public TransparentCallDialog(Context context, int theme) {
		super(context, theme);
		view = View.inflate(context, R.layout.dialog_call, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}
	@Override
	public View findViewById(int id) {
		return super.findViewById(id);
	}
	public View getCustomView(){
		return view;
	}

	@Override
	public void onBackPressed() {
	}
}
