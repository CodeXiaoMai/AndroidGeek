package com.xiaomai.geek.ui.module.password;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.presenter.password.FileExplorePresenter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.view.IFileExploreView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/11 16:52.
 */

public class FileExploreActivity extends BaseActivity implements IFileExploreView {

    public static final String EXTRA_TYPE = "extra_type";

    public static final int TYPE_FILE = 0x0001;

    public static final int TYPE_FOLDER = 0x0002;

    public static final String EXTRA_PATH = "extra_path";

    private static final String TAG = "FileExploreActivity";
    private static final int REQUEST_CODE_FILE_PERMISSION = 0x0003;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    FileExplorePresenter mPresenter = new FileExplorePresenter();
    private int mCurrentType = TYPE_FILE;
    private File mCurrentFile;
    private File[] mFiles;
    private FileListAdapter mAdapter;
    private String mRootPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explore);
        ButterKnife.bind(this);
        initData();
        initViews();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_FILE_PERMISSION);
        } else {
            mPresenter.scanStorage(mContext);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mCurrentType = intent.getIntExtra(EXTRA_TYPE, TYPE_FILE);
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView
                .addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).build());

        mPresenter.attachView(this);
    }

    private void enterFolder(File file) {
        getSupportActionBar().setSubtitle(file.getAbsolutePath());
        if (file.canRead()) {
            mFiles = file.listFiles();
            sortFiles(mFiles);
            mAdapter.setNewData(Arrays.asList(mFiles));
        } else {
            mAdapter.setNewData(null);
        }
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
        if (mCurrentType == TYPE_FOLDER) {
            getMenuInflater().inflate(R.menu.explore_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_ok:
                if (mCurrentFile != null) {
                    selectFolder(mCurrentFile.getAbsolutePath());
                } else {
                    Snackbar.make(recyclerView, "请选择目录", Snackbar.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectFolder(String filePath) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PATH, filePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showSelectStorageView(final List<String> storages) {
        if (storages.size() == 1) {
            mRootPath = storages.get(0);
            showFiles(mRootPath);
        } else if (storages.size() > 1) {
            final StorageListAdapter adapter = new StorageListAdapter(storages);
            if (mCurrentType == TYPE_FOLDER) {
                TextView footerView = new TextView(mContext);
                footerView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                footerView.setText(
                        "由于系统限制，外置SD卡只能存储到Android/data/" + getPackageName() + "/目录下，且数据会在应用卸载时删除！");
                footerView.setTextColor(getResources().getColor(R.color.colorAccent));
                footerView.setPadding(10, 10, 10, 10);
                footerView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                adapter.addFooterView(footerView);
            }
            adapter.setOnRecyclerViewItemClickListener(
                    new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                            /**
                             * 如果是选择文件夹，那么SD卡只支持android/data/包名
                             */
                            if (mCurrentType == TYPE_FOLDER && !storages.get(i).equals(
                                    Environment.getExternalStorageDirectory().getAbsolutePath())) {
                                String path = storages.get(i) + "/Android/data/" + getPackageName()
                                        + "/" + "files";
                                selectFolder(path);
                            } else {
                                mRootPath = storages.get(i);
                                showFiles(mRootPath);
                            }
                        }
                    });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showFiles(String path) {
        mCurrentFile = new File(path);
        toolBar.setSubtitle(mCurrentFile.getAbsolutePath());
        mFiles = mCurrentFile.listFiles();
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
                        if (mCurrentType == TYPE_FILE) {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_PATH, file.getAbsolutePath());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Snackbar.make(recyclerView, "请选择一个文件夹目录", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.scanStorage(mContext);
        } else {
            Snackbar.make(recyclerView, "您没有授予访问文件的权限，请重试！", Snackbar.LENGTH_LONG).show();
        }
    }
}
