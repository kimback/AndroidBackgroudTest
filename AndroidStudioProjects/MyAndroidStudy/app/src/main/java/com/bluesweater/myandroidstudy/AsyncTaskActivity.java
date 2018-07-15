package com.bluesweater.myandroidstudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/*

   AsyncTask 구현
   AsyncTask는 백그라운드 쓰레드와 UI쓰레드의 통신을 잘 구현할수 있는 기능이다.
   내부에 백쓰레드와 ui쓰레드의 콜백이 같이 들어있다.
   메모리 누수에 주의 하며 static (정적메서드로)로 생성한다.


    *** 간단한 예제라 url이미지 정보 부분을 수정하고 구동가능.

 */
public class AsyncTaskActivity extends AppCompatActivity {
    private static final String[] DOWNLOAD_URLS = {
      "http://asldkfjlakjsdlfkd",
      "http://asdfdsfsdfsdf",
      "http://sdljfeisndf"
    };

    DownloadTask mFileDownloaderTask;

    //레이아웃 파일의 뷰
    ProgressBar mProgressBar;
    LinearLayout mLayoutImages;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setMax(DOWNLOAD_URLS.length);
        mLayoutImages = findViewById(R.id.layout_images);

        mFileDownloaderTask = new DownloadTask(this);
        mFileDownloaderTask.execute(DOWNLOAD_URLS);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mFileDownloaderTask.setActivity(null);
        mFileDownloaderTask.cancel(true);


    }


    private static class DownloadTask extends AsyncTask<String, Bitmap, Void>{
        private AsyncTaskActivity mActivity;
        private int mCount = 0;

        public DownloadTask(AsyncTaskActivity activity){
            mActivity = activity;
        }

        public void setActivity(AsyncTaskActivity activity){
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.mProgressBar.setVisibility(View.VISIBLE);
            mActivity.mProgressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(String... urls) {
            for(String url : urls){
                if(!isCancelled()){
                    //Bitmap bitmap = downloadFile(url);
                    //publicProgress(bitmap);

                    //오랜 백그라운드 작업 진행
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Bitmap... bitmaps) {
            super.onProgressUpdate(bitmaps);

            //중간중간 프로그레스상태 표시를 위해

            if(mActivity != null){
                mActivity.mProgressBar.setProgress(++mCount);
                ImageView iv = new ImageView(mActivity);
                iv.setImageBitmap(bitmaps[0]);
                mActivity.mLayoutImages.addView(iv);
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(mActivity != null){
                mActivity.mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap downloadFile(String url){

        Bitmap bitmap = null;
        try{
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }



}
