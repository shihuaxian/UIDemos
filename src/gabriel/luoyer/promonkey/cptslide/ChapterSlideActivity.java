package gabriel.luoyer.promonkey.cptslide;

import gabriel.luoyer.promonkey.R;
import gabriel.luoyer.promonkey.cptslide.ChapterFragment.onChapterPageClickListener;
import gabriel.luoyer.promonkey.utils.Utils;
import gabriel.luoyer.promonkey.view.ChapterToggleView;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author gluoyer@gmail.com
 *
 */
public class ChapterSlideActivity extends FragmentActivity implements onChapterPageClickListener {
	private ChapterToggleView mToggleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter);
		
		getViews();
		getFlowView();
	}

	private void getViews() {
		String[] names = getResources().getStringArray(R.array.array_cpt_slide_name);
		ViewPager vp = (ViewPager) findViewById(R.id.cpt_view_pager);
		final LinearLayout indexContainer = (LinearLayout) findViewById(R.id.cpt_index_container);
		// Get names list, and Alloc for page.
		int len = names.length;
		final int pn = ChapterAdapter.CHAPTER_PAGE_NUM;
		final int loop = len / pn + (len % pn == 0 ? 0 : 1);
		Utils.logh("ChapterSlideActivity", "name length: " + len + " loop: " + loop);
		ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
		for(int i=0; i<loop; i++) {
			ArrayList<String> list = new ArrayList<String>();
			int base = i * pn;
			int rang = base + pn > len ? len : base + pn;
			for(int j=base+0; j<rang; j++) {
				list.add(names[j]);
			}
			arrayLists.add(list);
		}
		// Set adapter for ViewPager, in order to slide.
		vp.setAdapter(new ChapterAdapter(getSupportFragmentManager(), arrayLists));
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }
	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
	
			@Override
			public void onPageSelected(int position) {
				mToggleView.hiddenBars();
				for(int i=0; i<loop; i++) {
					indexContainer.getChildAt(i).setSelected(i == position?true:false);
				}
			}
		});
		// Bottom page select navigation
		if(loop > 1) {
			Utils.setVisible(indexContainer);
			for(int i=0; i<loop; i++) {
				ImageView focus;
				focus = new ImageView(this);
				focus.setBackgroundResource(R.drawable.shelf_circle_selector);
				indexContainer.addView(focus);
				focus.setSelected(i == 0?true:false);
			}
		} else {
			Utils.setInvisible(indexContainer);
		}
	}

	private void getFlowView() {
		// Add flow buttons layout, and buttons click listener
		View buttons = View.inflate(this, R.layout.chapter_toggle_buttons, null);
		addContentView(buttons, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		View more = buttons.findViewById(R.id.cpt_right_more);
		mToggleView = new ChapterToggleView(buttons, more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mToggleView.hiddenBars();
				Toast.makeText(ChapterSlideActivity.this, R.string.str_toast_click_anzai, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onChapterItemClick(int position, String name) {
		Toast.makeText(this, "Click " + (position + 1) + ": " + name, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onChapterSpaceClick() {
		mToggleView.toggleBarView();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mToggleView.toggleBarView();
		return super.onPrepareOptionsMenu(menu);
	}
}
