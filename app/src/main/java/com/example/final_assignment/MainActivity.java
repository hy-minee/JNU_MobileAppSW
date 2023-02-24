package com.example.final_assignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SingleTouchView drawView;
    private ImageButton currPaint;
    ImageButton new_btn, draw_btn, fig_btn, erase_btn, save_btn;
    Button width_btn;
    EditText width_edit;
    ImageButton circle_btn, triangle_btn, square_btn;
    public int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("그림판");

        drawView = (SingleTouchView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        LinearLayout figureLayout = (LinearLayout) findViewById(R.id.figure);
        currPaint = (ImageButton) paintLayout.getChildAt(0);

        width_edit = findViewById(R.id.width_edit);

        //각 버튼과 연결
        new_btn = findViewById(R.id.new_btn);
        draw_btn = findViewById(R.id.draw_btn);
        fig_btn = findViewById(R.id.fig_btn);
        width_btn = findViewById(R.id.width_btn);
        erase_btn = findViewById(R.id.erase_btn);
        save_btn = findViewById(R.id.save_btn);

        drawView.setSize(10);

        //새로 만들기
        new_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                drawView.setFunc(0); //SingleTouchView의 setFunc
            }
        });

        //브러쉬
        draw_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(paintLayout.getVisibility() == View.VISIBLE){
                    paintLayout.setVisibility(View.INVISIBLE); //브러쉬 도구 숨김
                }
                else {
                    paintLayout.setVisibility(View.VISIBLE); //브러쉬 도구 보이기
                }
            }
        });

        //도형
        fig_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(figureLayout.getVisibility() == View.VISIBLE){
                    figureLayout.setVisibility(View.INVISIBLE); //도형 도구 숨김
                }
                else {
                    figureLayout.setVisibility(View.VISIBLE); //도형 도구 보이기
                }
            }
        });

        //완료 누르면 두께 설정 edittext 닫기
        width_edit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        //브러쉬 두께
        width_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                width = Integer.parseInt(width_edit.getText().toString());
                drawView.setSize(width);
            }
        });

        //지우개
        erase_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                drawView.setFunc(2); //SingleTouchView의 setFunc
            }
        });

        //저장
        save_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //내부 저장소에 저장
                String path = getExternalFilesDir(null).getPath();
                File file = new File(path);

                //이미지 넣을 폴더 생성
                if(!file.exists()){
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                }

                //시간별로 파일 이름 설정하여 중복 피함
                SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                drawView.buildDrawingCache();
                Bitmap captureview = drawView.getDrawingCache();

                FileOutputStream fos = null;
                try{
                    fos = new FileOutputStream(path+"/"+day.format(date)+".jpeg");
                    captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + day.format(date) + ".JPEG")));
                    Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    fos.flush();
                    fos.close();
                    drawView.destroyDrawingCache();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "파일 찾지 못함", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "입출력 예외 처리 필요", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void clicked(View view) {
        if (view != currPaint) {
            String color = view.getTag().toString();
            drawView.setColor(color);
            currPaint = (ImageButton) view;
        }
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draw_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.red:
                drawView.setBackgroundColor(1);
                break;
            case R.id.green:
                drawView.setBackgroundColor(2);
                break;
            case R.id.blue:
                drawView.setBackgroundColor(3);
                break;
            case R.id.black:
                drawView.setBackgroundColor(4);
                break;
            case R.id.white:
                drawView.setBackgroundColor(5);
                break;
            default:
                break;
        }
        //누를 때마다 설정 가능
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }
}