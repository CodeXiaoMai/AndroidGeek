
package com.xiaomai.geek.ui.module.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/11 16:52.
 */

public class FileExploreActivity extends BaseActivity {

    public static final String EXTRA_FILE_PATH = "extra_file_path";

    private static final String TAG = "FileExploreActivity";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private File mCurrentFile;

    private File[] mFiles;

    private FileListAdapter mAdapter;

    private FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    };

    public static void launch(Context context) {
        context.startActivity(new Intent(context, FileExploreActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explore);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCurrentFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        actionBar.setSubtitle(mCurrentFile.getAbsolutePath());
        mFiles = mCurrentFile.listFiles(filenameFilter);
        sortFiles(mFiles);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FileListAdapter(Arrays.asList(mFiles));
        mAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if (mCurrentFile.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                        Snackbar.make(recyclerView, "已到达跟节点", Snackbar.LENGTH_LONG).show();
                    } else {
                        enterFolder(mCurrentFile.getParentFile());
                    }
                } else {
                    File file = mFiles[position - 1];
                    if (file.isDirectory()) {
                        enterFolder(file);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_FILE_PATH, file.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void enterFolder(File file) {
        getSupportActionBar().setSubtitle(file.getAbsolutePath());
        mFiles = file.listFiles();
        sortFiles(mFiles);
        mAdapter.setNewData(Arrays.asList(mFiles));
        mCurrentFile = file;
    }

    private void sortFiles(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if (file1.isDirectory() != file2.isDirectory()) {
                    if (file1.isDirectory())
                        return -1;
                    else
                        return 1;
                } else {
                    return file1.getName().compareToIgnoreCase(file2.getName());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
