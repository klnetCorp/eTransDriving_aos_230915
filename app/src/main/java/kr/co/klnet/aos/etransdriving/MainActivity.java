package kr.co.klnet.aos.etransdriving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.Image;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.Browser;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kica.android.util.CMPUtil;
import com.kica.android.util.CertSet;
import com.kica.android.util.CertinfoList;
import com.kica.android.util.certcopy.HTTPSRequest;
import com.kica.android.util.certcopy.ResponseException;
import com.kica.android.vo.CertPath;
import com.kica.security.KICAProvider;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeaconManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.klnet.aos.etransdriving.trans.gps.push.JLocationManager;
import kr.co.klnet.aos.etransdriving.trans.gps.push.PeriodReportService;
import kr.co.klnet.aos.etransdriving.trans.gps.push.ReportInterface;
import kr.co.klnet.aos.etransdriving.trans.gps.push.SoundManager;
import kr.co.klnet.aos.etransdriving.trans.gps.push.UIBroadCastReceiver;
import kr.co.klnet.aos.etransdriving.util.CommonUtil;
import kr.co.klnet.aos.etransdriving.util.DataSet;
import kr.co.klnet.aos.etransdriving.util.KakaoLink;
import kr.co.klnet.aos.etransdriving.util.SecurityUtil;
import kr.co.klnet.aos.etransdriving.util.StringUtil;
import kr.co.klnet.aos.etransdriving.util.TTS;
import kr.co.klnet.aos.etransdriving.view.MOB_07_View02;
import signgate.core.provider.SignGATE;

import com.sg.openews.api.SGAPI;
import com.sg.openews.api.SGKeyCode;
import com.sg.openews.api.crypto.SGAlgorithmParameter;
import com.sg.openews.api.crypto.SGBase64;
import com.sg.openews.api.crypto.SGFile;
import com.sg.openews.api.crypto.SGRandom;
import com.sg.openews.api.crypto.SGSigner;
import com.sg.openews.api.exception.SGCertificateException;
import com.sg.openews.api.exception.SGCryptoException;
import com.sg.openews.api.exception.SGException;
import com.sg.openews.api.key.SGCertificate;
import com.sg.openews.api.key.SGCertificateFactory;
import com.sg.openews.api.key.SGKeyFactory;
import com.sg.openews.api.key.SGPrivateKey;
import com.sg.openews.api.key.SGPrivateKeyGenerator;
import com.sg.openews.api.pkcs12.SGPKCS12;
import com.sg.openews.api.pkcs12.SGPKCS12Exception;
import com.sg.openews.api.pkcs7.SGSignedDataGenerator;
import com.sg.openews.api.util.Hex;
import com.sg.openews.api.util.SGUtil;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.Q;

public class MainActivity extends AppCompatActivity {

    static {
        SGAPI.init();
    }

    private final static String TAG = "MainActivity";
    private static final Logger LOG = LoggerFactory.getLogger(TAG);
    private static MainActivity _instance;

    //    private final static String TMAP_API_KEY = "l7xx633578df86d940639cb332095762b398"; //tom 개인용
    private final static String TMAP_API_KEY = "l7xx8a69806ee60e4e918076f67014bdcfe8"; //klnet

    private boolean isFcLogging = true; //send the loggiFng to firebase crashytics

    private boolean _showDebug = false;
    private int _debugOnVolumeKeyUp = 0;
    private int _debugOnVolumeKeyDown = 0;

    private static final int MAX_REUSE_IMAGE_CNT = 10;
    private int _maxCountReuseImage = MAX_REUSE_IMAGE_CNT; //복화사진 최대첨부갯수
    ArrayList<Uri> _reuseImages = new ArrayList<Uri>();
    ArrayList<String> _reuseCamera = new ArrayList<String>();

    //for TTS
    private Context mContext;
    private Activity mActivity;
    private TTS tts;
    private SoundManager mSoundManager;
    private TelephonyManager mTelMan = null;
    private String _parsedTtsMsg = null;

    private String gPhotoType;


    /**
     * 서명화면 다이얼로그
     * @uml.property name="DLG_COPINO_ID_UPDATE"
     */
    private final int DLG_SHOW_SIGN = 7;
    private MOB_07_View02 mob_07_View02; //서명하기 화면

    public final static int CONTAINER_NO_FROM_CAMERA_REQUEST = 1001; //url scheme: 카메라에서 컨테이너번호 가져오기
    public final static int CONTAINER_NO_FROM_GALLERY_REQUEST = 1002; //url scheme: 갤러리에서 컨테이너번호 가져오기
    public final static int SEAL_NO_FROM_CAMERA_REQUEST = 1003; //url scheme: 카메라에서 씰번호 가져오기
    public final static int SEAL_NO_FROM_GALLERY_REQUEST = 1004; //url scheme: 갤러리에서 씰번호 가져오기
    public final static int CAR_BIZ_CD_FROM_GALLERY_REQUEST = 1005; //url scheme: 갤러리에서 차량등록증 가져오기
    public final static int BIZ_CD_FROM_GALLERY_REQUEST = 1006; //url scheme: 갤러리에서 사업자등록증 가져오기
    public final static int SIGN_IMAGE_FROM_GALLERY_REQUEST = 1007; //url scheme: 갤러리에서 서명이미지 가져오기

    public final static int REUSE_IMAGE_FROM_CAMERA_REQUEST = 1008; //url scheme: 카메라에서 복화사진 가져오기
    public final static int REUSE_IMAGE_FROM_GALLERY_REQUEST = 1009; //url scheme: 앨범에서 복화사진 가져오기

    public final static int UPLOADFILE_FROM_CAMERA_REQUEST = 1010; //url scheme: 카메라에서 업로드 파일 가져오기
    public final static int UPLOADFILE_FROM_GALLERY_REQUEST = 1011; //url scheme: 갤러리에서 업로드 파일 가져오기
    public final static int UPLOADFILE_FROM_FILE_REQUEST = 1012; //url scheme: 파일에서 업로드 파일 가져오기

    public final static int OVERLAY_PERMISSION_REQUEST = 1201; //App 그리기 권한 요청
    public final static int GPS_PERMISSION_REQUEST = 1202; //GPS 허용 요청
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1203;
    public final static int BLUETOOTH_PERMISSION_REQUEST = 1204; //BLUETOOTH 허용 요청
    public final static int BACKGROUND_LOCATION_PERMISSION_REQUEST = 1205;
    public final static int ALRAM_REMAIDER_PERMISSION_REQUEST = 1206; // 알람과 리마인더 요청


    private final int HANDLER_SEARCH_SALE_LIST = 2001;
    public final int HANDLER_UPDATE_DEBUG_STRING = 2002;

    public static final int REQUEST_PUSH_CONFIRMED = 3000;

    WebView WebView01;
    WebView _newWebView;
    RelativeLayout rel_intro;
    RelativeLayout rel_main;
    RelativeLayout rel_debug;
    TextView tv_debug;

    Uri photoUri_ = null;
    String currentPhotoPath_ = "";

    Location _latestLocation = null;

    public static final String ACTION_DEBUG_MESSAGE = "kr.co.klnet.aos.etransdriving.ACTION_DEBUG_MESSAGE";
    private final BroadcastReceiver debugReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.removeStickyBroadcast(intent);

            String debugMsg = intent.getStringExtra("debugMsg");

            Log.d(TAG, "DebugReceiver::onReceive, debugMsg=" + debugMsg);

            Message msg = Message.obtain(handler, HANDLER_UPDATE_DEBUG_STRING);
            msg.obj = debugMsg;
            handler.sendMessageDelayed(msg, 10);
        }
    };


    private JLocationManager locationMgr_;
    private UIBroadCastReceiver reportServiceReceiver_ = null;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SEARCH_SALE_LIST:
                    restoreSession();
                    break;
                case HANDLER_UPDATE_DEBUG_STRING:
                    debugPrint(msg);
                    break;
                default:
                    break;
            }
        }
    };

    public void debugPrint(Message msg) {
        String str = (String) msg.obj;
        debugString(str);
    }


    public void debugString(String str) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        if (tv_debug != null) {
            tv_debug.append("[" + formatDate + "]" + str + "\n");
        }

        LOG.debug(str);

    }

    public static MainActivity getInstance() {
        if(_instance==null) {
            _instance = new MainActivity();
        }

        return _instance;
    }

    private RequestQueue queue;
    private String sHash = "";
    private Toast toast;

    //Security Check
    public static final String ROOT_PATH = Environment.
            getExternalStorageDirectory() + "";
    public static final String ROOTING_PATH_1 = "/system/bin/su";
    public static final String ROOTING_PATH_2 = "/system/xbin/su";
    public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";

    public String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1,
            ROOT_PATH + ROOTING_PATH_2,
            ROOT_PATH + ROOTING_PATH_3,
            ROOT_PATH + ROOTING_PATH_4
    };

    long LoginBackKeyClickTme;
    long MainBackKeyClickTme;

    String DstprtCode = "";
    String DstprtName = "";
    SharedPreferences prefsDstPrt;

    boolean isLoginPage = false;
    boolean isMianPage = false;

    boolean isKeyboard = false;

    String deviceId = "";


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        Log.d("CHECK", "push_id : " + intent.getStringExtra("push_id"));
        //앱이 실행된 상태에서 푸시를 보는 경우
        if (intent != null) {
            String push_seq = intent.getStringExtra("seq");
            String push_type = intent.getStringExtra("type");
            String push_doc_gubun = intent.getStringExtra("doc_gubun");
            String push_title = intent.getStringExtra("title");
            String push_body = intent.getStringExtra("body");
            String push_param = intent.getStringExtra("param");

            if (push_seq == null) push_seq = "";
            if (push_type == null) push_type = "";
            if (push_doc_gubun == null) push_doc_gubun = "";
            if (push_title == null) push_title = "";
            if (push_body == null) push_body = "";
            if (push_param == null) push_param = "";

            DataSet.getInstance().setPushInfo(push_seq, push_type, push_doc_gubun
                    , push_title, push_body, push_param);


            Log.d("CHECK", "          push_id:" + DataSet.getInstance().push_seq);
            Log.d("CHECK", "   push_doc_gubun:" + DataSet.getInstance().push_doc_gubun);
            Log.d("CHECK", "        push_type:" + DataSet.getInstance().push_type);
            Log.d("CHECK", "       push_title:" + DataSet.getInstance().push_title);
            Log.d("CHECK", "        push_body:" + DataSet.getInstance().push_body);
            Log.d("CHECK", "       push_param:" + DataSet.getInstance().push_param);

            String loggedIn = EtransDrivingApp.getInstance().getLoggedIn();

            if ("Y".equalsIgnoreCase(loggedIn)) {
                //push_id
                if (push_seq != null && !"".equals(push_seq)) {
                    //앱이 실행된 상태에서 푸시 클릭한 경우 처리 부분
                    processPushNoti();
                }

                removeBadge();

                /**
                 * 푸시 처리 관련 앱 기능 추가
                 * */

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                manager.cancel(DataSet.getInstance().push_type + ":" + DataSet.getInstance().obj_id, JuisFirebaseMessagingService.REQUEST_PUSH_ARRIVED);
                manager.cancelAll();

            }
        }
    }

    @Override
    public void onDestroy() {

        sendEventGA(TAG, "onDestroy Start");

        super.onDestroy();

        stopLocationService();
//        stopReportService();

        unregisterReceiver(debugReceiver);

        sendEventGA(TAG, "onDestroy Finished");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().stopSync();
        }

        unregisterReceiver(completeReceiver);

    }

    @Override
    protected void onResume() {

        super.onResume();

        EtransDrivingApp.getInstance().debugMessage(TAG + ":onResume");

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().startSync();
        }


        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);


//        //백그라운드상태에서 수신된 푸시가 처리되었는지 확인
//        if (DataSet.getInstance().isrunapppush) {
//            processPushNoti();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTAINER_NO_FROM_CAMERA_REQUEST:
                    loadImageContainerNo(data);
                    break;
                case CONTAINER_NO_FROM_GALLERY_REQUEST:
                    loadImageContainerNo(data);
                    break;
                case SEAL_NO_FROM_CAMERA_REQUEST:
                    decodeFromOemCamera(data, SEAL_NO_FROM_CAMERA_REQUEST);
                    break;
                case SEAL_NO_FROM_GALLERY_REQUEST:
                    decodeFromGallery(data, SEAL_NO_FROM_GALLERY_REQUEST);
                    break;
                case CAR_BIZ_CD_FROM_GALLERY_REQUEST:
                    decodeFromGallery(data, CAR_BIZ_CD_FROM_GALLERY_REQUEST);
                    break;
                case BIZ_CD_FROM_GALLERY_REQUEST:
                    decodeFromGallery(data, BIZ_CD_FROM_GALLERY_REQUEST);
                    break;
                case REUSE_IMAGE_FROM_CAMERA_REQUEST:
                    addToReuseImageFromOemCamera(data, requestCode, "camera");
                    break;
                case REUSE_IMAGE_FROM_GALLERY_REQUEST:
                    addToReuseImageFromOemCamera(data, requestCode, "gallery");
                    break;
                case UPLOADFILE_FROM_CAMERA_REQUEST:
                    decodeFromOemCamera(data, UPLOADFILE_FROM_CAMERA_REQUEST);
                    break;
                case UPLOADFILE_FROM_GALLERY_REQUEST:
                    decodeFromGalleryMulti(data, UPLOADFILE_FROM_GALLERY_REQUEST);
                    break;
                case UPLOADFILE_FROM_FILE_REQUEST:
                    decodeFromFileMulti(data, UPLOADFILE_FROM_FILE_REQUEST);
                    break;
                default:
                    break;
            }
        }

        if (requestCode == HANDLER_SEARCH_SALE_LIST) {
            handler.sendMessage(handler.obtainMessage(HANDLER_SEARCH_SALE_LIST));
        }

        if (requestCode == OVERLAY_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림");
                builder.setMessage("다른 앱 위에 표시되는 앱을 설정하지 않으시면 코피노 정보 배차지시 등의 알림메시지가 정상적으로 작동하지 않을 수도 있습니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                popPermissionView();
                                onAppLoad();
                            }
                        });
                builder.show();
            } else {
//                popPermissionView();
                onAppLoad();
            }
        } else if (requestCode == GPS_PERMISSION_REQUEST) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showAlertDialogYes("알림", "GPS 를 설정하지 않으시면, 위치표시기능이 정상적으로 작동하지 않습니다."
                        , new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        } else if (requestCode == ALRAM_REMAIDER_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                //안드로이드 13 대응
                if (!alarmManager.canScheduleExactAlarms()) {
                    showAlertDialogYes("알림", "알람 및 리마인더 허용을 설정하지 않으시면, 위치표시기능이 정상적으로 작동하지 않습니다."
                            , new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onAppLoad();
                                }
                            });
                } else {
                    onAppLoad();
                }
            } else {
                onAppLoad();
            }
        }
    }

    private void isRooting() {
        if(!BuildConfig.DEBUG ) {
            queue = Volley.newRequestQueue(this);
            AlertDialog.Builder alertDialogBuilderExit = new AlertDialog.Builder(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DataSet.connect_url + "/newmobile/selectMobileHashKey.do?method=selectMobileHashKey&app_id=ETDRIVING&app_os=android&app_version=1", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        sHash = response.getString("hash_code");
                        if (!sHash.trim().equals(getHashKey().trim())) {
                            alertDialogBuilderExit.setMessage("프로그램 무결성에 위배됩니다. \nPlayStore 내에서 \n 설치하시기 바랍니다.").setCancelable(false)
                                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            MainActivity.this.finish();
                                        }
                                    });
                            AlertDialog dialog = alertDialogBuilderExit.create();
                            dialog.show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("########",error.toString());
                }
            });

            queue.add(jsonObjectRequest);
            //Rooting Check
            boolean isRootingFlag = false;
            try {
                Runtime.getRuntime().exec("su");
                isRootingFlag = true;
            } catch (Exception e) {
                // Exception 나면 루팅 false;
                isRootingFlag = false;
            }

            if (!isRootingFlag) {
                isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
            }

            Log.d("test", "isRootingFlag = " + isRootingFlag);

            alertDialogBuilderExit.setTitle("프로그램 종료");


            if (isRootingFlag == true) {
                alertDialogBuilderExit.setMessage("루팅된 단말기 입니다. \n개인정보 유출의 위험성이 있으므로\n 프로그램을 종료합니다.").setCancelable(false)
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog dialog = alertDialogBuilderExit.create();
                dialog.show();
            }


            if (kernelBuildTagTest() == true) {
                alertDialogBuilderExit.setMessage("루팅된 단말기 입니다. \n개인정보 유출의 위험성이 있으므로\n 프로그램을 종료합니다.\n Error Code : 2").setCancelable(false)
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog dialog = alertDialogBuilderExit.create();
                dialog.show();
            }
            if (shellComendExecuteCheck() == true) {
                alertDialogBuilderExit.setMessage("루팅된 단말기 입니다. \n개인정보 유출의 위험성이 있으므로\n 프로그램을 종료합니다.\n Error Code : 3").setCancelable(false)
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog dialog = alertDialogBuilderExit.create();
                dialog.show();
            }


        }
    }

    /* 커널 빌드 태그 검사 */
    public boolean kernelBuildTagTest() {

        String buildTags = Build.TAGS;

        if(buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }else {
            return false;
        }
    }
    /* Shell 명령어 실행 가능 여부 */
    public boolean shellComendExecuteCheck() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public String getHashKey(){
        String hashKey = "";
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("####",e.toString());
            e.printStackTrace();
        }
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);

            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest.");
                return null;
            }
        }
        return hashKey;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mContext = this;

        initializeFirebase();
        sendEventFC(TAG, "onCreate Start");
        sendEventGA(TAG, "onCreate");

        //위변조 체크
        isRooting();

//        checkPermissions();
        popPermissionView();

        registerReceiver(
                debugReceiver, new IntentFilter(ACTION_DEBUG_MESSAGE));

        String hashKey = EtransDrivingApp.getInstance().getHashKey();
        Log.e(TAG, "hash key=" + hashKey);

        //S:TTS volume set
