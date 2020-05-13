package swu.xl.xlseekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySeekBar mySeekBarHor = findViewById(R.id.seek_bar_hor);
        MySeekBar mySeekBarVer = findViewById(R.id.seek_bar_ver);

        mySeekBarHor.setProgressChangeListener(new MySeekBar.OnProgressChangeListener() {
            @Override
            public void progressChanged(float progress) {
                Toast.makeText(MainActivity.this, "横向滑动条的进度:"+progress, Toast.LENGTH_SHORT).show();
            }
        });

        mySeekBarVer.setProgressChangeListener(new MySeekBar.OnProgressChangeListener() {
            @Override
            public void progressChanged(float progress) {
                Toast.makeText(MainActivity.this, "竖向滑动条的进度:"+progress, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
