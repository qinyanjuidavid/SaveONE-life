package com.wyksofts.saveone.ui.homeUI.MainPage;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.wyksofts.saveone.Adapters.Orphanages.Orphanage.OrphanageListAdapter;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.BuildConfig;
import com.wyksofts.saveone.Interface.OrphanageViewInterface;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Orphanage.Orphanage.OrphanageModel;
import com.wyksofts.saveone.ui.Others.AboutApp;
import com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo.DetailedActivity;
import com.wyksofts.saveone.ui.homeUI.Settings;
import com.wyksofts.saveone.ui.profile.ProfileHolder;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.sendMail;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.NoAccountFound;
import com.wyksofts.saveone.util.Constants.Constants;
import com.wyksofts.saveone.util.ShareApp;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment implements OrphanageViewInterface {

    private List<OrphanageModel> listdata;
    FirebaseUser user;
    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private OrphanageListAdapter adapter;
    private EditText search;
    StorageReference storageRef;

    private LinearLayout no_result_found,loading_orphanage;
    private Button search_again, retry_connecting;

    TextView orphanage_name;
    ImageView menu;

    LinearLayout home_menu, menu_settings, menu_profile, menu_contact, menu_share,menu_about;
    PopupWindow popupWindow;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    //view
    View view;


    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        this.view = view;

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //shared preference
        pref = getContext().getSharedPreferences("OrphanageDetailedData", 0);
        editor = pref.edit();

        recyclerView = view.findViewById(R.id.recyclerView);
        loading_orphanage = view.findViewById(R.id.loading_orphanage);
        search = view.findViewById(R.id.search_orphanage);
        no_result_found = view.findViewById(R.id.no_result_found);
        search_again = view.findViewById(R.id.search_again);
        orphanage_name = view.findViewById(R.id.welcome_text);
        menu = view.findViewById(R.id.menu);

        //init UI
        initUI();

        //adapter
        adapter = new OrphanageListAdapter(listdata,getContext(),this);

        return view;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSearch();//init search

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMenuLayout(view);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //init UI
    private void initUI(){

        if (user != null) {
            //show username i=on the dashboard
            String username = user.getDisplayName();
            orphanage_name.setText(username);
        }else{
            orphanage_name.setText(R.string.gtsy);
        }

        GridLayoutManager layout = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layout);

        recyclerView.addItemDecoration(new LayoutMarginDecoration(2, 2));
        listdata = new ArrayList<OrphanageModel>();
        database = FirebaseFirestore.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        getData();//getdata method from firebase

        initSearch();
    }


    //init search
    private void initSearch() {
        search.setEnabled(true);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    //init filter
    public void filter(String search_data) {
        ArrayList<OrphanageModel> arrayList = new ArrayList<>();
        for (OrphanageModel model : listdata) {

            if (model.getName().toLowerCase().contains(search_data)
                    || model.getWhat_needed().toLowerCase().contains(search_data)
            ||model.getLocation().toLowerCase().contains(search_data)) {

                recyclerView.setVisibility(View.VISIBLE);
                no_result_found.setVisibility(View.GONE);
                arrayList.add(model);

            } else {
                if (arrayList.isEmpty()) {
                    no_result_found.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    no_result_found.setVisibility(View.GONE);
                }
            }
            if (search_data.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                no_result_found.setVisibility(View.GONE);
            }

            adapter.upDateList(arrayList);
        }

        search_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
    }


    //PopupWindow display method
    @SuppressLint("SetTextI18n")
    public void showMenuLayout(View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.navigation_menu, null);

        popupView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1,
                                       int i2, int i3, int i4, int i5, int i6, int i7) {
                showMenuNav(popupView);
            }
        });


        //length and width
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);


        //Initialize the elements of the menu
        home_menu = popupView.findViewById(R.id.home_menu);
        menu_settings = popupView.findViewById(R.id.menu_settings);
        menu_profile = popupView.findViewById(R.id.menu_profile);
        menu_contact = popupView.findViewById(R.id.menu_contact_us);
        menu_share = popupView.findViewById(R.id.menu_share);
        menu_about = popupView.findViewById(R.id.menu_about);

        TextView app_version = popupView.findViewById(R.id.app_version);

        app_version.setText("V"+BuildConfig.VERSION_NAME);

        //set on touch listeners
        home_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMenuNav(popupView);
            }
        });

        menu_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareApp().shareApp(getContext());
            }
        });

        menu_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new sendMail().contact_us(getContext(), Constants.EMAIL);
            }
        });

        menu_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AboutApp.class));
            }
        });

        menu_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Settings.class));
            }
        });

        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user !=null){
                    startActivity(new Intent(getContext(), ProfileHolder.class));
                }else{
                    new NoAccountFound().showCreateAccountDialog(getContext());
                }
            }
        });


        //close if other parts of the window are touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                dismissMenuNav(popupView);
                return true;
            }
        });
    }

    //show menu nav
    private void showMenuNav(View popupView) {

        int cx = popupView.getWidth();
        int cy = popupView.getHeight();

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);


        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(popupView,
                cx, cy, 1, finalRadius);

        // make the view visible and start the animation
        popupView.setVisibility(View.VISIBLE);
        anim.start();
    }

    //dismiss menu nav
    @SuppressLint("ObsoleteSdkInt")
    public void dismissMenuNav(View popupView) {
        // Check if the runtime version is at least Lollipop and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // get the center for the clipping circle
            int cx = popupView.getWidth();
            int cy = popupView.getHeight();


            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(popupView, cx, cy,
                    initialRadius, 0f);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //popupView.setVisibility(View.INVISIBLE);
                    popupWindow.dismiss();
                }
            });
            // start the animation
            anim.start();

        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            //popupView.setVisibility(View.VISIBLE);
            popupWindow.dismiss();
        }
    }


    //get orphanage data
    public void getData(){

        loading_orphanage.setVisibility(View.VISIBLE);

        database.collection("Orphanage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            loading_orphanage.setVisibility(View.GONE);

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String name = document.getString("name");
                                String bank_account = document.getString("bank_account");
                                String bank_account_name = document.getString("bank_name");
                                String till_number = document.getString("till_number");
                                String coordinates = document.getString("coordinates");
                                String country = document.getString("country");
                                String description = document.getString("description");
                                String location = document.getString("location");
                                String number_of_children = document.getString("number_of_children");
                                String phone_number = document.getString("phone_number");
                                String email = document.getString("email");
                                String verified = document.getString("verified");
                                String what_needed = document.getString("in_need_of");
                                String url = document.getString("url");

                                listdata.add(new OrphanageModel(name,location, coordinates,
                                        country,  description, number_of_children,
                                        phone_number, bank_account,bank_account_name,
                                        till_number, url, email,verified,what_needed));

                                recyclerView.setAdapter(adapter);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }



    @Override
    public void onOrphanageClicked(String name, String location, String coordinates, String country,
                                   String description, String number_of_children, String phone_number,
                                   String bank_account, String bank_account_name, String till_number,
                                   String email, String verified, String what_needed, TextView text) {

        //store card view data
        editor.putString("name", name);
        editor.putString("location", location);
        editor.putString("coordinates", coordinates);
        editor.putString("country", country);
        editor.putString("description", description);
        editor.putString("number_of_children", number_of_children);
        editor.putString("phone_number", phone_number);
        editor.putString("bank_account", bank_account);
        editor.putString("bank_account_name", bank_account_name);
        editor.putString("till_number", till_number);
        editor.putString("email", email);
        editor.putString("what_needed", what_needed);
        editor.putString("verified",verified);
        editor.commit();

        //open detailed fragment here and pass this values
        startActivity(new Intent(getContext(), DetailedActivity.class));
    }

    @Override
    public void onNavigationItemSelected(String name) {
        //open map
        ViewPager viewPager = getActivity().findViewById(R.id.pager);
        viewPager.setCurrentItem(1);
    }

}