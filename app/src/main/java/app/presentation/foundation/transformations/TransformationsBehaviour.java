/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.presentation.foundation.transformations;

import app.presentation.foundation.dialogs.Dialogs;
import app.presentation.foundation.notifications.Notifications;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import javax.inject.Inject;
import javax.inject.Named;

public final class TransformationsBehaviour implements Transformations {
  private ObservableTransformer lifecycle;
  private final ExceptionFormatter exceptionFormatter;
  private final Notifications notifications;
  private final Dialogs dialogs;
  private final Scheduler mainThread, backgroundThread;

  @Inject public TransformationsBehaviour(ExceptionFormatter exceptionFormatter,
      Notifications notifications, Dialogs dialogs, @Named("mainThread") Scheduler mainThread,
      @Named("backgroundThread") Scheduler backgroundThread) {
    this.exceptionFormatter = exceptionFormatter;
    this.notifications = notifications;
    this.dialogs = dialogs;
    this.mainThread = mainThread;
    this.backgroundThread = backgroundThread;
  }

  public void setLifecycle(ObservableTransformer lifecycle) {
    this.lifecycle = lifecycle;
  }

  public <T> ObservableTransformer<T, T> safely() {
    return observable -> observable
        .subscribeOn(backgroundThread)
        .observeOn(mainThread)
        .compose(lifecycle);
  }

  public <T> ObservableTransformer<T, T> reportOnSnackBar() {
    return observable -> observable
        .<T>doOnError(throwable -> {
          Observable<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showSnackBar(formattedError);
        })
        .onErrorResumeNext(throwable -> {
          return Observable.<T>empty();
        });
  }

  public <T> ObservableTransformer<T, T> reportOnToast() {
    return observable -> observable
        .<T>doOnError(throwable -> {
          Observable<String> formattedError = exceptionFormatter.format(throwable);
          notifications.showToast(formattedError);
        })
        .<T>onErrorResumeNext(throwable -> {
          return Observable.empty();
        });
  }

  public <T> ObservableTransformer<T, T> loading() {
    return observable -> observable
        .doOnSubscribe(disposable -> dialogs.showLoading())
        .doOnComplete(dialogs::hideLoading)
        .doOnError(throwable -> dialogs.hideLoading());
  }

  public <T> ObservableTransformer<T, T> loading(String content) {
    return observable -> observable
        .doOnSubscribe(disposable -> dialogs.showNoCancelableLoading(content))
        .doOnComplete(dialogs::hideLoading)
        .doOnError(throwable -> dialogs.hideLoading());
  }
}
