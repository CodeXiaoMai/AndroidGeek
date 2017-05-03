
package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.Repo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/3/17 16:59.
 */

public class RepoItemView extends FrameLayout {

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.desc)
    TextView desc;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.owner)
    TextView owner;

    @BindView(R.id.update_time)
    TextView updateTime;

    @BindView(R.id.star)
    TextView star;

    @BindView(R.id.label_view)
    LabelView labelView;

    @BindView(R.id.startIcon)
    ImageView startIcon;

    @BindView(R.id.star_view)
    LinearLayout starView;

    private Repo mRepo;

    public RepoItemView(Context context) {
        super(context);
        init();
    }

    public RepoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RepoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_repo, this);
        ButterKnife.bind(this);
    }

    public void setRepo(Repo repo) {
        this.mRepo = repo;
        name.setText(repo.getName());
        desc.setText(repo.getDescription());

        ImageLoader.load(getContext(), repo.getOwner().getAvatar_url(), image, R.drawable.github_blue);
        owner.setText(repo.getOwner().getLogin());

        if (!TextUtils.isEmpty(repo.getLanguage())) {
            labelView.setVisibility(VISIBLE);
            labelView.setText(repo.getLanguage());
        } else {
            labelView.setVisibility(GONE);
        }

        updateTime.setText(repo.getUpdated_at());
        star.setText(String.valueOf(repo.getStargazers_count()));

        startIcon.setImageResource(
                repo.isStarred() ? R.drawable.ic_star_selected : R.drawable.ic_star);
    }

    private onRepoActionListener mOnRepoActionListener;

    public void setOnRepoActionListener(onRepoActionListener listener) {
        mOnRepoActionListener = listener;
    }

    @OnClick(R.id.star_view)
    public void onClick() {
        if (mOnRepoActionListener != null) {
            if (mRepo.isStarred())
                mOnRepoActionListener.onUnStarAction(mRepo);
            else
                mOnRepoActionListener.onStarAction(mRepo);
        }
    }

    @OnClick(R.id.image)
    public void onUserIconClick() {
        if (mOnRepoActionListener != null) {
            mOnRepoActionListener.onUserAction(mRepo.getOwner().getLogin());
        }
    }

    public void setStarred(boolean starred) {
        mRepo.setStarred(starred);
        startIcon.setImageResource(starred ? R.drawable.ic_star_selected : R.drawable.ic_star);
    }

    public interface onRepoActionListener {
        void onStarAction(Repo repo);

        void onUnStarAction(Repo repo);

        void onUserAction(String userName);
    }
}
