package gabriel.luoyer.promonkey.cptslide;


import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author gluoyer@gmail.com
 *
 */
public class ChapterAdapter extends FragmentPagerAdapter{
	public final static int CHAPTER_PAGE_NUM = 9;
	private ArrayList<Fragment> mFragments;
	
	public ChapterAdapter(FragmentManager fm, ArrayList<ArrayList<String>> arrayLists) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		int startPos = 0; // count the click offset
		for(ArrayList<String>list : arrayLists) {
			mFragments.add(ChapterFragment.getNewInstance(startPos * CHAPTER_PAGE_NUM, list));
			startPos ++;
		}
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
