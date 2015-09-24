package com.kickstarter.services;

import android.content.Context;

import com.google.common.collect.ImmutableMap;
import com.kickstarter.R;
import com.kickstarter.models.Category;
import com.kickstarter.models.Location;

public class DiscoveryParams {
  private final boolean nearby;
  private final boolean staffPicks;
  private final int starred;
  private final int backed;
  private final int social;
  private final Category category;
  private final Location location;
  private final Sort sort;
  private final int page;
  private final int perPage;

  public enum Sort {
    MAGIC, POPULAR, ENDING_SOON, NEWEST, MOST_FUNDED;
    @Override
    public String toString() {
      switch (this) {
        case MAGIC:
          return "magic";
        case POPULAR:
          return "popularity";
        case ENDING_SOON:
          return "end_date";
        case NEWEST:
          return "newest";
        case MOST_FUNDED:
          return "most_funded";
      }
      throw new AssertionError("Unhandled sort");
    }
  }

  public boolean nearby() {
    return nearby;
  }

  public boolean staffPicks() {
    return staffPicks;
  }

  public int starred() {
    return starred;
  }

  public int backed() {
    return backed;
  }

  public int social() {
    return social;
  }

  public Category category() {
    return category;
  }

  public Location location() {
    return location;
  }

  public Sort sort() {
    return sort;
  }

  public int page() {
    return page;
  }

  public int perPage() {
    return perPage;
  }

  public static class Builder {
    private boolean nearby = false;
    private boolean staffPicks = false;
    private int starred = 0;
    private int backed = 0;
    private int social = 0;
    private Category category = null;
    private Location location = null;
    private Sort sort = Sort.MAGIC;
    private int page = 1;
    private int perPage = 15;

    public DiscoveryParams build() {
      return new DiscoveryParams(this);
    }

    public Builder() {
    }

    public Builder nearby(final boolean v) {
      nearby = v;
      return this;
    }

    public Builder staffPicks(final boolean v) {
      staffPicks = v;
      return this;
    }

    public Builder starred(final int v) {
      starred = v;
      return this;
    }

    public Builder backed(final int v) {
      backed = v;
      return this;
    }

    public Builder social(final int v) {
      social = v;
      return this;
    }

    public Builder category(final Category v) {
      category = v;
      return this;
    }

    public Builder location(final Location v) {
      location = v;
      return this;
    }

    public Builder sort(final Sort v) {
      sort = v;
      return this;
    }

    public Builder page(final int v) {
      page = v;
      return this;
    }

    public Builder perPage(final int v) {
      perPage = v;
      return this;
    }
  }

  private DiscoveryParams(final Builder builder) {
    nearby = builder.nearby;
    staffPicks = builder.staffPicks;
    starred = builder.starred;
    backed = builder.backed;
    social = builder.social;
    category = builder.category;
    location = builder.location;
    sort = builder.sort;
    page = builder.page;
    perPage = builder.perPage;
  }

  public Builder builder() {
    return new Builder()
      .nearby(nearby)
      .staffPicks(staffPicks)
      .starred(starred)
      .backed(backed)
      .social(social)
      .category(category)
      .location(location)
      .sort(sort)
      .page(page)
      .perPage(perPage);
  }

  public static DiscoveryParams params() {
    final DiscoveryParams p = new Builder()
      .staffPicks(true)
      .build();
    return p;
  }

  public DiscoveryParams nextPage () {
    return this.builder().page(page + 1).build();
  }

  public ImmutableMap<String, String> queryParams() {
    final ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>()
      .put("category_id", String.valueOf(category != null ? category.id() : ""))
      .put("woe_id", String.valueOf(location != null ? location.id() : ""))
      .put("staff_picks", String.valueOf(staffPicks))
      .put("starred", String.valueOf(starred))
      .put("backed", String.valueOf(backed))
      .put("social", String.valueOf(social))
      .put("sort", sort.toString())
      .put("page", String.valueOf(page))
      .put("per_page", String.valueOf(perPage));

    if (staffPicks && page == 1) {
      builder.put("include_potd", "true");
    }

    return builder.build();
  }

  @Override
  public String toString() {
    return queryParams().toString();
  }

  public String filterString(final Context context) {
    if (staffPicks) {
      return context.getString(R.string.Staff_Picks);
    } else if (nearby) {
      return context.getString(R.string.Nearby);
    } else if (starred == 1) {
      return context.getString(R.string.Starred);
    } else if (backed == 1) {
      return context.getString(R.string.Backing);
    } else if (social == 1) {
      return context.getString(R.string.Friends_Backed);
    } else if (category != null) {
      return category.name();
    } else if (location != null) {
      return location.name();
    } else {
      return context.getString(R.string.Everything);
    }
  }
}
