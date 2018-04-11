package com.goodocom.gocsdk.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

public class MyEditText extends EditText {

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyEditText(Context context) {
		super(context);
	}
	private onFinishComposingListener mFinishComposingListener;
	public void setOnFinishComposingListener(onFinishComposingListener FinishComposingListener){
		this.mFinishComposingListener = FinishComposingListener;
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		return new MyInputConnection(super.onCreateInputConnection(outAttrs),false);
	}
	
	public class MyInputConnection extends InputConnectionWrapper{

		public MyInputConnection(InputConnection target, boolean mutable) {
			super(target, mutable);
		}

		@Override
		public boolean finishComposingText() {
			boolean finishComposing = super.finishComposingText();
			if(mFinishComposingListener!=null){
				mFinishComposingListener.finishComposing();
			}
			return finishComposing;
		}
		
	}
	public interface onFinishComposingListener{
		public void finishComposing();
	}
}
