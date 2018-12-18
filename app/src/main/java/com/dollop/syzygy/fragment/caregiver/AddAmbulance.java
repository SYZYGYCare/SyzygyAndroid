package com.dollop.syzygy.fragment.caregiver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.syzygy.Model.CareGiverSpecialization;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.multipart.AppHelper;
import com.dollop.syzygy.sohel.multipart.VolleyMultipartRequest;
import com.dollop.syzygy.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.Manifest.permission.CAMERA;
import static android.os.Build.VERSION_CODES.M;

public class AddAmbulance extends Fragment {
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    @BindView(R.id.caregiversppiner)
    Spinner caregiversppiner;
    @BindView(R.id.vehicalno)
    EditText vehicalno;
    @BindView(R.id.vehicalmodelno)
    EditText vehicalmodelno;
    @BindView(R.id.driverdetail)
    EditText driverdetail;
    @BindView(R.id.uploaddocument)
    TextView uploaddocument;

    @BindView(R.id.uploaddocument1)
    TextView uploaddocument1;

    @BindView(R.id.uploaddocument2)
    TextView uploaddocument2;


    @BindView(R.id.documentImageView1)
    ImageView documentImageView1;

    @BindView(R.id.documentImageView2)
    ImageView documentImageView2;


    @BindView(R.id.uploaddocumentlayout)
    LinearLayout uploaddocumentlayout;
    @BindView(R.id.documentImageView)
    ImageView documentImageView;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.licenstlayout)
    LinearLayout licenstlayout;
    @BindView(R.id.licencdocument)
    ImageView licencdocument;
    @BindView(R.id.textLicence)
    TextView textLicence;
    Uri mImageCaptureUri;
    Bitmap productImageBitmap;
    int flag;
    String checkimage = "";
    String checkimage2 = "";
    String checkImage3 = "";
    String checkImage4 = "";
    Unbinder unbinder;
    String ambulanceTypeID;
    String service_id = "";
    CareGiverSpecialization careGiverSpecialization;
    ArrayList<CareGiverSpecialization> careGiverSpecializationslist = new ArrayList<>();
    ArrayList<String> careGiverSpecializations_id = new ArrayList<>();
    ArrayList<String> citynameList = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    @BindView(R.id.spCityListId)
    Spinner spCityListId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter aa;
    private String cityIdstr = "";
    private int selectedPostion=0;
    private String current_city="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_ambulance1, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Add Ambulance");
        getAmbulanceType();
        getPreviousAmbulance_Data();
        driverdetail.setText(SavedData.getDRIVER_NAME());
        licencdocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                flag = 1;
            }
        });
        textLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                flag = 1;
            }
        });
        documentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                flag = 2;
            }
        });
        uploaddocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                flag = 2;
            }
        });

        uploaddocument1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                flag = 3;
            }
        });

        documentImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                flag = 3;
            }
        });

        uploaddocument2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                flag = 4;
            }
        });

        documentImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                flag = 4;
            }
        });


        caregiversppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Ambulance Type")) {
                } else {
                    ambulanceTypeID = careGiverSpecializationslist.get(i).getCaregiver_specialization_id();
                    service_id = careGiverSpecializationslist.get(i).getCaregiver_specialization_id();
                    getCityList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String vehi_no = vehicalno.getText().toString().trim();
                String vehi_mod_no = vehicalmodelno.getText().toString().trim();
                if (TextUtils.isEmpty(vehi_no))
                {
                    vehicalno.setText("");
                    vehicalno.requestFocus();
                    vehicalno.setError("Please feel Your vehical no.");
                    return;
                } else if (TextUtils.isEmpty(vehi_mod_no)) {
                    vehicalmodelno.requestFocus();
                    vehicalmodelno.setText("");
                    vehicalmodelno.setError("Please feel Your vehical model_no");
                    return;
                } else if (caregiversppiner.getSelectedItem().equals("Select Ambulance Type")) {
                    S.T(getActivity(), "Please Choose a Ambulance Type");
                } else if (!checkimage.equals("Image")) {
                    S.T(getActivity(), "Please give a document");
                } else if (!checkImage3.equals("Image")) {
                    S.T(getActivity(), "Please give a document");
                } else if (!checkImage4.equals("Image")) {
                    S.T(getActivity(), "Please give a document");
                }else if (!checkimage2.equals("Image")) {
                    S.T(getActivity(), "Please give a document");
                } else if (cityIdstr.equals("")) {
                    S.T(getActivity(), "Please Select a City");
                } else {
                    addCareGiver();
                }
            }
        });
        return view;
    }

    private void getCityList() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetCityList, 1, getParamCityName(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("City ka response" + response);
                    S.E("city ka param" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200"))
                    {
                        JSONArray data = object1.getJSONArray("data");

                        citynameList.clear();
                        CityId.clear();

                        citynameList.add("Select a City");
                        CityId.add("");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String ids = jsonObject1.getString("id");
                            String city_name = jsonObject1.getString("city_name");

                            if(current_city.equals(ids)){
                                selectedPostion=i+1;

                            }
                            citynameList.add(city_name);
                            CityId.add(ids);


                        }
                        ArrayAdapter<String> citystringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, citynameList);

                        spCityListId.setAdapter(citystringArrayAdapter);
                        spCityListId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cityIdstr = CityId.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spCityListId.setSelection(selectedPostion);
                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), "NO data!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getParamCityName() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("service_id", service_id);

        return params;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else
                            requestPermission();
                    } else
                        cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= M) {
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
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            productImageBitmap = (Bitmap) data.getExtras().get("data");
            if (flag == 1) {
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                textLicence.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                licencdocument.setImageBitmap(productImageBitmap);
                checkimage2 = "Image";
            } else if (flag == 2) {
                documentImageView.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage = "Image";
            }else if(flag == 3){
                documentImageView1.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument1.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage3 = "Image";
            }else if(flag == 4){
                documentImageView2.setImageBitmap(productImageBitmap);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument2.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage4 = "Image";
            }
        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
//            lincensId.setImageURI(galleryURI);
            if (flag == 1) {
                licencdocument.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                textLicence.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage2 = "Image";
            } else if (flag == 2) {
                documentImageView.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkimage = "Image";
            }else if(flag == 3){
                documentImageView1.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument1.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage3 = "Image";
            }else if(flag == 4){
                documentImageView2.setImageURI(galleryURI);
                Drawable img = getContext().getResources().getDrawable(R.drawable.check_mark);
                uploaddocument2.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                checkImage4 = "Image";
            }
        }
    }

    private void addCareGiver() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait....");
        progressDialog.setTitle("Progress");
        progressDialog.show();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.Client_Ambulance_Url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                progressDialog.dismiss();
                try {

                    JSONObject mainObject = new JSONObject(resultResponse);
                    int status = mainObject.getInt("status");
                    String message = mainObject.getString("message");
                    if (status == 200) {

/*
                        Toast.makeText(getActivity(), " Ambulance Successfully ", Toast.LENGTH_SHORT).show();
*/
                        Intent intent = new Intent(getActivity(), CareGiverMainActivity.class);
                        startActivity(intent);
                    } else {
/*
                        Toast.makeText(getActivity(), "you already registered with CareGiver", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                progressDialog.dismiss();
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
                HashMap<String, String> postParams = new HashMap<>();
                postParams.put("ambulance_type", ambulanceTypeID);
                postParams.put("vehical_registration_no", vehicalno.getText().toString());
                postParams.put("vehical_model_no", vehicalmodelno.getText().toString());
                postParams.put("token", SavedData.gettocken_id());
                postParams.put("current_city", cityIdstr);
                postParams.put("type", "2");
                SavedData.saveType("2");
                Log.e("postParams", "postParams" + postParams);
                return postParams;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {

                    Bitmap bitmap3 = ((BitmapDrawable) licencdocument.getDrawable()).getBitmap();
                    Log.e("lincensId ", "Image_post" + bitmap3);
                    if (bitmap3 != null)
                        params.put("lincens", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap3), "image/png"));
                    Bitmap bitmap4 = ((BitmapDrawable) documentImageView.getDrawable()).getBitmap();
                    Log.e("registration_ ", "Image_post" + bitmap4);
                    if (bitmap4 != null)
                        params.put("registration_card", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap4), "image/png"));

                    Bitmap bitmap5 = ((BitmapDrawable) documentImageView1.getDrawable()).getBitmap();
                    Log.e("registration_ ", "Image_post" + bitmap5);
                    if (bitmap5 != null)
                        params.put("image1", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap5), "image/png"));

                    Bitmap bitmap6 = ((BitmapDrawable) documentImageView2.getDrawable()).getBitmap();
                    Log.e("registration_ ", "Image_post" + bitmap4);
                    if (bitmap6 != null)
                        params.put("image2", new DataPart(System.currentTimeMillis() + "Image_event.png", AppHelper.getFileDataFromDrawable(bitmap6), "image/png"));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
    }

    private void getAmbulanceType() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_AMBULANCE_TYPE, 1, getParamService(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        careGiverSpecializations_id.add("Select Ambulance Type");
                        careGiverSpecializationslist.add(new CareGiverSpecialization());
                        JSONArray content1 = object1.getJSONArray("data");
                        for (int j = 0; j < content1.length(); j++) {
                            JSONObject jsonObject = content1.getJSONObject(j);
                            careGiverSpecialization = new CareGiverSpecialization();
                            careGiverSpecialization.setSpecialization(jsonObject.getString("specialization"));
                            careGiverSpecialization.setCaregiver_specialization_id(jsonObject.getString("caregiver_specialization_id"));
                            careGiverSpecializationslist.add(careGiverSpecialization);
                            careGiverSpecializations_id.add(jsonObject.getString("specialization"));
                        }
                        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, careGiverSpecializations_id);
                        caregiversppiner.setAdapter(aa);

                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), "NO data!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getParamService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        S.E("getService ki request" + params);
        return params;
    }

    private void getPreviousAmbulance_Data() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_PREVIOUS_CAREGIVER, 1, getPreviousData(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {

                        JSONArray jsonObjectArrray = object.getJSONArray("dada");
                        for (int j = 0; j < jsonObjectArrray.length(); j++) {
                            S.E("Ander a gaya bete");
                            JSONObject jsonObject = jsonObjectArrray.getJSONObject(j);
                            vehicalmodelno.setText(jsonObject.getString("vehical_model_no"));
                            vehicalno.setText(jsonObject.getString("vehical_registration_no"));
                            current_city=jsonObject.getString("current_city");
                            Picasso.with(getActivity()).load(Const.URL.Image_Url_Ambulance + jsonObject.getString("registration_card")).error(R.drawable.ic_document)
                                    .into(documentImageView);
                            Picasso.with(getActivity()).load(Const.URL.Image_Url_Ambulance + jsonObject.getString("lincens")).error(R.drawable.ic_document)
                                    .into(licencdocument);

                            caregiversppiner.setSelection(careGiverSpecializations_id.indexOf(jsonObject.getString("service_name")));
                            aa.notifyDataSetChanged();
                            checkimage = "Image";
                            checkimage2 = "Image";
                            checkImage4 = "Image";
                            checkImage4 = "Image";
                        }
                    } else {

                        S.T(getActivity(), "NO data!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getPreviousData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        S.E("getService ki request" + params);
        return params;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}