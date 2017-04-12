
package com.xiaomai.geek.ui.module.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.presenter.FileExplorePresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.view.IFileExploreView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/11 16:52.
 */

public class FileExploreActivity extends BaseActivity implements IFileExploreView {

    public static final String EXTRA_FILE_PATH = "extra_file_path";

    private static final String TAG = "FileExploreActivity";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private File mCurrentFile;

    private File[] mFiles;

    private FileListAdapter mAdapter;

    private String mRootPath;

    private FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    };

    FileExplorePresenter mPresenter = new FileExplorePresenter();

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

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mPresenter.attachView(this);
        mPresenter.scanStorage(mContext);

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

    @Override
    public void showSelectStorageView(final List<String> storages) {
        if (storages.size() == 1) {
            mRootPath = storages.get(0);
            showFiles(mRootPath);
        } else if (storages.size() > 1) {
            StorageListAdapter adapter = new StorageListAdapter(storages);
            adapter.setOnRecyclerViewItemClickListener(
                    new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            mRootPath = storages.get(i);
                            showFiles(mRootPath);
                        }
                    });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showFiles(String path) {
        mCurrentFile = new File(path);
        toolBar.setSubtitle(mCurrentFile.getAbsolutePath());
        mFiles = mCurrentFile.listFiles(filenameFilter);
        sortFiles(mFiles);

        mAdapter = new FileListAdapter(Arrays.asList(mFiles));
        mAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if (mCurrentFile.getAbsolutePath().equals(mRootPath)) {
                        mPresenter.scanStorage(mContext);
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
}
