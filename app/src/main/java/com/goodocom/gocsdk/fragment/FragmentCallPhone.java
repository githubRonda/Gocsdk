package com.goodocom.gocsdk.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;

public class FragmentCallPhone extends Fragment implements OnClickListener {

	
	private MainActivity activity;

	private StringBuffer sb;
	private ImageView iv_one;
	private ImageView iv_two;
	private ImageView iv_three;
	private ImageView iv_four;
	private ImageView iv_five;
	private ImageView iv_six;
	private ImageView iv_seven;
	private ImageView iv_eight;
	private ImageView iv_nine;
	private ImageView iv_xinghao;
	private ImageView iv_zero;
	private ImageView iv_jinghao;
	private ImageView iv_callout;
	private ImageView iv_delete;
	private TextView tv_phonenumber;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		sb = new StringBuffer();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(activity, R.layout.fragmentcallphone, null);
		iv_one = (ImageView) view.findViewById(R.id.iv_one);
		iv_two = (ImageView) view.findViewById(R.id.iv_two);
		iv_three = (ImageView) view.findViewById(R.id.iv_three);
		iv_four = (ImageView) view.findViewById(R.id.iv_four);
		iv_five = (ImageView) view.findViewById(R.id.iv_five);
		iv_six = (ImageView) view.findViewById(R.id.iv_six);
		iv_seven = (ImageView) view.findViewById(R.id.iv_seven);
		iv_eight = (ImageView) view.findViewById(R.id.iv_eight);
		iv_nine = (ImageView) view.findViewById(R.id.iv_nine);
		iv_xinghao = (ImageView) view.findViewById(R.id.iv_xinghao);
		iv_zero = (ImageView) view.findViewById(R.id.iv_zero);
		iv_jinghao = (ImageView) view.findViewById(R.id.iv_jinghao);
		iv_callout = (ImageView) view.findViewById(R.id.iv_callout);
		iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
		tv_phonenumber = (TextView) view.findViewById(R.id.tv_phonenumber);

		iv_one.setOnClickListener(this);
		iv_two.setOnClickListener(this);
		iv_three.setOnClickListener(this);
		iv_four.setOnClickListener(this);
		iv_five.setOnClickListener(this);
		iv_six.setOnClickListener(this);
		iv_seven.setOnClickListener(this);
		iv_eight.setOnClickListener(this);
		iv_nine.setOnClickListener(this);
		iv_xinghao.setOnClickListener(this);
		iv_zero.setOnClickListener(this);
		iv_jinghao.setOnClickListener(this);
		iv_callout.setOnClickListener(this);
		iv_delete.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_one:
			sb.append("1");
			break;
		case R.id.iv_two:
			sb.append("2");
			break;
		case R.id.iv_three:
			sb.append("3");
			break;
		case R.id.iv_four:
			sb.append("4");
			break;
		case R.id.iv_five:
			sb.append("5");
			break;
		case R.id.iv_six:
			sb.append("6");
			break;
		case R.id.iv_seven:
			sb.append("7");
			break;
		case R.id.iv_eight:
			sb.append("8");
			break;
		case R.id.iv_nine:
			sb.append("9");
			break;
		case R.id.iv_xinghao:
			sb.append("*");
			break;
		case R.id.iv_zero:
			sb.append("0");
			break;
		case R.id.iv_jinghao:
			sb.append("#");
			break;
		case R.id.iv_callout:
			if (GocsdkCallbackImp.hfpStatus >= 3) {
				String number = tv_phonenumber.getText().toString().trim();
				if (TextUtils.isEmpty(number)) {
					Toast.makeText(activity, "请输入电话号码", Toast.LENGTH_SHORT).show();
				} else {
					placeCall(number);
				}
			} else {
				Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_delete:
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			break;
		}
		tv_phonenumber.setText(sb.toString());
	}

	// 拨打正确的电话
	private static void placeCall(String number) {
		if (number.length() == 0) return;
        if(MainActivity.getService() == null)return;

		if (PhoneNumberUtils.isGlobalPhoneNumber(number)) {
			if (number == null || !TextUtils.isGraphic(number)) {
				return;
			}
			try {
				MainActivity.getService().phoneDail(number);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
