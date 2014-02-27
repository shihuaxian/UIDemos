package gabriel.luoyer.promonkey.explist;

import gabriel.luoyer.promonkey.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;

public class ExpListSelectActivity extends Activity {
	private ExpandableListView mExpListView;
	private ExpListAdapter mExpListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explist_select);
		initViews();
	}
	
	private void initViews() {
		mExpListView = (ExpandableListView) findViewById(R.id.explist_select);
		// Set adapter
		mExpListAdapter = new ExpListAdapter();
		mExpListView.setAdapter(mExpListAdapter);
		mExpListView.setOnChildClickListener(mOnChildClickListener);		
	}

//	private OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {
//		@Override
//		public boolean onGroupClick(ExpandableListView parent, View v,
//				int groupPosition, long id) {
//			if(!mExpListView.isGroupExpanded(groupPosition)) {
//				
//			}
//			return false;
//		}
//	};
	
	private OnChildClickListener mOnChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			mExpListAdapter.onChildClick(groupPosition, childPosition);
			return true;
		}
	};
	
	private class ExpListAdapter extends BaseExpandableListAdapter {
		private int[] childResIds = new int[] {
			R.array.array_explist_child_wei_name,
			R.array.array_explist_child_shu_name,
			R.array.array_explist_child_wu_name
		};
		// data list
		private ArrayList<SelectChildData> list;
		// select child item id map
		private HashMap<Integer, HashSet<Integer>> mSelectIds;

		@SuppressLint("UseSparseArrays")
		public ExpListAdapter() {
			mSelectIds = new HashMap<Integer, HashSet<Integer>>();
			list = new ArrayList<SelectChildData>();
			String[] parent = getResources().getStringArray(R.array.array_explist_title_name);
			for(int i=0, size=parent.length; i<size; i++) {
				SelectChildData p = new SelectChildData();
				String[] child = getResources().getStringArray(childResIds[i]);
				ArrayList<SelectChildData> cl = new ArrayList<SelectChildData>();
				for(int j=0, len=child.length; j<len; j++) {
					SelectChildData c = new SelectChildData();
					c.id = j;
					c.name = child[j];
					cl.add(c);
				}
				p.id = i;
				p.name = parent[i];
				p.children = cl;
				list.add(p);
				mSelectIds.put(i, new HashSet<Integer>());
			}
		}
		
		public void onChildClick(int groupPosition, int childPosition) {
			HashSet<Integer> children = mSelectIds.get(groupPosition);
			if(children.contains(childPosition)) {
				children.remove(childPosition);
			} else {
				children.add(childPosition);
			}
			notifyDataSetChanged();
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if(list.get(groupPosition).children == null) {
				return null;
			}
			return list.get(groupPosition).children.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder;
			if(null == convertView) {
				convertView = View.inflate(ExpListSelectActivity.this, R.layout.explist_child_item, null);
				holder = new ChildViewHolder();
				holder.mContent = (TextView) convertView.findViewById(R.id.explist_child_item_name);
				holder.mIcon = (ImageView) convertView.findViewById(R.id.explist_child_item_select);
				convertView.setTag(holder);
			} else {
				holder = (ChildViewHolder) convertView.getTag();
			}
			SelectChildData data = (SelectChildData) getChild(groupPosition, childPosition);
			holder.mContent.setText(data.name);
			if(mSelectIds.get(groupPosition).contains(data.id)) {
				holder.mIcon.setSelected(true);
			} else {
				holder.mIcon.setSelected(false);
			}
			return convertView;
		}
		
		private class ChildViewHolder {
			protected TextView mContent;
			protected ImageView mIcon;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			if(list.get(groupPosition).children == null) {
				return 0;
			}
			return list.get(groupPosition).children.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return list.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder holder;
			if(null == convertView) {
				convertView = View.inflate(ExpListSelectActivity.this, R.layout.explist_group_item, null);
				holder = new GroupViewHolder();
				holder.mTitle = (TextView) convertView.findViewById(R.id.explist_group_title);
				holder.mSelNum = (TextView) convertView.findViewById(R.id.explist_group_selected_num);
				holder.mIndicator = (ImageView) convertView.findViewById(R.id.explist_group_indicator);
				convertView.setTag(holder);
			} else {
				holder = (GroupViewHolder) convertView.getTag();
			}
			
			SelectChildData data = (SelectChildData) getGroup(groupPosition);
			holder.mTitle.setText(data.name);
			ArrayList<SelectChildData>list = data.children;
			int cnt = 0, num = 0;
			if(null != list && !list.isEmpty()) {
				HashSet<Integer> cids = mSelectIds.get(groupPosition);
				for(SelectChildData d : list) {
					if(cids.contains(d.id)) {
						num ++;
					}
				}
				cnt = list.size();
			}
			holder.mSelNum.setText(
					String.format(getResources().getString(R.string.str_trans_receiver_num), num, cnt));
			if(isExpanded) {
				holder.mIndicator.setImageResource(R.drawable.icon_sub);
				convertView.setSelected(true);
			} else {
				holder.mIndicator.setImageResource(R.drawable.icon_add);
				convertView.setSelected(false);
			}
			return convertView;
		}
		
		private class GroupViewHolder {
			protected TextView mTitle;
			protected TextView mSelNum;
			protected ImageView mIndicator;
		}
		
		@Override
		public void onGroupExpanded(int groupPosition) {
			for(int i=0, cnt=getGroupCount(); i<cnt; i++) {
				if(groupPosition != i && mExpListView.isGroupExpanded(i)) {
					mExpListView.collapseGroup(i);
				}
			}
			super.onGroupExpanded(groupPosition);
		}
		
		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		public String getSelectInfo() {
			StringBuilder s = new StringBuilder();
			for(int i=0, size=mSelectIds.size(); i<size; i++) {
				HashSet<Integer> children = mSelectIds.get(i);
				if(!children.isEmpty()) {
					s.append(list.get(i).name);
					s.append(": ");
					ArrayList<SelectChildData> cl = list.get(i).children;
					for(int j=0, len=cl.size(); j<len; j++) {
						if(children.contains(cl.get(j).id)) {
							s.append(cl.get(j).name);
							s.append(" ");
						}
					}
					s.append("\n");
				}
			}
			if(s.length() > 0) {
				return s.toString();
			} else {
				return "No Selection";
			}
		}
		
		public void clearSelectInfo() {
			boolean clear = false;
			for(int i=0, size=mSelectIds.size(); i<size; i++) {
				HashSet<Integer> children = mSelectIds.get(i);
				if(!children.isEmpty()) {
					children.clear();
					if(!clear) clear = true;
				}
			}
			if(clear) notifyDataSetChanged();
		}
	};
	
	private class SelectChildData {
		public int id;
		public String name;
		public ArrayList<SelectChildData> children;
	}

	public void onOkClick(View view) {
		Toast.makeText(this, mExpListAdapter.getSelectInfo(), Toast.LENGTH_LONG).show();
	}
	
	public void onCancelClick(View view) {
		mExpListAdapter.clearSelectInfo();
	}
}
