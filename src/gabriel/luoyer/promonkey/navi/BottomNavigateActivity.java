package gabriel.luoyer.promonkey.navi;

import gabriel.luoyer.promonkey.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BottomNavigateActivity extends FragmentActivity {
	private RadioGroup mSwitcher;
	private ViewPager mSearchVp;
	private final int CB_INDEX_HP = 0;
	private final int CB_INDEX_LV = 1;
	private final int CB_INDEX_VP = 2;
	
	public static void startBottomNavigateActivity(Context context) {
		context.startActivity(
			new Intent(context, BottomNavigateActivity.class)
		);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btm_navi);
		getViews();
	}
	
	private void getViews() {
		mSwitcher = (RadioGroup) findViewById(R.id.navi_switcher);
		mSwitcher.setOnCheckedChangeListener(mCheckedChgLitener);
		mSearchVp = (ViewPager) findViewById(R.id.navi_view_pager);
		mSearchVp.setAdapter(new BtmNaviSwitchAdapter(getSupportFragmentManager()));
		mSearchVp.setOnPageChangeListener(mPageChgListener);
	}
	
	private OnCheckedChangeListener mCheckedChgLitener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int cur = CB_INDEX_HP;
			switch(checkedId) {
				case R.id.navi_switcher_item_hp:
					cur = CB_INDEX_HP;
					break;
				case R.id.navi_switcher_item_lv:
					cur = CB_INDEX_LV;
					break;
				case R.id.navi_switcher_item_vp:
					cur = CB_INDEX_VP;
					break;
			}
			if(mSearchVp.getCurrentItem() != cur) {
				mSearchVp.setCurrentItem(cur);
			}
		}
	};
	
	private OnPageChangeListener mPageChgListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

		@Override
		public void onPageSelected(int position) {
			int vpItem = mSearchVp.getCurrentItem();
			switch(vpItem) {
				case CB_INDEX_HP:
					mSwitcher.check(R.id.navi_switcher_item_hp);
					break;
				case CB_INDEX_LV:
					mSwitcher.check(R.id.navi_switcher_item_lv);
					break;
				case CB_INDEX_VP:
					mSwitcher.check(R.id.navi_switcher_item_vp);
					break;
			}
		}

	};

}
