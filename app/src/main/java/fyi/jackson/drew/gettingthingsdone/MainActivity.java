package fyi.jackson.drew.gettingthingsdone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.DummyData;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.recycler.TaskAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String INTENT_BUCKET_NAME = "INTENT_BUCKET_NAME";

    private FloatingActionButton fab;
    private NavigationView navigationView;
    private RecyclerView rvTaskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_process:
                Snackbar.make(fab, "Process Action Selected", Snackbar.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupComponents() {
        setupToolbarAndDrawer();
        setupFab();
        setupRecycler();
    }

    private void setupToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        String bucketName = item.getIntent().getStringExtra(INTENT_BUCKET_NAME);

                        Snackbar.make(fab, bucketName + " selected", Snackbar.LENGTH_LONG).show();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                };

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        fillMenu(DummyData.buckets);
    }

    private void setupFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupRecycler() {
        rvTaskList = findViewById(R.id.rv_task_list);

        taskAdapter = new TaskAdapter(DummyData.tasks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvTaskList.setAdapter(taskAdapter);
        rvTaskList.setLayoutManager(layoutManager);
    }

    // This method dynamically fills the drawer menu based on the Buckets data
    private void fillMenu(List<Bucket> buckets) {
        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu("Buckets");

        for (int i = 0; i < buckets.size(); i++) {
            Bucket bucket = buckets.get(i);
            String bucketName = bucket.getName();
            Integer iconId = bucket.getIconId();

            if (iconId == null) iconId = R.drawable.ic_folder_black_24dp;

            Intent menuItemIntent = new Intent();
            menuItemIntent.putExtra(INTENT_BUCKET_NAME, bucketName);

            subMenu.add(0, Menu.FIRST + i, Menu.FIRST, bucketName)
                    .setCheckable(true)
                    .setIntent(menuItemIntent)
                    .setIcon(iconId);
        }
    }

}
