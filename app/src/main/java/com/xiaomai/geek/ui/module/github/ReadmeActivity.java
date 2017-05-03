package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mukesh.MarkdownView;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.net.response.Content;
import com.xiaomai.geek.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/2.
 */

public class ReadmeActivity extends BaseActivity {

    private static final String EXTRA_README = "extra_readme";
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.readme_content)
    MarkdownView readmeContent;

    public static void launch(Context context, Content content) {
        Intent intent = new Intent(context, ReadmeActivity.class);
        intent.putExtra(EXTRA_README, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readme);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        Content readme = intent.getParcelableExtra(EXTRA_README);
        if (readme != null)
            readmeContent.setMarkDownText(readme.content);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void initViews() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Readme");
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