//        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int sb2value =am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, sb2value, 0);
        //E:TTS volume set

        SignGATE.addProvider();
        KICAProvider.addProvider();

        sendEventFC(TAG, "onCreate Done");
    }

    private void onAppInit() {

    }

    private boolean execSuApp(String exec) {
        boolean exist = true;
        try {
            Runtime.getRuntime().exec(exec);
        } catch (Exception e) {
            exist = false;
        }
        return exist;

    }

    private void backgroundPermission() {
        int isbackgroundPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (isbackgroundPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Q) {
                if (EtransDrivingApp.getInstance().getFstLogin().equalsIgnoreCase("N")) {
                    //jlim 최초 설치 및 실행 후 권한 허용 지나 로그인까지 갔다온 케이스에는
                    showCustomToast("위치서비스를 수락하지 않은 경우 서비스에 제약이 있을수 있습니다. 백그라운드 위치권한을 항상 허용으로 설정해 주세요.");
                    if (checkOverlayWindowService()) {
                        if (checkAlarmRemainderPermission())
                            onAppLoad();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light));
                    alertDialogBuilder.setTitle("알림");
                    alertDialogBuilder
                            .setMessage("이트랜스 드라이빙에서 제공하는 모든 서비스를 원활하게 사용하기 위해서는 백그라운드 위치권한을 항상 허용으로 설정해 주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("CHECK", "권한을 요청합니다.");
                                    ArrayList<String> needPermissions01 = new ArrayList<String>();
                                    needPermissions01.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                                    String[] newPermission01 = new String[needPermissions01.size()];
                                    newPermission01 = needPermissions01.toArray(newPermission01);
                                    requestPermissions(newPermission01, 2);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (checkOverlayWindowService()) {
                                        if (checkAlarmRemainderPermission())
                                            onAppLoad();
                                    }
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }
            }
        }
    }

    private void onAppLoad() {

        //Rooting Check
        boolean isRootingFlag = false;

        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            isRootingFlag = true;
        } else {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                isRootingFlag = true;
            } else {
                if (
                        execSuApp("su")
                                || execSuApp("/system/xbin/su")
                                || execSuApp("/system/bin/su")
                    //|| execSuApp("which su")
                ) {
                    isRootingFlag = true;
                } else {
                    isRootingFlag = false;
                }
            }
        }

        if (!isRootingFlag) {
            isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
        }

        Log.d("test", "isRootingFlag = " + isRootingFlag);

        if (isRootingFlag == true) {
            AlertDialog.Builder alertDialogBuilderExit = new AlertDialog.Builder(this);
            alertDialogBuilderExit.setTitle("프로그램 종료");
            alertDialogBuilderExit.setMessage("루팅된 단말기 입니다. \n개인정보 유출의 위험성이 있으므로\n 프로그램을 종료합니다.").setCancelable(false)
                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finish();
                        }
                    });

            AlertDialog dialog = alertDialogBuilderExit.create();
            dialog.show();
        }
        //Rooting Check End

        rel_intro = (RelativeLayout) findViewById(R.id.rel_intro);
        rel_main = (RelativeLayout) findViewById(R.id.rel_main);
        rel_debug = (RelativeLayout) findViewById(R.id.rel_debug);

        tv_debug = (TextView) findViewById(R.id.tv_debug);
        tv_debug.setMovementMethod(new ScrollingMovementMethod());

        deviceId = EtransDrivingApp.getInstance().getDeviceID(this);
        Log.d("CHECK", "deviceId :" + deviceId);

        WebView01 = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = WebView01.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        //WebView01.addJavascriptInterface(new AndroidBridge(), "AndroidInterface");
        WebView01.clearHistory();
        WebView01.clearCache(true);
        WebView01.clearView();

        // asmyoung
        WebView01.getSettings().setTextSize(WebSettings.TextSize.NORMAL); // Deprecated
        WebView01.getSettings().setTextZoom(100);

        WebView01.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                WebView01.getWindowVisibleDisplayFrame(r);
                int screenHeight = WebView01.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    if (isKeyboard == false) {
                        Log.d("CHECK", "keyboard show!!" + keypadHeight);
                        //wvLoadUrl("javascript:setKeyboard("+keypadHeight+",'Y')");
                        isKeyboard = true;
                    }
                } else {
                    if (isKeyboard == true) {
                        Log.d("CHECK", "keboard hide!!");
                        //wvLoadUrl("javascript:setKeyboard(0, 'Y')");
                        isKeyboard = false;
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        removeBadge();

        // asmyoung old
//        initBroadcastReceiver();
//        getInstanceIdToken();
//        registerWebViewListener();
//        gotoLoginPage();
//        startLocationService();

        // asmyoung
        getInstanceIdToken();
        registerWebViewListener();
        gotoLoginPage();

        //jlim 권한 허용을 지나서 앱이 한번 실행이 되었다면 최초 설치 후 1회 실행은 된 것이다.
        if(EtransDrivingApp.getInstance().getGpsAgreementYn().equalsIgnoreCase("Y")) {
            EtransDrivingApp.getInstance().setFstLogin("N");
        }

        //initBroadcastReceiver();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure();
        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        setCookieAllow(WebView01);

        handler.postDelayed(new Runnable() {
            public void run() {
                rel_intro.setVisibility(View.GONE);
                rel_main.setVisibility(View.VISIBLE);

//                String msg = "TTS 읽기 테스트용 알림이 도착했습니다. #TTS#";
//                processPushTts(msg, msg, msg);
//                Intent intent = new Intent(MainActivity.this, PushActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                DataSet.getInstance().isbackground = "true";
//                MainActivity.this.startActivity(intent);


            }
        }, 5000);
    }

    private void setCookieAllow(WebView webView) {

        try {
            CookieManager.getInstance().setAcceptCookie(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            }
        } catch (Exception e) {

        }
    }

    private void initBroadcastReceiver() {
        UIBroadCastReceiver.setOnForcedLogoutListener(new UIBroadCastReceiver.OnForcedLogoutListener() {
            @Override
            public void OnCommand() {
                Log.d("UIBroadCastReceiver", "OnForcedLogoutListener::OnCommand");
            }
        });

        UIBroadCastReceiver.setOnRehandlingMsgListener(new UIBroadCastReceiver.OnRehandlingMsgListener() {
            @Override
            public void OnReceiveMsg() {
                Log.d("UIBroadCastReceiver", "OnRehandlingMsgListener::OnReceiveMsg");
                // showRehandlingPopView();
            }
        });
    }

    public static String getMarketVersion(String packageName) {

        Log.d("CHECK", packageName);

        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName).get();
            Elements Version = document.select(".content");
            for (Element element : Version) {
                if (element.attr("itemprop").equals("softwareVersion")) {
                    return element.text().trim();
                }
            }
        } catch (Exception ex) {
            return "1.0";
        }
        return null;
    }

    private void backgroundLocationPermissionCheck() {

        String checkYn = "Y";
        if (Build.VERSION.SDK_INT >= Q) {
            checkYn = "N";
            boolean backgroundPermissionCheck =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;
            if(backgroundPermissionCheck) checkYn = "Y";
        }

        String format = "javascript:getBackgroundLocationPermissionCheck('%s')";
        String jsString = String.format(format, checkYn);

        wvLoadUrl(jsString);
    }

    private boolean checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            String[] permissions = new String[]{};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions = new String[]{
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        //android.Manifest.permission.READ_PHONE_STATE,
                        //android.Manifest.permission.READ_SMS,
                        //android.Manifest.permission.READ_PHONE_NUMBERS,
                        android.Manifest.permission.READ_MEDIA_IMAGES,
                        android.Manifest.permission.READ_MEDIA_VIDEO,
                        android.Manifest.permission.GET_ACCOUNTS,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.POST_NOTIFICATIONS,
                        //Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT
                };
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions = new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        //android.Manifest.permission.READ_PHONE_STATE,
                        //android.Manifest.permission.READ_SMS,
                        //android.Manifest.permission.READ_PHONE_NUMBERS,
                        android.Manifest.permission.GET_ACCOUNTS,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.CALL_PHONE,
                        //Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT
                };
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissions = new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        //android.Manifest.permission.READ_PHONE_STATE,
                        //android.Manifest.permission.READ_SMS,
                        //android.Manifest.permission.READ_PHONE_NUMBERS,
                        android.Manifest.permission.GET_ACCOUNTS,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.BLUETOOTH
                };
            }


            ArrayList<String> needPermissions = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();

            for (String permission : permissions) {
                int permissionResult = getApplicationContext().checkSelfPermission(permission);

                if (permissionResult == PackageManager.PERMISSION_DENIED) {
                    sb.append(permission + ",");
                    needPermissions.add(permission);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= Q) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
                        != PackageManager.PERMISSION_GRANTED) {
                    sb.append(Manifest.permission.FOREGROUND_SERVICE + ",");
                    needPermissions.add(Manifest.permission.FOREGROUND_SERVICE);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                        != PackageManager.PERMISSION_GRANTED) {
                    sb.append(Manifest.permission.READ_PHONE_NUMBERS + ",");
                    needPermissions.add(Manifest.permission.READ_PHONE_NUMBERS);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    sb.append(Manifest.permission.READ_PHONE_STATE + ",");
                    needPermissions.add(Manifest.permission.READ_PHONE_STATE);
                }
            }


            boolean permissionAccessLocationApproved =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (!permissionAccessLocationApproved) {
                sb.append(Manifest.permission.ACCESS_FINE_LOCATION + ",");
                needPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//                needPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION); //asmm
            } else { //asmm 전체 주석
//                boolean backgroundLocationPermissionApproved =
//                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                                == PackageManager.PERMISSION_GRANTED;
//
//                if (!backgroundLocationPermissionApproved) {
//                    if (android.os.Build.VERSION.SDK_INT >= Q) {
//                        sb.append(Manifest.permission.ACCESS_BACKGROUND_LOCATION + ",");
//                        needPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//                    }
//                }
            }
