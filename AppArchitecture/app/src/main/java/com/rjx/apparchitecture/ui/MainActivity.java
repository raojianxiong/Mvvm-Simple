package com.rjx.apparchitecture.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.rjx.apparchitecture.R;
import com.rjx.apparchitecture.databinding.ActivityMainBinding;
import com.rjx.apparchitecture.fragments.AccountFragment;
import com.rjx.apparchitecture.fragments.CategoryFragment;
import com.rjx.apparchitecture.fragments.ServiceFragment;
import com.rjx.base.activity.MvvmActivity;
import com.rjx.base.viewmodel.MvvmBaseViewModel;
import com.rjx.common.arouter.RouteServiceManager;
import com.rjx.common.arouter.news.INewsService;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;

/**
 * @author Jianxiong Rao
 */
public class MainActivity extends MvvmActivity<ActivityMainBinding, MvvmBaseViewModel> {
    private INewsService iNewsService;
    private Fragment mHomeFragment ;
    private CategoryFragment mCategoryFragment = new CategoryFragment();
    private ServiceFragment mServiceFragment = new ServiceFragment();
    private AccountFragment mAccountFragment = new AccountFragment();
    Fragment fromFragment;
    public static final int REQUEST_INTERNET_PERMISSION = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iNewsService = RouteServiceManager.provide(INewsService.class,INewsService.NEWS_SERVICE);
        if(iNewsService != null){
            mHomeFragment = iNewsService.getHeadlineNewFragment();
        }

        fromFragment = mHomeFragment;

        //set toolbar
        setSupportActionBar(viewDataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.menu_home);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(viewDataBinding.bottomView);
        }
        viewDataBinding.bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                // init corresponding fragment
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        fragment = mHomeFragment;
                        break;
                    case R.id.menu_categories:
                        fragment = mCategoryFragment;
                        break;
                    case R.id.menu_services:
                        fragment = mServiceFragment;
                        break;
                    case R.id.menu_account:
                        fragment = mAccountFragment;
                        break;
                    default:
                        break;
                }
                //Set bottom menu selected item text in toolbar
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(menuItem.getTitle());
                }
                if(fragment == null){
                    return true;
                }
                switchFragment(fromFragment, fragment);
                fromFragment = fragment;
                return  true;
            }
        });
        //新api去除动画的方法
        viewDataBinding.bottomView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},REQUEST_INTERNET_PERMISSION);
            return;
        }
        initViews();
    }

    private void initViews() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mHomeFragment, mHomeFragment.getClass().getSimpleName());
        transaction.commit();
        showBadgeView(3, 5);
    }

    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex  tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) viewDataBinding.bottomView.getChildAt(0);
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(viewIndex);
            // 从子tab中获得其中显示图片的ImageView
            View icon = view.findViewById(com.google.android.material.R.id.icon);
            // 获得图标的宽度
            int iconWidth = icon.getWidth();
            // 获得tab的宽度/2
            int tabWidth = view.getWidth() / 2;
            // 计算badge要距离右边的距离
            int spaceWidth = tabWidth - iconWidth;

            // 显示badegeview
            new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth + 50, 13, false).setBadgeNumber(showNumber);
        }
    }
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.add(R.id.container, to, to.getClass().getName()).commit();
                }

            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.show(to).commit();
                }

            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                // item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_INTERNET_PERMISSION){
            if(permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED ){
                initViews();
            }
        }
    }
}
