package fyi.jackson.drew.gettingthingsdone;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.AppDatabase;
import fyi.jackson.drew.gettingthingsdone.data.AppViewModel;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.data.entities.Task;
import fyi.jackson.drew.gettingthingsdone.recycler.TaskAdapter;
import fyi.jackson.drew.gettingthingsdone.recycler.helpers.TaskItemTouchHelperCallback;
import fyi.jackson.drew.gettingthingsdone.recycler.helpers.OnStartDragListener;
import fyi.jackson.drew.gettingthingsdone.ui.Helpers;
import fyi.jackson.drew.gettingthingsdone.ui.NewTaskDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String INTENT_MENU_METHOD = "INTENT_MENU_METHOD";
    private static final String INTENT_SELECT_BUCKET = "INTENT_SELECT_BUCKET";
    private static final String INTENT_CREATE_BUCKET = "INTENT_CREATE_BUCKET";
    private static final String INTENT_BUCKET_NAME = "INTENT_BUCKET_NAME";
    private static final int MENU_GROUP_ID_BUCKETS = 667;

    private NavigationView navigationView;
    private RecyclerView rvTaskList;
    private TaskAdapter taskAdapter;
    private ItemTouchHelper itemTouchHelper;

    private AppViewModel viewModel;
    private AppDatabase appDatabase;

    private NewTaskDialog newTaskDialog;

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
                Snackbar.make(newTaskDialog.fab, "Process Action Selected", Snackbar.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupComponents() {
        setupViewModel();
        setupToolbarAndDrawer();
        setupFab();
        setupRecycler();
    }

    private void setupViewModel() {
        viewModel = new AppViewModel();
        appDatabase = AppDatabase.getInstance(this);
        viewModel.getItemsAndBuckets(appDatabase.taskDao()).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                taskAdapter.updateTaskList(tasks);
            }
        });
        viewModel.getBuckets(appDatabase.bucketDao()).observe(this, new Observer<List<Bucket>>() {
            @Override
            public void onChanged(@Nullable List<Bucket> buckets) {
                fillMenu(buckets);
            }
        });
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
                        switch (item.getIntent().getStringExtra(INTENT_MENU_METHOD)) {
                            case INTENT_SELECT_BUCKET:
                                String bucketName = item.getIntent().getStringExtra(INTENT_BUCKET_NAME);
                                Snackbar.make(newTaskDialog.fab, bucketName + " selected", Snackbar.LENGTH_LONG).show();
                                newTaskDialog.hideDialog();
                                break;
                            case INTENT_CREATE_BUCKET:
                                newTaskDialog.setModeBucket();
                                newTaskDialog.showDialog();
                                break;
                        }
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                };

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void setupFab() {
        newTaskDialog = new NewTaskDialog(this);
        newTaskDialog.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newTaskDialog.isVisible()) {
                    String inputString = newTaskDialog.getText().toString();
                    newTaskDialog.setText("");

                    if (!inputString.equals("")){
                        switch (newTaskDialog.getMode()) {
                            case NewTaskDialog.MODE_NEW_TASK:
                                Task task = new Task();
                                task.setName(inputString);
                                task.setBucket("Inbox");
                                viewModel.createTask(task, appDatabase.taskDao());
                                break;
                            case NewTaskDialog.MODE_NEW_BUCKET:
                                Bucket bucket = new Bucket(inputString, null);
                                viewModel.createBucket(bucket, appDatabase.bucketDao());
                                break;
                        }
                    }
                    newTaskDialog.clearFocus();
                } else {
                    newTaskDialog.requestFocus();
                }
                newTaskDialog.showHideDialog();
            }
        });
    }

    private void setupRecycler() {
        rvTaskList = findViewById(R.id.rv_task_list);

        taskAdapter = new TaskAdapter(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvTaskList.setAdapter(taskAdapter);
        rvTaskList.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new TaskItemTouchHelperCallback(taskAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvTaskList);
    }

    // This method dynamically fills the drawer menu based on the Buckets data
    private void fillMenu(List<Bucket> buckets) {
        Helpers.organizeBuckets(buckets);

        Menu menu = navigationView.getMenu();
        menu.clear();
        SubMenu subMenu = menu.addSubMenu("Buckets");

        int i;
        for (i = 0; i < buckets.size(); i++) {
            Bucket bucket = buckets.get(i);
            String bucketName = bucket.getName();
            Integer iconId = bucket.getIconId();

            if (iconId == null) iconId = R.drawable.ic_folder_black_24dp;

            Intent menuItemIntent = new Intent();
            menuItemIntent.putExtra(INTENT_MENU_METHOD, INTENT_SELECT_BUCKET);
            menuItemIntent.putExtra(INTENT_BUCKET_NAME, bucketName);

            subMenu.add(MENU_GROUP_ID_BUCKETS, Menu.FIRST + i, Menu.FIRST, bucketName)
                    .setCheckable(true)
                    .setIntent(menuItemIntent)
                    .setIcon(iconId);
        }

        // Add a final menu item for adding new buckets
        Intent createBucketIntent = new Intent();
        createBucketIntent.putExtra(INTENT_MENU_METHOD, INTENT_CREATE_BUCKET);

        subMenu.add(MENU_GROUP_ID_BUCKETS, Menu.FIRST + i, Menu.FIRST, "Create Bucket")
                .setCheckable(false)
                .setIntent(createBucketIntent)
                .setIcon(R.drawable.ic_add_black_24dp);

    }

}
