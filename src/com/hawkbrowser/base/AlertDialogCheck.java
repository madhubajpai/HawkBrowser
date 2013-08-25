package com.hawkbrowser.base;

import com.hawkbrowser.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AlertDialogCheck extends DialogFragment {

	private boolean mChecked;
	private int mClickedBtnId;
	private int mMsgId;
	private int mPositiveBtnTextId;
	private int mNegativeBtnTextId;
	
	public AlertDialogCheck(int msgResId, 
			int positiveBtnTextId, int negativeBtnTextId) {
		
		mChecked = false;
		mClickedBtnId = 0;
		mMsgId = msgResId;
		mPositiveBtnTextId = positiveBtnTextId;
		mNegativeBtnTextId = negativeBtnTextId;
	}
	
	public boolean isConfirmed() {
		return mClickedBtnId == DialogInterface.BUTTON_POSITIVE;
	}
	
	public boolean isChecked() {
		return mChecked;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
		class DialogListener implements DialogInterface.OnClickListener,
			DialogInterface.OnMultiChoiceClickListener {
						
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				mClickedBtnId = id;
			}
			
			@Override
			public void onClick(DialogInterface dialog, int which, 
				boolean isChecked) {
				mChecked = isChecked;		
			}
		}
		
		DialogListener listener = new DialogListener();
		builder.setMessage(mMsgId);
		builder.setPositiveButton(mPositiveBtnTextId, listener);
		builder.setNegativeButton(mNegativeBtnTextId, listener);
		
		String checkText = getResources().getString(
			R.string.delete_file_same_time);
		builder.setMultiChoiceItems(new CharSequence[] { checkText },
			null, listener);
			
        return builder.create();
    }
}
