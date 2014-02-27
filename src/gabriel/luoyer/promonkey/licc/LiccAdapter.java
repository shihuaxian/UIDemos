package gabriel.luoyer.promonkey.licc;

import gabriel.luoyer.promonkey.R;

import java.util.ArrayList;

import android.content.Context;
import android.os.*;
import android.view.*;
import android.widget.*;

public class LiccAdapter extends BaseAdapter {
	private Context context;
	// click index
	protected final static int CLICK_INDEX_ITEM = 0;
	protected final static int CLICK_INDEX_COUNTRY = 1;
	protected final static int CLICK_INDEX_NAME = 2;
	// data list
	private ArrayList<LiData> list;
	// hadler to transfer data by message
	private Handler mHandle;
	// bundle LiData data key
	protected final static String BUNDLE_KEY_LIDATA = "lidata";
	
	public LiccAdapter(Context context, Handler handle, ArrayList<LiData> list) {
		this.context = context;
		mHandle = handle;
		this.list = new ArrayList<LiData>();
		for(LiData data : list) {
			this.list.add(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler holder = null;
		if(null == convertView) {
			convertView = View.inflate(context, R.layout.li_child_click_item, null);
			holder = new ViewHodler();
			holder.mCountry = (Button) convertView.findViewById(R.id.licc_btn_country);
			holder.mName = (TextView) convertView.findViewById(R.id.licc_tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHodler) convertView.getTag();
		}
		LiData data = list.get(position);
		holder.mCountry.setText(data.country);
		holder.mCountry.setOnClickListener(new OnItemChildClickListener(CLICK_INDEX_COUNTRY, position));
		holder.mName.setText(data.name);
		holder.mName.setOnClickListener(new OnItemChildClickListener(CLICK_INDEX_NAME, position));
		convertView.setOnClickListener(new OnItemChildClickListener(CLICK_INDEX_ITEM, position));
		return convertView;
	}

	private class ViewHodler {
		protected Button mCountry;
		protected TextView mName;
	}
	
	private class OnItemChildClickListener implements View.OnClickListener {
		private int clickIndex;
		private int position;
		
		public OnItemChildClickListener(int clickIndex, int position) {
			this.clickIndex = clickIndex;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Message msg = new Message();
			msg.what = clickIndex;
			msg.arg1 = position;
			Bundle b = new Bundle();
			b.putSerializable(BUNDLE_KEY_LIDATA, list.get(position));
			msg.setData(b);
			mHandle.sendMessage(msg);
		}
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
