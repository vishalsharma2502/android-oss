package com.kickstarter.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kickstarter.R;
import com.kickstarter.libs.ApiCapabilities;
import com.kickstarter.libs.BaseActivity;
import com.kickstarter.libs.KSColorUtils;
import com.kickstarter.libs.RequiresPresenter;
import com.kickstarter.models.Category;
import com.kickstarter.presenters.DiscoveryFilterPresenter;
import com.kickstarter.services.DiscoveryParams;
import com.kickstarter.ui.adapters.DiscoveryFilterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresPresenter(DiscoveryFilterPresenter.class)
public class DiscoveryFilterActivity extends BaseActivity<DiscoveryFilterPresenter> {
  final List<DiscoveryParams> discoveryParams = new ArrayList<>();
  DiscoveryFilterAdapter adapter;
  LinearLayoutManager layoutManager;

  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  @BindColor(R.color.dark_blue_gradient_start) int darkBlueGradientStartColor;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // TODO: Pass params from discovery

    setContentView(R.layout.discovery_filter_layout);
    ButterKnife.bind(this);

    layoutManager = new LinearLayoutManager(this);
    adapter = new DiscoveryFilterAdapter(discoveryParams, presenter);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    setStatusBarColor();

    presenter.initialize(new DiscoveryParams.Builder().build());
  }

  @OnClick(R.id.close_button)
  public void closeActivity() {
    onBackPressed();
  }

  public void loadDiscoveryParams(final List<DiscoveryParams> newDiscoveryParams) {
    discoveryParams.clear();
    discoveryParams.addAll(newDiscoveryParams);
    adapter.notifyDataSetChanged();
  }

  public void startDiscoveryActivity(final DiscoveryParams newDiscoveryParams) {
    // TODO: WIP, need to pass through params in intent
    final Intent intent = new Intent(this, DiscoveryActivity.class)
      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }

  private void setStatusBarColor() {
    if (ApiCapabilities.canSetStatusBarColor()) {
      final Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(KSColorUtils.darken(darkBlueGradientStartColor, 0.15f));
    }
  }
}
