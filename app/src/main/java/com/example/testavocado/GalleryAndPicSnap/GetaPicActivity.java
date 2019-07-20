package com.example.testavocado.GalleryAndPicSnap;

import android.content.Intent;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.Utils.SectionPagerAdapter;

public class GetaPicActivity extends AppCompatActivity implements  onSelectedImageListener{
    private static final String TAG = "GetaPicActivity";



    @Override
    public void onSelectedUri(Uri uri) {
        Intent intent=new Intent();
        intent.putExtra(getString(R.string.uri),uri);
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    public void onSelectedPathImage(String imagePath) {
        Log.d(TAG, "onSelectedPathImage: "+imagePath);
        Intent intent=new Intent();
        intent.putExtra(getString(R.string.imagePath),imagePath);
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    public void onCancel() {
        finish();
    }

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geta_pic);

        setupViewPager();
    }


    public int getCurrentTab(){
        return viewPager.getCurrentItem();
    }




    //setup view pager
    private void setupViewPager(){
        SectionPagerAdapter adapter=new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
      //  adapter.addFragment(new PhotoFragment());
        adapter.addFragment(new CameraFragment());

        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("gallery");
        tabLayout.getTabAt(1).setText("photo");


    }



}
