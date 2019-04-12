package com.akashapplications.technicalanna.PersonalMenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.Login;
import com.akashapplications.technicalanna.MainContainer;
import com.akashapplications.technicalanna.Models.User;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Registeration;
import com.akashapplications.technicalanna.UserProfile.PasswordRecoveryFirst;
import com.akashapplications.technicalanna.UserProfile.PhoneVerification;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.InternetConnectivity;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Profile extends Activity {

    EditText name, phone;
    ImageView image;
    User userModel;
    String imagePath;
    String profileImageCDN = "";
    private ArrayList<Image> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        image = findViewById(R.id.imageView);

        findViewById(R.id.updateImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImagePicker(v); 
            }
        });

        findViewById(R.id.updateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userModel.setName(name.getText().toString());
                userModel.setPhone(phone.getText().toString());

                if(!InternetConnectivity.isNetworkAvailable(getBaseContext()))
                {
                    InternetConnectivity.showPrompt(getBaseContext());
                    return;
                }
                new UpdateProfile().execute();
            }
        });

        findViewById(R.id.password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), PasswordRecoveryFirst.class));
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteAccount().execute();
            }
        });

        loadLocalData();
    }

    private void launchImagePicker(View v) {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#ffffff")         //  Toolbar color
                .setStatusBarColor("#fafafa")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#212121")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#212121")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#F1465A")     //  ProgressBar color
                .setBackgroundColor("#FAFAFA")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(false)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setMaxSize(1)                     //  Max images can be selected
                .setSavePath("TechnicalAnnaCamera")         //  Image capture folder name
                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            imagePath = Uri.fromFile(new File(images.get(0).getPath())).toString();
            Glide.with(getBaseContext())
                    .load(imagePath)
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user))
                    .into(image);
            uploadProfileImage(imagePath);
        } else
        {
            imagePath = "";
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileImage(String image) {
        if (image == "")
            return;

        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this)
                .setIcon(R.drawable.upload)
                .setTitle("Uploading your profile image")
                .setCancelable(false)
                .setMessage("Sit back and relax while we create your account")
                .setTopColorRes(R.color.blue_button);

        progressDialog.show();

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


        final StorageReference profRef = mStorageRef.child("profile_pictures/" + userModel.getUserID());
        profRef.putFile(Uri.parse(image))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        profRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileImageCDN = uri.toString();
                                Log.i(Tokens.LOG,profileImageCDN);
                                userModel.setImage(profileImageCDN);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        new UserData(getBaseContext()).setImage(profileImageCDN);
                        Log.i(Tokens.LOG, profileImageCDN);
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads

                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
//                        progressDialog.setMessage((int)progress + " of the upload is complete");
                        if(progress >= 100)
                            Toast.makeText(getBaseContext(), "Profile image uploaded", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void loadLocalData() {
        userModel = new User();
        UserData data = new UserData(getBaseContext());
        name.setText(data.getName());
        phone.setText(data.getPhone());
        Glide.with(getBaseContext())
                .load(data.getImage())
                .apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user))
                .into(image);

        userModel.setEmail(data.getEmail());
        userModel.setEmailVerified(data.isEmailVerified());
        userModel.setPhone(data.getPhone());
        userModel.setPhoneVerified(data.isPhoneVerified());
        userModel.setImage(data.getImage());
        userModel.setName(data.getName());
        userModel.setUserID(data.getUserID());
    }


    private class UpdateProfile extends AsyncTask<Void,Void,Void> {
        LovelyProgressDialog progressDialog = new LovelyProgressDialog(Profile.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog
                    .setIcon(R.drawable.update)
                    .setTitle("Updating your profile")
                    .setCancelable(false)
                    .setMessage("Sit back and relax while we update your account")
                    .setTopColorRes(R.color.blue_button);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("name", userModel.getName());
                reqBody.put("email", userModel.getEmail());
                reqBody.put("phone",userModel.getPhone());
                reqBody.put("image",userModel.getImage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.UPDATE_PROFILE, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Log.e(Tokens.LOG, response.toString());

                            UserData data = new UserData(getBaseContext());
                            data.setName(userModel.getName());
                            data.setPhone(userModel.getPhone());
                            data.setImage(userModel.getImage());

                            MainContainer.name.setText(data.getName());
                            Glide.with(getBaseContext())
                                    .load(data.getImage())
                                    .apply(RequestOptions.circleCropTransform()
                                            .placeholder(R.drawable.user)
                                            .error(R.drawable.user))
                                    .into(MainContainer.imageView);

                            Toast.makeText(getBaseContext(),"Profile updated successfully",Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    if(error.networkResponse != null && new String(networkResponse.data) != null) {
                        if(new String(networkResponse.data) != null)
                        {
                            try {
                                JSONObject object = new JSONObject(new String(networkResponse.data));
                                Toast.makeText(getBaseContext(), object.getString("res") , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            jsonObjectRequest.setShouldCache(false);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }


    private class DeleteAccount extends AsyncTask<Void,Void,Void> {
        LovelyProgressDialog progressDialog = new LovelyProgressDialog(Profile.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog
                    .setIcon(R.drawable.update)
                    .setTitle("Deleting your account")
                    .setCancelable(false)
                    .setMessage("We will miss you \uD83D\uDE14")
                    .setTopColorRes(R.color.blue_button);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", userModel.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.DELETE_PROFILE, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Toast.makeText(getBaseContext(),"Account Deleted",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), Registeration.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    if(error.networkResponse != null && new String(networkResponse.data) != null) {
                        if(new String(networkResponse.data) != null)
                        {
                            try {
                                JSONObject object = new JSONObject(new String(networkResponse.data));
                                Toast.makeText(getBaseContext(), object.getString("res") , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            jsonObjectRequest.setShouldCache(false);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }
}
