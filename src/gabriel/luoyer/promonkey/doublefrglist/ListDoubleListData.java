package gabriel.luoyer.promonkey.doublefrglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Data type to save request response information for multiple list,
 * Note:
 * 		Save data to application variable, so, if the client enter and
 * 	exit setting interface repeatedly, we need not request each time.
 * 		Keep 'mMax', 'mTitles', and 'mContents' to be constant, for
 * 	those need not to be changed.
 * 		Manage 'mSelected' and 'mStatus' in addition variable, for we
 *  will change them while click list item. But the change maybe give up
 *  if click 'Back button' or 'Back key'. Only save data if click
 *  'Confirm button'.
 *  
 * @author Administrator
 *
 */
public class ListDoubleListData {
	public int mMax;
	public int mSelected;
	public ArrayList<String> mTitles;
	public HashMap<String, ArrayList<String>> mContents;
	public HashMap<String, ArrayList<Boolean>> mStatus;
	
	public ListDoubleListData(int max, int selected, ArrayList<String> titles,
								HashMap<String, ArrayList<String>> contents,
								HashMap<String, ArrayList<Boolean>> status) {
		mMax = max;
		
		mTitles = new ArrayList<String>();
		for(String str : titles) {
			mTitles.add(str);
		}
		
		mContents = new HashMap<String, ArrayList<String>>();
		Iterator<String> keyIter = contents.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			mContents.put(key, contents.get(key));
		}

		mSelected = selected;
		mStatus = new HashMap<String, ArrayList<Boolean>>();
		keyIter = status.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			ArrayList<Boolean> albOri = status.get(key);
			ArrayList<Boolean> albDst = new ArrayList<Boolean>();
			for(Boolean item : albOri) {
				albDst.add(Boolean.valueOf(item.booleanValue()));
			}
			mStatus.put(key, albDst);
		}
	}
	
	/**
	 * Refresh status, while click confirm button to exit.
	 * @param status The new data need to save
	 */
	public void refreshStatus(HashMap<String, ArrayList<Boolean>> status) {
		Iterator<String> keyIter = status.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			mStatus.put(key, status.get(key));
		}
	}
	
	/**
	 * Get status copy if data exist.
	 * Note:
	 * 		create new instance to return, rather than address, otherwise,
	 * 	the copy data will be changed while operate on it.
	 * @return The status data copy
	 */
	public HashMap<String, ArrayList<Boolean>> getStatusCopy() {
		HashMap<String, ArrayList<Boolean>> status = new HashMap<String, ArrayList<Boolean>>();
		Iterator<String> keyIter = mStatus.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			ArrayList<Boolean> albOri = mStatus.get(key);
			ArrayList<Boolean> albDst = new ArrayList<Boolean>();
			for(Boolean item : albOri) {
				albDst.add(Boolean.valueOf(item.booleanValue()));
			}
			status.put(key, albDst);
		}
		return status;
	}
	
	/**
	 * @return The combined string by selected items.
	 */
	public String getCurrentSelectedString() {
		if(mSelected > 0) {
			StringBuilder sb = new StringBuilder();
			int index, cnt = 0;
			for(String key : mTitles) {
				index = 0;
				ArrayList<Boolean> status = mStatus.get(key);
				for(Boolean b : status) {
					if(b.booleanValue()) {
						sb.append(mContents.get(key).get(index));
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
	
	/**
	 * @param selected
	 * 			Current selected number.
	 * @return true if the selected is less than the 'mMax',
	 * 			then, more item can be select;
	 * 		   false, if the selected is less than the 'mMax',
	 * 			and you can do select.
	 */
	public boolean canSelect(int selected) {
		return selected < mMax;
	}
	
	public String logString() {
		return "mMax: " + mMax + " mSelected: " + mSelected + " \r\n" +
				"mStatus: " + mStatus + " \r\n" +
				"mContents: " + mContents + "\r\n" +
				"mTitles: " + mTitles;
	}
}
