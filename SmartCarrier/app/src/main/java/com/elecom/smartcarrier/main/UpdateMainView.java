package com.elecom.smartcarrier.main;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elecom.smartcarrier.R;

public class UpdateMainView {
    private final int STATE_OPEN = 0;
    private final int STATE_CLOSE = 1;

    private int mainStateImageArray[] = {
            R.drawable.ic_carrier_green,
            R.drawable.ic_carrier_red   };

    private int mainStateTextArray[] = {
            R.string.state_closed,
            R.string.state_opened   };

    private int mainLockImageArray[] = {
            R.drawable.ic_outline_lock_24,
            R.drawable.ic_outline_lock_open_24   };

    private int mainLockTextArray[] = {
            R.string.lock_closed,
            R.string.lock_opened   };

    private Activity mActivity;
    private ImageView mImgState;
    private ImageView mImgLock;
    private TextView mTxtState;
    private TextView mTxtLock;

    public UpdateMainView(Activity activity) {
        this.mActivity = activity;
    }

    public void initView() {
        //Bottom Fine Dust Image
        mImgState = (ImageView)mActivity.findViewById(R.id.img_state);

        //Bottom Fine Dust Image
        mImgLock = (ImageView)mActivity.findViewById(R.id.img_lock);

        //Time Text
        mTxtState = (TextView)mActivity.findViewById(R.id.text_state);

        //Fine Dust Text
        mTxtLock = (TextView)mActivity.findViewById(R.id.text_lock);
    }

    public void updateView(int openState, int lockState) {
        if(null != mImgState)
        {
            mImgState.setImageDrawable(mActivity.getDrawable(mainStateImageArray[openState]));
        }

        if(null != mImgLock)
        {
            mImgLock.setImageDrawable(mActivity.getDrawable(mainLockImageArray[lockState]));
        }

        if(null != mTxtState)
        {
            mTxtState.setText(mainStateTextArray[openState]);
        }

        if(null != mTxtLock)
        {
            mTxtLock.setText(mainLockTextArray[lockState]);
        }
    }
}
