package gabriel.luoyer.promonkey.doublefrglist;

import gabriel.luoyer.promonkey.R;
import gabriel.luoyer.promonkey.utils.Utils;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DoubleListSecondFragment extends ListFragment {
	private static final String TAG = "DoubleListSecondFragment";
	private DoubleFrgListActivity activity;
	private boolean mAdapterSet = false;
	private ListView mListView = null;
	private MultiSelectAdapter mAdapter = null;
	private onSecondListItemSelectedListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.logh(TAG , " -2- onCreateView");
		View view = inflater.inflate(R.layout.list_double_list_second_frg, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
	    super.onActivityCreated(savedInstanceState);
	    mListView = getListView();
	    Utils.logh(TAG, "--------- onActivityCreated " + mListView + "\r\n mAdapter: " + mAdapter);
	    if(null != mAdapter && !mAdapterSet) {
	    	mListView.setAdapter(mAdapter);
	    	mAdapterSet = true;
	    }
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// reset data in activity
		if(mListener.onSecondListItemSelected(position, mAdapter.getCurrentTitle())) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (DoubleFrgListActivity) activity;
		try {
			mListener = (onSecondListItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onSecondListItemSelectedListener");
		}
	}
	
	protected void setListContent(String title, ArrayList<String> list) {
		Utils.logh(TAG, "setListContent --- mAdapterSet: " + mAdapterSet
							+ " mListView: " + mListView);
		if(null == mAdapter) {
			mAdapter = new MultiSelectAdapter();			
		}
		if(null != mListView && !mAdapterSet) {
	    	mListView.setAdapter(mAdapter);
	    	mAdapterSet = true;
		}
		mAdapter.refreshList(title, list);
	}

	private class MultiSelectAdapter extends BaseAdapter {
		private String mTitle;
		private ArrayList<String> mList;

		public MultiSelectAdapter() {
			mList = new ArrayList<String>();
		}

		public void refreshList(String title, ArrayList<String> list) {
			mTitle = title;
			mList.clear();
			for(String str : list) {
				mList.add(str);
			}
			notifyDataSetChanged();
		}
		
		public String getCurrentTitle() {
			return mTitle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(null == convertView) {
				convertView = View.inflate(activity, R.layout.list_double_list_second_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.list_dobule_list_sec_item_name);
				holder.icon = (ImageView) convertView.findViewById(R.id.list_dobule_list_sec_item_select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(activity.isSelected(mTitle, position)) {
				holder.icon.setSelected(true);
			} else {
				holder.icon.setSelected(false);
			}
			holder.text.setText(mList.get(position));
			
			return convertView;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private class ViewHolder {
			TextView text;
			ImageView icon;
		}
	}
	
	interface onSecondListItemSelectedListener {
		public abstract boolean onSecondListItemSelected(int position, String title);
	}
}
