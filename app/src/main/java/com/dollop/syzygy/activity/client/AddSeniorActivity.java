package com.dollop.syzygy.activity.client;

import android.Manifest;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.syzygy.Model.AddSeniorModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.multipart.AppHelper;
import com.dollop.syzygy.sohel.multipart.VolleyMultipartRequest;
import com.dollop.syzygy.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;


public class AddSeniorActivity extends BaseActivity {
    Button BtnSaveSeniorId;
    CircleImageView SeniorImageLoadId;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    private static final int REQUEST = 1337;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    Uri mImageCaptureUri;
    Bitmap productImageBitmap;
    EditText EtSeniorNameId, EtAgeId, EtMobilrId, EtDescriptionId, EtNeedId, EtInstrutionId, EtAddressId;
    RadioGroup RadioGroupId;
    RadioButton maleBtn;
    RadioButton femaleBtn;

    String Token;
    private AddSeniorModel addSeniorModel1;
 EditText etRelationId;

    @Override
    protected int getContentResId() {
        return R.layout.activity_add_senior;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Add Someone");
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);
//        loadinLayout = (LinearLayout) findViewById(R.id.loadinLayout);

        EtSeniorNameId = (EditText) findViewById(R.id.EtSeniorNameId);

        EtAgeId = (EditText) findViewById(R.id.EtAgeId);
        EtAddressId = (EditText) findViewById(R.id.EtAddressId);
        EtMobilrId = (EditText) findViewById(R.id.EtMobilrId);
        EtDescriptionId = (EditText) findViewById(R.id.EtDescriptionId);
        EtInstrutionId = (EditText) findViewById(R.id.EtInstrutionId);
        EtNeedId = (EditText) findViewById(R.id.EtNeedId);
        etRelationId = (EditText) findViewById(R.id.etRelaitonId);
        BtnSaveSeniorId = (Button) findViewById(R.id.BtnSaveSeniorId);
        RadioGroupId = (RadioGroup) findViewById(R.id.RadioGroupId);
        maleBtn = (RadioButton) findViewById(R.id.maleBtn);
        femaleBtn = (RadioButton) findViewById(R.id.femaleBtn);
        SeniorImageLoadId = (CircleImageView) findViewById(R.id.SeniorImageLoadId);
        SeniorImageLoadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent() != null) {
                    if (getIntent().getStringExtra("onlyshow").equals("show")) {
               }else{ SelectImage();





























               }}
            }
        });
        BtnSaveSeniorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(EtSeniorNameId, EtAgeId, EtMobilrId)) {
                    addSenior();
                } else {
                    UserAccount.EditTextPointer.setError("This Can't be Empty!");
                    UserAccount.EditTextPointer.requestFocus();

                }

            }
        });

        if (getIntent() != null) {
            if (getIntent().getStringExtra("onlyshow").equals("show")) {
                Bundle addSeniorModel = getIntent().getExtras();
                addSeniorModel1 = (AddSeniorModel) addSeniorModel.getSerializable("show");

                EtSeniorNameId.setText(addSeniorModel1.getSeniorName());
                EtSeniorNameId.setEnabled(false);

                EtAgeId.setText(addSeniorModel1.getSeniorAge());
                EtAgeId.setEnabled(false);

                EtAddressId.setText(addSeniorModel1.getSeniorAddress());
                EtAddressId.setEnabled(false);
                EtMobilrId.setText(addSeniorModel1.getSeniorcontact());
                EtMobilrId.setEnabled(false);

                EtDescriptionId.setText(addSeniorModel1.getSeniorDecription());
                EtDescriptionId.setEnabled(false);

                EtNeedId.setText(addSeniorModel1.getSeniorSpical_need());
                EtNeedId.setEnabled(false);

                EtInstrutionId.setText(addSeniorModel1.getSpecial_instruction());
                EtInstrutionId.setEnabled(false);
                BtnSaveSeniorId.setEnabled(false);
                BtnSaveSeniorId.setVisibility(View.GONE);

                if (addSeniorModel1.getSeniorGender().toLowerCase().equals("male")) {
                    maleBtn.setChecked(true);
                    femaleBtn.setChecked(false);
                } else if (addSeniorModel1.getSeniorGender().toLowerCase().equals("female")) {
                    maleBtn.setChecked(false);
                    femaleBtn.setChecked(true);
                }

                Picasso.with(AddSeniorActivity.this)
                        .load(Const.URL.Image_Url + addSeniorModel1.getImage())
                        .into(SeniorImageLoadId);

            } else {
            }
        }
    }

    private void SelectImage() {
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
        ActivityCompat.requestPermissions(AddSeniorActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(AddSeniorActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(AddSeniorActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(AddSeniorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            productImageBitmap = (Bitmap) data.getExtras().get("data");
            SeniorImageLoadId.setImageBitmap(productImageBitmap);
            SeniorImageLoadId.setVisibility(View.VISIBLE);

        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            SeniorImageLoadId.setImageURI(galleryURI);
        }

    }


    public void addSenior() {
        final Dialog materialDialog = S.initProgressDialog(AddSeniorActivity.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.Add_Senior_Url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                try {
                    JSONObject mainObject = new JSONObject(resultResponse);
                    int status = mainObject.getInt("status");
                    String message = mainObject.getString("message");
                    if (status == 200) {
                        materialDialog.dismiss();
/*
                        Toast.makeText(AddSeniorActivity.this, "Senior Added Successfull", Toast.LENGTH_SHORT).show();
*/
                        finish();

                    } else {
/*
                        Toast.makeText(AddSeniorActivity.this, "SeniorNot Added", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    materialDialog.dismiss();
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
                materialDialog.dismiss();
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
                postParameters.put("token", Token);
                Log.e("Token", "Token" + Token);
                postParameters.put("senior_name", EtSeniorNameId.getText().toString());
                postParameters.put("senior_age", EtAgeId.getText().toString());
                postParameters.put("address", EtAddressId.getText().toString());
                postParameters.put("special_instruction", EtInstrutionId.getText().toString());
                postParameters.put("special_needs", EtNeedId.getText().toString());
                postParameters.put("description", EtDescriptionId.getText().toString());
                postParameters.put("senior_relation", etRelationId.getText().toString());
                if (maleBtn.isChecked()) {
                    postParameters.put("senior_gender", "Male");
                } else {
                    postParameters.put("senior_gender", "Female");
                }
                postParameters.put("senior_phone", EtMobilrId.getText().toString());
                Log.e("", "" + postParameters);
                return postParameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) SeniorImageLoadId.getDrawable()).getBitmap();
                    Log.e("Image_post ", "Image_post" + bitmap);
                    if (bitmap != null) {
                        params.put("profile_pic", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                        S.E("profile - " + AppHelper.getFileDataFromDrawable(bitmap));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }

}