/*
            boolean permissionAccessFineLocationApproved =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if(!permissionAccessFineLocationApproved) {
                sb.append(Manifest.permission.ACCESS_FINE_LOCATION + ",");
                needPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                boolean backgroundLocationPermissionApproved =
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                == PackageManager.PERMISSION_GRANTED;
                if (!backgroundLocationPermissionApproved) {
                    if (android.os.Build.VERSION.SDK_INT >= Q) {
                        sb.append(Manifest.permission.ACCESS_BACKGROUND_LOCATION + ",");
                        needPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                    }
                }
            }
*/
            if (needPermissions.size() > 0) {
                sendEventFC(TAG, "이[" + sb.toString() + "]");
                for (String permission : needPermissions)
                    //권한 필요함
                    if (shouldShowRequestPermissionRationale(permission)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light));
                        dialog.setTitle("권한이 필요합니다.")
                                .setMessage("단말기의 (위치, 저장소, 전화, 주소록, 카메라)권한이 필요합니다.\n 취소하실 경우 앱이 종료됩니다. \n계속하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendEventFC(TAG, "request the permissions");
                                        if (Build.VERSION.SDK_INT >= M) {

                                            Log.d("CHECK", "권한을 요청합니다.");
                                            //requestPermissions(permissions, 1);
                                            String[] newPermission = new String[needPermissions.size()];
                                            newPermission = needPermissions.toArray(newPermission);
                                            requestPermissions(newPermission, 1);
                                        }
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendEventFC(TAG, "cancelled[request the permissions], app will be finished");
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                        return false;
                    } else {
                        //최초 권한 요청
                        Log.d("CHECK", "최초로 권한을 요청합니다.");
                        sendEventFC(TAG, "request the permissions[first time]");
                        String[] newPermission = new String[needPermissions.size()];
                        newPermission = needPermissions.toArray(newPermission);
                        requestPermissions(newPermission, 1);
                        return false;

                    }
            } else {
                if (Build.VERSION.SDK_INT >= Q) {
                    int isbackgroundPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                    if (isbackgroundPermission != PackageManager.PERMISSION_GRANTED) {
                         backgroundPermission();
                    } else {
                        testLoadParam(); //send log to firebase
                        if (checkOverlayWindowService()) {
                            if (checkAlarmRemainderPermission())
                                onAppLoad();
                        }
                    }
                } else {
                    testLoadParam(); //send log to firebase
                    if (checkOverlayWindowService()) {
                        if (checkAlarmRemainderPermission())
                            onAppLoad();
                    }
                }
            }
        } else {
            Log.d("CHECK", "(마시멜로우 이하 버전입니다.)");

            testLoadParam(); //send log to firebase
            onAppLoad();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean needPermission = false;
        boolean needbackgroudPermission = false;
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE)) {
                        //로그인 페이지 다시로드, 로그인페이지에서 mobile no 번호 다시 가져오는 로직 수행
                        testLoadParam(); //send log to firebase

                    }
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        if (checkOverlayWindowService()) {
                            if (checkAlarmRemainderPermission())
                                onAppLoad();
                        }
                    }
                } else {
//                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) { //예외처리
//                        backgroundPermission();
//                    } else {
//                        needPermission = true;
//                    }
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) { //예외처리
                        //testLoadParam();
                        needbackgroudPermission = true;
                        if (grantResults.length > 0 && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            showAlertDialogYes("알림", "위치서비스를 수락하지 않은 경우 서비스에 제약이 있을수 있습니다."
                                    , new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (checkOverlayWindowService()) {
                                                if (checkAlarmRemainderPermission())
                                                    onAppLoad();
                                            }
                                        }
                                    });
                            return;
                        } else {
                            if (checkOverlayWindowService()) {
                                if (checkAlarmRemainderPermission())
                                    onAppLoad();
                            }
                        }
                    } else {
                        needPermission = true;
                    }
                    break;
                }
            }

            if (needPermission && !needbackgroudPermission) {
                finshNeedToPermission();
                return;
            } else {
                if(!needbackgroudPermission) {
                    if (Build.VERSION.SDK_INT >= Q) {
                        backgroundPermission();
                    } else {
                        if (checkOverlayWindowService()) {
                            if (checkAlarmRemainderPermission())
                                onAppLoad();
                        }
                    }
                } else {
                    if (checkOverlayWindowService()) {
                        if (checkAlarmRemainderPermission())
                            onAppLoad();
                    }
                }

            }



        } if (requestCode == 2) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    needPermission = false;
                } else {
                    needPermission = true;
                }
            }
            if (needPermission) {
                showAlertDialogYes("알림", "위치서비스를 수락하지 않은 경우 서비스에 제약이 있을수 있습니다."
                        , new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (checkOverlayWindowService()) {
                                    if (checkAlarmRemainderPermission())
                                        onAppLoad();
                                }
                            }
                        });
                return;
            } else {
                if (checkOverlayWindowService()) {
                    if (checkAlarmRemainderPermission())
                        onAppLoad();
                }
            }
        } else {
            Log.d("CHECK", "onRequestPermissionsResult ( 권한 거부) ");
        }
    }

    public void toggleDebugView(boolean show) {
        if (show) {
            rel_debug.setVisibility(View.VISIBLE);
            rel_intro.setVisibility(View.GONE);
            rel_main.setVisibility(View.GONE);
        } else {
            rel_debug.setVisibility(View.GONE);
            rel_intro.setVisibility(View.GONE);
            rel_main.setVisibility(View.VISIBLE);
        }
    }

    public void debugKeyCheck(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            _debugOnVolumeKeyDown++;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            _debugOnVolumeKeyUp++;
        }

        if (_debugOnVolumeKeyDown >= 10 || _debugOnVolumeKeyUp >= 10) {
            _debugOnVolumeKeyDown = 0;
            _debugOnVolumeKeyUp = 0;
            EtransDrivingApp.getInstance().showToast("디버그키 리셋");
        }

        if (_debugOnVolumeKeyDown == 5 && _debugOnVolumeKeyUp == 5) {
            _debugOnVolumeKeyDown = 0;
            _debugOnVolumeKeyUp = 0;
            _showDebug = !_showDebug; //toggle

            toggleDebugView(_showDebug);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int duration = 2000;
        debugKeyCheck(keyCode);
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (_newWebView != null) {
                _newWebView.loadUrl("javascript:self.close();");
            }

            if (isLoginPage) {
                if (System.currentTimeMillis() > LoginBackKeyClickTme + 2000) {
                    LoginBackKeyClickTme = System.currentTimeMillis();
                    finishGuide();
                    return true;
                }

                if (System.currentTimeMillis() <= LoginBackKeyClickTme + 2000) {
                    toast.cancel();
                    DataSet.getInstance().isrunning = "false";
                    DataSet.getInstance().islogin = "false";
                    DataSet.getInstance().userid = "";
                    stopGpsService();    //asmyoung
                    moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
                    }
                    android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
                    finish();
                    return true;
//                    finish();    // asmyoung old
//                    return true;
                }
            } else if (isMianPage) {
                if (System.currentTimeMillis() > MainBackKeyClickTme + 2000) {
                    MainBackKeyClickTme = System.currentTimeMillis();
                    finishGuide();
                    return true;
                }

                if (System.currentTimeMillis() <= MainBackKeyClickTme + 2000) {
                    wvLoadUrl("javascript:fnLogOut();");
                    toast.cancel();
                    DataSet.getInstance().isrunning = "false";
                    DataSet.getInstance().islogin = "false";
                    DataSet.getInstance().userid = "";
                    stopGpsService();    //asmyoung
                    moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
                    }
                    android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
                    finish();
                    return true;
//                    finish();    // asmyoung old
//                    return true;
                }
            } else if (DataSet.getInstance().istext.equals("true")) {
                DataSet.getInstance().istext = "false";
                WebView01.goBack();
                return true;
            } else {
                wvLoadUrl("javascript:fn_back();");
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    public void finishGuide() {
        toast = Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
     */
    private File[] createFiles(String[] sfiles) {
        File[] rootingFiles = new File[sfiles.length];
        for (int i = 0; i < sfiles.length; i++) {
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }

    /**
     * 루팅파일 여부를 확인 한다.
     */
    private boolean checkRootingFiles(File... file) {
        boolean result = false;
        for (File f : file) {
            if (f != null && f.exists() && f.isFile()) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 최초 실행 알림창
     */
    private void DialogHtmlView() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("[필수적 접근 권한] \n" +
                "*인터넷 : 인터넷을 이용한 이트랜스드라이빙 서비스 접근 \n" +
                "*저장공간 : 기기 사진, 미디어, 파일 액세스 권한으로 다운로드 파일 보관\n" +
                "[선태적 접근 권한] \n" +
                "*푸시알림 : PUSH 알림 서비스");
        ab.setPositiveButton("확인", null);
        AlertDialog title = ab.create();
        title.setTitle("앱 권한 이용 안내");
        title.show();
    }


    private void loadImageContainerNo(Intent data) {
        decodeFromCustomCamera(data);
    }


    private void decodeFromOemCamera(Intent data, int requestType) {
        try {
//            Uri originalUri = data.getData();
            Uri originalUri = photoUri_;

            switch (requestType) {
                case SEAL_NO_FROM_CAMERA_REQUEST:
            sealNoToServer(originalUri, requestType);
                    break;
                case UPLOADFILE_FROM_CAMERA_REQUEST:
                    photoToServer(originalUri, requestType);
                    break;
            }

        } catch (Exception e) {
            wvLoadUrl("javascript:addSealImage('', '{}'");
        }
    }

    private void addToReuseImageFromOemCamera(Intent data, int requestType, String type) {
        try {
            if (data != null) {
                Uri originalUri = data.getData();
                photoUri_ = originalUri;
            }

            Uri uri = photoUri_;
            checkCntReuseImages(uri, requestType, type);

        } catch (Exception e) {
            wvLoadUrl("javascript:addReuseImage_loc_multi('', '', '', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}'");
        }
    }

    private void checkCntReuseImages(Uri uri, int requestType, String type) {
        if (_reuseImages.size() < _maxCountReuseImage) {
            addReuseImages(uri);
        }

        if (_reuseImages.size() < _maxCountReuseImage) {
            alertReuseDialog(requestType, type);
        } else {
            reuseImagesToServer(type);
        }
    }

    private void addReuseImages(Uri uri) {
        _reuseImages.add(uri);
    }

    private void addReuseCameras(String path) {
        _reuseCamera.add(path);
    }

    private void clearReuseImages() {
        _reuseImages.clear();
    }

    private void clearReuseCameras() {
        _reuseCamera.clear();
    }

    private int getReuseImageNextNo() {
        return _reuseImages.size() + 1;
    }

    private int getReuseCameraNextNo() {
        return _reuseCamera.size() + 1;
    }

    void alertReuseDialog(int requestType, String type) {
        int remainCnt = _maxCountReuseImage - _reuseImages.size();
        String msg = remainCnt + "장 남았습니다. 사진을 추가하시겠습니까?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진");//최초 복화사진, 이후 멀티첨부를 공동사용위해 사진으로 타이틀 변경
        builder.setMessage(msg);

        builder.setPositiveButton("사진추가",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (requestType == REUSE_IMAGE_FROM_CAMERA_REQUEST) {
                            takePhotoReuseImage("camera");
                        } else if (requestType == REUSE_IMAGE_FROM_GALLERY_REQUEST) {
                            takePhotoReuseImage("gallery");
                        }
                    }
                });
        builder.setNegativeButton("전송",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reuseImagesToServer(type);
                    }
                });

        builder.show();
    }

    private void decodeFromGallery(Intent data, int requestCode) {
        try {
            Uri originalUri = data.getData();
            switch (requestCode) {
                case SEAL_NO_FROM_GALLERY_REQUEST:
                    sealNoToServer(originalUri, SEAL_NO_FROM_GALLERY_REQUEST);
                    break;
                case CAR_BIZ_CD_FROM_GALLERY_REQUEST:
                    imageDataToServer("car", originalUri);
                    break;
                case BIZ_CD_FROM_GALLERY_REQUEST:
                    imageDataToServer("biz", originalUri);
                    break;
                case SIGN_IMAGE_FROM_GALLERY_REQUEST:
                    imageDataToServer("sign", originalUri);
                    break;
            }


        } catch (Exception e) {
            wvLoadUrl("javascript:addSealImage('', '{}'");
        }
    }

    private void decodeFromGalleryMulti(Intent data, int requestCode) {
        try {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            if(data.getData()!=null){

                Uri mImageUri=data.getData();


                switch (requestCode) {
                    case UPLOADFILE_FROM_GALLERY_REQUEST:
                        photoToServer(mImageUri, UPLOADFILE_FROM_GALLERY_REQUEST);
                        break;
                }

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    if(mClipData.getItemCount() > 5) {
                        wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지는 한번에 5개까지 선택가능합니다.');");

                        return;
                    }
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);

                    }

                    switch (requestCode) {
                        case UPLOADFILE_FROM_GALLERY_REQUEST:
                            photoToServer(mArrayUri, UPLOADFILE_FROM_GALLERY_REQUEST);
                            break;
                    }
                }

                ArrayList<Parcelable> fileUris = (ArrayList<Parcelable>)data.getParcelableArrayListExtra("selectedItems");
                if (fileUris != null) {
                    if (fileUris.size() > 5) {
                        wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지는 한번에 5개까지 선택가능합니다.');");
                        return;
                    }
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < fileUris.size(); i++) {

                        Parcelable fileUri = fileUris.get(i);
                        Uri uri = (Uri) fileUri;
                        mArrayUri.add(uri);

                    }

                    switch (requestCode) {
                        case UPLOADFILE_FROM_GALLERY_REQUEST:
                            photoToServer(mArrayUri, UPLOADFILE_FROM_GALLERY_REQUEST);
                            break;
                    }
                }


            }
        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");
        }
    }

    private void decodeFromCustomCamera(Intent data) {
        try {
            Uri originalUri = data.getParcelableExtra("originalImage"); //intent
            Uri ocrUri = data.getParcelableExtra("ocrImage"); //intent
            String ocrText = data.getStringExtra("text");

            conNoToServer(originalUri, ocrUri, ocrText);

        } catch (Exception e) {
            wvLoadUrl("javascript:addSealImage('', '{}'");
        }
    }

    private void sealNoToServer(Uri originalUri, int requestType) {
        try {
            InputStream in = getContentResolver().openInputStream(originalUri);
            Bitmap img = BitmapFactory.decodeStream(in);

            img = PhotoFragment.resizeBitmap(img, 1080, 1920);

            //todo: rotation
            ExifInterface exif = null;
            try {
                String imagePath = currentPhotoPath_;//getRealPathFromURI(originalUri); // path 경로
                exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);

                rotate(img, exifDegree);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
            byte[] bytes = baos.toByteArray();

            String base64String = Base64.encodeToString(bytes, Base64.DEFAULT);

            // String imagePath = getRealPathFromURI(uri);
            long fileSize = bytes.length;

//            Location loc = getLatestLocation();
//            String gpsType = getGpsType(loc.getProvider());
            String gpsType = "G";

            Log.d("CHECK", "base64String :" + base64String);
            Log.d("CHECK", "fileSize :" + fileSize);

            String jsString = "";

            if (requestType == SEAL_NO_FROM_GALLERY_REQUEST) {
                String format = "javascript:addSealImage_loc('%s', '', '', '');";
                jsString = String.format(format, "data:image/jpg;base64," + base64String);
            } else {
                //카메라
                try {
                    Location location = null;
                    LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (!isGPSEnabled && !isNetworkEnabled) {
                        String format = "javascript:addSealImage_loc('%s', 'x', 'x', '%s');";
                        jsString = String.format(format, "data:image/jpg;base64," + base64String, gpsType);
                    } else {

                        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION);


                        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                            ;
                        } else {
                            String format = "javascript:addSealImage_loc('%s', 'x', 'x', '%s');";
                            jsString = String.format(format, "data:image/jpg;base64," + base64String, gpsType);
                        }

                        float latitude = 0;
                        float longitude = 0;

                        if (isNetworkEnabled) {
                            if (locationManager != null) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    gpsType = "N";
                                    latitude = (float) location.getLatitude();
                                    longitude = (float) location.getLongitude();
                                }

                            }
                        }

                        if (isGPSEnabled) {
                            if (location == null) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                                if (locationManager != null) {
                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        gpsType = "G";
                                        latitude = (float) location.getLatitude();
                                        longitude = (float) location.getLongitude();
                                    }
                                }
                            }
                        }
                        String format = "javascript:addSealImage_loc('%s', '%f', '%f', '%s');";
                        jsString = String.format(format, "data:image/jpg;base64," + base64String, longitude, latitude, gpsType);

                    }
                } catch (Exception e) {
                    String format = "javascript:addSealImage_loc('%s', 'x', 'x', '%s');";
                    jsString = String.format(format, "data:image/jpg;base64," + base64String, gpsType);
                }
            }
            wvLoadUrl(jsString);
        } catch (Exception e) {
            wvLoadUrl("javascript:addSealImage('', '{}'");
        }
    }

    private void conNoToServer(Uri originalUri, Uri ocrUri, String ocrText) {
        if(gPhotoType.equals("1")) {
            try {
                String filePath = DataSet.getInstance().currentPhotoPath_;
                ExifInterface exif = null;
                try {
                    filePath = getRealPathFromURI(originalUri);
                    exif = new ExifInterface(filePath);
                } catch (Exception e) {
                    exif = new ExifInterface(filePath);
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);


                // 찍은 사진 mPhotoFile을 bitmap으로 decodeFile
                Bitmap cameraphoto = BitmapFactory.decodeFile(filePath);

                Bitmap img = rotateBitmap(cameraphoto, orientation);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
                byte[] bytes = baos.toByteArray();

                String base64String = Base64.encodeToString(bytes, Base64.DEFAULT);

                // String imagePath = getRealPathFromURI(uri);
                long fileSize = bytes.length;

                Log.d("CHECK", "base64String :" + base64String);
                Log.d("CHECK", "fileSize :" + fileSize);

                String format = "javascript:addCntrNoData('%s', '%s');";
//            String jsString = String.format(format, ocrText);
                String jsString = String.format(format, ocrText, "data:image/jpg;base64," + base64String);
                wvLoadUrl(jsString);

            } catch (Exception e) {
                wvLoadUrl("javascript:addCntrNo('', '{}'");
            }
        } else {
            try{
                InputStream in = getContentResolver().openInputStream(originalUri);
                Bitmap img = BitmapFactory.decodeStream(in);

                String imagePath = currentPhotoPath_;//getRealPathFromURI(originalUri); // path 경로
                img = PhotoFragment.resizeBitmap(img, 1080, 1920);

                //todo: rotation
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imagePath);
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);

                    rotate(img, exifDegree);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
                byte[] bytes = baos.toByteArray();

                String base64String = Base64.encodeToString(bytes, Base64.DEFAULT);

                // String imagePath = getRealPathFromURI(uri);
                long fileSize =  bytes.length;

                Log.d("CHECK", "base64String :" + base64String);
                Log.d("CHECK", "fileSize :" + fileSize);

                String format = "javascript:addCntrNoData('%s', '%s');";
//            String jsString = String.format(format, ocrText);
                String jsString = String.format(format, ocrText, "data:image/jpg;base64,"+ base64String);
                wvLoadUrl(jsString);

            }catch(Exception e)
            {
                wvLoadUrl("javascript:addCntrNo('', '{}'");
            }
        }

    }


    private void reuseImagesToServer(String type) {
        if (_reuseImages.size() <= 0) {
            EtransDrivingApp.getInstance().showToast("선택된 사진이 없습니다.");
            return;
        }

        ArrayList<String> base64Images = new ArrayList<String>();

        int cameraCnt = 0;
        for (Uri originalUri : _reuseImages) {
            try {
//                InputStream in = getContentResolver().openInputStream(originalUri);
//                Bitmap img = BitmapFactory.decodeStream(in);
//
//                img = PhotoFragment.resizeBitmap(img, 1080, 1920);
//
//                //todo: rotation
//                ExifInterface exif = null;
//                try {
//                    String imagePath = currentPhotoPath_;//getRealPathFromURI(originalUri); // path 경로
//                    exif = new ExifInterface(imagePath);
//                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                    int exifDegree = exifOrientationToDegrees(exifOrientation);
//
//                    rotate(img, exifDegree);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                String filePath = "";
                ExifInterface exif = null;
                try {
                    filePath = getRealPathFromURI(originalUri);
                    exif = new ExifInterface(filePath);
                } catch (Exception e) {
                    filePath = _reuseCamera.get(cameraCnt);
                    exif = new ExifInterface(filePath);
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                cameraCnt++;

                // 찍은 사진 mPhotoFile을 bitmap으로 decodeFile
                Bitmap cameraphoto = BitmapFactory.decodeFile(filePath);
                cameraphoto = PhotoFragment.resizeBitmap(cameraphoto, 1080, 1920);

                // 찍은 사진 rotate
                Bitmap img = rotateBitmap(cameraphoto, orientation);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
                byte[] bytes = baos.toByteArray();

                String base64String = "data:image/jpg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
                base64Images.add(base64String);

                // String imagePath = getRealPathFromURI(uri);
                long fileSize = bytes.length;

                Log.d("CHECK", "base64String :" + base64String);
                Log.d("CHECK", "fileSize :" + fileSize);
            } catch (Exception e) {
                wvLoadUrl("javascript:addReuseImage_loc_multi('', '', '', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}', '{}'");
            }
        }

        int imageCnt = base64Images.size();
        for (int i = imageCnt; i < MAX_REUSE_IMAGE_CNT; i++) {
            base64Images.add(""); //남아있는 갯수만큼 빈값으로 채우기
        }


        if(type.equals("camera")) {
            try {
                Location location = null;
                LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    String format = "javascript:addReuseImage_loc_multi('x', 'x', '', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
                    String jsString = String.format(format
                            , base64Images.get(0)
                            , base64Images.get(1)
                            , base64Images.get(2)
                            , base64Images.get(3)
                            , base64Images.get(4)
                            , base64Images.get(5)
                            , base64Images.get(6)
                            , base64Images.get(7)
                            , base64Images.get(8)
                            , base64Images.get(9)
                    );
                    wvLoadUrl(jsString);
                } else {

                    int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION);


                    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                        ;
                    } else {
                        String format = "javascript:addReuseImage_loc_multi('x', 'x', '', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
                        String jsString = String.format(format
                                , base64Images.get(0)
                                , base64Images.get(1)
                                , base64Images.get(2)
                                , base64Images.get(3)
                                , base64Images.get(4)
                                , base64Images.get(5)
                                , base64Images.get(6)
                                , base64Images.get(7)
                                , base64Images.get(8)
                                , base64Images.get(9)
                        );
                        wvLoadUrl(jsString);
                    }

                    float latitude = 0;
                    float longitude = 0;


                    String gpsType = "N";
                    if (isNetworkEnabled) {
                        if (locationManager != null) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                gpsType = "N";
                                latitude = (float) location.getLatitude();
                                longitude = (float) location.getLongitude();
                            }

                        }
                    }


                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    gpsType = "G";
                                    latitude = (float) location.getLatitude();
                                    longitude = (float) location.getLongitude();
                                }
                            }
                        }
                    }


                    String format = "javascript:addReuseImage_loc_multi('%f', '%f', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
                    String jsString = String.format(format, longitude, latitude, gpsType
                            , base64Images.get(0)
                            , base64Images.get(1)
                            , base64Images.get(2)
                            , base64Images.get(3)
                            , base64Images.get(4)
                            , base64Images.get(5)
                            , base64Images.get(6)
                            , base64Images.get(7)
                            , base64Images.get(8)
                            , base64Images.get(9)
                    );
                    wvLoadUrl(jsString);

                }
            } catch (Exception e) {
                String format = "javascript:addReuseImage_loc_multi('x', 'x', '', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
                String jsString = String.format(format
                        , base64Images.get(0)
                        , base64Images.get(1)
                        , base64Images.get(2)
                        , base64Images.get(3)
                        , base64Images.get(4)
                        , base64Images.get(5)
                        , base64Images.get(6)
                        , base64Images.get(7)
                        , base64Images.get(8)
                        , base64Images.get(9)
                );
                wvLoadUrl(jsString);
            }
        } else {
            String format = "javascript:addReuseImage_loc_multi('', '', '', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
            String jsString = String.format(format
                    , base64Images.get(0)
                    , base64Images.get(1)
                    , base64Images.get(2)
                    , base64Images.get(3)
                    , base64Images.get(4)
                    , base64Images.get(5)
                    , base64Images.get(6)
                    , base64Images.get(7)
                    , base64Images.get(8)
                    , base64Images.get(9)
            );
            wvLoadUrl(jsString);
        }



    }

    public boolean processUrlScheme(String url) {
        boolean processed = true;

        if (!url.contains("hybridapp://")) return false;

        Log.d(TAG, "url scheme[" + url + "]");

        if (url.contains("hybridapp://initparam")) {
            processInitParam(url);
        } else if (url.contains("hybridapp://authkey=")) {
            urlSchemeLogin(url);
        } else if (url.contains("hybridapp://loginOut")) {
            //???
            doLogout();
        } else if (url.contains("hybridapp://naviSet")) {
            //네비게이션 변경됨. 01:티맵, 02:카카오맵
            urlSchemeNaviSet(url);
        } else if (url.contains("hybridapp://clipboard")) {
            //공유하기 클립보드에 복사됨
            copyToClipboard();
        } else if (url.contains("hybridapp://kakaoShare")) {
            //카카오 공유하기 선택됨
            kakaoRecommend();
        } else if (url.contains("hybridapp://userimagesetting")) {
            //설정: 차량등록증/사업자등록증/서명이미지 등록
            String setting = getQueryString(url, "userimagesetting");
            urlSchemeImageSetting(setting);
        } else if (url.contains("hybridapp://viewUrl")) {
            //위수탁증 보기
            urlSchemeViewUrl(url);
        } else if (url.contains("hybridapp://setAddress")) {
            //위치정보조회 보기
            urlSchemeSetAddress();
        } else if (url.contains("hybridapp://setBackgroundLocationPermissionCheck")) {
            backgroundLocationPermissionCheck();
        } else if (url.contains("hybridapp://setAuthPhoneNo")) {
            //ios 폰인증
            urlSchemesetAuthPhoneNo(url);
        } else if (url.contains("hybridapp://callphone")) {
            //전화걸기
            urlSchemeCallPhone(url);
        } else if (url.contains("hybridapp://method")) {
            try {
                String method = getQueryString(url, "method");
                if (method != null) {
                    if (method.equalsIgnoreCase("takeSealImage")) {
                        //씰번호 촬영
                        chooseSealSource();
                    } else if (method.equalsIgnoreCase("takeReuseImage")) {
                        //복화사진전송
                        chooseReuseImageSource(url);
                    } else if (method.equalsIgnoreCase("takeCntrNo")) {
                        //컨번호 촬영
                        String photoType = getQueryString(url, "photoType");
                        gPhotoType = getQueryString(url, "photoType");
                        takePhotoContainerNumber("camera", photoType);
                    } else if (method.equalsIgnoreCase("main")) {
                        //서브페이지에서 메인으로 이동할때 호출됨.
                        isMianPage = true;
                        EtransDrivingApp.getInstance().setLoggedIn("Y");
                    } else if (method.equalsIgnoreCase("setaddr")) {
                        //도착지 설정
                        urlSchemeSetAddr(url);
                    } else if (method.equalsIgnoreCase("canceladdr")) {
                        //도착지 설정 취소
                        urlSchemeCancelAddr(url);
                    } else if (method.equalsIgnoreCase("setstatus")) {
                        //위치저장
                        urlSchemeSetStatus(url);
                    } else if (method.equalsIgnoreCase("setstatusdetail")) {
                        //상세
                        urlSchemeSetStatusDetail(url);
                    } else if (method.equalsIgnoreCase("wii")) {
                        //명세서 보기
                        urlSchemeWii(url);
                    } else if (method.equalsIgnoreCase("dostartinit")) {
                        //출발지 알림
                        urlSchemeDoStartInit(url);
                    } else if (method.equalsIgnoreCase("navistart")) {
                        //출발지 알림
                        urlSchemeNaviStart(url);
                    } else if (method.equalsIgnoreCase("startGpsService")) { // asmyoung
                        //gps server start  asmyoung
                        startGpsService();
                    } else if (method.equalsIgnoreCase("stopGpsService")) { // asmyoung
                        //gps server stop   asmyoung
                        stopGpsService();
                    } else if (method.equalsIgnoreCase("settingLocatcion")) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    } else if (method.equalsIgnoreCase("popView")) {
                        String loadUrl = getQueryString(url, "loadUrl");
                        String loadTitle = getQueryString(url, "loadTitle");
                        Intent intent = new Intent(MainActivity.this, PopWebViewActivity.class);
                        intent.putExtra("loadUrl", loadUrl);
                        intent.putExtra("loadTitle", loadTitle);
                        startActivity(intent);
                    } else if (method.equalsIgnoreCase("getDeviceInfo")) { // asmyoung
                        //gps server stop   asmyoung
                        getDeviceInfo();
                    } else if (method.equalsIgnoreCase("getUploadFile")) {
                        //업로드파일선택
                        chooseUploadSource();
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "getQueryString() " + e.getMessage());
            }
        } else if (url.contains("hybridapp://pki")) {
            try {
                String sss = url.substring("hybridapp://".length());
                String split[] = sss.split("\\?");

                HashMap<String, String> map = new HashMap<String, String>();

                if (split.length > 1) {
                    String nvs[] = split[1].split("&");

                    for (int i = 0; i < nvs.length; i++) {
                        String nv = nvs[i];
                        String nvsplit[] = nv.split("=");
                        String name = nvsplit[0];
                        String value = "";
                        if (nvsplit.length == 2)
                            value = nvsplit[1];

                        map.put(name, value);
                    }
                }
                String cmd = map.get("cmd");
                if (cmd != null) {
                    if (cmd.equalsIgnoreCase("getCertAuthCode")) {
                        //인증서 가져오기 인증코드 조회
                        getCertAuthCode();
                    }
                    else if (cmd.equalsIgnoreCase("certImport")) {
                        //인증서 가져오기
                        String authCodeStr = map.get("authCode");
                        String passwdStr = map.get("passwd");
                        passwdStr = URLDecoder.decode(passwdStr, "UTF-8");

                        certImport(authCodeStr, passwdStr);
                    }
                    else if (cmd.equalsIgnoreCase("getCertList")) {
                        //인증서 목록
                        getCertList();
                    }
                    else if (cmd.equalsIgnoreCase("getCertInfo")) {
                        //인증서 상세정보
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        getCertInfo(dnName);
                    }
                    else if (cmd.equalsIgnoreCase("makeSignDataLogin")) {
                        //로그인 전자서명 생성
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String passwd = map.get("passwd");
                        passwd = URLDecoder.decode(passwd, "UTF-8");
                        makeSignDataLogin(dnName, passwd);
                    }
                    else if (cmd.equalsIgnoreCase("makeSignDataPKCS7")) {
                        //PKCS7 전자서명 생성
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String data = map.get("oData");
                        data = URLDecoder.decode(data, "UTF-8");
                        String passwd = map.get("passwd");
                        passwd = URLDecoder.decode(passwd, "UTF-8");
                        makeSignDataPKCS7(dnName, data, passwd);
                    }
                    else if (cmd.equalsIgnoreCase("makeSignDataX509")) {
                        //PKCS7 전자서명 생성
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String digest = map.get("digest");
                        digest = URLDecoder.decode(digest, "UTF-8");
                        String passwd = map.get("passwd");
                        passwd = URLDecoder.decode(passwd, "UTF-8");
                        String pcsSSN = map.get("pcsSSN");
                        pcsSSN = URLDecoder.decode(pcsSSN, "UTF-8");
                        makeSignDataX509(dnName, digest, passwd, pcsSSN);
                    }
                    else if (cmd.equalsIgnoreCase("changeCertPassword")) {
                        //인증서 비밀번호변경
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String p = map.get("p");
                        p = URLDecoder.decode(p, "UTF-8");
                        String n1 = map.get("n1");
                        n1 = URLDecoder.decode(n1, "UTF-8");
                        String n2 = map.get("n2");
                        n2 = URLDecoder.decode(n2, "UTF-8");
                        changeCertPassword(dnName, p, n1, n2);
                    }
                    else if (cmd.equalsIgnoreCase("certExport")) {
                        //인증서 내보내기
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String authCode = map.get("authCode");
                        String passwd = map.get("passwd");
                        passwd = URLDecoder.decode(passwd, "UTF-8");
                        certExport(dnName, authCode, passwd);
                    }
                    else if (cmd.equalsIgnoreCase("deleteCert")) {
                        //인증서 삭제하기
                        String dnName = map.get("dnName");
                        dnName = URLDecoder.decode(dnName, "UTF-8");
                        Log.e("CHECK", dnName);
                        String passwd = map.get("passwd");
                        passwd = URLDecoder.decode(passwd, "UTF-8");
                        deleteCert(dnName, passwd);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "getQueryString() " + e.getMessage());
            }
        }  else if (url.contains("hybridapp://download")) {
            try {
                String sss = url.substring("hybridapp://".length());
                String split[] = sss.split("\\?");

                HashMap<String, String> map = new HashMap<String, String>();

                if (split.length > 1) {
                    String nvs[] = split[1].split("&");

                    for (int i = 0; i < nvs.length; i++) {
                        String nv = nvs[i];
                        String nvsplit[] = nv.split("=");
                        String name = nvsplit[0];
                        String value = "";
                        if (nvsplit.length == 2)
                            value = nvsplit[1];

                        map.put(name, value);
                    }
                }
                String download_url = map.get("url");
                download_url = URLDecoder.decode(download_url, "UTF-8");
                download(download_url);

            } catch (Exception e) {
                Log.e(TAG, "getQueryString() " + e.getMessage());
            }
        }

        return processed;
    }


    //컨테이너 번호촬영
    public void takePhotoContainerNumber(String photoFrom, String photoType) {
        handler.post(new Runnable() {
            public void run() {
                Log.d("CHECK", "takePhotoContainerNumber()");
                sendEventFC(TAG, "takePhotoContainerNumber()");

                URI uri = null;
                Intent intent = new Intent(MainActivity.this, kr.co.klnet.aos.etransdriving.CameraFrameActivity.class);
                intent.putExtra("photoType", photoType);
                if (photoFrom.equalsIgnoreCase("gallery")) {
                    startActivityForResult(intent, CONTAINER_NO_FROM_GALLERY_REQUEST);

                } else if (photoFrom.equalsIgnoreCase("camera")) {
                    startActivityForResult(intent, CONTAINER_NO_FROM_CAMERA_REQUEST);
                }
            }
        });
    }

    //씰 번호촬영
    public void takePhotoSealNumber(String photoFrom) {
        handler.post(new Runnable() {
            public void run() {
                sendEventFC(TAG, "takePhotoSealNumber()");

                URI uri = null;
                if (photoFrom.equalsIgnoreCase("gallery")) {
                    selectSealGallery();

                } else if (photoFrom.equalsIgnoreCase("camera")) {
                    selectSealPhoto();
                }
            }
        });
    }

    //복화사진 전송
    public void takePhotoReuseImage(String photoFrom) {
        handler.post(new Runnable() {
            public void run() {
                sendEventFC(TAG, "takePhotoReuseImage()");

                URI uri = null;
                if (photoFrom.equalsIgnoreCase("gallery")) {
                    selectReuseImageGallery();

                } else if (photoFrom.equalsIgnoreCase("camera")) {
                    selectReuseImageCamera();
                }
            }
        });
    }

    private void selectSealGallery() {
        sendEventFC(TAG, "selectSealGallery()");
        requestGalleryImage(SEAL_NO_FROM_GALLERY_REQUEST);
    }


    private void selectSealPhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile("seal.jpg");
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri_ = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri_);
                    startActivityForResult(intent, SEAL_NO_FROM_CAMERA_REQUEST);
                }
            }
        }
    }


    private void selectReuseImageGallery() {
        sendEventFC(TAG, "selectReuseImageGallery()");
        requestGalleryImage(REUSE_IMAGE_FROM_GALLERY_REQUEST);
    }

    private void selectReuseImageCamera() {
        sendEventFC(TAG, "selectReuseImageCamera()");
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    String filename = String.format("reuse%2d.jpg", getReuseImageNextNo());
                    //photoFile = createImageFile(filename);
                    photoFile = createImageFileMulti(filename);
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    //asmm 주석
                    photoUri_ = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri_);
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REUSE_IMAGE_FROM_CAMERA_REQUEST);
                }
            }
        }
    }

    private void callPhone(String number) {
        Uri uri = Uri.parse("tel:" + number);
//        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
////        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//        startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

        startActivity(intent);
    }

    private void requestGalleryImage(int requestType) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, requestType);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix(); // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private void fromGallery(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri); // path 경로

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processWebviewShouldLoading(WebView view, String url) {
        //네이티브 기능 처리용
        if (url.contains("hybridapp://")) {

            boolean processed = processUrlScheme(url);
//            return true;
            return processed;
        }

        if (url.startsWith("sms:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("mailto:")) {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("intent:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    startActivity(marketIntent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (url.startsWith("sms:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("mailto:")) {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("intent:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    startActivity(marketIntent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (url.contains("play.google.com")) {
            // play.google.com 도메인이면서 App 링크인 경우에는 market:// 로 변경
            String[] params = url.split("details");
            if (params.length > 1) {
                url = "market://details" + params[1];
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        }

        if (url.contains(".text") || url.contains(".txt")) {
            DataSet.getInstance().istext = "true";
        }

        if (url.startsWith("http:") || url.startsWith("https:")) {
            // HTTP/HTTPS 요청은 내부에서 처리한다.
            view.loadUrl(url);
        } else {
            Intent intent;

            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException e) {
                // 처리하지 못함
                return false;
            }

            try {
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Intent Scheme인 경우, 앱이 설치되어 있지 않으면 Market으로 연결
                if (url.startsWith("intent:") && intent.getPackage() != null) {
                    url = "market://details?id=" + intent.getPackage();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    // 처리하지 못함
                    return false;
                }
            }

        }

        return true;
    }


    public File createImageFile(String filename) throws IOException {
        File path = new File(Environment.getExternalStorageDirectory() + "/klnet/");

        String timeStamp = new SimpleDateFormat("MMdd_HHmmssSSS").format(new Date());
        //String imageFileName =  "klnet_" + timeStamp +  "_"  + filename;
        String imageFileName = "klnet_" + "ocr" + "_" + filename;
//        File file = new File(path, imageFileName);
//
//        try {
//            // Make sure the Pictures directory exists.
//            if (path.mkdirs()) {
////                Toast.makeText(this, "Not exist :" + path.getName(), Toast.LENGTH_SHORT).show();
//            }
//
//            Log.d("ExternalStorage", "Writed " + path + file.getName());
//
//
//        } catch (Exception e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath_ = file.getAbsolutePath();

        return file;

    }

    public File createImageFileMulti(String filename) throws IOException {
        File path = new File(Environment.getExternalStorageDirectory() + "/klnet/");

        String timeStamp = new SimpleDateFormat("MMdd_HHmmssSSS").format(new Date());
        //String imageFileName =  "klnet_" + timeStamp +  "_"  + filename;
        String imageFileName = "klnet_" + "ocr" + "_" + filename;

        //asmm 주석
//        File file = new File(path, imageFileName);
//
//        try {
//            // Make sure the Pictures directory exists.
//            if (path.mkdirs()) {
////                Toast.makeText(this, "Not exist :" + path.getName(), Toast.LENGTH_SHORT).show();
//            }
//
//            Log.d("ExternalStorage", "Writed " + path + file.getName());
//
//
//        } catch (Exception e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        _reuseCamera.add(file.getAbsolutePath());

        return file;

    }

    public boolean urlSchemeLogin(String url) {


//2020.5.18 로그인 완료되면, 자동로그인 여부와 authkey값이 전달된다
        EtransDrivingApp.getInstance().setLoggedIn("Y");

        //로그인이 완료되면 위치정보 보고용 서비스 시작
        //startReportService();  //asmyoung

        String webVer = getQueryString(url, "webVer");
        webVer = "1.1";

        String carrierCd = getQueryString(url, "carrierCd");
        String collectTerm = getQueryString(url, "collectTerm");
        String sendTerm = getQueryString(url, "sendTerm");
        String restFlag = getQueryString(url, "restFlag");
        String chassisNo = getQueryString(url, "chassisNo");
        String carCd = "";
        try {
            carCd = URLDecoder.decode(getQueryString(url, "carCd"), "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        EtransDrivingApp.getInstance().setCarCd(carCd);
        EtransDrivingApp.getInstance().setVehicleId(carCd);
        EtransDrivingApp.getInstance().setCarrierId(carrierCd);
        EtransDrivingApp.getInstance().setReportPeroid(sendTerm);
        EtransDrivingApp.getInstance().setCreationPeroid(collectTerm);
        EtransDrivingApp.getInstance().setRestFlag(restFlag);
        EtransDrivingApp.getInstance().setChassisNo(chassisNo);

        try {
            String isAutoLogin = getQueryString(url, "isAutoLogin");
            String authkey = "";
            if (isAutoLogin != null) {
                EtransDrivingApp.getInstance().setAutoLogin(isAutoLogin);
                authkey = getQueryString(url, "authkey");
                if ("Y".equalsIgnoreCase(isAutoLogin)) {

                    if (authkey != null && authkey.length() > 0) {
                        EtransDrivingApp.getInstance().setAuthKey(authkey);
                    }
                } else {
                    EtransDrivingApp.getInstance().setAuthKey("");
                }
            } else {
                EtransDrivingApp.getInstance().setAuthKey("");
                EtransDrivingApp.getInstance().setAutoLogin("N");
            }

            if (webVer != null && webVer.length() > 0) {
                //
                String token = EtransDrivingApp.getInstance().loadPushToken();
                Log.d(TAG, "::::::::::::::::::::::::::::::::::::::::::::::::::::");
                Log.d(TAG, "      carCd=" + carCd);
                Log.d(TAG, "  VehicleId=" + carCd);
                Log.d(TAG, "  carrierCd=" + carrierCd);
                Log.d(TAG, "collectTerm=" + collectTerm);
                Log.d(TAG, "   sendTerm=" + sendTerm);
                Log.d(TAG, "   restFlag=" + restFlag);
                Log.d(TAG, "  chassisNo=" + chassisNo);
                Log.d(TAG, "      token=" + token);
                Log.d(TAG, "isAutoLogin=" + isAutoLogin);
                Log.d(TAG, "    authkey=" + authkey);
                Log.d(TAG, "     userid=" + DataSet.getInstance().userid);
                Log.d(TAG, "::::::::::::::::::::::::::::::::::::::::::::::::::::");
                Log.d(TAG, "      setJPPMobileAppId=" + "ETDRIVING");
                Log.d(TAG, "        setJPPDeviceOs=" + "fcm_and");
                Log.d(TAG, "        setJPPDeviceId=" + deviceId);
                Log.d(TAG, "           setJPPToken=" + token);
                Log.d(TAG, "          setJPPUserId=" + DataSet.getInstance().userid);
                Log.d(TAG, "         setJPPPushUrl=" + DataSet.push_url);
                Log.d(TAG, "       setJPPModelName=" + Build.MODEL);
                Log.d(TAG, " setJPPDeviceOsVersion=" + Build.VERSION.RELEASE);
                Log.d(TAG, "               setPush=" + "Y");
                Log.d(TAG, "::::::::::::::::::::::::::::::::::::::::::::::::::::");

                wvLoadUrl("javascript:setJPPMobileAppId('ETDRIVING')");
                wvLoadUrl("javascript:setJPPDeviceOs('fcm_and')");
                wvLoadUrl("javascript:setJPPDeviceId('" + deviceId + "')");
                wvLoadUrl("javascript:setJPPToken('" + token + "')");
                wvLoadUrl("javascript:setJPPUserId('" + DataSet.getInstance().userid + "')");
                wvLoadUrl("javascript:setJPPPushUrl('" + DataSet.push_url + "')");
                wvLoadUrl("javascript:setJPPModelName('" + Build.MODEL + "')");
                wvLoadUrl("javascript:setJPPDeviceOsVersion('" + Build.VERSION.RELEASE + "')");
                wvLoadUrl("javascript:setPush('Y')");
            } else {
                //old version
                wvLoadUrl("javascript:goMain()");
            }

        } catch (Exception e) {
            Log.e(TAG, "getQueryString() " + e.getMessage());
        }
        return true;
    }

    public boolean urlSchemeNaviSet(String url) {
        try {
            String naviType = getQueryString(url, "naviSet");
            EtransDrivingApp.getInstance().setNavigationType(naviType);
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeNavigation() " + e.getMessage());
        }
        return true;
    }


    public boolean urlSchemeImageSetting(String setting) {
        if ("car".equalsIgnoreCase(setting)) {
            //차량등록증
            requestCarBizCdImage();
        } else if ("biz".equalsIgnoreCase(setting)) {
            //사업자등록증
            requestBizCdImage();
        } else if ("sign".equalsIgnoreCase(setting)) {
            //서명이미지
            requestSignImage();
        }

        return true;
    }

    public boolean urlSchemeViewUrl(String url) {
        String viewUrl = getQueryString(url, "viewUrl");
        //"hybridapp://viewUrl=/mi330U/etruckbank/jsp/reportWiSutakPrint.jsp?dispatchNo='200608566696'"
        if (viewUrl != null && viewUrl.length() > 0) {
            String params = removeUrlPath(viewUrl);
            String dispatchNo = getQueryString(params, "dispatchNo");
            dispatchNo = dispatchNo.replace("\'", "");
            CommonUtil.setOnWisutakView(this, viewUrl, dispatchNo);
        }

        return true;
    }

    public boolean urlSchemeWii(String urlscheme) {
        //hybridapp://method=wii&accountGrpCd=18&billNo=B180829217352&orgReadStatus=1&carCd=%EC%84%9C%EC%9A%B880%EA%B0%803366&adjustDtView=2018-08-29&carrierNm=%EC%9A%B4%EC%86%A1%EC%82%AC_C1
        String accountGrpCd = getQueryString(urlscheme, "accountGrpCd");
        String billNo = getQueryString(urlscheme, "billNo");
        String orgReadStatus = getQueryString(urlscheme, "orgReadStatus");
        String carCd = getQueryString(urlscheme, "carCd");
        String adjustDtView = getQueryString(urlscheme, "adjustDtView");
        String carrierNm = getQueryString(urlscheme, "carrierNm");

        String fileNm = adjustDtView.replace("-", "").substring(2, 4) + "-" + adjustDtView.replace("-", "").substring(4, 6) + "_" + carCd.substring(carCd.length() - 4, carCd.length()) + "_" + carrierNm;
        //String url = "http://" + getString(R.string.URL_CONN_SVR) + "/mi330U/etruckbank/jsp/";
        //String url = "https://" + getString(R.string.URL_CONN_SVR) + ":8443/etdriving/dispatch/";
        String url = getString(R.string.URL_CONN_SVR8)+"/etdriving/dispatch/"; //(dev)etdriving

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("url", url);
        paramMap.put("supplierReadStatus", orgReadStatus);
        paramMap.put("orgReadStatus", orgReadStatus);
        paramMap.put("billNo", billNo);
        paramMap.put("accountGrpCd", accountGrpCd);
        paramMap.put("carCd", carCd);
        paramMap.put("reportNm", "reportMobilePayPrint");
        paramMap.put("fileNm", fileNm);
        paramMap.put("adjustDtView", adjustDtView);

        EtransDrivingApp.getInstance().webViewURL = CommonUtil.makeReportUrl(paramMap);
        EtransDrivingApp.getInstance().webViewFilePath = StringUtil.nullConvert(paramMap.get("filePath"));
        EtransDrivingApp.getInstance().webViewFileNm = StringUtil.nullConvert(paramMap.get("fileNm"));

        WebViewContainer mobWebViewContainer = new WebViewContainer();
        Intent i = new Intent(this, mobWebViewContainer.getClass());
        startActivityForResult(i, HANDLER_SEARCH_SALE_LIST);

        return true;
    }

    // asmyoung old
//    public void urlSchemeSetAddress() {
//        try {
//            String fmt = "javascript:getAddress('%f', '%f')";
//            Location loc = getLatestLocation();
//            String params = String.format(fmt, loc.getLongitude(), loc.getLatitude());
//            wvLoadUrl(params);
//
//        } catch (Exception e) {
//            Log.e(TAG, "urlSchemeSetAddress() " + e.getMessage());
//        }
//    }

    public void urlSchemeSetAddress() {
        try {
            Location location = null;
            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                String fmt = "javascript:getAddress('x', 'x')";
                wvLoadUrl(fmt);
            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    ;
                } else {
                    String fmt = "javascript:getAddress('x', 'x')";
                    ;
                    wvLoadUrl(fmt);
                }

                float latitude = 0;
                float longitude = 0;

                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                        }

                    }
                }


                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = (float) location.getLatitude();
                                longitude = (float) location.getLongitude();
                            }
                        }
                    }
                }

                String fmt = "javascript:getAddress('%f', '%f')";
                Location loc = getLatestLocation();
                String params = String.format(fmt, longitude, latitude);
                wvLoadUrl(params);
            }
        } catch (Exception e) {
            String fmt = "javascript:getAddress('x', 'x')";
            wvLoadUrl(fmt);
            Log.e(TAG, "urlSchemeSetAddress() " + e.getMessage());
        }
    }

    public void urlSchemesetAuthPhoneNo(String urlscheme) {
        try {
            String url2 = urlscheme.replace("hybridapp://setAuthPhoneNo?", "");
            String phoneNo = getQueryString(url2, "phoneNo");

            EtransDrivingApp.getInstance().setAuthMobileNo(phoneNo);

            gotoLoginPage();

        } catch (Exception e) {
            Log.e(TAG, "urlSchemesetAuthPhoneNo() " + e.getMessage());
        }
    }

    public void urlSchemeCallPhone(String urlscheme) {
        try {
            String phoneNo = getQueryString(urlscheme, "callphone");
            if(phoneNo!=null && phoneNo!="") {
                callPhone(phoneNo);
            }
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeCallPhone() " + e.getMessage());
        }
    }



    public void urlSchemeSetAddr(String url) {
        try {
            String idx = getQueryString(url, "idx");
            String addrType = getQueryString(url, "addrType");

            String fmt = "javascript:setAddr('%s', '%s', '%f', '%f', '%s')";
            Location loc = getLatestLocation();
            String gpsType = getGpsType(loc.getProvider());
            String params = String.format(fmt, addrType, idx, loc.getLongitude(), loc.getLatitude(), gpsType);
            wvLoadUrl(params);
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeSetAddr() " + e.getMessage());
        }
    }

    public void urlSchemeCancelAddr(String url) {
        try {
            String idx = getQueryString(url, "idx");
            String addrType = getQueryString(url, "addrType");

            String fmt = "javascript:cancelAddr('%s', '%s', '%f', '%f', '%s')";
            Location loc = getLatestLocation();
            String gpsType = getGpsType(loc.getProvider());
            String params = String.format(fmt, addrType, idx, loc.getLongitude(), loc.getLatitude(), gpsType);
            wvLoadUrl(params);
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeCancelAddr() " + e.getMessage());
        }
    }

    public void urlSchemeSetStatus(String url) {
        try {
            String idx = getQueryString(url, "idx");

            String fmt = "javascript:setStatus('%s', '%f', '%f', '%s')";
            Location loc = getLatestLocation();
            String gpsType = getGpsType(loc.getProvider());
            String params = String.format(fmt, idx, loc.getLongitude(), loc.getLatitude(), gpsType);
            wvLoadUrl(params);
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeSetStatus() " + e.getMessage());
        }
    }

    public void urlSchemeSetStatusDetail(String url) {
        try {
            String fmt = "javascript:setStatusDetail('%f', '%f', '%s')";
            Location loc = getLatestLocation();
            String gpsType = getGpsType(loc.getProvider());
            String params = String.format(fmt, loc.getLongitude(), loc.getLatitude(), gpsType);
            wvLoadUrl(params);
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeSetStatusDetail() " + e.getMessage());
        }
    }

    public void urlSchemeDoStartInit(String url) {
        try {
            String type = getQueryString(url, "type");
            String bleCheck = getQueryString(url, "bluetooth");

            chkGpsOnOff(bleCheck,  "HJNPC");

            Location loc = getLatestLocation();
            String gpsType = getGpsType(loc.getProvider());

            if ("start".equalsIgnoreCase(type)) {
                String fmt = "javascript:fn_doStart('%f', '%f', '%s')";
                String params = String.format(fmt, loc.getLongitude(), loc.getLatitude(), gpsType);
                wvLoadUrl(params);

            } else if ("end".equalsIgnoreCase(type)) {
                String fmt = "javascript:fn_doEnd('%f', '%f', '%s')";
                String params = String.format(fmt, loc.getLongitude(), loc.getLatitude(), gpsType);
                wvLoadUrl(params);
            }
        } catch (Exception e) {
            Log.e(TAG, "urlSchemeDoStartInit() " + e.getMessage());
        }
    }

    public void urlSchemeNaviStart(String url) {
        try {
            String x = getQueryString(url, "wgs84X"); //longitude
            String y = getQueryString(url, "wgs84Y"); //latitude
            String goalName = "목적지";

            launchNavigation(Float.parseFloat(x), Float.parseFloat(y), goalName);

        } catch (Exception e) {
            Log.e(TAG, "urlSchemeNaviStart() " + e.getMessage());
        }
    }

    public String getGpsType(String provider) {
        if(LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)) {
            return "G";
        } else if(LocationManager.NETWORK_PROVIDER.equalsIgnoreCase(provider)) {
            return "N";
        } else if(JLocationManager.FUSED_PROVIDER.equalsIgnoreCase(provider)) {
            return "F";
        }

        return "I";
    }

    public void requestCarBizCdImage() {
        requestGalleryImage(CAR_BIZ_CD_FROM_GALLERY_REQUEST);
    }

    public void requestBizCdImage() {
        requestGalleryImage(BIZ_CD_FROM_GALLERY_REQUEST);
    }


    public void requestSignImage() {
        popSignView();
    }

    public static String removeUrlPath(String url) {
        // 경로를 삭제하고, 파라미터만 반환
        String[] params = url.split("\\?");

        if(params.length==2){
            return params[1];
        }
        return url;
    }


    public static String getQueryString2(String seperator, String urlScheme, String tag) {

        String url = urlScheme.replace("hybridapp://", "");

        String[] params = url.split(seperator);
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            param = param.trim();
            StringBuffer sb = new StringBuffer(param);

            boolean finished = false;

            for(int i=0;i<sb.length();i++) {
                //문자열 마지막에 '=' 있으면 예외처리
                int pos = param.length()-1-i;
                char ch = param.charAt(pos);
                if(ch=='=') {
                    if(!finished)
                        ch = '\n';

                } else {
                    finished = true;
                }

                sb.setCharAt(pos, ch); //대체문자 저장
            }
            String adjParam = sb.toString();
            String[] pair = adjParam.split("=");


            if(pair.length==2){
                map.put(pair[0], pair[1].replace("\n", "="));
            } else {
                //invalid key & value
            }
        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            if(key.equals(tag)){
                return map.get(key);
            }
        }
        return "";
    }

    public static String getQueryString(String seperator, String urlScheme, String tag) {

        String url = urlScheme.replace("hybridapp://", "");

        String[] params = url.split(seperator);
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] pair = param.split("=");
            if(pair.length==2){
                map.put(pair[0].trim(), pair[1]);
            } else {
                //invalid key & value
            }
        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            if(key.equals(tag)){
                return map.get(key);
            }
        }
        return "";
    }

    public static String getQueryString(String urlScheme, String tag) {

        String url = urlScheme.replace("hybridapp://", "");

        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] pair = param.split("=");
            if(pair.length==2){
                map.put(pair[0], pair[1]);
            } else if(pair.length==3){
                // 아래와 같은 형태일때의 workarround
                // hybridapp://viewUrl=/mi330U/etruckbank/jsp/reportWiSutakPrint.jsp?dispatchNo='200608566696'"
                map.put(pair[0], pair[1]+"=" + pair[2]);
            } else {
                //invalid key & value
            }
        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            if(key.equals(tag)){
                return map.get(key);
            }
        }
        return "";
    }

    private void popPermissionView(){
        Log.d("############jlim",EtransDrivingApp.getInstance().getGpsAgreementYn());
        if(!EtransDrivingApp.getInstance().getGpsAgreementYn().equalsIgnoreCase("Y")) {
            final PopupNoticePermissons popup = new PopupNoticePermissons(this);
            popup.setCancelable(false);
            popup.setCanceledOnTouchOutside(false);
            popup.setButton1("확 인", new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    popup.dismiss();
                    EtransDrivingApp.getInstance().setGpsAgreementYn("Y");
//                    onAppLoad();
                    checkPermissions();
                }
            });
        }else{
//            onAppLoad();
            checkPermissions();
        }
    }

    private boolean checkOverlayWindowService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !android.provider.Settings.canDrawOverlays(this)) {
            if (EtransDrivingApp.getInstance().getFstLogin().equalsIgnoreCase("N")) {
                //jlim 최초 설치 및 실행 후 권한 허용 지나 로그인까지 갔다온 케이스에는
                showCustomToast("원활한 알림메시지(코피노 정보, 배차지시 등) 수신을 위해의 '다른 앱 위에 표시되는 앱' 권한을 설정해주시기 바랍니다.");
                onAppLoad();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림");
                builder.setMessage("원활한 알림메시지(코피노 정보, 배차지시 등) 수신을 위해의 '다른 앱 위에 표시되는 앱' 권한을 설정해주시기 바랍니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
                            }
                        });
                builder.show();
                return false;
            }
        }
        return true;
    }

    private boolean checkAlarmRemainderPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //안드로이드 13 대응
            if (!alarmManager.canScheduleExactAlarms()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림");
                builder.setMessage("원활한 위치정보 수집을 위해의 '알람 및 리마인더 설정 허용' 권한을 설정해주시기 바랍니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent appDetail = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                        Uri.parse("package:" + getPackageName()));
                                appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                                appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(appDetail, ALRAM_REMAIDER_PERMISSION_REQUEST);
                            }
                        });
                builder.show();

                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    void dialogPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카메라/앨범 선택");
        builder.setMessage("씰 번호를 불러옵니다");

        builder.setPositiveButton("카메라",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        takePhotoSealNumber("camera");

                    }
                });
        builder.setNegativeButton("앨범",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        takePhotoSealNumber("gallery");

                    }
                });

        builder.show();
    }

    void chooseSealSource()
    {
        final CharSequence[] items = {"카메라", "앨범"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("씰번호 선택")
                .setItems(items, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int index){
                        if(index==0) {
                            takePhotoSealNumber("camera");
                        } else {
                            takePhotoSealNumber("gallery");
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void chooseReuseImageSource(String url) //복화사진 전송
    {
        final CharSequence[] items = {"카메라", "앨범"};

        String maxCount = getQueryString(url, "maxcount");
        _maxCountReuseImage = MAX_REUSE_IMAGE_CNT;
        if(maxCount!=null && maxCount.length()>0) {
            try {
                _maxCountReuseImage = Integer.parseInt(maxCount);
            }catch(Exception e) {
                e.printStackTrace();;
            }
        }

        clearReuseImages();
        clearReuseCameras();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //최초 복화사진 가져오기 선택 >> 멀티 첨부 공동 사용위해 복화 라는 단어 빼기
        builder.setTitle("사진 가져오기 선택")
                .setItems(items, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int index){
                        if(index==0) {
                            takePhotoReuseImage("camera");
                        } else {
                            takePhotoReuseImage("gallery");
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void gotoLoginPage() {
        sendEventFC(TAG, "goto login, url :" + DataSet.connect_url + DataSet.login_path);
        wvLoadUrl(DataSet.connect_url + DataSet.login_path+"?method=mainForm"
                +"&jppDeviceId="+EtransDrivingApp.getInstance().getDeviceID(this)
                +"&isLbsStartYN="+EtransDrivingApp.getInstance().getIsLbsStartYn()
                +"&deviceMobileNo="+EtransDrivingApp.getInstance().getSystemMobileNo());
//        wvLoadUrl("http://juis.asuscomm.com:8880/z/index.jsp");
    }

    private void gotoPushRedirectPage(String params, String push_seq, String push_type
            , String push_title, String push_body
            , String push_doc_gubun, String push_param) {
        sendEventFC(TAG, "goto push, url :" + DataSet.connect_url + DataSet.push_redirect_url + params);
        //wvLoadUrl(DataSet.connect_url + DataSet.push_redirect_url + params);

        wvLoadUrl("javascript:pushLink_common('"+push_seq+"', '"+push_doc_gubun+"', '"+push_type+"', '"+push_title+"', '"+push_body+"', '"+push_param+"','"+EtransDrivingApp.getInstance().getCreationPeroid()+"','"+EtransDrivingApp.getInstance().getReportPeroid()+"');");
    }


    private void processInitParam(String url) {
        String webVer = getQueryString(url, "webVer");
        String isAutoLogin = EtransDrivingApp.getInstance().getAutoLogin();
        String vAuthKey = EtransDrivingApp.getInstance().readPref("AutoLogin", "vAuthKey");

        String mobileNo = EtransDrivingApp.getInstance().getMobileNo();

        //자동로그인일 경우, 1.authkey 를 읽어들인 후 로그인
        String initparam = "";
        if (isAutoLogin.equals("Y") && !vAuthKey.equals("")) {
            Log.i(TAG, "::::: Success, FCM instance token=2");
            initparam = "javascript:initparam_new("
                    + "'" + mobileNo + "'"
                    + ", '" + EtransDrivingApp.getInstance().getAuthKey() + "'"
                    + ", '" + isAutoLogin + "'"
                    + ", '" + EtransDrivingApp.getInstance().getSdkName() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneModel() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneAppVer() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getMacAddress() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneMsp() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getOsVer() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getDownGB() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getIsAgreeType() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getGpsAgreementYn() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getSystemMobileNo() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getFstLogin() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getEmail() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getDeviceID(this) + "'"
                    + ")";

            wvLoadUrl(initparam);

        } else {
            //일반 로그인
            rel_intro.setVisibility(View.GONE);
            rel_main.setVisibility(View.VISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            initparam = "javascript:initparam_new("
                    + "'" + mobileNo + "'"
                    + ", ''" //authkey
                    + ", 'N'" //AutoLogin
                    + ", '" + EtransDrivingApp.getInstance().getSdkName() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneModel() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneAppVer() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getMacAddress() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getPhoneMsp() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getOsVer() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getDownGB() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getIsAgreeType() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getGpsAgreementYn() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getSystemMobileNo() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getFstLogin() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getEmail() + "'"
                    + ", '" + EtransDrivingApp.getInstance().getDeviceID(this) + "'"
                    + ")";

            wvLoadUrl(initparam);

        }
    }

    private void registerWebViewListener() {
        final Context myApp = this;

        WebView01.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimeType);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file");
//                    String fileName = contentDisposition.replace("inline; filename=", "");
                    String fileName = contentDisposition.substring(contentDisposition.indexOf("=")+1, contentDisposition.lastIndexOf(";"));
                    fileName = fileName.replaceAll("\"", "");
                    request.setTitle(fileName);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
//                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();

                } catch (Exception e) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        } else {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }

                }
            }
        });

        WebView01.setWebViewClient(new WebViewClient() {

            //@SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("CHECK", url);
                if (view == null || url == null) {
                    return false;
                }
                boolean processed = processWebviewShouldLoading(view, url);
                return processed;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains(DataSet.login_path)
                        || url.contains(DataSet.loginOutDo_path)
                ) {
                    isLoginPage = true;
                    isMianPage = false;
                } else if (url.contains(DataSet.main_path)) {
                    rel_intro.setVisibility(View.GONE);
                    rel_main.setVisibility(View.VISIBLE);
                    isLoginPage = false;
                    isMianPage = true;
                } else {
                    isLoginPage = false;
                    isMianPage = false;
                }
            }


            @Override
            public void onReceivedError(final WebView view, int errorCode, String description,
                                        final String failingUrl) {
                sendEventFC("onReceivedError", description);
                sendEventGA("onReceivedError", description);

                if(view.getContext()!=null && !isFinishing()) {
                    new AlertDialog.Builder(view.getContext() /* myApp */)
                            .setTitle("확인")
                            .setMessage("접속 할 수 없습니다. 관리자에게 문의 바랍니다.")
                            .setPositiveButton(android.R.string.ok,
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            DataSet.getInstance().isrunning = "false";
                                            DataSet.getInstance().islogin = "false";
                                            DataSet.getInstance().userid = "";
//                                        finish();
                                            gotoLoginPage();
                                        }
                                    })
                            .setCancelable(false)
                            .create()
                            .show();
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                boolean processed = processUrlScheme(url);

                if (url.contains(DataSet.login_path)) {
                    //로그인url 인 경우 처리
                    rel_intro.setVisibility(View.GONE);
                    rel_main.setVisibility(View.VISIBLE);

                } else if(url.contains(DataSet.push_redirect_url)) {
                    //푸시 수신처리하는 페이지
                    rel_intro.setVisibility(View.GONE);
                    rel_main.setVisibility(View.VISIBLE);

                    String params = removeUrlPath(url); // 파라미터만 반환

                    String seq = getQueryString(params, "seq");
                    String doc_gubun = getQueryString(params, "doc_gubun");
                    String type = getQueryString(params, "type");
                    String call_text = getQueryString(params, "call_text");
                    String call_text_sub = getQueryString(params, "call_text_sub");
                    String call_param = getQueryString(params, "call_param");

                    //javascript:pushLink(seq, doc_gubun, type, call_text, call_text_sub, call_param)
                    String format = "javascript:pushLink('%s', '%s', '%s', '%s', '%s', '%s');";
                    String jsString = String.format(format, seq, doc_gubun, type, call_text, call_text_sub, call_param);
                    wvLoadUrl(jsString);
                }

                if (url.contains(DataSet.main_path)) {
                    prefsDstPrt = getSharedPreferences("Dstprt", Activity.MODE_PRIVATE);
                    DstprtCode = prefsDstPrt.getString("DstprtCode", "");
                    DstprtName = prefsDstPrt.getString("DstprtName", "");

                    //백그라운드상태에서 수신된 푸시가 처리되었는지 확인
                    if (DataSet.getInstance().isrunapppush) {
                        processPushNoti();
                    }
                }

                if (url.contains(DataSet.loginOutDo_path)) {
                    doLogout();

                }
//                String url = "http://" + getString(R.string.URL_CONN_SVR);
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                } else {
                    String urlHost = null;
                    try {
                        urlHost = new URL(url).getHost().toString();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    String cookie = CookieManager.getInstance().getCookie(urlHost);
                    String sessionId = "JSESSIONID=" + getQueryString2(";", cookie, "JSESSIONID");
                    EtransDrivingApp.getInstance().setUserCookie(sessionId);

                }

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }

                //최초 실행 여부 판단
                String first = EtransDrivingApp.getInstance().getFstLogin();
                if (!"Y".equalsIgnoreCase( "Y")) {
                    Log.d("first", "THE FIRST TIME");
                    EtransDrivingApp.getInstance().setFstLogin("Y");
                    DialogHtmlView();
                }
            }
        });

        WebView01.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                // Dialog Create Code
                _newWebView = new WebView(MainActivity.this);
                WebSettings webSettings = _newWebView.getSettings();
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setSupportMultipleWindows(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(_newWebView);

                ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                dialog.show();
                _newWebView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onCloseWindow(WebView window) {
                        dialog.dismiss();
                    }
                });

                // WebView Popup에서 내용이 안보이고 빈 화면만 보여 아래 코드 추가
                _newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return false;
                    }
                });

