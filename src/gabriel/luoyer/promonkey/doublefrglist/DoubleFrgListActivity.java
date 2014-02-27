package gabriel.luoyer.promonkey.doublefrglist;

import java.util.ArrayList;
import java.util.HashMap;

import gabriel.luoyer.promonkey.R;
import gabriel.luoyer.promonkey.doublefrglist.DoubleListFirstFragment.onFirstListItemSelectedListener;
import gabriel.luoyer.promonkey.doublefrglist.DoubleListSecondFragment.onSecondListItemSelectedListener;
import gabriel.luoyer.promonkey.utils.Utils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DoubleFrgListActivity extends FragmentActivity 
						implements onFirstListItemSelectedListener,
								onSecondListItemSelectedListener {
	private final static String TAG = "DoubleFrgListActivity";
	private DoubleListFirstFragment mFirstFrg;
	private DoubleListSecondFragment mSecondFrg;
	private TextView mSelectInfo;
	
	private int mSelected;
	private HashMap<String, ArrayList<Boolean>> mStatus = null;
	private ArrayList<String> titles;
	private HashMap<String, ArrayList<String>> contents;
	private final static int MAX_SELECT = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_double_list);
		getViews();
		initData();
	}
	
	private void getViews() {
		FragmentManager fm = getSupportFragmentManager();
		mFirstFrg = (DoubleListFirstFragment) fm.findFragmentById(R.id.list_first_title_list);
		mSecondFrg = (DoubleListSecondFragment) fm.findFragmentById(R.id.list_second_content_list);
		mSelectInfo = (TextView) findViewById(R.id.list_double_list_select_info);
	}

	private void initData() {
		titles = new ArrayList<String>();
		contents = new HashMap<String, ArrayList<String>>();
		mStatus = new HashMap<String, ArrayList<Boolean>>();
		String[] parent = getResources().getStringArray(R.array.array_explist_title_name);
		String title = parent[0];
		titles.add(title);
		contents.put(title, transferContent(title, getResources().getStringArray(R.array.array_explist_child_wei_name)));
		title = parent[1];
		titles.add(title);
		contents.put(title, transferContent(title, getResources().getStringArray(R.array.array_explist_child_shu_name)));	
		title = parent[2];
		titles.add(title);
		contents.put(title, transferContent(title, getResources().getStringArray(R.array.array_explist_child_wu_name)));
		
		mSelected = 0;
		
		mFirstFrg.setListTitle(titles);
		mSecondFrg.setListContent(titles.get(0), contents.get(titles.get(0)));
		setSelectInfo(MAX_SELECT, mSelected);

	}
	
	private void setSelectInfo(int max, int selected) {
		String info = String.format(getResources().getString(R.string.str_list_double_list_select_info), max, selected);
		SpannableStringBuilder style = new SpannableStringBuilder(info);
		String colorStr = String.valueOf(max);
		int index = info.indexOf(colorStr);
		style.setSpan(new ForegroundColorSpan(Color.RED), index, index + colorStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		colorStr = String.valueOf(selected);
		// add start index for that status: max == selected
		index = info.indexOf(colorStr, index + 1);
		style.setSpan(new ForegroundColorSpan(Color.RED), index, index + colorStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		mSelectInfo.setText(style);
	}
	
	private ArrayList<String> transferContent(String title, String[] content) {
		ArrayList<Boolean> status = new ArrayList<Boolean>();
		ArrayList<String> cl = new ArrayList<String>();
		for(String c : content) {
			cl.add(c);
			status.add(false);
		}
		mStatus.put(title, status);
		return cl;
	}
	
	public boolean isSelected(String title, int position) {
		return mStatus.get(title).get(position);
	}

	@Override
	public boolean onSecondListItemSelected(int position, String title) {
		boolean refresh;
		boolean value = mStatus.get(title).get(position);
		Utils.logh(TAG, "onSecondListItemSelected title: " + title + " position: " + position +
				" value: " + value + " mSelected: "  + mSelected);
		if(value) {
			mSelected --;
			refresh = true;
		} else {
			if(mSelected < MAX_SELECT) {
				mSelected ++;
				refresh = true;
			} else {
				refresh = false;
			}
		}
		if(refresh) {
			mStatus.get(title).set(position, !value);
			setSelectInfo(MAX_SELECT, mSelected);
		}
		Utils.logh(TAG, "onSecondListItemSelected title: " + title + " position: " + position +
				" value: " + value + " mSelected: "  + mSelected + " refresh: " + refresh);
		
		return refresh;
	}

	@Override
	public void onFirstListItemSelected(int position) {
		String title = titles.get(position);
		mSecondFrg.setListContent(title, contents.get(title));
	}

	public void onConfirmBtnClick(View view) {
		Toast.makeText(this, getCurrentSelectedString(), Toast.LENGTH_LONG).show();
	}
	
	public String getCurrentSelectedString() {
		if(mSelected > 0) {
			StringBuilder sb = new StringBuilder();
			int index, cnt = 0;
			for(String key : titles) {
				index = 0;
				ArrayList<Boolean> status = mStatus.get(key);
				for(Boolean b : status) {
					if(b.booleanValue()) {
						sb.append(contents.get(key).get(index));
						cnt ++;
						if(cnt >= mSelected) {
							return sb.toString();
						}
						sb.append(",");
					}
					index ++;
				}
			}
		}
		return "No Selection";
	}
}
