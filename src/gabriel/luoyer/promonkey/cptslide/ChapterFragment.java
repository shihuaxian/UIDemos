package gabriel.luoyer.promonkey.cptslide;

import gabriel.luoyer.promonkey.R;
import gabriel.luoyer.promonkey.utils.Utils;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author gluoyer@gmail.com
 *
 */
public class ChapterFragment extends Fragment {
	private static final String TAG = "ChapterFragment";
	private final static String BUNDLE_KEY_NAME_LIST = "key_cpt_name_list";
	private final static String BUNDLE_KEY_START_POS = "key_cpt_start_pos";
	private int mStartPos; // start position for current fragment
	private ArrayList<String> mNameList;
	private onChapterPageClickListener mClickListener;

	public static ChapterFragment getNewInstance(int startPos, ArrayList<String> list) {
		Utils.logh(TAG, "getNewInstance startPos: " + startPos + " list(" + list.size() + ") ");
		ChapterFragment fragment = new ChapterFragment();
		Bundle b = new Bundle();
		b.putStringArrayList(BUNDLE_KEY_NAME_LIST, list);
		b.putInt(BUNDLE_KEY_START_POS, startPos);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mNameList = args.getStringArrayList(BUNDLE_KEY_NAME_LIST);
		mStartPos = args.getInt(BUNDLE_KEY_START_POS);
		Utils.logh(TAG, "onCreate mStartPos: " + mStartPos + " mNameList(" + mNameList.size() + ") ");
		// fill empty item, for click
		if(mNameList.size() < ChapterAdapter.CHAPTER_PAGE_NUM) {
			for(int i=mNameList.size(); i<ChapterAdapter.CHAPTER_PAGE_NUM; i++) {
				mNameList.add(null);
			}
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mClickListener = (onChapterPageClickListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName() 
                    + " must implement onChapterPageClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Utils.logh(TAG, "onCreateView mNameList(" + mNameList.size() + ")");
		View view = inflater.inflate(R.layout.chapter_gridview_frg, container, false);
		GridView gv = (GridView) view.findViewById(R.id.chapter_gridview);
		gv.setAdapter(new ChapterGvAdapter());
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickListener.onChapterSpaceClick();
			}
		});
		return view;
	}
	
	private class ChapterGvAdapter extends BaseAdapter {

		public ChapterGvAdapter() {}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHoder holder;
			if(null == convertView) {
				holder = new ViewHoder();
				convertView = View.inflate(getActivity(), R.layout.chapter_gridview_item, null);
				holder.mSelectRegion = (LinearLayout) convertView.findViewById(R.id.cpt_gv_item_select_region);
				holder.mName = (TextView) convertView.findViewById(R.id.cpt_gv_item_name);
				holder.mIcon = (ImageView) convertView.findViewById(R.id.cpt_gv_item_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHoder) convertView.getTag();
			}
			/**
			 * 		One item for GridView
			 * 		________________
			 * 		|  1_________	|	
			 * 		|	|  2	 |	|
			 * 		|	|   I    |	|
			 * 		|	|   m    |	|
			 * 		|	|   g    |	|
			 * 		|	|  name  |	|
			 * 		|	|________|	|
			 * 		|_______________|
			 * 		
			 * 	1. All item region: convertView.
			 * 	2. content region: one image and name there.
			 * 	between 1, 2. click space to display or hidden flow views,
			 * 		event for onChapterSpaceClick following.
			 */
			// shadow the default GridView item click event
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mClickListener.onChapterSpaceClick();
				}
			});
			// Has content, set its click listener
			if(null != mNameList.get(position)) {
				holder.mSelectRegion.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mClickListener.onChapterItemClick(mStartPos + position, mNameList.get(position));
					}
				});
				Utils.setVisible(holder.mSelectRegion);
				holder.mName.setText(mNameList.get(position));
			}
			// no content, do the same as convertView do.
			// if not set this listener, click no content space will no response.
			else {
				holder.mSelectRegion.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mClickListener.onChapterSpaceClick();
					}
				});
				Utils.setInvisible(holder.mSelectRegion);
			}
			// Default image, do transfer and set what your want, like i set name upside.
			holder.mIcon.setImageResource(R.drawable.ic_launcher);
			return convertView;
		}
		
		@Override
		public int getCount() {
			return mNameList.size();
		}

		@Override
		public Object getItem(int position) {
			return mNameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class ViewHoder {
			LinearLayout mSelectRegion;
			TextView mName;
			ImageView mIcon;
		}
	}
	
	public interface onChapterPageClickListener {
		public abstract void onChapterItemClick(int position, String name);
		public abstract void onChapterSpaceClick();
	}
}
