package com.example.madproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Blog stuff
    private EditText blogTitle;
    private EditText blogStory;
    private ImageButton blogImage;
    private Button publishBlog;
    private StorageReference storageRef;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;
    private DatabaseReference dbUsersRef;

    private final int IMAGE_REQUEST = 1;
    static int reqCode=1;
    private Uri uri;

    public BlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlogFragment newInstance(String param1, String param2) {
        BlogFragment fragment = new BlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_blog, container, false);

        //Instantiate variables to fetch input
        blogTitle = rootView.findViewById(R.id.blogTitle);
        blogStory = rootView.findViewById(R.id.blogStory);
        blogImage = rootView.findViewById(R.id.blogImage);
        publishBlog = rootView.findViewById(R.id.publishBlog);
        storageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("blog");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbUsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        //Image button Listener
        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=22){
                    checkAndRequestPermission();
                }else{
                    openGallery();
                }
            }
        });
        // Store blog post
        publishBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Publishing", Toast.LENGTH_SHORT).show();
                //Fetch input values
                String blog_title = blogTitle.getText().toString().trim();
                String blog_story = blogStory.getText().toString().trim();

                //fetch current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat cDate = new SimpleDateFormat("MM/dd/yyyy");
                final String currentDate = cDate.format(calendar.getTime());

                Calendar calendar2 = Calendar.getInstance();
                SimpleDateFormat cTime = new SimpleDateFormat("HH:mm");
                final String currentTime = cTime.format(calendar2.getTime());

                if (!TextUtils.isEmpty(blog_story) && (!TextUtils.isEmpty(blog_title))) {

                    //declare storage path for blog images
                    StorageReference path = storageRef.child("blog_images").child(uri.getLastPathSegment());
                    path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    //get download url
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final String imageUrl = uri.toString();
                                            Toast.makeText(getActivity(), "Published successfully", Toast.LENGTH_SHORT).show();
                                            final DatabaseReference blog = dbRef.push();

                                            dbUsersRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    blog.child("blogTitle").setValue(blog_title);
                                                    blog.child("blogStory").setValue(blog_story);
                                                    blog.child("blogImage").setValue(imageUrl);
                                                    blog.child("uid").setValue(currentUser.getUid());
                                                    blog.child("uploadTime").setValue(currentTime);
                                                    blog.child("uploadDate").setValue(currentDate);

                                                    blog.child("profilePhoto").setValue(snapshot.child("profilephoto").getValue());
                                                    blog.child("displayName").setValue(snapshot.child("displayName").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError error) {
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        }
                    });
                }
            }




        });

        return rootView;
    }
    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(getActivity(),"Please accept required permission",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},reqCode);
            }
        }else
            openGallery();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BlogFragment.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null){
            uri = data.getData();
            blogImage.setImageURI(uri);


        }
    }
    private void openGallery(){
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent,IMAGE_REQUEST);
    }
}