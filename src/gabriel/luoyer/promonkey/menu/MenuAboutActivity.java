package gabriel.luoyer.promonkey.menu;

import gabriel.luoyer.promonkey.R;
import gabriel.luoyer.promonkey.utils.*;

import java.io.*;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

public class MenuAboutActivity extends Activity {
	private static final String TAG = "MenuAboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	public void clickMailTo(View view) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String [] {getResources().getString(R.string.str_ma_mail)});
		startActivity(Intent.createChooser(emailIntent, null));
	}
	
	public void clickAccessHomePage(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.str_ma_hompage)));
		startActivity(intent);
	}
	
	public void clickOtherApp(View view) {
		AppConnect.getInstance(MenuAboutActivity.this).showMore(MenuAboutActivity.this);
	}
	
	public void clickRecommend(View view) {
		AppConnect.getInstance(MenuAboutActivity.this).showOffers(MenuAboutActivity.this);
	}
	
	public void clickGetSource(View view) {
		mHandle.sendEmptyMessage(MSG_SUPPORT);
	}
	
	private final static int MSG_SUPPORT = 1;
	private final static int MSG_DOWNLOAD_DIALOG = 2;
	private final static int MSG_SCORE_DIALOG = 3;
	private Handler mHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case MSG_SUPPORT:
					getSupportDialog();
					break;
				case MSG_DOWNLOAD_DIALOG:
					downloadDialog();
					break;
				case MSG_SCORE_DIALOG:
					scoreDialog();
					break;
			}
		}
	};

	protected void getSupportDialog() {
		showWaitDialog();
		AppConnect.getInstance(this).getPoints(new UpdatePointsNotifier() {
			@Override
			public void getUpdatePoints(String msg, int score) {
				Utils.logh(TAG, "getUpdatePoints msg: " + msg + " score: " + score);
				if(score > 0) {
					mHandle.sendEmptyMessage(MSG_DOWNLOAD_DIALOG);
				} else {
					mHandle.sendEmptyMessage(MSG_SCORE_DIALOG);
				}
				dismissWaitDialog();
			}

			@Override
			public void getUpdatePointsFailed(String msg) {
				dismissWaitDialog();
				Utils.logh(TAG, "getUpdatePointsFailed msg: " + msg);
				Toast.makeText(MenuAboutActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
			
		});		
	}
	
	private void downloadDialog() {
		new AlertDialog.Builder(MenuAboutActivity.this)
			.setTitle(R.string.str_ma_get_source_ok_dialog_title)
			.setMessage(String.format(
					getResources().getString(R.string.str_ma_get_source_ok_dialog_msg), Utils.mkTmpDirs()))
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	copyBackground();
	            	dialog.dismiss();
	            }
			})
	        .setCancelable(false)
			.create()
			.show();
	}
	
	private void scoreDialog() {
		new AlertDialog.Builder(MenuAboutActivity.this)
			.setTitle(R.string.str_ma_get_source_dialog_title)
			.setMessage(R.string.str_ma_get_source_dialog_msg)
			.setPositiveButton(R.string.str_ma_get_source_dialog_btn_go, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
					AppConnect.getInstance(MenuAboutActivity.this).showOffers(MenuAboutActivity.this);
	            }
			})
			.setNegativeButton(R.string.str_ma_get_source_dialog_btn_cancel,  new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	;
	            }
			})
	        .setCancelable(false)
			.create()
			.show();
	}
	
	private void copyBackground() {
		showWaitDialog();
		new AsyncTask<Object, Object, String>() {
			@Override
			protected String doInBackground(Object... params) {
				return doAsyncOperations();
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dismissWaitDialog();
				showConfirmDialog(result);
			}
		}.execute(null, null, null);
	}

	private void showConfirmDialog(String result) {
		new AlertDialog.Builder(MenuAboutActivity.this)
			.setTitle(R.string.str_ma_get_source_dialog_title_ok)
			.setMessage(String.format(
					getResources().getString(R.string.str_ma_get_source_dialog_msg_ok), result))
			.setNegativeButton(R.string.str_ma_get_source_dialog_btn_rego, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
					AppConnect.getInstance(MenuAboutActivity.this).showOffers(MenuAboutActivity.this);
	            }
			})
			.setPositiveButton(android.R.string.ok,  new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	;
	            }
			})
	        .setCancelable(false)
			.create()
			.show();
	}

	private String doAsyncOperations() {
		String dir = Utils.mkTmpDirs();
		// First clear directory
		File dirs = new File(dir);
		if(dirs.exists()) {
			File[] files = dirs.listFiles();
			if(files.length > 0) {
				for(File f : files) {
					f.delete();
				}
			}
		}
		// Do copy
		File file = new File(dir, Utils.FILE_BASE_NAME + System.currentTimeMillis() + ".rar");
		String origin = Utils.getFilePathName(Utils.FILE_ASSETS_PATH, Utils.FILE_BASE_NAME);
		Utils.logh(TAG, "origin: " + origin);
		try {
			InputStream is = getAssets().open(origin);
			String path = saveFileToLocal(file, is);
			Utils.logh(TAG, "out: " + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	
	private String saveFileToLocal(File file, InputStream input) {
        byte[] buffer = new byte[8192];
        int n = 0;
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);     
            while ((n = input.read(buffer)) >= 0) {     
                output.write(buffer, 0, n);
            }                
        } catch (FileNotFoundException e) {
        	Utils.logh(TAG, e.getMessage().toString());
        } catch(IOException e) {
        	try {
        		file.delete();
        	} catch(Exception ex) {}
        	Utils.logh(TAG, e.getMessage().toString());
        } finally {
            if(null != output) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                	Utils.logh(TAG, e.getMessage().toString());
                }
            }
        }
        return file.getAbsolutePath();
	}
	
	private ProgressDialog mWaitDialog;
	private void showWaitDialog() {
		if(null == mWaitDialog) {
			mWaitDialog = new ProgressDialog(MenuAboutActivity.this);
			mWaitDialog.setMessage(MenuAboutActivity.this.getResources().getString(R.string.str_loading_wait));
			mWaitDialog.setCancelable(false);
		}
		if(!mWaitDialog.isShowing()) {
			mWaitDialog.show();
		}
	}
	
	public void dismissWaitDialog() {
		if(null != mWaitDialog && mWaitDialog.isShowing()) {
			mWaitDialog.dismiss();
		}
	}
}
