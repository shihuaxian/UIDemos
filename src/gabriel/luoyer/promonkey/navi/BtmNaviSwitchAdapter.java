package gabriel.luoyer.promonkey.navi;

import java.util.ArrayList;

import android.support.v4.app.*;

public class BtmNaviSwitchAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> mFragments;
	
	public BtmNaviSwitchAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new NaviHpFragment());
		mFragments.add(new NaviLvFragment());
		mFragments.add(new NaviVpFragment());
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

}
