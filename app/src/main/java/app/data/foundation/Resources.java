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

package app.data.foundation;

import android.support.annotation.StringRes;

/**
 * An abstraction layer for access Android resources without coupling with the platform.
 * This is necessary to be able to run unit tests without mocking the Android platform.
 */
public interface Resources {
  String getString(@StringRes int idResource);
  String getLang();
}
