package com.jigpud.snow.page.moments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.jigpud.snow.databinding.MomentsBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.page.newstory.NewStoryActivity;
import com.jigpud.snow.page.recommendsocialgroup.RecommendSocialGroupActivity;
import com.jigpud.snow.page.recommendtourismtalent.RecommendTourismTalentActivity;
import com.jigpud.snow.page.search.SearchActivity;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.page.userprofile.UserProfileActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MomentsFragment extends BaseFragment<MomentsBinding> implements StoryListAdapter.StoryClickListener {
    private static final String TAG = "MomentsFragment";

    private MomentsViewModel momentsViewModel;
    private RecommendUserListAdapter recommendUserListAdapter;
    private FollowedAttractionListAdapter followedAttractionListAdapter;
    private MomentsStoryListAdapter momentsStoryListAdapter;
    private boolean isRefreshingRecommendUser;
    private boolean isRefreshingFollowedAttraction;
    private boolean isRefreshingMomentsStory;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        momentsViewModel = getViewModel(MomentsViewModel.class, MomentsViewModelFactory.create());

        recommendUserListAdapter = new RecommendUserListAdapter(this::onUserClick);

        followedAttractionListAdapter = new FollowedAttractionListAdapter(this::onAttractionClick);

        momentsStoryListAdapter = new MomentsStoryListAdapter(MomentsViewModel.MOMENTS_STORY_LIST_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.moreRecommendUser.setOnClickListener(this::onMoreTourismTalentClick);

        binding.moreSocialGroup.setOnClickListener(this::onMoreSocialGroupClick);

        binding.searchBar.search.setKeyListener(null);
        binding.searchBar.search.setFocusable(false);
        binding.searchBar.search.setOnClickListener(this::onSearchBarClick);
        binding.searchBar.getRoot().setOnClickListener(this::onSearchBarClick);

        binding.recommendUserList.setAdapter(recommendUserListAdapter);
        binding.recommendUserList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.followedAttractionList.setAdapter(followedAttractionListAdapter);
        binding.followedAttractionList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.followedAttractionList.addItemDecoration(new GridSpacingItemDecoration(8));

        binding.hotMomentsList.setAdapter(momentsStoryListAdapter);
        binding.hotMomentsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.hotMomentsList.setItemAnimator(null);

        binding.newStory.setOnClickListener(this::onNewStoryClick);

        binding.moments.setOnRefreshListener(this::onRefresh);
        binding.moments.setOnLoadMoreListener(this::onLoadMore);
        binding.moments.setLoadMoreEnabled(false);

        autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        useLightStatusBar();
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(requireContext(), StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(momentsViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(momentsViewModel.getStory(storyId), momentsStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(momentsViewModel.unlikeStory(storyId), unlikeStoryStatus -> {
            if (unlikeStoryStatus.first) {
                observeNotNull(momentsViewModel.getStory(storyId), momentsStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onAuthorClick(String authorId) {
        Intent intent = new Intent(requireContext(), UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, authorId);
        startActivity(intent);
    }

    private void onRefresh() {
        isRefreshingRecommendUser = true;
        observeNotNull(momentsViewModel.getRecommendUserList(), recommendUserList -> {
            isRefreshingRecommendUser = false;
            updateRefreshState();
            recommendUserListAdapter.setRecords(recommendUserList);
        });

        isRefreshingFollowedAttraction = true;
        observeNotNull(momentsViewModel.getFollowedAttractionList(), followedAttractionList -> {
            isRefreshingFollowedAttraction = false;
            updateRefreshState();
            followedAttractionListAdapter.setRecords(followedAttractionList);
        });

        isRefreshingMomentsStory = true;
        observeNotNull(momentsViewModel.refreshMomentsStoryList(), momentsStoryList -> {
            isRefreshingMomentsStory = false;
            updateRefreshState();
            momentsStoryListAdapter.setRecords(momentsStoryList);
            binding.moments.setLoadMoreEnabled(momentsStoryList.size() >= MomentsViewModel.MOMENTS_STORY_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.moments.setRefreshing(true);
    }

    private void updateRefreshState() {
        if (!isRefreshingRecommendUser && !isRefreshingFollowedAttraction && !isRefreshingMomentsStory) {
            binding.moments.setRefreshing(false);
        }
    }

    private void onLoadMore() {
        observeNotNull(momentsViewModel.loadMoreMomentsStoryList(), momentsStoryList -> {
            binding.moments.setLoadingMore(false);
            momentsStoryListAdapter.addRecords(momentsStoryList);
            binding.moments.setLoadMoreEnabled(momentsStoryList.size() >= MomentsViewModel.MOMENTS_STORY_LIST_PAGE_SIZE);
        });
    }

    private void onUserClick(String userid) {
        Intent intent = new Intent(requireContext(), UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, userid);
        startActivity(intent);
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(requireContext(), AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onNewStoryClick(View target) {
        startActivity(new Intent(requireContext(), NewStoryActivity.class));
    }

    private void onSearchBarClick(View target) {
        startActivity(new Intent(requireContext(), SearchActivity.class));
    }

    private void onMoreTourismTalentClick(View target) {
        startActivity(new Intent(requireContext(), RecommendTourismTalentActivity.class));
    }

    private void onMoreSocialGroupClick(View target) {
        startActivity(new Intent(requireContext(), RecommendSocialGroupActivity.class));
    }

    private void useLightStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(true);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MomentsBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MomentsBinding.inflate(inflater, container, false);
    }
}
