package com.example.aoge.skillexchange;

import android.content.ContentResolver;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstLoginActivity extends BaseActivity {

    private CircleImageButton circleButton;
    private RadioGroup gender;
    private Button JoinUsButton;
    private String headPicture = null;
    private Bitmap bitmap = null;
    private EditText edtCan,edtWant;
    private String gd = "male";
    private String email = null;
    private String head = String.valueOf(R.drawable.man1);

//    private ImageView iv_icon = null;

    private ImageUtils imageUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        circleButton = (CircleImageButton) findViewById(R.id.img_headportrait);
        circleButton.setImageResource(R.drawable.man1);
        edtCan = (EditText)findViewById(R.id.edt_can);
        edtWant = (EditText)findViewById(R.id.edt_want);

        // 实例化控件
        gender = (RadioGroup) findViewById(R.id.sex);

        // 方法一监听事件,通过获取点击的id来实例化并获取选中状态的RadioButton控件
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) findViewById(id);
                // 获取这个RadioButton的text内容
                gd = choise.getText().toString();
//                Toast.makeText(FirstLoginActivity.this, "你的性别为：" + output, Toast.LENGTH_SHORT).show();
            }
        });

        JoinUsButton = (Button) findViewById(R.id.btnjoinus);
    }

    /**
     * Press the button Login, go to Login form
     *
     * @param view from the activity_login.xml
     */
    public void btnJoinUs(View view) {
        String cando = edtCan.getText().toString().trim();
        String wantdo = edtWant.getText().toString().trim();
        JoinUsButton.setClickable(false);
        if(cando.isEmpty() || wantdo.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    R.string.enter_credentials, Toast.LENGTH_LONG)
                    .show();
        }else{
            FirstLoginRequest(email,gd,cando,wantdo,headPicture);
        }
    }





    /**
     * link to server to depend whether the username and password are right.
     * @param email
     * @param
     */
    public void FirstLoginRequest(final String email, final String gender,final String can, final String want, final String headpicture) {
        //request url
        String url = "http://106.14.117.91:8080/SkillsExchangeServer/FirstLoginServlet";    //注①
        String tag = "FirstLogin";    //注②

        //get the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //cancel the request queue that marked by "Register" in order to not request again.
        requestQueue.cancelAll(tag);

        //build StringRequest and set the request method "POST"(default "GET")
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Something wrong! Please try again.", Toast.LENGTH_LONG)
                                        .show();
                                JoinUsButton.setClickable(true);
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No internet!", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", error.getMessage(), error);
                JoinUsButton.setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", email);  //set the parameter.
                params.put("Gender", gender);
                params.put("Can", can);
                params.put("Want", want);
                params.put("HeadPicture", head);
                return params;
            }
        };

        //set the tag.
        request.setTag(tag);

        //add the request to queue.
        requestQueue.add(request);
    }






    public void selectHead(View view){
        Intent intent = new Intent(this,ChooseHeadActivity.class);

        startActivityForResult(intent,0);
    }

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data!=null){
//            Bundle bundle = getIntent().getExtras();
//            int itemp= bundle.getInt("number");
            String number = data.getStringExtra("number");

            switch (number) {
                case "1": {
                    circleButton.setImageResource(R.drawable.man1);
                    head = String.valueOf(R.drawable.man1);
                }

                break;
                case "2": {
                    circleButton.setImageResource(R.drawable.man2);
                    head = String.valueOf(R.drawable.man2);
                }
                break;
                case "3": {
                    circleButton.setImageResource(R.drawable.man3);
                    head = String.valueOf(R.drawable.man3);
                }
                break;
                case "4": {
                    circleButton.setImageResource(R.drawable.man4);
                    head = String.valueOf(R.drawable.man4);
                }
                break;
                case "5": {
                    circleButton.setImageResource(R.drawable.man5);
                    head = String.valueOf(R.drawable.man5);
                }
                break;
                case "6": {
                    circleButton.setImageResource(R.drawable.woman1);
                    head = String.valueOf(R.drawable.woman1);
                }
                break;
                case "7": {
                    circleButton.setImageResource(R.drawable.woman2);
                    head = String.valueOf(R.drawable.woman2);
                }
                break;
                case "8":{
                    circleButton.setImageResource(R.drawable.woman3);
                    head = String.valueOf(R.drawable.woman3);
            }
                    break;
                case "9":{
                    circleButton.setImageResource(R.drawable.woman4);
                    head = String.valueOf(R.drawable.woman4);
            }
                    break;
                case "10":{
                    circleButton.setImageResource(R.drawable.woman5);
                    head = String.valueOf(R.drawable.woman5);
            }
                    break;
            }

        }
        super.onActivityResult(requestCode,resultCode,data);
}













