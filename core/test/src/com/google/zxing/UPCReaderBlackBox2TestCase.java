/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing;

import com.google.zxing.common.AbstractBlackBoxTestCase;

import java.io.File;

/**
 * @author srowen@google.com (Sean Owen)
 */
public final class UPCReaderBlackBox2TestCase extends AbstractBlackBoxTestCase {

  public UPCReaderBlackBox2TestCase() {
    super(new File("test/data/blackbox/upc2"), new MultiFormatReader(), 0.66);
  }

}