//                _newWebView.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                            _newWebView.loadUrl("javascript:self.close();");
//                            return true;
//                        }
//
//                        return false;
//                    }
//
//                });
                ((WebView.WebViewTransport)resultMsg.obj).setWebView(_newWebView);
                resultMsg.sendToTarget();
                return true;

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                String msg = "url=[" + url + "]" + ", message=" + message;

                sendEventFC("onJsAlert", msg);
                sendEventGA("onJsAlert", msg);

                if(view.getContext()!=null && !isFinishing()) {
                    new AlertDialog.Builder(view.getContext() /* myApp */)
                            .setTitle("")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok,
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.confirm();
                                        }
                                    })
                            .setCancelable(false)
                            .create()
                            .show();
                    return true;
                }

                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message,
                                       final JsResult result) {
                // TODO Auto-generated method stub
                String msg = "url=[" + url + "]" + ", message=" + message;

                sendEventFC("onJsConfirm", msg);
                sendEventGA("onJsConfirm", msg);

                if(view.getContext()!=null && !isFinishing()) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("확인")
                            .setMessage(message)
                            .setPositiveButton("확인",
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.confirm();
                                        }
                                    })
                            .setNegativeButton("취소",
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.cancel();
                                        }
                                    })
                            .setCancelable(false)
                            .create()
                            .show();
                    return true;
                }

                return super.onJsConfirm(view, url, message, result);

            }
        });

    }

    public void wvLoadUrlMainThread(String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wvLoadUrl(url);
            }
        });
    }

    public void wvLoadUrl(String url) {
        String log = (url.length()>256)?url.substring(0, 255):url;
        Log.d("url", "url :: "+url);

        sendEventFC("WebView", "load url=[" + log + "]");
        sendEventGA("WebView", "load url=[" + log + "]");
        if(WebView01!=null) {
            WebView01.loadUrl(url);
        }
    }

    public void testLoadParam() {
        String initparam = "initparam("
                + "'" + EtransDrivingApp.getInstance().getMobileNo() + "'"
                + ", ''" //authkey
                + ", 'N'" //AutoLogin
                + ", '" + EtransDrivingApp.getInstance().getSdkName() + "'"
                + ", '" + EtransDrivingApp.getInstance().getPhoneModel() + "'"
                + ", '" + EtransDrivingApp.getInstance().getPhoneAppVer() + "'"
                + ", '" + EtransDrivingApp.getInstance().getMacAddress() + "'"
                + ", '" + EtransDrivingApp.getInstance().getPhoneMsp() + "'"
                + ", '" + EtransDrivingApp.getInstance().getOsVer() + "'"
                + ", '" + EtransDrivingApp.getInstance().getDownGB() + "'"
                + ", '" + EtransDrivingApp.getInstance().getIsAgreeType() + "'"
                + ", '" + EtransDrivingApp.getInstance().getGpsAgreementYn() + "'"
                + ", '" + EtransDrivingApp.getInstance().getSystemMobileNo() + "'"
                + ", '" + EtransDrivingApp.getInstance().getFstLogin() + "'"
                + ", '" + EtransDrivingApp.getInstance().getEmail() + "'"
                + ")";

        sendEventGA(TAG, "test initparam=" + initparam);
        sendEventFC(TAG, "test initparam=" + initparam);
    }

    public void doLogout() {
        ReportInterface.inst().doPeriodReportService(this, false);

        EtransDrivingApp.getInstance().setLoggedIn("N");
        //자동로그인 해제할 것
        EtransDrivingApp.getInstance().setAuthKey("");
        EtransDrivingApp.getInstance().setAutoLogin("N");

        sendEventFC("doLogout", "doLogout done");
        sendEventGA("doLogout", "doLogout done");

    }

    //google analytics
    public void sendEventGA(String title, String message) {
        Log.d("Google Analytics", title + ": " + message);
        EtransDrivingApp.getInstance().debugMessage("Google Analytics:" + title + ": " + message);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle b = new Bundle();
        b.putString(FirebaseAnalytics.Param.ITEM_ID, "GA:" + message);
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, b);
    }

    //Firebase Crashytics
    public void initializeFirebase() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }

    public void sendEventFC(String tag, String message) {
        Log.d("FB Crashlytics", "[" + EtransDrivingApp.getInstance().getFcUserId() + "] " + tag + ": " + message);
        EtransDrivingApp.getInstance().debugMessage("FB Crashlytics:[" + EtransDrivingApp.getInstance().getFcUserId() + "] " + tag + ": " + message);

        if(isFcLogging) {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.setUserId(EtransDrivingApp.getInstance().getFcUserId());
            crashlytics.log(tag + ": " + "FC:" + message);
        }
    }

    public boolean checkGps() {
        int status = EtransDrivingApp.getInstance().getGpsStatus();
        if (status==1) {
            return true;
        } else if (status==0) {
            showPopupGps();
        }
        return false ;
    }

    public void startLocationService()  {
        sendEventGA(TAG, "startLocationService");
        sendEventFC(TAG, "startLocationService");

        if(locationMgr_==null) {
            locationMgr_ = new JLocationManager(this, "[Manual]");
            locationMgr_.setName("DIRECT");
            locationMgr_.execute();
            collectNetworkGpsData();
            getLatestLocation();
        }
    }

    public void stopLocationService() {
        sendEventGA(TAG, "stopLocationService");
        sendEventFC(TAG, "stopLocationService");

        if (locationMgr_ != null) {
            locationMgr_.removeUpdates();
            locationMgr_ = null;
        }
    }

    public void collectNetworkGpsData() {
        if (locationMgr_ != null)
            locationMgr_.startNetworkProvider();
    }

    public Location getLatestLocation(String provider) {
        Location loc = null;
        if(locationMgr_!=null)
            loc = locationMgr_.getLatestLocation(provider);
        return loc;
    }

    public Location getLatestLocation() {
        collectNetworkGpsData();

        Location fusedLoc = getLatestLocation(JLocationManager.FUSED_PROVIDER);

        if(fusedLoc!=null) {

            Log.d(TAG, "    FUSED location lat=" + fusedLoc.getLatitude() + ", lon=" + fusedLoc.getLongitude() + ", speed=" + fusedLoc.getSpeed() + ", dir=" + fusedLoc.getBearing());
            return fusedLoc;
        }

        Location gpsLoc = getLatestLocation(LocationManager.GPS_PROVIDER);

        if(gpsLoc!=null) {
            Log.d(TAG, "    GPS location lat=" + gpsLoc.getLatitude() + ", lon=" + gpsLoc.getLongitude() + ", speed=" + gpsLoc.getSpeed() + ", dir=" + gpsLoc.getBearing());
            return gpsLoc;
        }

        Location netLoc = getLatestLocation(LocationManager.NETWORK_PROVIDER);
        if(netLoc!=null) {
            Log.d(TAG, "Network location lat=" + netLoc.getLatitude() + ", lon=" + netLoc.getLongitude() + ", speed=" + netLoc.getSpeed() + ", dir=" + netLoc.getBearing());
            return netLoc;
        }
        Location loc = new Location(LocationManager.GPS_PROVIDER);
        loc.setLatitude(0.0d);
        loc.setLongitude(0.0d);
        loc.setSpeed(0.0f);
        loc.setBearing(0.0f);
        return loc;
    }

    public void showAlertDialogYes(String title, String msg, AlertDialog.OnClickListener clickListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("확인", clickListener)
                .create()
                .show();
    }

    public void showAlertDialogYesNo(String title, String msg, String yes, AlertDialog.OnClickListener yesClickListener
            , String no, AlertDialog.OnClickListener noClickListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(yes, yesClickListener)
                .setNegativeButton(no, noClickListener)
                .setCancelable(false)
                .create()
                .show();
    }

    private void showPopupGps()
    {
        showAlertDialogYesNo(
                getString(R.string.gps_error),
                getString(R.string.gps_error_popup),
                getString(R.string.gps_activate),
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_PERMISSION_REQUEST);
                    }
                },
                getString(R.string.gps_deactivate),
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    private void showBLEDialog() {
        showAlertDialogYesNo(
                getString(R.string.strBluetoothError),
                getString(R.string.strBluetoothErrorPopUp),
                getString(R.string.strBluetoothActivate),
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        mBluetoothAdapter.enable();
                    }
                },
                getString(R.string.strBluetootCancel),
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

    }

    //GPS, ,블루투스 2018.11.27
    private boolean chkGpsOnOff(String startYN, String tmrCd) {

        String strBluetoothStatePowerOff = "";

        //요청이면...2018.11.09
        if(startYN.equals("R") || startYN.equals("N"))  {
            //출발취소 시 GPS 체크 안하기
            return true;
        }

        //한진경우
        if(tmrCd.equals("HJNPC")) {
            try {
                MinewBeaconManager mMinewBeaconManager = MinewBeaconManager.getInstance(this);

                BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();

                switch (bluetoothState) {
                    case BluetoothStateNotSupported:
                        break;
                    case BluetoothStatePowerOff:
                        strBluetoothStatePowerOff = "Y";
                        break;
                    case BluetoothStatePowerOn:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LocationManager lmMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lmMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) == false && !EtransDrivingApp.getInstance().getCarGb().equals("1")
                && EtransDrivingApp.getInstance().getIsLbsStartYn().equals("Y"))
        {
            // createGpsDisabledAlert(strBluetoothStatePowerOff);
            return false;

        } else {
            if(strBluetoothStatePowerOff.equals("Y")) {
                showBLEDialog();
                return false;
            }
        }
        return true;
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
//        if (checkPlayServices()) {
        // Start IntentService to register this application with FCM.
 /*       Intent intent = new Intent(this, FcmRegistrationIntentService.class);
        startService(intent);
        EtransDrivingApp.getInstance().requestFcmToken(MainActivity.this, new EtransDrivingApp.FcmRequestCallback() {
                    public void onSuccess(String token) {
                        Log.i(TAG, "::::: Success, FCM instance token=" + token );
                        EtransDrivingApp.getInstance().savePushToken(token);

                        sendEventGA(TAG, "Success, FCM instance token" + token);
                        sendEventFC(TAG, "Success, FCM instance token" + token);
                    }
                    public void onFail(String msg) {

                        EtransDrivingApp.getInstance().removePushToken();
                        sendEventGA(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");
                        sendEventFC(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");

                        Log.d(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");
                        Log.d(TAG, msg);

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        EtransDrivingApp.getInstance().showToast(msg);

                    }
                }
        );
*/
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            String msg = getString(R.string.alert_msg_request_token) + ":" + task.getException();
                            EtransDrivingApp.getInstance().removePushToken();
                            sendEventGA(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");
                            sendEventFC(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");

                            Log.d(TAG, "[오류발생] FCM 토큰을 얻지 못했습니다");
                            Log.d(TAG, msg);

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            EtransDrivingApp.getInstance().showToast(msg);
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        sendEventGA(TAG, "Success, FCM instance token" + token);
                        sendEventFC(TAG, "Success, FCM instance token" + token);

                        EtransDrivingApp.getInstance().savePushToken(token);

                    }
                });
    }

    /**
     * Google Play Service를 사용할 수 있는 환경인지 체크한다.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = new GoogleApiAvailability();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported google-play-service.");
            }
            return false;
        }

        //TODO - return true; https://github.com/firebase/quickstart-android/tree/0f17b14ce785cf2b77e358154d987ecc30269616/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/java
        return true;
    }

    //카카오톡실행
    private void kakaoRecommend()
    {
        sendEventGA(TAG, "kakaoRecommend~");
        sendEventFC(TAG, "kakaoRecommend~");

        String crypto = "";

        KakaoLink kakaoLink = KakaoLink.getLink(this);

        if (!kakaoLink.isAvailableIntent()) {
            EtransDrivingApp.getInstance().showToast("카카오톡이 설치되어 있지 않습니다");
        }


        //암호화
        try
        {
            crypto = SecurityUtil.getEncode(EtransDrivingApp.getInstance().getMobileNo());
        }
        catch(Exception exception)
        {
            crypto = "";
        }

        //모바일번호
        if (crypto.length() != 0 || !crypto.equals("")){

            crypto = "?app="+ crypto;

        } else {

            crypto = "";
        }

        String link = getString(R.string.URL_DONE_PAGE_DOWNLOAD)+"acon.jsp"+ crypto;

        kakaoLink.openKakaoLink(this, link, "스마트폰 어플에서 제공하는 코피노결과 자동 조회를 추천합니다. 아래의 주소를 클릭하시고, 설치 후 사용하세요.",
                getPackageName(),EtransDrivingApp.getInstance().getPhoneAppVer(), "이트랜스드라이빙", "UTF-8");
    }

    public void copyToClipboard() {
        sendEventGA(TAG, "copyToClipboard~");
        sendEventFC(TAG, "copyToClipboard~");

        String link = getString(R.string.URL_MARKET_WEB_PAGE);

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("eTrandDriving", link);
        clipboard.setPrimaryClip(clip);

        EtransDrivingApp.getInstance().showToast("URL 복사가 완료되었습니다");

    }

    public void launchNavigation(float x, float y, String goalNm) {
        if("01".equalsIgnoreCase(EtransDrivingApp.getInstance().getNavigationType())) {
            CommonUtil.moveTMap(MainActivity.this, goalNm, x, y, TMAP_API_KEY);
        } else {
            CommonUtil.moveKaKaoNavi(MainActivity.this, goalNm, x, y);
        }
    }

    private void finshNeedToPermission() {
        showAlertDialogYes("알림", "권한을 모두 허용해주셔야 앱을 이용할 수 있습니다."
                , new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    private void startReportService() {
        EtransDrivingApp.getInstance().setIsLbsStartYn("Y");

        registerBroadcastReceiver();

        sendBroadcast(new Intent(UIBroadCastReceiver.BROADCAST_ACTION_START_REPORT_SERVICE));

        sendEventGA(TAG, "startReportService");
        sendEventFC(TAG, "startReportService");
    }

    private void stopReportService() {
        EtransDrivingApp.getInstance().setIsLbsStartYn("N");

        unRegisterBroadcastReceiver();
        ReportInterface.inst().doPeriodReportService(this, false);

        sendEventGA(TAG, "stopReportService");
        sendEventFC(TAG, "stopReportService");

    }


    private void registerBroadcastReceiver() {
        //for later oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(reportServiceReceiver_==null) {
                reportServiceReceiver_ = new UIBroadCastReceiver();
                IntentFilter intentFilter = new IntentFilter(UIBroadCastReceiver.BROADCAST_ACTION_START_REPORT_SERVICE);
                registerReceiver(reportServiceReceiver_, intentFilter);
            }
        }
    }

    private void unRegisterBroadcastReceiver() {
        //for later oreo
        if(reportServiceReceiver_!=null) {
            try {
                unregisterReceiver(reportServiceReceiver_);
            }catch(IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }catch (Exception e) {}
        }
    }

    private void popSignView() {
        showDialog(DLG_SHOW_SIGN);
    }

    /**
     * 다이얼로그 팝업 생성
     */
    @Override
    protected Dialog onCreateDialog(int id)
    {
        final Dialog dialog = new Dialog(this, R.style.AlertDialog);
        View view;

        switch (id)
        {
            case DLG_SHOW_SIGN:
                mob_07_View02 = new MOB_07_View02(this);
                dialog.setContentView(mob_07_View02);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);

                window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                });
                return dialog;

            default:
                throw new IllegalArgumentException();
        }
    }


    /**
     * 서명이미지 등록:화면의 서명 지우기
     */
    public void onClickButton_03(View target)
    {
        Log.d(TAG, "clear sign image");
        mob_07_View02.clear();
    }

    /**
     * 서명이미지 등록:서명완료
     * @param target
     */
    public void onClickButton_04(View target) {
        Log.d(TAG, "send sign image");

        removeDialog(DLG_SHOW_SIGN);

        final String fileName = "klnet_sign_image" + ".jpg";
        Uri uri = mob_07_View02.CaptureView(fileName);
        imageDataToServer("sign", uri);

    }

    private void imageDataToServer(String dataType, Uri uri) {
        //차량등록증/사업자등록증/서명이미지
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap img = BitmapFactory.decodeStream(in);

            img = PhotoFragment.resizeBitmap(img, 1080, 1920);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            byte[] bytes = baos.toByteArray();

            String base64String = Base64.encodeToString(bytes, Base64.DEFAULT);

            long fileSize =  bytes.length;

            Log.d("CHECK", "base64String :" + base64String);
            Log.d("CHECK", "fileSize :" + fileSize);

            String format = "javascript:dataUpdate('%s', '%s');";
            String jsString = String.format(format, dataType, "data:image/jpg;base64,"+ base64String);
            wvLoadUrl(jsString);
        } catch(Exception e) {
            e.printStackTrace();
            wvLoadUrl("javascript:dataUpdate('" + dataType + "', '{}'");
        }
    }

    public void restoreSession() {
        //String url = "https://" + getString(R.string.URL_CONN_SVR) + ":8443";
        String url = getString(R.string.URL_CONN_SVR8); //(dev)etdriving

        String newCookie = CookieManager.getInstance().getCookie(url);
        String userCookie = EtransDrivingApp.getInstance().getUserCookie();



        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieSyncManager.getInstance().sync();

            CookieManager.getInstance().removeAllCookie();
            CookieManager.getInstance().setCookie(url, userCookie);
            CookieManager.getInstance().flush();
        }

        String chkCookie = CookieManager.getInstance().getCookie(url);

        Log.d(TAG, "user cookie=" + userCookie + "\n"
                + "new cookie=" + newCookie + "\n"
                + "check cookie=" + chkCookie + "\n"
        );
    }

    public void onClickButtonClearDebugText(View target)
    {
        tv_debug.setText("");
    }

    public void processPushNoti() {
        String loggedIn = EtransDrivingApp.getInstance().getLoggedIn();

        if ("Y".equalsIgnoreCase(loggedIn)) {
            String push_seq         = DataSet.getInstance().push_seq;
            String push_type        = DataSet.getInstance().push_type;
            String push_title       = DataSet.getInstance().push_title;
            String push_body        = DataSet.getInstance().push_body;
            String push_doc_gubun   = DataSet.getInstance().push_doc_gubun;
            String push_param       = DataSet.getInstance().push_param;

            if ("99".equalsIgnoreCase(push_doc_gubun)) {
                EtransDrivingApp.getInstance().procChangeCollectTerm();
            } else if ("88".equalsIgnoreCase(push_doc_gubun)) {
                //즉시 위치정보 보고(88)
                //wvLoadUrl("javascript:pushLink_common('"+push_seq+"', '"+push_type+"', '"+push_title+"', '"+push_body+"', '"+push_doc_gubun+"', '"+push_param+"','"+EtransDrivingApp.getInstance().getCreationPeroid()+"','"+EtransDrivingApp.getInstance().getReportPeroid()+"');");
                wvLoadUrl("javascript:pushLink_common('"+push_seq+"', '"+push_doc_gubun+"', '"+push_type+"', '"+push_title+"', '"+push_body+"', '"+push_param+"','"+EtransDrivingApp.getInstance().getCreationPeroid()+"','"+EtransDrivingApp.getInstance().getReportPeroid()+"');");
            } else {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int sb2value =am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, sb2value, 0);
                processPushTts(push_title, push_body, push_param);

                //앱이 켜져 있는 상태에선 push_body가 null 로 넘어온다.
                //push_body : call_text_sub
                //push_title : call_text
                if(push_title!=null && !push_title.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    String popTitle = push_title.replaceAll("#TTS#","");
                    builder.setTitle(popTitle);

                    String popMsg = push_body.replaceAll("#TTS#","");
                    builder.setMessage(popMsg);
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    procPushMessage(push_seq, push_type, push_title, push_body, push_doc_gubun, push_param);
                                }
                            });
                    builder.setNegativeButton("닫기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }else {
                    procPushMessage(push_seq, push_type, push_title, push_body, push_doc_gubun, push_param);
                }
            }
        }
    }

    public void procPushMessage(String push_seq, String push_type
            , String push_title, String push_body
            , String push_doc_gubun, String push_param) {
        String call_param = "";
        try {
            call_param = URLEncoder.encode(push_param, "euc-kr");
        }catch(Exception e) {
            e.printStackTrace();
        }
        String params = "?seq=" + push_seq
                + "&doc_gubun=" + push_doc_gubun
                + "&type=" + push_type
                + "&call_text=" + push_title
                + "&call_text_sub=" + push_body
                + "&call_param=" + call_param
                + "&EMU_TOKEN=" + EtransDrivingApp.getInstance().getDeviceID(this);

        DataSet.getInstance().clearPushInfo();
        DataSet.getInstance().isrunapppush = false;

        gotoPushRedirectPage(params, push_seq, push_type
                , push_title, push_body
                , push_doc_gubun, push_param);



    }

    private void processPushTts(String push_title, String push_body, String push_param) {

        String notiMsg = push_body;

        if (notiMsg == null || notiMsg.equals("")) {
            notiMsg = push_title;
        }

        //이트럭뱅크웹에서 푸시 메시지중 TTS 대상이되는
        //푸시 서버로부터 notiMsg  뒷 부분에 #TTS# 구분자가 있는지 확인한다.
        //if(notiMsg != null) {//기존 코딩들도 notiMsg가 null이지 않은 전제라 나도 체크 안함

        boolean enabledTTS = true; //Util.getSharedData(mContext, "Tts_Read", "").equals("Y");
        int soundMode = 0;//0 : 무음, 외외 : R.raw.meassage_arrive 등등등 : 음성 성우  or 또는 TTS 읽기
        String forcedTTSMsg = null;

        if (notiMsg.indexOf("#TTS#") > -1) { //TTS 읽으라고 설정 되어 있는데 마침 메세지에 ttsCommand(#TTS#)가 있다면
            tts = null;

            if (enabledTTS) {
                soundMode = 1;
                String[] toCheckNotiMsg = notiMsg.split("#TTS#");//"#TTS#"는 무조건 한번 체크해야 함
                notiMsg = toCheckNotiMsg[0];//#TTS# 좌측 데이터만이 이후단 처리될 메시지임
                if(!StringUtil.isEmpty(notiMsg)) {
                    if (toCheckNotiMsg.length > 1 && !StringUtil.isEmpty(toCheckNotiMsg[1])) {//따로 읽어줄 TTS 읽을 내용이 있다면
                        forcedTTSMsg = toCheckNotiMsg[1];//우측 별도의 TTS 텍스트로
                    }
                    forcedTTSMsg = notiMsg;//onSetPopUpData 에서 처리된 순수TTS용메시지만 담겨있다

                    ttsPlaySound(soundMode, forcedTTSMsg);
                }
            }
        }
    }

    private void ttsPlaySound(int sound, String ttsMsg) {
        if(sound == 0) {
            //묵음 등
        } else {

            if(sound == 1 || sound == 2) {
                if(BuildConfig.DEBUG) {
                    EtransDrivingApp.getInstance().showToast("TTS:" + ttsMsg);
                }

                tts = new TTS(mContext, Locale.KOREAN, ttsMsg);//TTS실행;
            } else {
                mSoundManager = new SoundManager();
                mSoundManager.initSounds(mContext);
                mSoundManager.addSound(1, sound);
                mSoundManager.playSound(1);
                mSoundManager.setOnSoundMangeListener(new SoundManager.onSoundMangerListener() {

                    @Override
                    public void onCompleted(SoundPool soundPool, AudioManager audioManager) {
                        soundPool = null;
                        audioManager = null;
                        mSoundManager = null;
                    }
                });
            }
        }

        // 통화중 상태 캐치
        mTelMan = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener mListener = new PhoneStateListener() {

            //이 리스너는 playSound와 연관되어 설명하면 playSound가 먼저 호출될지 여기가 먼저 호출될지는 케이스마다 다름.
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (mTelMan.getCallState()) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        //callState = 0;

                        //체크를 한번이라도 줄기기 위해 주석처리
                        if(tts != null /*&& tts.getBeforeCalling()*/) {//전화관련 상태였고 그때 완료된게 아니라면
                            tts.setOnCalling(false);
                            if(!tts.getDoneYN())
                                tts.speak(null);
                        }
                        break;

                    case TelephonyManager.CALL_STATE_RINGING:
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //callState = 1;

                        //체크를 한번이라도 줄기기 위해 주석처리
                        //if(Util.getSharedData(mContext, "Tts_Read", "").equals("Y")) {
                        if(tts != null) {
                            tts.setOnCalling(true);
                            if (tts.isSpeaking()) {
                                tts.stop();
                            }
                        }
                        //}
                        break;
                }


                Log.d("TTS", "CALL_STATE:" + mTelMan.getCallState());
            }
        };
        mTelMan.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private String getLauncherClassName() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }
    private void removeBadge() {
        String channelId = getString(R.string.default_notification_channel_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                // Create channel to show notifications.
//                String channelName = getString(R.string.default_notification_channel_name);
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//                channel.setShowBadge(false);

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(JuisFirebaseMessagingService.REQUEST_PUSH_ARRIVED);
            notificationManager.cancelAll();

        } else {
            //앱 실행 아이콘 개수 조절
            Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            badgeIntent.putExtra("badge_count", 0);
            badgeIntent.putExtra("badge_count_package_name", getPackageName());
//                badgeIntent.putExtra("badge_count_class_name", "kr.co.klnet.aos.etransdriving.MainActivity");
            badgeIntent.putExtra("badge_count_class_name", getLauncherClassName());
            sendBroadcast(badgeIntent);
        }
    }

    private void startGpsService() { // asmyoung
        try {
            LocationManager lm =
                    (LocationManager)getSystemService(
                            Context.LOCATION_SERVICE);
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                reportServiceReceiver_ = null;
                initBroadcastReceiver();
                startLocationService();
                startReportService();
                wvLoadUrl("javascript:startGpsResult('Y');");
            } else {
                wvLoadUrl("javascript:startGpsResult('N');");
            }
        }
        catch(Exception e) {
            wvLoadUrl("javascript:startGpsResult('N');");
        }
    }

    private void stopGpsService() { // asmyoung
        try {
            LocationManager lm =
                    (LocationManager)getSystemService(
                            Context.LOCATION_SERVICE);
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                stopReportService();
                stopLocationService();
                Intent i = new Intent(this, PeriodReportService.class);
                stopService(i);
                wvLoadUrl("javascript:stopGpsResult('Y');");
            } else {
                stopReportService();
                stopLocationService();
                Intent i = new Intent(this, PeriodReportService.class);
                stopService(i);
                wvLoadUrl("javascript:stopGpsResult('Y');");
            }
        } catch(Exception e) {
            wvLoadUrl("javascript:stopGpsResult('N');");
        }
    }
    private void getDeviceInfo() { // asmyoung
        try {

            String deviceId = EtransDrivingApp.getInstance().getDeviceID(this);
            String version = EtransDrivingApp.getInstance().getOsVer();
            String autoLoginYn = EtransDrivingApp.getInstance().getAutoLogin();

            JSONObject ret = new JSONObject();
            ret.put("deviceId", deviceId);
            ret.put("version", version);
            ret.put("autoLoginYn", autoLoginYn);

            wvLoadUrlMainThread("javascript:fn_getDeviceInfoCallback('"+ret.toString()+"');");

        } catch(Exception e) {
            wvLoadUrl("javascript:fn_getDeviceInfoCallbackFail('오류가 발생하였습니다.');");
        }
    }

    private static final Pattern pattern = Pattern.compile("\\{(\\w+)=([^\\}]*)\\}");

    private String authCode_result = "";
    private String authcode = "";
    private int counttime = 0;

    private void getCertAuthCode() {
        TimerTask myTask = new TimerTask() {
            public void run() {
                String resultCode = "";
                String errorMsg = "";

                String url = DataSet.relay_server_url_sid;
                String response = null;
                try {
                    Log.d("CHECK", url);
                    response = HTTPSRequest.getInstance().reqRelayAuthCode(url);
                } catch (ResponseException e) {
                    Log.e("CHECK", "", e);
                    authCode_result = "인증번호 수신에 실패하였습니다.\n네크워크를 확인해 주세요." + e.getMessage();
                } catch (Exception e) {
                    Log.e("CHECK", "", e);
                    authCode_result = "인증번호 수신에 실패하였습니다.\n네크워크를 확인해 주세요." + e.getMessage();
                }

                // response : resultCode(4) | SID(12) | validTime(6)
                if(response == null) {
                    // error
                    wvLoadUrlMainThread("javascript:fn_getCertAuthCodeCallbackFail('오류가 발생하였습니다.[중계서버 접속 오류]');");
                } else {
                    //
                    Matcher matcher = pattern.matcher(response);
                    while(matcher.find()) {

                        String key = matcher.group(1);
                        String value = matcher.group(2);

                        if(key.equals("resultCode")) {
                            // 결과코드 세팅
                            resultCode = value;

                        } else if(key.equals("message")) {
                            errorMsg = value;

                        } else if(key.equals("authcode")) {
                            // 인증코드 세팅
                            authcode = value;

                        } else if(key.equals("validTime")) {
                            // 유효시간 세팅
                            counttime = Integer.parseInt(value);
                        }
                    }

                    if (resultCode != null && resultCode.equals("2000")) {
                        authCode_result = "";
                    } else if (resultCode != null && !resultCode.equals("2000")) {
                        authCode_result = errorMsg;
                    }

                    // 에러 처리.
                    if(authCode_result.equals("")){
                        final String num1 = authcode.substring(0, 4);
                        final String num2 = authcode.substring(4, 8);
                        final String num3 = authcode.substring(8, 12);
                        wvLoadUrlMainThread("javascript:fn_getCertAuthCodeCallback('"+num1+"', '"+num2+"', '"+num3+"');");

                    } else{
                        wvLoadUrlMainThread("javascript:fn_getCertAuthCodeCallbackFail('오류가 발생하였습니다.["+authCode_result+"]');");

                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    byte[] pkcs12byte = null;
    private String import_result = "";
    private String subjectDN = "";

    private void certImport(String authCodeStr, String passwdStr) {
        final String fauthcode = authCodeStr;
        final String fpasswd = passwdStr;

        TimerTask myTask = new TimerTask() {
            public void run() {
                String resultCode = "";
                String errorMsg = "";

                try {
                    String url = DataSet.relay_server_url_import;
                    Log.d("CHECK", url);
                    String response = HTTPSRequest.getInstance().reqRelayCert(url, authcode);

                    if(response == null) {
                        // error
                        wvLoadUrlMainThread("javascript:fn_certImportCallbackFail('오류가 발생하였습니다.[중계서버 접속 오류]');");

                    } else {
                        Matcher matcher = pattern.matcher(response);
                        while(matcher.find()) {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            if(key.equals("resultCode")) {
                                resultCode = value;

                            }else if(key.equals("message")) {
                                errorMsg = value;

                            }else if(key.equals("pkcs12")) {

                                //Logger.print("pkcs12", value);
                                pkcs12byte = SGBase64.decode(value);

                            }
                        }

                        if(resultCode != null && resultCode.equals("2000")) {
                            import_result = "";
                        }else if(resultCode != null && !resultCode.equals("2000")) {
                            import_result = "PC에서 인증서 내보내기가 완료되지 않았습니다. STEP1~4 진행 후 비밀번호를 입력해주세요.";
                        }else {
                            import_result = "PC에서 인증서 내보내기가 완료되지 않았습니다. STEP1~4 진행 후 비밀번호를 입력해주세요.";
                        }

                    }

                } catch (ResponseException e) {
                    import_result = "인증서 가져오기에 실패하였습니다.";
                }

                SGPKCS12 pkcs12 = null;
                if(import_result.equals("")) {
                    try {
                        pkcs12 = new SGPKCS12();
                    } catch (SGCryptoException e) {
                        Log.e("SGPKCS12", "오류발생", e);
                        import_result = "인증서를 전송하는데 필요한 암호 알고리즘을 찾을 수 없습니다."+"["+e.getMessage()+"]";
                    }
                }

                if(import_result.equals("")){
                    try {
                        pkcs12.load(pkcs12byte, fpasswd);
                    } catch (IOException e) {
                        import_result = "인증서 비밀번호가 올바르지 않습니다. 확인하고 다시 입력해주세요.";
                    }
                }

                byte[] signKey = null;
                if(import_result.equals("")){
                    try {
                        signKey = pkcs12.getPrivateKeyBytes(SGKeyCode.SIGN, fpasswd);
                    } catch (SGCryptoException e) {
                        import_result = "인증서 비밀번호가 올바르지 않습니다. 확인하고 다시 입력해주세요.";
                    }
                }

                SGCertificate signCert = null;
                if(import_result.equals("")){
                    try {
                        signCert = pkcs12.getCertificate(SGKeyCode.SIGN);
                    } catch (SGCertificateException e) {
                        import_result = "인증서를 얻는데 실패하였습니다.";
                    } catch (SGPKCS12Exception e) {
                        import_result = "인증서를 얻는데 실패하였습니다.";
                    }
                }

                byte[] kmKey = null;
                if(import_result.equals("")){
                    try {
                        kmKey = pkcs12.getPrivateKeyBytes(SGKeyCode.KM, fpasswd);
                    } catch (SGCryptoException e) {
                        import_result = "인증서 비밀번호가 올바르지 않습니다. 확인하고 다시 입력해주세요.";
                    }
                }

                SGCertificate kmCert = null;
                byte[] capubs = null;
                if(import_result.equals("")){
                    try {
                        kmCert = pkcs12.getCertificate(SGKeyCode.KM);
                    } catch (SGCertificateException e) {
                        import_result = "인증서를 얻는데 실패하였습니다.";
                    } catch (SGPKCS12Exception e) {
                        import_result = "인증서를 얻는데 실패하였습니다.";
                    }

                    try {
                        capubs = pkcs12.getCaPubs();
                    } catch (SGCertificateException e1) {
                    }
                }

                if(import_result.equals("")){
                    // KICA, CrossCert, NCASign, SignKorea, TradeSign, yessign
                    String dn_name = signCert.getSubjectDN();
                    subjectDN = dn_name;
                    String cert_org = com.kica.android.util.CommonUtil.getOrgName(dn_name);

                    String base_homedir = CertPath.getCertpathInternal(MainActivity.this) + cert_org + "/USER/" + dn_name + "/";

                    File certdir = new File(base_homedir);
                    if(!certdir.exists())
                        certdir.mkdirs();

                    try {
                        if(signKey != null)
                            SGFile.writeBytes(signKey, base_homedir + "signPri.key");
                        if(signCert != null)
                            SGFile.writeBytes(signCert.getEncoded(), base_homedir + "signCert.der");
                        if(kmKey != null)
                            SGFile.writeBytes(kmKey, base_homedir + "kmPri.key");
                        if(kmCert != null)
                            SGFile.writeBytes(kmCert.getEncoded(), base_homedir + "kmCert.der");
                        if(capubs != null)
                            SGFile.writeBytes(capubs, base_homedir + "CaPubs");
                    } catch (IOException e) {
                        import_result = "전송요청이 실패하였습니다.";
                    }
                }

                if(import_result.equals("")){
                    wvLoadUrlMainThread("javascript:fn_certImportCallback('인증서 복사에 성공하였습니다."+"');");

                    // 인증서 해쉬값의 hex 인코딩한 값을 중계서버로 전송 (실패해도 성공 처리)
                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                        byte[] hash = messageDigest.digest(pkcs12byte);

                        if(hash != null) {
                            String url = DataSet.relay_server_url_import_ok;
                            String hex = Hex.encode(hash);
                            HTTPSRequest.getInstance().sendRelayCertOk(url, hex);
                        }

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    wvLoadUrlMainThread("javascript:fn_certImportCallbackFail('"+import_result+"');");

                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    ArrayList<CertSet> certlist = null;
    private void getCertList() {
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();

                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_getCertListCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용하세요.');");

                }else{
                    JSONObject ret = new JSONObject();
                    JSONArray jlist = new JSONArray();
                    try {
                        ret.put("certList", jlist);
                    } catch (JSONException e) {
                    }
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        String lastdate = certset.getNotAfter();
                        String certOID = certset.getPolicyOid();
                        Log.w("TEST", "## dn_name: " + dn_name);
                        Log.w("TEST", "## lastdate: " + lastdate);
                        Log.w("TEST", "## certOID: " + certOID);
                        String name = com.kica.android.util.CommonUtil.getName(dn_name);
                        String cert_org = com.kica.android.util.CommonUtil.getCertOrg(dn_name);
                        String cert_lastdate = getLastDate(lastdate);
                        String cert_usage = getCertUsage(certOID);
                        JSONObject item = new JSONObject();
                        try {
                            item.put("name", name);
                            item.put("dnName", dn_name);
                            item.put("certOrg", cert_org);
                            item.put("lastDate", cert_lastdate);
                            item.put("usageToName", cert_usage);
                            item.put("oid", certOID);
                        } catch (JSONException e) {
                            continue;
                        }


                        jlist.put(item);

                    }

                    wvLoadUrlMainThread("javascript:fn_getCertListCallback('"+ret.toString()+"');");

                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }


    public String getLastDate(String strlastdate) {
        String lastdate = "";

        try {
            lastdate = strlastdate.substring(0, 4) + "-" + strlastdate.substring(4, 6) + "-" + strlastdate.substring(6, 8) + "";
            return lastdate;
        } catch (Exception var3) {
            return lastdate;
        }
    }

    private String getCertUsage(String policy) {
        if (policy.equals("1.2.410.200004.5.2.1.1")
                || policy.equals("1.2.410.200004.5.1.1.7")
                || policy.equals("1.2.410.200005.1.1.5")
                || policy.equals("1.2.410.200004.5.4.1.2")
                || policy.equals("1.2.410.200012.1.1.3")
        ) {
            return "사업자(범용)";
        } else if (policy.equals("1.2.410.200004.5.2.1.2")
                || policy.equals("1.2.410.200004.5.1.1.5")
                || policy.equals("1.2.410.200005.1.1.1")
                || policy.equals("1.2.410.200004.5.4.1.1")
                || policy.equals("1.2.410.200012.1.1.1")
        ) {
            return "개인(범용)";
        } else {
            return "용도제한";
        }
    }

    private void getCertInfo(final String dnName) {
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();

                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_getCertInfoCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    JSONObject item = new JSONObject();
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            String lastdate = certset.getNotAfter();
                            String certOID = certset.getPolicyOid();
                            Log.w("TEST", "## dn_name: " + dn_name);
                            Log.w("TEST", "## lastdate: " + lastdate);
                            Log.w("TEST", "## certOID: " + certOID);
                            String name = com.kica.android.util.CommonUtil.getName(dn_name);
                            String cert_org = com.kica.android.util.CommonUtil.getCertOrg(dn_name);
                            String cert_lastdate = getLastDate(lastdate);
                            String cert_usage = getCertUsage(certOID);
                            try {
                                item.put("name", name);
                                item.put("dnName", dn_name);
                                item.put("certOrg", cert_org);
                                item.put("lastDate", cert_lastdate);
                                item.put("usageToName", cert_usage);
                                item.put("oid", certOID);
                            } catch (JSONException e) {
                                break;
                            }
                            break;
                        }
                    }

                    if (item.has("lastDate")) {
                        wvLoadUrlMainThread("javascript:fn_getCertInfoCallback('"+item.toString()+"');");
                    } else {
                        wvLoadUrlMainThread("javascript:fn_getCertInfoCallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private void makeSignDataLogin(final String dnName, final String passwd){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {

                        String result = null;

                        // 난수 생성 시작
                        SGRandom sgrandom = new SGRandom();
                        byte[] challengebyte = null;
                        challengebyte = sgrandom.generate(16);
                        if (challengebyte == null) {
                            result = "난수 생성에 실패하였습니다.";
                            wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallbackFail('"+result+"');");
                            return;
                        }
                        // 난수 생성 끝
                        else {
                            byte[] sign = null;
                            String certpath = selectedcertset.getCertPath();
                            SGPrivateKey priKey = null;
                            SGCertificate cert = null;

                            try {
                                priKey = SGKeyFactory.getInstance().generatePrivate(SGKeyCode.SIGN, SGFile.readBytes(certpath + "signPri.key"), passwd);
                                cert = SGCertificateFactory.getInstance().generateCertificate(SGFile.readBytes(certpath + "signCert.der"));
                            } catch (Exception e) {
                                result = "인증서 비밀번호가 틀립니다.\n" + e.getMessage();
                                wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallbackFail('"+result+"');");
                                return;
                            }
                            // 전자서명 생성 시작
                            //System.out.println("cert.getSigAlgName(): " + cert.getSigAlgName());
                            try {
                                SGSigner signer = new SGSigner(cert.getSigAlgName());
                                signer.init(priKey, cert);
                                signer.update(challengebyte);
                                sign = signer.sign();
                                signer.doFinal();
                            } catch (Exception e) {
                                result = "전자서명 생성에 실패하였습니다.\n" + e.getMessage();
                                wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallbackFail('"+result+"');");
                                return;
                            }
                            // 전자서명 생성 끝

                            // 3. 전자서명 값
                            String signsrc = SGBase64.encode(sign);
                            JSONObject item = new JSONObject();
                            try {
                                item.put("signResult", signsrc);
                            } catch (JSONException e) {

                            }

                            wvLoadUrlMainThread("javascript:fn_makeSignDataLoginCallback('"+item.toString().replaceAll("\\\\n", "\\\\\\\\n")+"');");
                        }
                    }

                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private void makeSignDataPKCS7(final String dnName, final String data, final String passwd){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_makeSignDataPKCS7CallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_makeSignDataPKCS7CallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {

                        // PKCS7 Sign
                        byte[] sign = null;

                        // 선택된 인증서 얻기
                        String certpath = selectedcertset.getCertPath();
                        SGPrivateKey priKey = null;
                        SGCertificate cert = null;
                        try {
                            priKey = SGKeyFactory.getInstance().generatePrivate(SGKeyCode.SIGN, SGFile.readBytes(certpath + "signPri.key"), passwd);
                            cert = SGCertificateFactory.getInstance().generateCertificate(SGFile.readBytes(certpath + "signCert.der"));
                        } catch (Exception e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataPKCS7CallbackFail('인증서의 키가 없거나 비밀번호가 틀립니다.\n" + e.getMessage()+"');");
                            return;
                        }

                        // 전자서명 생성 시작
                        byte[] orgBytes = data.trim().getBytes();
                        byte[] signedBytes = null;
                        try {
                            SGSignedDataGenerator p7signer = new SGSignedDataGenerator(SGAlgorithmParameter.SHA256);
                            p7signer.init(SGSignedDataGenerator.ATTACHED);
                            p7signer.addSignerCertificate(cert, priKey);
                            p7signer.update(orgBytes);
                            signedBytes = p7signer.getEncoded();
                            if (signedBytes != null) {
                                String base64SignedData = SGBase64.encode(signedBytes);

                                JSONObject item = new JSONObject();
                                try {
                                    item.put("signResult", base64SignedData);
                                } catch (JSONException e) {

                                }

                                wvLoadUrlMainThread("javascript:fn_makeSignDataPKCS7Callback('"+item.toString().replaceAll("\\\\n", "\\\\\\\\n")+"');");

                                return;
                            }
                        } catch (Exception e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataPKCS7CallbackFail('전자서명 생성에 실패하였습니다.\n" + e.getMessage()+"');");
                            return;
                        }
                        // 전자서명 생성 끝

                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private void makeSignDataX509(final String dnName, final String digest, final String passwd, final String pcsSSN){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {
                        JSONObject item = new JSONObject();

                        // 난수 생성 시작
                        SGRandom sgrandom = new SGRandom();
                        byte[] challengebyte = null;
                        challengebyte = sgrandom.generate(16);
                        if (challengebyte == null) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('난수 생성에 실패하였습니다.');");
                            return;
                        }
                        // 난수 생성 끝

                        //인증서 본인확인

                        //Digest 생성
                        MessageDigest messageDigest;
                        try {
                            messageDigest = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('오류가 발생하였습니다.');");
                            return;
                        }
//                        messageDigest.update(digest.getBytes(StandardCharsets.UTF_8));
//                        byte[] hashedData = messageDigest.digest();
//                        String hashData = SGBase64.encode(hashedData);
//                        try {
//                            item.put("digestValue", hashData);
//                        } catch (JSONException e) {
//
//                        }

                        try {
                            item.put("digestValue", digest);
                        } catch (JSONException e) {

                        }

                        //전자서명
                        byte[] sign = null;
                        //서명할 원문
                        String plainText = "<ds:SignedInfo xmlns=\"urn:kr:or:kec:standard:Tax:ReusableAggregateBusinessInformationEntitySchemaModule:1:0\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"></ds:SignatureMethod><ds:Reference URI=\"\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></ds:Transform><ds:Transform Algorithm=\"http://www.w3.org/TR/1999/REC-xpath-19991116\"><ds:XPath>not(self::*[name()='TaxInvoice'] | ancestor-or-self::*[name()='ExchangedDocument'] | ancestor-or-self::ds:Signature)</ds:XPath></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"></ds:DigestMethod><ds:DigestValue>"+digest+"</ds:DigestValue></ds:Reference></ds:SignedInfo>";

                        // 선택된 인증서 얻기
                        String certpath = selectedcertset.getCertPath();
                        SGPrivateKey priKey = null;
                        SGCertificate cert = null;
                        try {
                            priKey = SGKeyFactory.getInstance().generatePrivate(SGKeyCode.SIGN, SGFile.readBytes(certpath + "signPri.key"), passwd);
                            cert = SGCertificateFactory.getInstance().generateCertificate(SGFile.readBytes(certpath + "signCert.der"));
                        } catch (Exception e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('인증서의 키가 없거나 비밀번호가 틀립니다.\n" + e.getMessage()+"');");
                            return;
                        }

                        //인증서 본인확인
                        try {
                            cert.validateUser(pcsSSN, SGBase64.encode(priKey.getRandom()));
                        } catch (SGCertificateException e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('신원검증에 실패하였습니다. 주민등록번호 혹은 사업자번호가 올바르게 입력되었는지 확인하십시오.');");
                            return;
                        }

                        // 전자서명 생성 시작
                        try {
                            SGSigner signer = new SGSigner(cert.getSigAlgName());
                            signer.init(priKey, cert);
                            signer.update(plainText.getBytes(StandardCharsets.UTF_8));
                            sign = signer.sign();
                            signer.doFinal();
                        } catch (Exception e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('전자서명 생성에 실패하였습니다.\n" + e.getMessage()+"');");
                            return;
                        }

                        try {
                            item.put("signatureValue", SGBase64.encode(sign));
                        } catch (JSONException e) {

                        }


                        //인증서정보
                        String userSignCertPem = "";
                        try {
                            userSignCertPem = SGUtil.derToPem(SGFile.readBytes(certpath + "/signCert.der"));
                        } catch (SGException e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('인증서 정보를 찾을 수 없습니다.');");
                            return;
                        } catch (IOException e) {
                            wvLoadUrlMainThread("javascript:fn_makeSignDataX509CallbackFail('인증서 정보를 찾을 수 없습니다.');");
                            return;
                        }

                        Log.e("CHECK", userSignCertPem);

                        cert.getX509Certificate();
                        JSONObject certInfo = new JSONObject();
                        try {
                            certInfo.put("issueDN", cert.getIssuerDN());
                            certInfo.put("serial", cert.getSerialNumber());
                            certInfo.put("userDN", cert.getSubjectDN());
                            PublicKey pk = cert.getX509Certificate().getPublicKey();
                            certInfo.put("publickey", bytesToHexString(pk.getEncoded()));
                            certInfo.put("certificate", userSignCertPem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            item.put("X509Data", certInfo);
                        } catch (JSONException e) {

                        }


                        //본인확인용 R 값
                        byte[] rVal = priKey.getRandom();
                        try {
                            item.put("base64R", SGBase64.encode(rVal));
                        } catch (JSONException e) {

                        }

                        wvLoadUrlMainThread("javascript:fn_makeSignDataX509Callback('"+item.toString().replaceAll("\\\\n", "\\\\\\\\n").replaceAll("\\\\r", "\\\\\\\\r")+"');");

                        Log.i("CHECK", item.toString());

                        return;

                        // 전자서명 생성 끝

                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    private void changeCertPassword(final String dnName, final String p, final String n1, final String n2){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_changeCertPasswordCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    JSONObject item = new JSONObject();
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_changeCertPasswordCallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {
                        int checkNum = -1;

                        String certpath = selectedcertset.getCertPath();
                        SGPrivateKey priKey = null;
                        try{
                            priKey = SGKeyFactory.getInstance().generatePrivate(SGKeyCode.SIGN, SGFile.readBytes(certpath+"signPri.key"), p);
                            checkNum=0;
                        } catch(Exception e){
                            checkNum=8;
                        }

                        if(p.equals(n1)&&checkNum ==0)
                            checkNum = 9;

                        if(checkNum==0)
                            checkNum =	com.kica.android.util.CommonUtil.checkPasswd(n1,n2);

                        if(checkNum == 0){
                            boolean iskm = new SGFile(certpath+"kmPri.key").exists();

                            boolean isOk = npkiChangePass(iskm, certpath, p, n1);
                            if(!isOk)
                                checkNum = 12;
                        }

                        String message= "";
                        switch (checkNum) {
                            case 1:	message ="재입력한  인증서 비밀번호가  일치하지 않습니다.";		break;
                            case 2:	message ="인증서 비밀번호는  비밀번호 길이가 8자리  이상이어야 합니다.";		break;
                            case 3:	message ="인증서 비밀번호는  비밀번호 길이가 8자리  이상이어야 합니다.";		break;
                            case 4:	message ="인증서 비밀번호는 3회 이상 연속된 문자 (숫자)는 사용 수 없습니다.";		break;
                            case 5:	message ="인증서 비밀 번호는 한개 이상의 문자가 포함 되어야 합니다.";		break;
                            case 6:	message ="인증서 비밀 번호는 한개 이상의 숫자가 포함 되어야 합니다.";		break;
                            case 7:	message ="인증서 비밀 번호는 숫자,영문자,특수문자(\'\',\',/,공백 은 제외 )만 입력 할 수 있습니다.";		break;
                            case 8:	message ="현재 인증서 암호 입력이 틀렸습니다.";		break;
                            case 9:	message ="바꾸려는 인증서 비밀 번호와 현재 비밀번호가  같습니다.";		break;
                            case 10:message ="인증서 암호 변경에 실패하였습니다.";		break;
                            case 11:message ="인증서 암호 변경에 성공하였습니다.";		break;
                            case 12:message =tempError;		break;
                            default:message ="인증서 암호 변경에 성공하였습니다.";		break;
                        }
                        if (checkNum == 0 || checkNum == 11) {
                            wvLoadUrlMainThread("javascript:fn_changeCertPasswordCallback('"+message+"');");
                        } else {
                            wvLoadUrlMainThread("javascript:fn_changeCertPasswordCallbackFail('"+message+"');");
                        }
                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }
    private String tempError;
    private boolean npkiChangePass(boolean isExistCert, String certpath, String oldPass, String newPass){

        byte[] keyBytes = null;
        SGPrivateKey key = null;
        SGPrivateKeyGenerator pkcs8 = null;

        try
        {
            keyBytes = SGFile.readBytes(certpath+"signPri.key");
            key = SGKeyFactory.getInstance().generatePrivate(keyBytes, oldPass);
            byte[] random = key.getRandom();

            pkcs8 = new SGPrivateKeyGenerator(key);
            SGPrivateKey signPri = pkcs8.generate(newPass, random);

            if(isExistCert)
            {
                keyBytes = SGFile.readBytes(certpath+"kmPri.key");
                key = SGKeyFactory.getInstance().generatePrivate(keyBytes, oldPass);

                pkcs8 = new SGPrivateKeyGenerator(key);
                SGPrivateKey kmPri = pkcs8.generate(newPass);

                SGFile.writeBytes(signPri.getEncoded(), certpath+"signPri.key");
                SGFile.writeBytes(kmPri.getEncoded(), certpath+"kmPri.key");
            }
            else
            {
                SGFile.writeBytes(signPri.getEncoded(), certpath+"signPri.key");
            }
            return true;
        }
        catch (Exception e)
        {
            tempError = e.toString();
            return false;
        }
    }
    private String export_result;
    private void certExport(final String dnName, final String authCode, final String passwd){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    JSONObject item = new JSONObject();
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {
                        String certpath = selectedcertset.getCertPath();

                        String resultCode = "";
                        String result_message = "";

                        byte[] certBytes = null;
                        byte[] p12Bytes = null;
                        byte[] rBytes = null;
                        byte[] signBytes = null;

                        try {
                            certBytes = SGFile.readBytes(certpath + "/signCert.der");
                        } catch (IOException e) {
                            wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('인증서 정보를 읽을 수 없ㅅ습니다.');");
                            return;
                        }
                        p12Bytes = com.kica.android.util.CommonUtil.getP12Bytes(certpath, passwd);
                        signBytes = CMPUtil.signData(authCode, certpath + "/signCert.der", certpath + "/signPri.key", passwd, p12Bytes);
                        rBytes = CMPUtil.getRbytes(certpath + "/signPri.key", passwd);

                        String response = null;
                        if(p12Bytes == null) {
                            export_result = "인증서 패스워드가 틀렸거나, 유효성 검증을 할 수 없는 인증서입니다.";
                            wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('"+export_result+"');");
                        } else if(rBytes == null){
                            export_result = "인증서 패스워드가 틀렸거나, 신원확인 정보가 없는 인증서는 내보내기를 할 수 없습니다.";
                            wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('"+export_result+"');");
                        } else{

                            for(int i=0; i<rBytes.length; i++) {
                                rBytes[i] = 0x00;
                            }
                            rBytes = null;

                            String sign = SGBase64.encode(signBytes, false);
                            String cert = SGBase64.encode(certBytes, false);
                            String p12 = SGBase64.encode(p12Bytes, false);

                            String url = DataSet.relay_server_url_export;
                            try {
                                response = HTTPSRequest.getInstance().sendRelayCert(url, authCode, sign, cert, p12);
                            } catch (ResponseException e) {
                                e.printStackTrace();
                                result_message = "인증서 내보내기에 실패했습니다. \n 인증번호 또는 네크워크를 확인해 주세요.";
                            }

                            if(response == null) {
                                result_message = "인증서 내보내기에 실패했습니다. \n 인증번호 또는 네크워크를 확인해 주세요.";
                                wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('"+result_message+"');");

                            } else {
                                Matcher matcher = pattern.matcher(response);
                                int count = 0;
                                while(matcher.find()) {
                                    count++;

                                    String key = matcher.group(1);
                                    String value = matcher.group(2);

                                    if(key.equals("resultCode")) {
                                        // 결과코드 세팅
                                        resultCode = value;

                                    } else if(key.equals("message")) {
                                        result_message = value;
                                    }
                                }

                                if (resultCode != null && resultCode.equals("2000")) {
                                    export_result = "";
                                    wvLoadUrlMainThread("javascript:fn_certExportCallback('인증서 내보내기에 성공하였습니다.');");
                                } else if (resultCode != null && !resultCode.equals("2000")) {
                                    export_result = result_message;
                                    wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('"+export_result+"');");
                                } else {
                                    export_result = "인증서 내보내기에 실패했습니다. \n 인증번호 또는 네크워크를 확인해 주세요.";
                                    wvLoadUrlMainThread("javascript:fn_certExportCallbackFail('"+export_result+"');");
                                }

                            }

                        }
                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private void deleteCert(final String dnName, final String passwd){
        TimerTask myTask = new TimerTask() {
            public void run() {
                // 인증서 리스트 얻기 시작
                CertinfoList certinfolist = new CertinfoList();
                String[] allowOid = {
//        		        "1.2.410.200004.5.2.1.2",	//한국정보인증(개인)
//        				"1.2.410.200004.5.2.1.1",	//한국정보인증(법인)
//        				"1.2.410.200004.5.1.1.5",	//증권전산(개인)
//        				"1.2.410.200004.5.1.1.7",	//증권전산(법인)
//        				"1.2.410.200005.1.1.1",		//금융결제원(개인)
//        				"1.2.410.200005.1.1.5",		//금융결제원(법인)
//        				"1.2.410.200004.5.4.1.1",	//전자인증(개인)
//        				"1.2.410.200004.5.4.1.2",	//전자인증(법인)
//        				"1.2.410.200012.1.1.1",		//한국무역정보통신(개인)
//        				"1.2.410.200012.1.1.3"		//한국무역정보통신(법인)
                };
                certinfolist.setAllowOidPattern("*"); // 모든 인증서 허용.
//                certinfolist.load();
                certinfolist.loadInternal(MainActivity.this);
                certlist = certinfolist.getCertSetList();
                CertSet selectedcertset = null;
                if(certinfolist.getCertSetList().size() == 0){
                    wvLoadUrlMainThread("javascript:fn_deleteCertCallbackFail('현재 등록된 인증서가 없습니다. 인증서 가져오기 메뉴에서 인증서를 등록 후 사용 하세요.');");

                }else{
                    for(int i=0; i < certinfolist.getCertSetList().size() ; i++){
                        CertSet certset = certinfolist.getCertSetList().get(i);
                        String dn_name = certset.getSubjectDN();
                        if (dnName.equals(dn_name)) {
                            selectedcertset = certset;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (selectedcertset == null) {
                        wvLoadUrlMainThread("javascript:fn_deleteCertCallbackFail('인증서 정보를 찾을 수 없습니다.');");
                    } else {
                        String certpath = selectedcertset.getCertPath();
                        SGPrivateKey  priKey = null;
                        SGCertificate cert = null;
                        System.out.println("certpath: " + certpath);
                        try{
                            priKey = SGKeyFactory.getInstance().generatePrivate(SGKeyCode.SIGN, SGFile.readBytes(certpath+"signPri.key"), passwd);

                            cert = SGCertificateFactory.getInstance().generateCertificate(SGFile.readBytes(certpath+"signCert.der"));
                        }catch(Exception e){
                            wvLoadUrlMainThread("javascript:fn_deleteCertCallbackFail('인증서 비밀번호가 올바르지 않습니다. 확인하고 다시 입력해주세요.');");
                            return;
                        }

                        File certdir = new File(certpath);

                        // 해당 폴더의 모든 파일을 지운다.
                        if(com.kica.android.util.CommonUtil.deleteFolder(certdir) == true){
                            wvLoadUrlMainThread("javascript:fn_deleteCertCallback('선택한 인증서를 삭제하였습니다.');");
                        } else{
                            wvLoadUrlMainThread("javascript:fn_deleteCertCallbackFail('선택한 인증서를 삭제하는데 오류가 발생하였습니다.');");
                        }
                    }
                }
                // 인증서 리스트 얻기 끝

            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Uri urlToDownload;
    private long latestId = -1;

    private BroadcastReceiver completeReceiver = new BroadcastReceiver() {

        @SuppressLint("Range")
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();

            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = downloadManager.query(q);
            String title = "";
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    try {
                        title = URLDecoder.decode(title, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    // Log.i("CHECK", c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)));
                    Log.i("CHECK", c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    Log.i("CHECK", c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)));
                    Log.i("CHECK", c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                    // get other required data by changing the constant passed to getColumnIndex
                }
            }

            long id = intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            ParcelFileDescriptor file = null;
            try {
                file = dm.openDownloadedFile(id);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String fileName = title;
            File cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String bTempFilepath = cacheDir.getAbsolutePath() + "/" + title;

            int i = 1;
            String toFilename = bTempFilepath;
            while (true) {
                File tfile = new File(toFilename);
                if (!tfile.exists()) {
                    break;
                } else {
                    int sidx = bTempFilepath.lastIndexOf(".");
                    if (sidx > 0) {
                        toFilename = bTempFilepath.substring(0, sidx) + "-" + i + bTempFilepath.substring(sidx);
                    } else {
                        toFilename = bTempFilepath + "-" + i;
                    }
                    i++;
                }
            }
            bTempFilepath = toFilename;

            //Use that URI to open the file.

            InputStream fileStream = new FileInputStream(file.getFileDescriptor());
            OutputStream fileOutStream = null;
            try {
                fileOutStream = new FileOutputStream(bTempFilepath);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            byte[] buffer = new byte[1024];
            int length;

            try {
                while ((length = fileStream.read(buffer)) > 0) {
                    fileOutStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                fileOutStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                fileStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                fileOutStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            dm.remove(id);

            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);


            //(2018-11-16 HyunHuiJo) 파일이 없을 경우 앱 강제종료 방지[End]
            File file2 = new File(bTempFilepath);

            //확장자 구하기

            int sidx = bTempFilepath.lastIndexOf(".");
            String File_extend = "";
            if (sidx > 0)
                File_extend = bTempFilepath.substring(sidx + 1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri uri = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(bTempFilepath));
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                } else {
                    uri = Uri.fromFile(new File(bTempFilepath));
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }

                intent2.setData(uri);
                intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("알림");
                builder.setMessage("다운로드가 완료되었습니다. 파일을 확인하시겠습니까?.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(intent2);
                                } catch (ActivityNotFoundException anfex) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("알림");
                                    builder.setMessage("파일을 확인할 수 있는 앱이 설치되어 있지 않습니다. Download 폴더를 확인해 주세요.");
                                    builder.setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.show();
                                }
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
/*
                PackageManager pm = getPackageManager();
                List<ResolveInfo> list = pm.queryIntentActivities(intent2, PackageManager.GET_META_DATA);
                if (Build.VERSION.SDK_INT >= 33) {
                    list = pm.queryIntentActivities(intent2, PackageManager.ResolveInfoFlags.of(PackageManager.GET_META_DATA|PackageManager.MATCH_ALL));
                }
                if (list.size() == 0) {
//                    Toast.makeText(getApplicationContext(), "파일을 확인할 수 있는 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("다운로드가 완료되었습니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("다운로드가 완료되었습니다. 파일을 확인하시겠습니까?.");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent2);
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
//                    startActivity(intent2);
                }
*/

            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // 파일 확장자 별로 mime type 지정해 준다.
                if (File_extend.equalsIgnoreCase("mp3")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "audio/*");
                } else if (File_extend.equalsIgnoreCase("mp4")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "video/*");
                } else if (File_extend.equalsIgnoreCase("jpg")
                        || File_extend.equalsIgnoreCase("jpeg")
                        || File_extend.equalsIgnoreCase("gif")
                        || File_extend.equalsIgnoreCase("png")
                        || File_extend.equalsIgnoreCase("bmp")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "image/*");
                } else if (File_extend.equalsIgnoreCase("txt")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "text/*");
                } else if (File_extend.equalsIgnoreCase("doc")
                        || File_extend.equalsIgnoreCase("docx")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "application/msword");
                } else if (File_extend.equalsIgnoreCase("xls")
                        || File_extend.equalsIgnoreCase("xlsx")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "application/vnd.ms-excel");
                } else if (File_extend.equalsIgnoreCase("ppt")
                        || File_extend.equalsIgnoreCase("pptx")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "application/vnd.ms-powerpoint");
                } else if (File_extend.equalsIgnoreCase("pdf")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "application/pdf");
                } else if (File_extend.equalsIgnoreCase("hwp")) {
                    intent2.setDataAndType(Uri.fromFile(file2), "application/haansofthwp");
                }
                PackageManager pm = getPackageManager();
                List<ResolveInfo> list = pm.queryIntentActivities(intent2, PackageManager.GET_META_DATA);

                if (list.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("다운로드가 완료되었습니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
//                    Toast.makeText(getApplicationContext(), "파일을 확인할 수 있는 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("다운로드가 완료되었습니다. 파일을 확인하시겠습니까?.");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent2);
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
//                    startActivity(intent2);
                }

            }

        }
    };

    private void download(final String download_url){
        TimerTask myTask = new TimerTask() {
            public void run() {
                downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                urlToDownload = Uri.parse(download_url);
                Log.i("CHECK", "urlToDownload = "+urlToDownload);
                List<String> pathSegments = urlToDownload.getPathSegments();
                request = new DownloadManager.Request(urlToDownload);
                String cookieString = CookieManager.getInstance().getCookie(download_url);
                request.addRequestHeader("Cookie", cookieString);
                request.setDescription("파일 다운로드 중 입니다.");
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                latestId = downloadManager.enqueue(request);
            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask, 100);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onProviderDisabled(String provider) {

        }
    };


    void chooseUploadSource()
    {
        final CharSequence[] items = {"카메라", "앨범", "파일"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("업로드 파일 선택")
                .setItems(items, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int index){
                        if(index==0) {
                            getUploadFile("camera");
                        } else if(index==1) {
                            getUploadFile("gallery");
                        } else {
                            getUploadFile("file");
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //업로드할 파일 가져오기
    public void getUploadFile(String photoFrom) {
        handler.post(new Runnable() {
            public void run() {
                sendEventFC(TAG, "getUploadFile()");

                URI uri = null;
                if (photoFrom.equalsIgnoreCase("gallery")) {
                    selectUploadFileGallery();

                } else if (photoFrom.equalsIgnoreCase("camera")) {
                    selectUploadFilePhoto();

                } else if (photoFrom.equalsIgnoreCase("file")) {

                    selectUploadFileFile();
                }
            }
        });
    }


    private void selectUploadFileGallery() {
        sendEventFC(TAG, "selectUploadFileGallery()");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.putExtra("multi-pick", true);
        startActivityForResult(intent, UPLOADFILE_FROM_GALLERY_REQUEST);

    }

    private void selectUploadFilePhoto() {
        sendEventFC(TAG, "selectUploadFilePhoto()");
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
//                    photoFile = createImageFile("upload.jpg");
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    photoFile = File.createTempFile(
                            "klnet_upload_",  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                    currentPhotoPath_ = photoFile.getAbsolutePath();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri_ = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri_);
                    startActivityForResult(intent, UPLOADFILE_FROM_CAMERA_REQUEST);
                }
            }
        }
    }

    private void selectUploadFileFile() {
        sendEventFC(TAG, "selectUploadFileFile()");
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, UPLOADFILE_FROM_FILE_REQUEST);

        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }

        if (result == null) {
            result = "noname";
        }

        return result;
    }

    private void photoToServer(Uri originalUri, int requestType) {
        try {
            InputStream in = getContentResolver().openInputStream(originalUri);
            
            Bitmap img = BitmapFactory.decodeStream(in);

            img = PhotoFragment.resizeBitmap(img, 1080, 1920);

            //todo: rotation
            ExifInterface exif = null;
            try {
                String imagePath = currentPhotoPath_;//getRealPathFromURI(originalUri); // path 경로
                if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST)
                    imagePath = getRealPathFromURI(originalUri); // path 경로
                exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);

                rotate(img, exifDegree);

            } catch (IOException e) {
                Log.e("CHECK", "", e);
                wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");

                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
            byte[] bytes = baos.toByteArray();

            String base64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

            // String imagePath = getRealPathFromURI(uri);
            long fileSize = bytes.length;

            Log.d("CHECK", "base64String :" + base64String);
            Log.d("CHECK", "fileSize :" + fileSize);

            String jsString = "";

            if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST) {
                String format = "javascript:fn_getUploadFileCallback('%s', '%s');";
                jsString = String.format(format, "data:image/jpg;base64," + base64String, getFileName(originalUri));
            } else {
                //카메라
                String format = "javascript:fn_getUploadFileCallback('%s', '%s');";
                jsString = String.format(format, "data:image/jpg;base64," + base64String, getFileName(originalUri));
            }
            wvLoadUrl(jsString);
        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");
        }
    }


    private void photoToServer(ArrayList<Uri> mArrayUri, int requestType) {
        try {
            JSONObject ret = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            for (int i = 0; i < mArrayUri.size(); i++) {
                Uri originalUri = mArrayUri.get(i);

                InputStream in = getContentResolver().openInputStream(originalUri);
                Bitmap img = BitmapFactory.decodeStream(in);

                img = PhotoFragment.resizeBitmap(img, 1080, 1920);

                //todo: rotation
                ExifInterface exif = null;
                try {
                    String imagePath = currentPhotoPath_;//getRealPathFromURI(originalUri); // path 경로
                    if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST)
                        imagePath = getRealPathFromURI(originalUri); // path 경로
                    exif = new ExifInterface(imagePath);
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);

                    rotate(img, exifDegree);

                } catch (IOException e) {
                    Log.e("CHECK", "", e);
                    wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");

                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 55, baos);
                byte[] bytes = baos.toByteArray();

                String base64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

                // String imagePath = getRealPathFromURI(uri);
                long fileSize = bytes.length;

                Log.d("CHECK", "base64String :" + base64String);
                Log.d("CHECK", "fileSize :" + fileSize);

                if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST) {
                    JSONObject item = new JSONObject();
                    item.put("data", "data:image/jpg;base64," + base64String);
                    item.put("name", getFileName(originalUri));
                    jsonArr.put(item);
                } else {
                    //카메라
                    JSONObject item = new JSONObject();
                    item.put("data", "data:image/jpg;base64," + base64String);
                    item.put("name", getFileName(originalUri));
                    jsonArr.put(item);
                }
            }
            ret.put("fileList", jsonArr);
            wvLoadUrlMainThread("javascript:fn_getUploadFileMultiCallback('"+ret.toString()+"');");

        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");
        }
    }


    private void fileToServer(Uri originalUri, int requestType) {
        try {
            InputStream in = getContentResolver().openInputStream(originalUri);
            ByteArrayOutputStream byteOutStream = null;
            byteOutStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            try {
                while ((length = in.read(buffer)) > 0) {
                    byteOutStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                byteOutStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                byteOutStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            byte[] bytes = byteOutStream.toByteArray();

            String base64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

            long fileSize = bytes.length;

            Log.d("CHECK", "base64String :" + base64String);
            Log.d("CHECK", "fileSize :" + fileSize);

            String jsString = "";

            if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST) {
                String format = "javascript:fn_getUploadFileCallback('%s', '%s');";
                jsString = String.format(format, base64String, getFileName(originalUri));
            } else {
                //카메라
                String format = "javascript:fn_getUploadFileCallback('%s', '%s');";
                jsString = String.format(format, base64String, getFileName(originalUri));
            }
            wvLoadUrl(jsString);
        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 파일 생성에 실패하였습니다.');");
        }
    }

    private void fileToServer(ArrayList<Uri> mArrayUri, int requestType) {
        try {
            JSONObject ret = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            for (int i = 0; i < mArrayUri.size(); i++) {
                Uri originalUri = mArrayUri.get(i);

                InputStream in = getContentResolver().openInputStream(originalUri);
                ByteArrayOutputStream byteOutStream = null;
                byteOutStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int length;

                try {
                    while ((length = in.read(buffer)) > 0) {
                        byteOutStream.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    byteOutStream.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    byteOutStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                byte[] bytes = byteOutStream.toByteArray();

                String base64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

                // String imagePath = getRealPathFromURI(uri);
                long fileSize = bytes.length;

                Log.d("CHECK", "base64String :" + base64String);
                Log.d("CHECK", "fileSize :" + fileSize);

                if (requestType == UPLOADFILE_FROM_GALLERY_REQUEST) {
                    JSONObject item = new JSONObject();
                    item.put("data", base64String);
                    item.put("name", getFileName(originalUri));
                    jsonArr.put(item);
                } else {
                    //카메라
                    JSONObject item = new JSONObject();
                    item.put("data", base64String);
                    item.put("name", getFileName(originalUri));
                    jsonArr.put(item);
                }
            }
            ret.put("fileList", jsonArr);
            wvLoadUrlMainThread("javascript:fn_getUploadFileMultiCallback('"+ret.toString()+"');");

        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 파일 생성에 실패하였습니다.');");
        }
    }

    private void decodeFromFileMulti(Intent data, int requestCode) {
        try {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            if(data.getData()!=null){

                Uri mImageUri=data.getData();

                switch (requestCode) {
                    case UPLOADFILE_FROM_FILE_REQUEST:
                        fileToServer(mImageUri, UPLOADFILE_FROM_FILE_REQUEST);
                        break;
                }

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    if(mClipData.getItemCount() > 5) {
                        wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 파일은 한번에 5개까지 선택가능합니다.');");
                        return;
                    }
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);

                    }

                    switch (requestCode) {
                        case UPLOADFILE_FROM_FILE_REQUEST:
                            fileToServer(mArrayUri, UPLOADFILE_FROM_FILE_REQUEST);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            wvLoadUrl("javascript:fn_getUploadFileCallbackFail('업로드 이미지 생성에 실패하였습니다.');");
        }
    }



    public void showCustomToast(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_border, (ViewGroup)findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}