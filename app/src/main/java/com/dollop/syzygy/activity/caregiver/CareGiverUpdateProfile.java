package com.dollop.syzygy.activity.caregiver;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
import com.dollop.syzygy.activity.client.ClientUpdateProfile;
import com.dollop.syzygy.database.datahelper.UserDataHelper;
import com.dollop.syzygy.database.model.UserModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by sohel on 9/27/2017.
 */

public class CareGiverUpdateProfile extends BaseActivity {
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    @BindView(R.id.caregiverUpdateProfileImage)
    CircleImageView caregiverUpdateProfileImage;
    @BindView(R.id.caregiverUpdateProfileFullName)
    EditText caregiverUpdateProfileFullName;
    @BindView(R.id.caregiverUpdateProfileemail)
    EditText caregiverUpdateProfileemail;
    @BindView(R.id.caregiverUpdateProfilemobile)
    EditText caregiverUpdateProfilemobile;
    @BindView(R.id.caregiverUpdateProfilepassword)
    EditText caregiverUpdateProfilepassword;
    @BindView(R.id.caregiverUpdateProfileRadioMale)
    RadioButton caregiverUpdateProfileRadioMale;
    @BindView(R.id.caregiverUpdateProfileRadioFemale)
    RadioButton caregiverUpdateProfileRadioFemale;
    @BindView(R.id.caregiveraddress)
    EditText caregiveraddress;
    @BindView(R.id.caregiveraddarnumber)
    EditText caregiveraddarnumber;
    Bitmap productImageBitmap;
    Uri mImageCaptureUri;
    private Uri mCropImageUri;
    int screenWidth = 500;
    private String gender;


