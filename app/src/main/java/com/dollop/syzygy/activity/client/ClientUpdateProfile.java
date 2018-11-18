package com.dollop.syzygy.activity.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.UserAccount;
import com.dollop.syzygy.sohel.multipart.AppHelper;
import com.dollop.syzygy.sohel.multipart.VolleyMultipartRequest;
import com.dollop.syzygy.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by sohel on 9/27/2017.
 */

public class ClientUpdateProfile extends BaseActivity {

    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.clientUpdateProfileFullName)
    EditText clientUpdateProfileFullName;
    @BindView(R.id.clientUpdateProfileEmail)
    EditText clientUpdateProfileEmail;
    @BindView(R.id.clientUpdateProfileMobile)
    EditText clientUpdateProfileMobile;
    @BindView(R.id.clientUpdateProfileRadioMale)
    RadioButton clientUpdateProfileRadioMale;
    @BindView(R.id.clientUpdateProfileRadioFemale)
    RadioButton clientUpdateProfileRadioFemale;
    @BindView(R.id.clientUpdateProfileAadharNumber)
    EditText clientUpdateProfileAadharNumber;
    @BindView(R.id.clientUpdateProfileAddress)
    EditText clientUpdateProfileAddress;
    @BindView(R.id.clientUpdateProfileImage)
    ImageView clientUpdateProfileImage;
    Uri mImageCaptureUri;
    Bitmap productImageBitmap;
    private String gender;
    int screenWidth = 500;
    @Override
    protected int getContentResId() {
        return R.layout.activity_client_update_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Update Profile");
        SavedData.getInstance();
        clientUpdateProfileMobile.setEnabled(false);
        clientUpdateProfileMobile.setText(SavedData.getUserPhone());

        clientUpdateProfileRadioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientUpdateProfileRadioMale.setChecked(true);
                clientUpdateProfileRadioFemale.setChecked(false);
                gender = "Male";
            }
        });
        clientUpdateProfileRadioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientUpdateProfileRadioMale.setChecked(false);
                clientUpdateProfileRadioFemale.setChecked(true);
                gender = "Female";
            }
        });

        clientUpdateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        getClientProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.client_update_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (UserAccount.isEmpty(clientUpdateProfileFullName, clientUpdateProfileEmail, clientUpdateProfileAddress, clientUpdateProfileAadharNumber,clientUpdateProfileMobile)) {

                    updateProfile();
                }

                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void getClientProfile() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_PROFILE, 1, getParams12(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("Check By Ani" + response);
                S.E("Check By Ani" + getParams12());
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (messgae.equals("token Doesn't matched")) {

                        finish();
                        Intent intent = new Intent(ClientUpdateProfile.this, ClientEnterMobileActivity.class);
                        startActivity(intent);

                    }
                    if (status == 200) {
                        JSONObject jsonObject1 = mainobject.getJSONObject("data");

                        if(jsonObject1.getString("email_id").equals("null")){clientUpdateProfileEmail.setText("");}
                        else {clientUpdateProfileEmail.setText(jsonObject1.getString("email_id"));}

                        if(jsonObject1.getString("full_name").equals("null")){clientUpdateProfileFullName.setText("");}
                        else {clientUpdateProfileFullName.setText(jsonObject1.getString("full_name"));}

                        if(jsonObject1.getString("address").equals("null")){clientUpdateProfileAddress.setText("");}
                        else {clientUpdateProfileAddress.setText(jsonObject1.getString("address"));}

                        if(jsonObject1.getString("aadharNumber").equals("null")){clientUpdateProfileAadharNumber.setText("");}
                        else {clientUpdateProfileAadharNumber.setText(jsonObject1.getString("aadharNumber"));}

                        Picasso.with(ClientUpdateProfile.this).load(Const.URL.Image_Url + jsonObject1.getString("profile_pic")).error(R.drawable.user_profile_pic)
                                .into(clientUpdateProfileImage);
                        gender = jsonObject1.getString("gender");
                        if (jsonObject1.getString("gender").equals("Male")) {
                            clientUpdateProfileRadioMale.setChecked(true);
                            clientUpdateProfileRadioFemale.setChecked(false);
                        } else {
                            clientUpdateProfileRadioMale.setChecked(false);
                            clientUpdateProfileRadioFemale.setChecked(true);
                        }
                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams12() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "1");
        return params;
    }

    public void updateProfile() {
        final Dialog dialog = S.initProgressDialog(ClientUpdateProfile.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.UPDATE_PROFILE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                S.E(response.toString());
                try {
                    dialog.dismiss();

                    /*getClientProfile();*/
                    JSONObject mainObject = new JSONObject(resultResponse);
                    int status = mainObject.getInt("status");
                    if (status == 200) {
                        S.T(ClientUpdateProfile.this, "Profile update successfully.");
                        S.I(ClientUpdateProfile.this,ClientMainActivity.class,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                dialog.dismiss();
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }

        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postParameters = new HashMap<>();
                postParameters.put("token", SavedData.gettocken_id());
                postParameters.put("full_name", clientUpdateProfileFullName.getText().toString());
                postParameters.put("email_id", clientUpdateProfileEmail.getText().toString());
                postParameters.put("address", clientUpdateProfileAddress.getText().toString());
                postParameters.put("gender", gender);
                postParameters.put("aadharNumber", clientUpdateProfileAadharNumber.getText().toString());
                S.E("prams for update =-=-=-=-=    " + postParameters);
                return postParameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) clientUpdateProfileImage.getDrawable()).getBitmap();
                    Log.e("profile_pic ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("profile_pic", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(

                getBaseContext()

        ).

                addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }


    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else
                            requestPermission();
                    } else
                        cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkGalleryPermission())
                            galleryIntent();
                        else
                            requestGalleryPermission();
                    } else
                        galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    private void galleryIntent() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ClientUpdateProfile.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(ClientUpdateProfile.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(ClientUpdateProfile.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(ClientUpdateProfile.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            productImageBitmap = (Bitmap) data.getExtras().get("data");
            clientUpdateProfileImage.setImageBitmap(productImageBitmap);
            clientUpdateProfileImage.setVisibility(View.VISIBLE);

        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            clientUpdateProfileImage.setImageURI(galleryURI);
        }
    }


}
