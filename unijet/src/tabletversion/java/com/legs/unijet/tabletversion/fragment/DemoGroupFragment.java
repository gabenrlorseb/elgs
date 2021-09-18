 package com.legs.unijet.tabletversion.fragment;

 import android.app.ProgressDialog;
 import android.graphics.Bitmap;
 import android.net.Uri;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.fragment.app.Fragment;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.android.material.appbar.CollapsingToolbarLayout;
 import com.google.android.material.floatingactionbutton.FloatingActionButton;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;
 import com.legs.unijet.smartphone.R;
 import com.legs.unijet.tabletversion.course.Course;
 import com.legs.unijet.tabletversion.group.Group;
 import com.legs.unijet.tabletversion.post.PostAdapter;
 import com.legs.unijet.tabletversion.post.PostSample;
 import com.legs.unijet.tabletversion.utils.BachecaUtils;

 import java.util.ArrayList;

 public class DemoGroupFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    String groupUID;
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;
     RecyclerView recyclerViewBacheca;
     private PostAdapter postAdapter;
     private ArrayList<PostSample> fetchedPosts;
     BachecaUtils postFetcher;
     ProgressDialog dialog;
     TextView rating;
     RelativeLayout postLayout;
     EditText postNow;
     String usertype;

     String name;
     String subtitle;







    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
    }

     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.collapsing_toolbar_layout_sample, container, false);
         final Bundle args = this.getArguments();

         final Object[] element = new Object[0];

         if (args != null) {
             name = args.getString("titleName");
             subtitle = args.getString("subtitle");
         }



         postLayout = view.findViewById(R.id.area_post);
         postNow = view.findViewById(R.id.post_update_editText);

         postLayout.setVisibility(View.GONE);

         final int[] NumberOfMembers = new int[1];

        isAuthor = false;

        storageReference = FirebaseStorage.getInstance().getReference();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();


         final TextView demoModeActive =view.findViewById(R.id.demo_mode_textview);
         demoModeActive.setVisibility(View.VISIBLE);

         FloatingActionButton fab = view.findViewById(R.id.common_fab);
         fab.setVisibility(View.GONE);



        database.child(args.getString("type")).orderByChild("name").equalTo(args.getString("titleName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {




                    Group element = postSnapshot.getValue(Group.class);

                    groupUID = postSnapshot.getKey();


                    final String[] groupAuthorName = new String[1];


                    CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(element.getName());

                    TextView subtitleTextView = view.findViewById(R.id.toolbar_subtitle);

                    collapsingToolbar.setTitle(name);
                    subtitleTextView.setText(subtitle);

                    rating = view.findViewById(R.id.toolbar_additional_infos);
                    rating.setVisibility(View.GONE);

                    ImageView ratingStar = view.findViewById(R.id.comments_image);
                    ratingStar.setVisibility(View.GONE);

                    TextView demoMode = view.findViewById(R.id.demo_mode_textview);
                    demoMode.setVisibility(View.VISIBLE);


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });





        return view;

     }

 }


