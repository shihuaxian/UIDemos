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
import android.widget.ListView;
import android.widget.TextView;

public class DoubleListFirstFragment extends ListFragment {
	private static final String TAG = "DoubleListFirstFragment";
	private int mClickPosition = 0;
	private boolean mAdapterSet = false;
	private ListView mListView = null;
	private FirstTitleAdapter mAdapter = null;
	private onFirstListItemSelectedListener mListener;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.logh(TAG , " -1- onCreateView");
		View view = inflater.inflate(R.layout.list_double_list_first_frg, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
	    super.onActivityCreated(savedInstanceState);
	    mListView = getListView();
	    Utils.logh(TAG, "========= onActivityCreated " + mListView + "\r\n mAdapter: " + mAdapter);
	    if(null != mAdapter && !mAdapterSet) {
	    	mListView.setAdapter(mAdapter);
	    }
	}
	
	protected void setListTitle(ArrayList<String> titles) {
		Utils.logh(TAG, "setListContent +++ mAdapterSet: " + mAdapterSet
				+ " mListView: " + mListView);
		if(null == mAdapter) {
			mAdapter = new FirstTitleAdapter(titles);
		}
	    if(null != mListView && !mAdapterSet) {
	    	mListView.setAdapter(mAdapter);
	    }
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(position == mClickPosition) {
			return ;
		}
		mClickPosition = position;
		mListener.onFirstListItemSelected(position);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (onFirstListItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onFirstListItemSelectedListener");
		}
		
	}

	private class FirstTitleAdapter extends BaseAdapter {
		private ArrayList<String> mList;

		public FirstTitleAdapter(ArrayList<String> titles) {
			mList = titles;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tag;
			if(null == convertView) {
				convertView = View.inflate(getActivity(), R.layout.list_double_list_first_item, null);
				tag = (TextView) convertView.findViewById(R.id.list_dobule_list_first_item_title);
				convertView.setTag(tag);
			} else {
				tag = (TextView) convertView.getTag();
			}
			if(position == mClickPosition) {
				convertView.setBackgroundColor(getResources().getColor(R.color.color_list_double_list_select_bkg));
			} else {
				convertView.setBackgroundColor(getResources().getColor(R.color.color_transparent));
			}
			tag.setText(mList.get(position));// 
			
			return convertView;
		}
		
	}
	
	interface onFirstListItemSelectedListener {
		public abstract void onFirstListItemSelected(int position);
	}
}
