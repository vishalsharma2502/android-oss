package com.kickstarter.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.kickstarter.libs.Config;
import com.kickstarter.libs.rx.operators.ApiErrorOperator;
import com.kickstarter.libs.rx.operators.Operators;
import com.kickstarter.models.Activity;
import com.kickstarter.models.Backing;
import com.kickstarter.models.Category;
import com.kickstarter.models.Comment;
import com.kickstarter.models.Empty;
import com.kickstarter.models.Notification;
import com.kickstarter.models.Project;
import com.kickstarter.models.User;
import com.kickstarter.services.apirequests.CommentBody;
import com.kickstarter.services.apirequests.LoginWithFacebookBody;
import com.kickstarter.services.apirequests.NotificationBody;
import com.kickstarter.services.apirequests.PushTokenBody;
import com.kickstarter.services.apirequests.RegisterWithFacebookBody;
import com.kickstarter.services.apirequests.ResetPasswordBody;
import com.kickstarter.services.apirequests.SettingsBody;
import com.kickstarter.services.apirequests.SignupBody;
import com.kickstarter.services.apiresponses.AccessTokenEnvelope;
import com.kickstarter.services.apiresponses.ActivityEnvelope;
import com.kickstarter.services.apiresponses.CategoriesEnvelope;
import com.kickstarter.services.apiresponses.CommentsEnvelope;
import com.kickstarter.services.apiresponses.DiscoverEnvelope;
import com.kickstarter.services.apiresponses.StarEnvelope;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class ApiClient implements ApiClientType {
  private final ApiService service;
  private final Gson gson;

  public ApiClient(@NonNull final ApiService service, @NonNull final Gson gson) {
    this.gson = gson;
    this.service = service;
  }

  public Observable<AccessTokenEnvelope> loginWithFacebook(@NonNull final String accessToken) {
    return service
      .login(LoginWithFacebookBody.builder().accessToken(accessToken).build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<AccessTokenEnvelope> loginWithFacebook(@NonNull final String fbAccessToken, @NonNull final String code) {
    return service
      .login(LoginWithFacebookBody.builder().accessToken(fbAccessToken).code(code).build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<AccessTokenEnvelope> registerWithFacebook(@NonNull final String fbAccessToken, final boolean sendNewsletters) {
    return service
      .login(RegisterWithFacebookBody.builder()
        .accessToken(fbAccessToken)
        .sendNewsletters(sendNewsletters)
        .build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<ActivityEnvelope> fetchActivities() {
    final List<String> categories = Arrays.asList(
      Activity.CATEGORY_BACKING,
      Activity.CATEGORY_CANCELLATION,
      Activity.CATEGORY_FAILURE,
      Activity.CATEGORY_LAUNCH,
      Activity.CATEGORY_SUCCESS,
      Activity.CATEGORY_UPDATE,
      Activity.CATEGORY_FOLLOW
    );

    return service
      .activities(categories)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<ActivityEnvelope> fetchActivities(String paginationPath) {
    return service
      .activities(paginationPath)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<List<Notification>> fetchNotifications() {
    return service
      .notifications()
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<List<Category>> fetchCategories() {
    return service
      .categories()
      .lift(apiErrorOperator())
      .map(CategoriesEnvelope::categories)
      .subscribeOn(Schedulers.io());
  }

  public Observable<CommentsEnvelope> fetchProjectComments(final @NonNull Project project) {
    return service
      .projectComments(project.param())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<CommentsEnvelope> fetchProjectComments(final @NonNull String paginationPath) {
    return service
      .paginatedProjectComments(paginationPath)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<DiscoverEnvelope> fetchProjects(@NonNull final DiscoveryParams params) {
    return service
      .projects(params.queryParams())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<DiscoverEnvelope> fetchProjects(@NonNull final String paginationUrl) {
    return service
      .projects(paginationUrl)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Project> fetchProject(@NonNull final String param) {
    return service
      .project(param)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Project> fetchProject(@NonNull final Project project) {
    return fetchProject(project.param()).startWith(project);
  }

  public Observable<Backing> fetchProjectBacking(@NonNull final Project project, @NonNull final User user) {
    return service
      .projectBacking(project.param(), user.param())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Category> fetchCategory(final long id) {
    return service
      .category(id)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Category> fetchCategory(@NonNull final Category category) {
    return fetchCategory(category.id());
  }

  public Observable<User> fetchCurrentUser() {
    return service
      .currentUser()
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<AccessTokenEnvelope> login(@NonNull final String email, @NonNull final String password) {
    return service
      .login(email, password)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<AccessTokenEnvelope> login(@NonNull final String email, @NonNull final String password,
    @NonNull final String code) {
    return service
      .login(email, password, code)
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Comment> postProjectComment(@NonNull final Project project, @NonNull final String body) {
    return service
      .postProjectComment(project.param(), CommentBody.builder().body(body).build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public @NonNull Observable<Empty> registerPushToken(@NonNull final String token) {
    return service
      .registerPushToken(PushTokenBody.builder().token(token).pushServer("development").build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public @NonNull Observable<User> resetPassword(@NonNull final String email) {
    return service
      .resetPassword(ResetPasswordBody.builder().email(email).build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<AccessTokenEnvelope> signup(@NonNull final String name, @NonNull final String email,
    @NonNull final String password, @NonNull final String passwordConfirmation,
    final boolean sendNewsletters) {
    return service
      .signup(
        SignupBody.builder()
          .name(name)
          .email(email)
          .password(password)
          .passwordConfirmation(passwordConfirmation)
          .sendNewsletters(sendNewsletters)
          .build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Project> starProject(@NonNull final Project project) {
    return service
      .starProject(project.param())
      .lift(apiErrorOperator())
      .map(StarEnvelope::project)
      .subscribeOn(Schedulers.io());
  }

  public Observable<Project> toggleProjectStar(@NonNull final Project project) {
    return service
      .toggleProjectStar(project.param())
      .lift(apiErrorOperator())
      .map(StarEnvelope::project)
      .subscribeOn(Schedulers.io());
  }

  public Observable<Notification> updateProjectNotifications(final @NonNull Notification notification, final boolean checked) {
    return service
      .updateProjectNotifications(notification.id(),
        NotificationBody.builder()
          .email(checked)
          .mobile(checked)
          .build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<User> updateUserSettings(final @NonNull User user) {
    return service
      .updateUserSettings(
        SettingsBody.builder()
          .notifyMobileOfFollower(user.notifyMobileOfFollower())
          .notifyMobileOfFriendActivity(user.notifyMobileOfFriendActivity())
          .notifyMobileOfUpdates(user.notifyMobileOfUpdates())
          .notifyOfFollower(user.notifyOfFollower())
          .notifyOfFriendActivity(user.notifyOfFriendActivity())
          .notifyOfUpdates(user.notifyOfUpdates())
          .happeningNewsletter(user.happeningNewsletter() ? 1 : 0)
          .promoNewsletter(user.promoNewsletter() ? 1 : 0)
          .weeklyNewsletter(user.weeklyNewsletter() ? 1 : 0)
          .build())
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  public Observable<Config> config() {
    return service
      .config()
      .lift(apiErrorOperator())
      .subscribeOn(Schedulers.io());
  }

  /**
   * Utility to create a new {@link ApiErrorOperator}, saves us from littering references to gson throughout the client.
   */
  private @NonNull <T> ApiErrorOperator<T> apiErrorOperator() {
    return Operators.apiError(gson);
  }
}
