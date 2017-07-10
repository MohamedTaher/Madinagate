package solversteam.madinagate.data.connection;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import solversteam.madinagate.R;

//import com.google.gson.Gson;


public class Connection {


    private Handler mainHandler;
    private OkHttpClient client;
    private Request requesturl;
    private Activity _context;
    private String json, method, url;
    private FormEncodingBuilder formBody;
    private Result DelegateMethod;
    private ConnectionDetector testConnection;
    private static final String baseurl = "http://166.63.123.121:86/madinagate/";
    private Boolean check=true;


    public Connection(Activity context, String URL, String method) {

        url = baseurl + URL;
        Log.i("urlconnection", url);
        this._context = context;
        mainHandler = new Handler(_context.getMainLooper());
        client = new OkHttpClient();
//        this.loading = (ProgressBar) _context.findViewById(progressBar);
        this.method = method;
        testConnection = new ConnectionDetector(_context);
    }

    // ===================== Deleage Method ==============================================
    public interface Result {
        public void data(String str) throws JSONException;
    }

    public void Connect(Result dlg) {
        DelegateMethod = dlg;

        if (testConnection.isConnectingToInternet()) {
            if (method.equals("Post")) {
                ReadFromServer();
            } else if (method.equals("Get")) {
                ReadFromServer1();
            } else if (method.equals("Put"))
                ReadFromServer2();
        } else {
            connectionToast();
            repeat();
        }


    }

    public void executeNow(String result) {
        try {
            DelegateMethod.data(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ===================== End Deleage Method ==============================================
    public void reset() {
        formBody = new FormEncodingBuilder();
    }

    public void addParmmter(String key, String value) {
        formBody.add(key, value);
    }
    /*public void addList(String key, ArrayList<CartCLass> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        Log.d("checkjson",json);
        formBody.add(key, json);

    }*/


    private void ReadFromServer() {


        try {
            _context.showDialog(1);
        } catch (Exception ex) {
        }
        RequestBody body = formBody.build();
        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            client.newCall(requesturl).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    try {
                        _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                               repeat();
                            } else {
                                check=true;
                                repeat();
                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });

                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void ReadFromServer1() {
        //Log.i("12555" , progressBar+"");
        //loading = (ProgressBar) _context.findViewById(progressBar);
        //loading.setVisibility(View.VISIBLE);
        try {
            _context.showDialog(1);
        } catch (Exception e) {
        }

        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            client.newCall(requesturl).enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {
                    try {
                     _context.dismissDialog(1);
                    } catch (Exception ex) {
                    }
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                                 repeat();
                            } else {
                                check=true;
                                repeat();
                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
//                    loading.setVisibility(View.INVISIBLE);
                    try {
                           _context.dismissDialog(1);
                    } catch (Exception e) {
                    }
                    int code = response.code();
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void connectionToast() {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.my_customtoast, null);
        LinearLayout linear = (LinearLayout) layout.findViewById(R.id.Forget_customlayout_LinearLayout);
        Toast toast = new Toast(_context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void ReadFromServer2() {

        _context.showDialog(1);
        RequestBody body = formBody.build();
        try {
            requesturl = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            client.newCall(requesturl).enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {
                    _context.dismissDialog(1);
                    json = request.toString();
                    Log.i("Failuremsg", json);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (testConnection.isConnectingToInternet()) {
                                check=true;
                                 repeat();
                            } else {
                                check=true;
                                repeat();
                                connectionToast();
                            }
                        }
                    });


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    int code = response.code();
                    //_context.dismissDialog(1);
                    json = response.body().string();
                    Log.i("ResponseMsg", json);
                    Log.i("ResponseCode", code + "");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            executeNow(json);
                            _context.dismissDialog(1);
                        }
                    });

                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void repeat()
    {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(check) {
                    Log.d("checkconn","first");
                    repeat();
                    if (testConnection.isConnectingToInternet()) {
                        check=false;
                        Log.d("checkconn","second");

                        if (method.equals("Post")) {
                            ReadFromServer();
                        } else if (method.equals("Get")) {
                            ReadFromServer1();
                        } else if (method.equals("Put"))

                        {
                            ReadFromServer2();
                        }
                    }
                }
            }
        },3000);

    }



}
