package gabriel.luoyer.promonkey.titlebarani;

import gabriel.luoyer.promonkey.utils.Utils;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class SafeAnimator {
	
	public void startInvisibleAnimator(Context context, final View target, int animationId) {
		Animation anim = AnimationUtils.loadAnimation(context, animationId);
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				Utils.setVisible(target);
			}
		
			@Override
			public void onAnimationEnd(Animation animation) {
				Utils.setInvisible(target);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		target.startAnimation(anim);
	}
	
	public void startVisibleAnimator(Context context, final View target, int animationId) {
		Animation anim = AnimationUtils.loadAnimation(context, animationId);
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				Utils.setVisible(target);
			}
		
			@Override
			public void onAnimationEnd(Animation animation) { }

			@Override
			public void onAnimationRepeat(Animation animation) { }
		});
		target.startAnimation(anim);
	}
}