//    /**
//     * 打开本地相册选择图片
//     */
//    public void selectPic(View view){
//        //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
//        Intent intent=new Intent();
//        intent.setType("image/*");
//        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
//        //类型的内容给你选择
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法
//        startActivityForResult(intent, 1);
//    }
//    /**
//     *把用户选择的图片显示在imageview中
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //用户操作完成，结果码返回是-1，即RESULT_OK
//        if(resultCode==RESULT_OK){
//            //获取选中文件的定位符
//            Uri uri = data.getData();
//            Log.e("uri", uri.toString());
//            //使用content的接口
//            ContentResolver cr = this.getContentResolver();
//            try {
//                //获取图片
//                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
////                bitmap = BitmapFactory.decodeFile("C:\\Users\\dell\\Pictures\\lovewallpaper\\woman.bmp");
//                circleButton.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                Log.e("Exception", e.getMessage(),e);
//            }
//        }else{
//            //操作错误或没有选择图片
//            Log.i("FirstLoginActivtiy", "operation error");
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}




//        // 初始化工具类的实例
//        imageUtils = new ImageUtils(this);
//
//        circleButtom.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                chooseDialog();
//            }
//        });
//    }

//    private void chooseDialog() {
//        new AlertDialog.Builder(this)//
//                .setTitle("选择头像")//
//
//                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                        imageUtils.byAlbum();
//
//                    }
//                })
//
//                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                        String status = Environment.getExternalStorageState();
//                        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否存在SD卡
//                            imageUtils.byCamera();
//                        }
//                    }
//                }).show();
//
//    }
//
//    // 这里需要注意resultCode，正常情况返回值为 -1 没有任何操作直接后退则返回 0
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("-->requestCode:" + requestCode + "-->resultCode:"
//                + resultCode);
//
//        switch (requestCode) {
//            case ImageUtils.ACTIVITY_RESULT_CAMERA: // 拍照
//                try {
//                    if (resultCode == -1) {
//                        imageUtils.cutImageByCamera();
//                    } else {
//                        // 因为在无任何操作返回时，系统依然会创建一个文件，这里就是删除那个产生的文件
//                        if (imageUtils.picFile != null) {
//                            imageUtils.picFile.delete();
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case ImageUtils.ACTIVITY_RESULT_ALBUM:
//                try {
//                    if (resultCode == -1) {
//                        Bitmap bm_icon = imageUtils.decodeBitmap();
//                        if (bm_icon != null) {
//                            circleButtom.setImageBitmap(bm_icon);
//                        }
//                    } else {
//                        // 因为在无任何操作返回时，系统依然会创建一个文件，这里就是删除那个产生的文件
//                        if (imageUtils.picFile != null) {
//                            imageUtils.picFile.delete();
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    public void init(){}
//
//}


//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//
//
//public class FirstLoginActivity extends AppCompatActivity {
//
//
//    private CircleImageButton imageView;
//    private String[] items = new String[] { "选择本地图片", "拍照" };
//
//
//    /* 头像名称 */
//    private static final String IMAGE_FILE_NAME = "temp.jpg";
//
//
//    /* 请求码 */
//    private static final int IMAGE_REQUEST_CODE = 0;
//    private static final int CAMERA_REQUEST_CODE = 1;
//    private static final int RESULT_REQUEST_CODE = 2;
//    private File cutFile;
//    private File tempFile;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first_login);
//        imageView = (CircleImageButton) findViewById(R.id.img_headportrait);
//        cutFile = new File(getCacheFile(), "head.jpg");
//        imageView.setOnClickListener(new OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });
//    }
//
//
//    private void showDialog() {
//        new AlertDialog.Builder(this).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {
//
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        Intent intentFromGallery = new Intent();
//                        intentFromGallery.setType("image/*"); // 设置文件类型
//                        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
//                        break;
//                    case 1:
//                        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(new File(getCacheFile(), IMAGE_FILE_NAME)));
//                        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
//                        break;
//                }
//            }
//        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).show();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//// 结果码不等于取消时候
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case IMAGE_REQUEST_CODE:
//                    startPhotoZoom(data.getData());
//                    break;
//                case CAMERA_REQUEST_CODE:
//                    tempFile = new File(getCacheFile(), IMAGE_FILE_NAME);
//                    startPhotoZoom(Uri.fromFile(tempFile));
//                    break;
//                case RESULT_REQUEST_CODE:
//                    if (tempFile != null && tempFile.exists())
//                        tempFile.delete();
//                    Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(cutFile));
//                    imageView.setImageBitmap(bitmap);
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    /**
//     * 获取缓存目录
//     * 
//     * @return
//     */
//    private File getCacheFile() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            return Environment.getExternalStorageDirectory();
//        }
//        return getCacheDir();
//    }
//
//
//    /**
//     * 裁剪图片方法实现
//     * 
//     * @param uri
//     */
//    public void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.setDataAndType(uri, "image/*");
//// 设置裁剪
//        intent.putExtra("crop", "true");
//// aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//// outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 400);
//        intent.putExtra("outputY", 400);
//// true：返回在intent中，通过Bitmap bitmap=intent.getExtras().getParcelable("data")获取
//// false:返回Uri
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cutFile));
//        startActivityForResult(intent, 2);
//    }
//
//
//    /**
//     * uri转换为bitmap
//     * 
//     * @param uri
//     * @return
//     */
//    private Bitmap decodeUriAsBitmap(Uri uri) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return bitmap;
//    }
//}
