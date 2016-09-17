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

package app.presentation.sections;

import app.presentation.foundation.transformations.Transformations;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class TransformationsMock implements Transformations {

  @Override public void setLifecycle(ObservableTransformer lifecycle) {
    // Do nothing.  Exists to satisfy Transformations.
  }

  @Override public <T> ObservableTransformer<T, T> safely() {
    return observable -> observable;
  }

  @Override public <T> ObservableTransformer<T, T> reportOnSnackBar() {
    return observable -> observable.onErrorResumeNext(throwable -> {
      return Observable.empty();
    });
  }

  @Override public <T> ObservableTransformer<T, T> reportOnToast() {
    return observable -> observable.onErrorResumeNext(throwable -> {
      return Observable.empty();
    });
  }

  @Override public <T> ObservableTransformer<T, T> loading() {
    return observable -> observable;
  }

}
