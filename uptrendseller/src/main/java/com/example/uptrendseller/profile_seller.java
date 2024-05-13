package com.example.uptrendseller;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import DataModel.Admin;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class profile_seller extends AppCompatActivity {

    private CircleImageView profileIv;
    private DatabaseReference sellerNode,sellerInfoNode;


    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    //string array of permission
    private String[] cameraPermission;
    private String[] storagePermission;
    private String nodeId,infoId;

    //image uri var
    private Uri imageUri;
    private FirebaseUser user;
    private Query seller,store;
    EditText txtSellerName,txtSellerMobileNo,txtStoreName;
    TextView txtSellerEmail,txtSave,add_img_txt;
    private FirebaseStorage storage;
    loadingDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_seller);

        profileIv = findViewById(R.id.image);
        txtSellerName=findViewById(R.id.profile_name);
        txtSellerMobileNo=findViewById(R.id.profile_mobile_no);
        txtSellerEmail=findViewById(R.id.profile_email);
        txtStoreName=findViewById(R.id.profile_display);
        txtSave=findViewById(R.id.save_btn);
        add_img_txt = findViewById(R.id.add_img_txt);


        //init permission
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Getting The Current Login User Instance.
        user= FirebaseAuth.getInstance().getCurrentUser();

        //Making Instance of Database.
        sellerNode= FirebaseDatabase.getInstance().getReference("Admin");
        sellerInfoNode=FirebaseDatabase.getInstance().getReference("AdminStoreInformation");
        /*
                Getting Seller Information and displaying on Edit textview as well
                as textview
         */
        seller=sellerNode.orderByChild("adminId").equalTo(user.getUid());
        loading=new loadingDialog(profile_seller.this);
        seller.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.show();
                DataSnapshot sellerSnapshot = snapshot.getChildren().iterator().next();
                Admin admin=sellerSnapshot.getValue(Admin.class);
                nodeId=sellerSnapshot.getKey();
                txtSellerName.setText(admin.getAdminName());
                txtSellerEmail.setText(admin.getAdminEmail());
                txtSellerMobileNo.setText(admin.getAdminMobileNumber());
                Glide.with(getApplicationContext()).load(admin.getProfileImage()).into(profileIv);
                loading.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        store=sellerInfoNode.orderByChild("adminId").equalTo(user.getUid());
        store.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot sellerSnapshot = snapshot.getChildren().iterator().next();
                        String storeName=sellerSnapshot.child("storeName").getValue(String.class);
                        infoId=sellerSnapshot.getKey();
                        txtStoreName.setText(storeName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


        add_img_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImagePickerDialog();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                Map<String,Object> updateValues=new HashMap<>();
                updateValues.put("adminName",txtSellerName.getText().toString().trim());
                updateValues.put("adminMobileNumber",txtSellerMobileNo.getText().toString().trim());
                String storeName=txtStoreName.getText().toString().trim();
                imageUri=getUri();
                updateData(updateValues,imageUri,storeName,nodeId,infoId);
            }
        });

//        save_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userinfo u = new userinfo();
//                u.setUser_id(fuser.getUid());
//                u.setName(name_txt.getText().toString());
//                u.setNumber(number_txt.getText().toString());
//                u.setEmail(email_txt.getText().toString());
//                u.setNote(note_txt.getText().toString());
//                imageUri = getUri();
//                Log.d("image", "image uri is " + imageUri);
//                uploadToFirebase(imageUri, u);
//                startActivity(new Intent(getApplicationContext(), list.class));
//                finish();
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), list.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
    public void updateData(Map<String,Object> updateValues,Uri uri,String storeName,String nodeID,String infoID){
            storage=FirebaseStorage.getInstance();
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Admin").child(nodeID);
            DatabaseReference infoReference=FirebaseDatabase.getInstance().getReference("AdminStoreInformation").child(infoID);
            Map<String,Object> update=new HashMap<>();
            update.put("storeName",storeName);
            StorageReference profileImagesRef = storage.getReference().child("Profile Images");
            StorageReference upload = profileImagesRef.child("profileImage"+ UUID.randomUUID());
            upload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            loading.cancel();
                            updateValues.put("profileImage",uri.toString());
                            databaseReference.updateChildren(updateValues);
                            infoReference.updateChildren(update);
                            StyleableToast.makeText(getApplicationContext(),"Profile Updated Successfully",R.style.UptrendToast).show();
                        }
                    });
                }
            });
    }

    public Uri getUri() {
        Uri imageUri = null;
        // Assuming you have an ImageView called 'imageView' in your layout
        //ImageView imageView = findViewById(R.id.);
        Drawable drawable = profileIv.getDrawable();

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        FileOutputStream fileOutputStream;
        try {
            File file = new File(getCacheDir(), "image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            imageUri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("image", "image uri in method:" + imageUri);
        return imageUri;

    }

//    private void uploadToFirebase(Uri uri, userinfo u) {
//        storage = FirebaseStorage.getInstance();
//        StorageReference upload = storage.getReference(System.currentTimeMillis() + "." + getFileExtension(String.valueOf(uri)));
//        upload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        u.setImageurl(uri.toString());
//
//                        //push node create thy and u data j 6 te databse ma save thse
//                        database.push().setValue(u);
//                        Toast.makeText(createcontact.this, "Contact Saved Successfully", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//            }
//        });
//    }

    private void showImagePickerDialog() {

        //option for dialog
        String options[] = {"Camera", "Gallery"};

        //alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set title
        builder.setTitle("choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle item click
                if (which == 0) {
                    //camera selected
                    if (!checkCameraPermission()) {
                        //request camera permission
                        requestCameraPermission();

                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //gallery selected

                    if (!checkStoragePermission()) {
                        //request storage permission
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }

            }
        }).create().show();
    }

    //  activityResultLauncher code
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        profileIv.setImageURI(imageUri);
                    } else {
                        Toast.makeText(profile_seller.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickFromGallery() {
        //intent for taking image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/ *");
        // activityResultLauncher.launch(galleryIntent);

        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //content values for image  info

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAILS");

        //save imageUri

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to open camera

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }


    //check camera permission
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resul1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & resul1;
    }

    //request for camera permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE);
    }

    //check storage permission
    private boolean checkStoragePermission() {

        boolean resul1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resul1;
    }

    //request for camera permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0) {

                    //if all permission allowed return type ,otherwise false

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        //both permission granted

                        pickFromCamera();
                    } else {
                        //request not granted
                        Toast.makeText(getApplicationContext(), "Camera & Storage Permission Needed..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {

                    //if all permission allowed return true , otherwise false
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        //permission granted
                        pickFromGallery();
                    } else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Storage Permission Needed..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                //picked image from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                //picked image from camera
                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //crop image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profileIv.setImageURI(imageUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
        finish();
    }
}