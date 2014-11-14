package tranduythanh.com;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyAsyncTask extends
		AsyncTask<Integer, Integer, ArrayList<Integer>>
{
	private LinearLayout llrandom,llprime;
	private Random rd=new Random();
	private Activity context;
	public MyAsyncTask(Activity context)
	{
		//lấy các control trong MainThread
		this.llrandom=(LinearLayout) 
				context.findViewById(R.id.llrandomnumber);
		this.llprime=(LinearLayout) 
				context.findViewById(R.id.llprimenumber);;
		this.context=context;
	}
	//Begin - can use UI thread here
	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context, "Start here", 
				Toast.LENGTH_SHORT).show();
		//khi bắt đầu thực thi tiến trình thì tiến hành xóa toàn bộ control bên trong
		this.llrandom.removeAllViews();
		this.llprime.removeAllViews();
	}
	protected ArrayList<Integer> doInBackground(Integer... params) {
		int step=1;
		ArrayList<Integer>list=new ArrayList<Integer>();
		//vòng lặp chạy hết n số button truyền vào
		int n=params[0];
		while(isCancelled()==false && step<=n)
		{
			step++;
			SystemClock.sleep(100);
			//lấy số ngẫu nhiên
			int x=rd.nextInt(100)+1;
			//gọi cập nhật giao diện
			publishProgress(x);
			//nếu là số nguyên tố thì lưu vào danh sách
			if(isPrime(x))
				list.add(x);
		}
		//trả về danh sách số nguyên tố
		return list;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		//lấy giá trị truyền vào trong doinbackground
		Integer item=values[0];
		//tạo Button với Text có giá trị là số ngẫu nhiên
		Button btn=new Button(context);
		btn.setWidth(100);
		btn.setHeight(30);
		btn.setText(item+"");
		//đưa button lên view bên trái màn hình
		this.llrandom.addView(btn);
	}
	public void doRawPrime(int x)
	{
		//hàm vẽ các Button là chứac các số nguyên tố
		Button btn=new Button(context);
		btn.setWidth(100);
		btn.setHeight(30);
		btn.setText(x+"");
		//đưa Button vào view bên phải màn hình
		this.llprime.addView(btn);
	}
	//hàm kiểm tra số nguyên tố
	public boolean isPrime(int x)
	{
		if(x<2)return false;
		for(int i=2;i<=Math.sqrt(x);i++){
			if(x%i==0)return false;}
		return true;
	}
	@Override
	protected void onPostExecute(ArrayList<Integer> result) {
		super.onPostExecute(result);
		//sau khi tiến trình kết thúc thì hàm này sẽ được thực thi
		//lấy về danh sách các số nguyên tố
		final ArrayList<Integer>list=result;
		//tiến hành dùng Handler class để thực hiện
		final Handler handler=new Handler();
		Thread th=new Thread(new Runnable() {
			public void run() {
				//lặp để vẽ các Button là số nguyên tố
				for(int i=0;i<list.size();i++){
					final int x=list.get(i);
					SystemClock.sleep(100);
					handler.post(new Runnable() {
						public void run() {
							doRawPrime(x);}
					});
				}
				handler.post(new Runnable() {
					public void run() {
						Toast.makeText(context, "Finish", 
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		th.start();
	}}//end MyAsyncTask
