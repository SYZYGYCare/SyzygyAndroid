package com.dollop.syzygy.sohel;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dollop.syzygy.AppController;
import com.dollop.syzygy.activity.WelcomeActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null, response;
    static String json = "";
    private Context cx;

    // constructor
    public JSONParser(Context cx) {
        this.cx = cx;
    }

    // function get json from url
    // by making HTTP POST or GET mehtod


    public static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    public void parseVollyJSONObject(String url, int method, final JSONObject param, final Helper h) {
        //method GET=0,POST=1
       /* String perameters;
        if (param == null) {
            perameters = null;
        } else {
            perameters = param.toString();
        }*/
        if (method == 0 || method == 1) {
            final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (method, url, param, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.e("Response: ", response.toString());
                            if (response != null || response.length() > 0) {
                                h.backResponse(response.toString());

                            } else {
                                S.E("Something went wrong.!");
                                /*Snackbar snackbar = Snackbar
                                        .make(,"Your internet Connection is slow", Snackbar.LENGTH_LONG);
                                snackbar.show();*/
                            }
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String err = (error.getMessage() == null) ? "Parse Fail" : error.getMessage();

                            Log.e("sdcard-err2:", err);
                            S.E("Something went wrong.!");
/*
                            S.T(cx, "Your Internet Connection is slow..");
*/
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            jsObjRequest.setShouldCache(true);
            // Adding request to request queue
            AppController.getInstance().
                    addToRequestQueue(jsObjRequest);
        } else {
            S.E("Invalid Request Method");
        }
    }


    public void parseVollyStringRequestWithoutLoad(String url, int method, final Map<String, String> params, final Helper h) {
        //method GET=0,POST=1
        try
        {
            if (NetworkUtil.isNetworkAvailable(cx))
            {
                if (method == 0 || method == 1) {
                  //  final Dialog materialDialog = S.initProgressDialog(cx);
                    final StringRequest jsObjRequest = new StringRequest
                            (method, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //M.E("Response: " + response.toString());
                                    try {
                                        if (response != null) {
                                          //  materialDialog.dismiss();
                                            if (SavedData.gettocken_id().equals("Logout")) {

                                            } else {
                                                if (response.toString().toLowerCase().contains("token doesn't match")) {
                                                    S.I_clear(cx, WelcomeActivity.class, null);
                                                    SavedData.saveTocken("Logout");

                                                }
                                            }
                                            h.backResponse(response.toString());
                                        } else {
                                            S.E("Invalid Request Method");
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        String err = (error.getMessage() == null) ? "Parse Fail" : error.getMessage();
                                     //   materialDialog.dismiss();

                                        S.E("sdcard-err2:" + err);
                                        S.E("Something went wrong.!");
/*
                                S.T(cx, "Your Internet Connection is slow..");
*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //returning parameters
                            return params;
                        }
                    };

                    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                            60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(jsObjRequest);
                } else {
                    S.E("Invalid Request Method");

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void parseVollyStringRequest(String url, int method, final Map<String, String> params, final Helper h) {
        //method GET=0,POST=1
       try
       {
           if (NetworkUtil.isNetworkAvailable(cx))
           {
               if (method == 0 || method == 1) {
                   final Dialog materialDialog = S.initProgressDialog(cx);
                   final StringRequest jsObjRequest = new StringRequest
                           (method, url, new Response.Listener<String>()
                           {
                               @Override
                               public void onResponse(String response) {
                                   //M.E("Response: " + response.toString());
                                   try {
                                       if (response != null) {
                                           materialDialog.dismiss();
                                           if (SavedData.gettocken_id().equals("Logout")) {

                                           } else {
                                               if (response.toString().toLowerCase().contains("token doesn't match")) {
                                                   S.I_clear(cx, WelcomeActivity.class, null);
                                                   SavedData.saveTocken("Logout");

                                               }
                                           }
                                           h.backResponse(response.toString());
                                       } else {
                                           S.E("Invalid Request Method");
                                       }
                                   } catch (Exception e) {
                                   }
                               }
                           }, new Response.ErrorListener() {

                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   try {
                                       String err = (error.getMessage() == null) ? "Parse Fail" : error.getMessage();
                                       materialDialog.dismiss();

                                       S.E("sdcard-err2:" + err);
                                       S.E("Something went wrong.!");
/*
                                S.T(cx, "Your Internet Connection is slow..");
*/
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }

                               }
                           }) {
                       @Override
                       protected Map<String, String> getParams() throws AuthFailureError {
                           //returning parameters
                           return params;
                       }
                   };

                   jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                           60000,
                           DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                           DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                   // Adding request to request queue
                   AppController.getInstance().addToRequestQueue(jsObjRequest);
               } else {
                   S.E("Invalid Request Method");

               }
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    public void parseVollyStringRequestWithautProgressBar(String url, int method, final Map<String, String> params, final Helper h) {
        //method GET=0,POST=1
        if (NetworkUtil.getConnectivityStatus(cx)) {
            if (method == 0 || method == 1) {
//                final MaterialDialog materialDialog=M.initProgressDialog(cx);
                final StringRequest jsObjRequest = new StringRequest
                        (method, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //M.E("Response: " + response.toString());
                                if (response != null) {
//                                    materialDialog.dismiss();
                                    if (SavedData.gettocken_id().equals("Logout")) {

                                    } else {
                                        if (response.toString().toLowerCase().contains("token doesn't match")) {
                                            S.I_clear(cx, WelcomeActivity.class, null);
                                            SavedData.saveTocken("Logout");

                                        }
                                    }
                                    h.backResponse(response.toString());
                                } else {
                                    S.E("Invalid Request Method");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String err = (error.getMessage() == null) ? "Parse Fail" : error.getMessage();
//                                materialDialog.dismiss();

                                S.E("sdcard-err2:" + err);
                                S.E("Something went wrong.!");
                               /* S.T(cx, "Your Internet Connection is slow..");*/
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //returning parameters
                        return params;
                    }
                };

                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                        60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsObjRequest);

            } else {
                S.E("Invalid Request Method");
            }
        } else {
            //  S.T(cx, cx.getString(R.string.no_internet));
//            S.noInternetDialog(cx);
        }
    }
}