    @Override
    protected int getContentResId() {
        return R.layout.activity_caregiver_update_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Update Profile");
        caregiverUpdateProfileRadioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caregiverUpdateProfileRadioMale.setChecked(true);
                caregiverUpdateProfileRadioFemale.setChecked(false);
                gender = "Male";
            }
        });
        caregiverUpdateProfileRadioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caregiverUpdateProfileRadioMale.setChecked(false);
                caregiverUpdateProfileRadioFemale.setChecked(true);
                gender = "Female";
            }
        });
        caregiverUpdateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(CareGiverUpdateProfile.this);
                // selectImage();
            }
        });
        getClientProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.caregiver_update_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                try
                {
                    if (UserAccount.isEmpty(caregiverUpdateProfileFullName, caregiverUpdateProfileemail))
                    {
                        String add = caregiveraddress.getText().toString().trim();
                        String adh_num = caregiveraddarnumber.getText().toString().trim();

                        if (add == null || add.equalsIgnoreCase(""))
                        {
                            Toast.makeText(CareGiverUpdateProfile.this,"Please enter address",Toast.LENGTH_LONG).show();
                        }else if (adh_num == null || adh_num.equalsIgnoreCase(""))
                        {
                            Toast.makeText(CareGiverUpdateProfile.this,"Please enter Adhar number",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            updateProfile();

                        }


                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
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
        ActivityCompat.requestPermissions(CareGiverUpdateProfile.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(CareGiverUpdateProfile.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(CareGiverUpdateProfile.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(CareGiverUpdateProfile.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
//            Uri cameraURI = data.getData();
//            productImageBitmap = (Bitmap) data.getExtras().get("data");
//            caregiverUpdateProfileImage.setImageBitmap(productImageBitmap);
//            caregiverUpdateProfileImage.setVisibility(View.VISIBLE);
//
//        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
//            Uri galleryURI = data.getData();
//            caregiverUpdateProfileImage.setImageURI(galleryURI);
//        }
//    }

    private void getClientProfile() {
        S.E("prams " + getParams());
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_PROFILE, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("getclient  profile" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONObject jsonObject1 = mainobject.getJSONObject("data");


                        if (jsonObject1.getString("email_id").equals("null")) {
                            caregiverUpdateProfileemail.setText("");
                        } else {
                            caregiverUpdateProfileemail.setText(jsonObject1.getString("email_id"));
                        }

                        if (jsonObject1.getString("full_name").equals("null")) {
                            caregiverUpdateProfileFullName.setText("");
                        } else {
                            caregiverUpdateProfileFullName.setText(jsonObject1.getString("full_name"));
                        }

                        SavedData.saveDRIVER_NAME(jsonObject1.getString("full_name"));
                        if (jsonObject1.getString("address").equals("null")) {
                            caregiveraddress.setText("");
                        } else {
                            caregiveraddress.setText(jsonObject1.getString("address"));
                        }

                        if (jsonObject1.getString("phone").equals("null")) {
                            caregiverUpdateProfilemobile.setText("");
                        } else {
                            caregiverUpdateProfilemobile.setEnabled(false);
                            caregiverUpdateProfilemobile.setText(jsonObject1.getString("phone"));
                        }

                        if (jsonObject1.getString("adhar_no").equals("null")) {
                            caregiveraddarnumber.setText("");
                        } else {
                            caregiveraddarnumber.setText(jsonObject1.getString("adhar_no"));
                        }

                        gender = jsonObject1.getString("gender");
                        Picasso.with(CareGiverUpdateProfile.this).load(Const.URL.Image_Url + jsonObject1.getString("profile_pic")).error(R.drawable.user_profile_pic)
                                .into(caregiverUpdateProfileImage);
                        if (jsonObject1.getString("gender").equals("Male")) {
                            caregiverUpdateProfileRadioMale.setChecked(true);
                            caregiverUpdateProfileRadioFemale.setChecked(false);
                        } else {
                            caregiverUpdateProfileRadioMale.setChecked(false);
                            caregiverUpdateProfileRadioFemale.setChecked(true);
                        }

                        UserModel userModel = new UserModel();

                        /*userId, userName, userEmail, userMobile, userGender, address, adharNumber, userImage, user_type*/

                        userModel.setUserName(jsonObject1.getString("full_name"));
                        userModel.setUserEmail(jsonObject1.getString("email_id"));
                        userModel.setUserMobile(jsonObject1.getString("phone"));
                        userModel.setUserGender(jsonObject1.getString("gender"));
                        userModel.setAddress(jsonObject1.getString("address"));
                        userModel.setAdharNumber(jsonObject1.getString("adhar_no"));
                        userModel.setUserImage(jsonObject1.getString("profile_pic"));
                        userModel.setUser_type("CareGiver");
                        UserDataHelper.getInstance().insertData(userModel);

                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                        S.E("else =-=-=-=-=-=-=");
                        setDataFromDataBase();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    setDataFromDataBase();
                }
            }
        });
    }

    private void setDataFromDataBase() {
        ArrayList<UserModel> arrayList = new ArrayList<UserModel>();
        arrayList = UserDataHelper.getInstance().getList();


        caregiverUpdateProfileemail.setText(arrayList.get(0).getUserEmail());
        caregiverUpdateProfileFullName.setText(arrayList.get(0).getUserName());
        caregiveraddress.setText(arrayList.get(0).getAddress());
        caregiverUpdateProfilemobile.setText(arrayList.get(0).getUserMobile());
        caregiveraddarnumber.setText(arrayList.get(0).getAdharNumber());
        gender = arrayList.get(0).getUserGender();
        Picasso.with(CareGiverUpdateProfile.this).load(Const.URL.Image_Url + arrayList.get(0).getUserImage()).error(R.drawable.user_profile_pic)
                .into(caregiverUpdateProfileImage);
        if (arrayList.get(0).getUserGender().equals("Male")) {
            caregiverUpdateProfileRadioMale.setChecked(true);
            caregiverUpdateProfileRadioFemale.setChecked(false);
        } else {
            caregiverUpdateProfileRadioMale.setChecked(false);
            caregiverUpdateProfileRadioFemale.setChecked(true);
        }
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "2");
        return params;
    }

    public void updateProfile() {
        final Dialog dialog = S.initProgressDialog(CareGiverUpdateProfile.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.UPDATE_PROFILE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                S.E(response.toString());
                try {
                    dialog.dismiss();
//                    getClientProfile();
                    S.T(CareGiverUpdateProfile.this, "Profile update successfully.");
                    JSONObject mainObject = new JSONObject(resultResponse);

                    S.I_clear(CareGiverUpdateProfile.this, CareGiverMainActivity.class, null);

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
                postParameters.put("full_name", caregiverUpdateProfileFullName.getText().toString());
                postParameters.put("email_id", caregiverUpdateProfileemail.getText().toString());
                postParameters.put("address", caregiveraddress.getText().toString());
                postParameters.put("gender", gender);
//                postParameters.put("password", caregiverUpdateProfilepassword.getText().toString());
                postParameters.put("aadharNumber", caregiveraddarnumber.getText().toString());
                return postParameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) caregiverUpdateProfileImage.getDrawable()).getBitmap();
                    Log.e("Image_post ", "Image_post" + bitmap);
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

        ).addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                caregiverUpdateProfileImage.setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

}

