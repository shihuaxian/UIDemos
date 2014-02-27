package gabriel.luoyer.promonkey.licc;

import gabriel.luoyer.promonkey.R;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.*;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class LiChildClickActivity extends Activity {
	private TextView mHintInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_li_child_click);
		getViewsAndData();
	}

	private void getViewsAndData() {
		mHintInfo = (TextView) findViewById(R.id.licc_hint_info);
		ListView lv = (ListView) findViewById(R.id.licc_list);
		
		ArrayList<LiData> list = new ArrayList<LiData>();
		int[] childResIds = new int[] {
				R.array.array_explist_child_wei_name,
				R.array.array_explist_child_shu_name,
				R.array.array_explist_child_wu_name
			};
		String[] parent = getResources().getStringArray(R.array.array_explist_title_name);
		for(int i=0, size=parent.length; i<size; i++) {
			String[] child = getResources().getStringArray(childResIds[i]);
			for(String c : child) {
				LiData data = new LiData();
				data.country = parent[i];
				data.name = c;
				list.add(data);
			}
			
		}
		LiccAdapter adapter = new LiccAdapter(this, mHandle, list);
		lv.setAdapter(adapter);
	}
	
	private void onItemClicked(int position, LiData data) {
		mHintInfo.setText(String.format(getResources().getString(R.string.str_licc_item), 
				position, data.country, data.name));
	}

	private void onCountryClicked(LiData data) {
		mHintInfo.setText(String.format(getResources().getString(R.string.str_licc_btn_country), 
						data.name, data.country));
	}
	
	private void onNameClicked(LiData data) {
		mHintInfo.setText(String.format(getResources().getString(R.string.str_licc_tv_name), 
						data.country, data.name));	
	}
	
	private Handler mHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LiData data = (LiData) msg.getData().getSerializable(LiccAdapter.BUNDLE_KEY_LIDATA);
			switch(msg.what) {
				case LiccAdapter.CLICK_INDEX_ITEM:
					onItemClicked(msg.arg1, data);
					break;
				case LiccAdapter.CLICK_INDEX_COUNTRY:
					onCountryClicked(data);
					break;
				case LiccAdapter.CLICK_INDEX_NAME:
					onNameClicked(data);
					break;
			}
		}
		
	};
}
