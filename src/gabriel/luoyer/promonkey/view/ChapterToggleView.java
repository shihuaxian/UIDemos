package gabriel.luoyer.promonkey.view;

import gabriel.luoyer.promonkey.utils.Utils;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class ChapterToggleView {
	private View mLayout;
	private View mMore;
	private boolean mBarsVisible;
	private final static int MSG_HIDDEN = 1;
	private final static long AUTO_DELAY = 2000;
	
	public ChapterToggleView(View layout, View more) {
		mLayout = layout;
		mMore = more;
		Utils.setVisible(mLayout);
		mAutoHiddenHandler.sendEmptyMessageDelayed(MSG_HIDDEN, AUTO_DELAY);
		mBarsVisible = true;
	}
	
	private Handler mAutoHiddenHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MSG_HIDDEN:
					hiddenBars();
					break;
			}
		}
	};
	
	public void toggleBarView() {
		if(mBarsVisible) {
			hiddenBars();
			return;
		}
		showBars();
	}
	
	public void showBars() {
		if(mBarsVisible) {
			return;
		}
		
		mBarsVisible = true;
		
		Utils.setVisible(mLayout);
		
		if(null != mMore) {
			Animation anim = new TranslateAnimation(mMore.getWidth(), 0, 0, 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					Utils.setVisible(mMore);
				}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) { }
			});
			mMore.startAnimation(anim);
		}
		
		mAutoHiddenHandler.sendEmptyMessageDelayed(MSG_HIDDEN, AUTO_DELAY);
	}
	
	public void hiddenBars() {
		if (!mBarsVisible) {
			return;
		}
		
		if(mAutoHiddenHandler.hasMessages(MSG_HIDDEN)) {
			mAutoHiddenHandler.removeMessages(MSG_HIDDEN);
		}

		mBarsVisible = false;
		
		Utils.setGone(mLayout);
		
		if(null != mMore) {
			Animation anim = new TranslateAnimation(0, mMore.getWidth(), 0, 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					Utils.setInvisible(mMore);
				}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) { }
			});
			mMore.startAnimation(anim);//
		}
	}
}
