package gabriel.luoyer.promonkey;

import gabriel.luoyer.promonkey.cptslide.ChapterSlideActivity;
import gabriel.luoyer.promonkey.doublefrglist.DoubleFrgListActivity;
import gabriel.luoyer.promonkey.explist.ExpListSelectActivity;
import gabriel.luoyer.promonkey.licc.LiChildClickActivity;
import gabriel.luoyer.promonkey.menu.MenuAboutActivity;
import gabriel.luoyer.promonkey.navi.BottomNavigateActivity;
import gabriel.luoyer.promonkey.titlebarani.TitleBarAniActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.waps.AppConnect;

/**
 * 
 * @author gluoyer@gmail.com
 *
 */
public class ProMonkey extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pro_monkey);
		AppConnect ac = AppConnect.getInstance(this);
		ac.setCrashReport(false);
		ac.initAdInfo();
		showDeclaration();
	}

	public void onChapterSlideClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, ChapterSlideActivity.class);
		startActivity(intent);
//		overridePendingTransition(android.R.anim.slide_in_left, 0);
	}
	
	public void onBottomNavigateClick(View view) {
		BottomNavigateActivity.startBottomNavigateActivity(ProMonkey.this);
	}
	
	public void onExpandListClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, ExpListSelectActivity.class);
		startActivity(intent);
	}
	
	public void onSlideClick(View view){
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, SlidingDrawerActivity.class);
		startActivity(intent);
	}
	
	public void onDoubleFrgClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, DoubleFrgListActivity.class);
		startActivity(intent);
	}
	
	public void onTitlebarAniClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, TitleBarAniActivity.class);
		startActivity(intent);
	}
	
	public void onLiccClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, LiChildClickActivity.class);
		startActivity(intent);
	}
	
	public void onAoutClick(View view) {
		Intent intent = new Intent();
		intent.setClass(ProMonkey.this, MenuAboutActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).close();
		super.onDestroy();
	}
	
	private void showDeclaration() {
		new AlertDialog.Builder(ProMonkey.this)
		.setTitle(R.string.str_declare_dialog_title)
		.setMessage(R.string.str_declare_dialog_msg)
		.setNegativeButton(R.string.str_declare_dialog_btn_refuse, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	finish();
            }
		})
		.setPositiveButton(R.string.str_declare_dialog_btn_accept,  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();
            }
		})
        .setCancelable(false)
		.create()
		.show();
	}
	
}
