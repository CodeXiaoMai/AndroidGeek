package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseLoadActivity;

public class RepoDetailActivity extends BaseLoadActivity {

    private static final String EXTRA_OWNER = "extra_owner";

    private static final String EXTRA_REPO_NAME = "extra_repo_name";

    public static void launch(Context context, String owner, String repoName) {
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(EXTRA_OWNER, owner);
        intent.putExtra(EXTRA_REPO_NAME, repoName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
    }
}
