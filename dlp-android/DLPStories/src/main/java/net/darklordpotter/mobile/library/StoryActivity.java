
package net.darklordpotter.mobile.library;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;
import net.darklordpotter.mobile.library.adapter.ViewPagerAdapter;
import net.darklordpotter.mobile.library.api.Story;
import net.darklordpotter.mobile.library.rest.RestClient;

import java.util.List;

@EActivity(R.layout.activity_main)
public class StoryActivity
    extends SherlockFragmentActivity
    implements TabListener,ViewPager.OnPageChangeListener
{

//    @ViewById
//    TextView hello;
//
//    @ViewById
//    ListView listView;

    @ViewById
    ViewPager pager;


    @RestService
    RestClient restClient;
    private String[] locations;

    @AfterViews
    void afterViews() {
        locations = getResources().getStringArray(R.array.locations);
        configureActionBar();
        configureViewPager();

//
//        ProgressBar bar = new ProgressBar(this);
//        bar.setIndeterminate(true);
//
//        listView.setEmptyView(bar);



        doSomethingInBackground();
    }

    @UiThread
    void doSomethingElseOnUiThread(Story[] stories) {
        List<String> storyTitles = Lists.transform(Lists.newArrayList(stories), new Function<Story, String>() {
            @Override
            public String apply(Story input) {
                return input.getTitle() + " by " + input.getAuthor();
            }
        });

//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storyTitles));
    }

    @Background
    void doSomethingInBackground() {
        doSomethingElseOnUiThread(restClient.main());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater();
        return true;
    }

    private void configureActionBar() {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (String location: locations) {
            Tab tab = getSupportActionBar().newTab();
            tab.setText(location);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }
    }

    private void configureViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), locations);
        pager.setAdapter(viewPagerAdapter);
        pager.setOnPageChangeListener(this);

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        int position = tab.getPosition();
        pager.setCurrentItem(position);

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    static class StoryArrayAdapter extends ArrayAdapter<Story> {
        StoryArrayAdapter(Context context, int textViewResourceId, Story[] objects) {
            super(context, textViewResourceId, objects);
        }


    }
}
