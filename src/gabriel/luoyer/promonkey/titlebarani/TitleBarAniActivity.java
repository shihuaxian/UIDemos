package gabriel.luoyer.promonkey.titlebarani;

import gabriel.luoyer.promonkey.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

public class TitleBarAniActivity extends Activity implements View.OnClickListener {
	private ViewAnimator mBarSwitcher;
	private RelativeLayout mSecSet, mSecClass, mSecTk;
	private LinearLayout mMain;
	enum TopBarMode {Main, Set, Class, Tk};
	private TopBarMode mTopBarMode = TopBarMode.Main;
	private SafeAnimator mSafeAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_bar_ani);
		getViews();
		mSafeAnimator = new SafeAnimator();
	}

	private void getViews() {
		mBarSwitcher = (ViewAnimator) findViewById(R.id.title_bar_switcher);
		mMain = (LinearLayout) findViewById(R.id.title_bar_main);
		mSecSet = (RelativeLayout) findViewById(R.id.title_bar_sec_set);
		mSecClass = (RelativeLayout) findViewById(R.id.title_bar_sec_class);
		mSecTk = (RelativeLayout) findViewById(R.id.title_bar_sec_tk);
		// back button form secondary
		findViewById(R.id.title_bar_switch_set_back).setOnClickListener(this);
		findViewById(R.id.title_bar_switch_class_back).setOnClickListener(this);
		findViewById(R.id.title_bar_switch_tk_back).setOnClickListener(this);
		// click to display secondary
		findViewById(R.id.title_bar_switch_sec_set).setOnClickListener(this);
		findViewById(R.id.title_bar_switch_sec_class).setOnClickListener(this);
		findViewById(R.id.title_bar_switch_sec_tk).setOnClickListener(this);
	}

	private void toSecSet() {
		if(TopBarMode.Set != mTopBarMode) {
			mTopBarMode = TopBarMode.Set;
			mBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
			mSafeAnimator.startVisibleAnimator(this, mSecSet, R.anim.slide_top_in);
			mSafeAnimator.startInvisibleAnimator(this, mMain, R.anim.slide_bottom_out);
		}
	}
	
	private void toSecClass() {
		if(TopBarMode.Class != mTopBarMode) {
			mTopBarMode = TopBarMode.Class;
			mBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
			mSafeAnimator.startVisibleAnimator(this, mSecClass, R.anim.slide_top_in);
			mSafeAnimator.startInvisibleAnimator(this, mMain, R.anim.slide_bottom_out);
		}
	}
	
	private void toSecTk() {
		if(TopBarMode.Tk != mTopBarMode) {
			mTopBarMode = TopBarMode.Tk;
			mBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
			mSafeAnimator.startVisibleAnimator(this, mSecTk, R.anim.slide_top_in);
			mSafeAnimator.startInvisibleAnimator(this, mMain, R.anim.slide_bottom_out);
		}
	}
	
	private void backToMain(TopBarMode mode) {
		if(TopBarMode.Main != mTopBarMode) {
			mTopBarMode = TopBarMode.Main;
			mBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
			if(TopBarMode.Set == mode) {
				mSafeAnimator.startVisibleAnimator(this, mMain, R.anim.slide_bottom_in);
				mSafeAnimator.startInvisibleAnimator(this, mSecSet, R.anim.slide_top_out);				
			} else if(TopBarMode.Class == mode) {
				mSafeAnimator.startVisibleAnimator(this, mMain, R.anim.slide_bottom_in);
				mSafeAnimator.startInvisibleAnimator(this, mSecClass, R.anim.slide_top_out);	
			} else if(TopBarMode.Tk == mode) {
				mSafeAnimator.startVisibleAnimator(this, mMain, R.anim.slide_bottom_in);
				mSafeAnimator.startInvisibleAnimator(this, mSecTk, R.anim.slide_top_out);
			} else {
				mSafeAnimator.startVisibleAnimator(this, mMain, android.R.anim.fade_in);				
			}
		}
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.title_bar_switch_sec_set:
				toSecSet();
				break;
			case R.id.title_bar_switch_sec_class:
				toSecClass();
				break;
			case R.id.title_bar_switch_sec_tk:
				toSecTk();
				break;
			case R.id.title_bar_switch_set_back:
				backToMain(TopBarMode.Set);
				break;
			case R.id.title_bar_switch_class_back:
				backToMain(TopBarMode.Class);
				break;
			case R.id.title_bar_switch_tk_back:
				backToMain(TopBarMode.Tk);
				break;
		}
		
	}

}
