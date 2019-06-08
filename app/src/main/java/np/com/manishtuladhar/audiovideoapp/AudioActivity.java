package np.com.manishtuladhar.audiovideoapp;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioActivity extends AppCompatActivity {

    //widgets
    Button playBtn;
    SeekBar positionBar, volumeBar;
    TextView elpasedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mplayer;
    int totaltime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        //widgets
        playBtn = (Button)findViewById(R.id.playBtn);
        elpasedTimeLabel = (TextView)findViewById(R.id.elapsedTime);
        remainingTimeLabel = (TextView)findViewById(R.id.remainingTime);

        //Media Player
        mplayer = MediaPlayer.create(this,R.raw.girlslikeyou);
        mplayer.setLooping(true);
        mplayer.seekTo(0);
        mplayer.setVolume(0.5f,0.5f);

        totaltime = mplayer.getDuration();

        //PositionBar
        positionBar = (SeekBar)findViewById(R.id.positionBar);
        positionBar.setMax(totaltime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //taking progress from the user and mplayer
                if(fromUser){
                    mplayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //VolumeBar
        volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               //intializing volume progress
                float volumeNum = progress/100f;
                mplayer.setVolume(volumeNum,volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Creating a new thread so that we know the time of the audio
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mplayer!=null)
                {
                    try{
                        Message msg = new Message();
                        msg.what = mplayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {

                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int currentPosition = msg.what;

            //update positionBar
            positionBar.setProgress(currentPosition);

            //update labels
            String elpasedTime = createTimeLabel(currentPosition);
            elpasedTimeLabel.setText(elpasedTime);

            String remainingTime = createTimeLabel(totaltime - currentPosition);
            remainingTimeLabel.setText("-" + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";

        int min = time/1000 /60;
        int sec = time/1000 % 60;

        timeLabel = min + ":";

        if(sec<10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    /*
    ----------------------- Playing Audio ------------------
     */
    public void playAudio(View view) {

        if(!mplayer.isPlaying()){
            //start
            mplayer.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        }
        else{
            mplayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
