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

package com.google.zxing.common.reedsolomon;

import junit.framework.TestCase;

import java.util.BitSet;
import java.util.Random;

/**
 * @author srowen@google.com (Sean Owen)
 */
public final class ReedSolomonDecoderTestCase extends TestCase {

  /** See ISO 18004, Appendix I, from which this example is taken. */
  private static final int[] QR_CODE_TEST =
      { 0x10, 0x20, 0x0C, 0x56, 0x61, 0x80, 0xEC, 0x11, 0xEC,
        0x11, 0xEC, 0x11, 0xEC, 0x11, 0xEC, 0x11, 0xA5 };
  private static final int[] QR_CODE_TEST_WITH_EC =
      { 0x10, 0x20, 0x0C, 0x56, 0x61, 0x80, 0xEC, 0x11, 0xEC,
        0x11, 0xEC, 0x11, 0xEC, 0x11, 0xEC, 0x11, 0xA5, 0x24,
        0xD4, 0xC1, 0xED, 0x36, 0xC7, 0x87, 0x2C, 0x55 };
  private static final int QR_CODE_CORRECTABLE = (QR_CODE_TEST_WITH_EC.length - QR_CODE_TEST.length) / 2;

  private final ReedSolomonDecoder qrRSDecoder = new ReedSolomonDecoder(GF256.QR_CODE_FIELD);

  public void testNoError() throws ReedSolomonException {
    int[] received = new int[QR_CODE_TEST_WITH_EC.length];
    System.arraycopy(QR_CODE_TEST_WITH_EC, 0, received, 0, received.length);
    // no errors
    checkQRRSDecode(received);
  }

  public void testMaxErrors() throws ReedSolomonException {
    int[] received = new int[QR_CODE_TEST_WITH_EC.length];
    Random random = new Random(0xDEADBEEFL);
    for (int i = 0; i < QR_CODE_TEST.length; i++) { // # iterations is kind of arbitrary
      System.arraycopy(QR_CODE_TEST_WITH_EC, 0, received, 0, received.length);
      corrupt(received, QR_CODE_CORRECTABLE, random);
      checkQRRSDecode(received);
    }
  }

  public void testTooManyErrors() {
    int[] received = new int[QR_CODE_TEST_WITH_EC.length];
    System.arraycopy(QR_CODE_TEST_WITH_EC, 0, received, 0, received.length);
    Random random = new Random(0xDEADBEEFL);
    corrupt(received, QR_CODE_CORRECTABLE + 1, random);
    try {
      checkQRRSDecode(received);
      fail("Should not have decoded");
    } catch (ReedSolomonException rse) {
      // good
    }
  }

  private void checkQRRSDecode(int[] received) throws ReedSolomonException {
    qrRSDecoder.decode(received, 2*QR_CODE_CORRECTABLE);
    for (int i = 0; i < QR_CODE_TEST.length; i++) {
      assertEquals(received[i], QR_CODE_TEST[i]);
    }
  }

  private static void corrupt(int[] received, int howMany, Random random) {
    BitSet corrupted = new BitSet(received.length);
    for (int j = 0; j < howMany; j++) {
      int location = random.nextInt(received.length);
      if (corrupted.get(location)) {
        j--;
      } else {
        corrupted.set(location);
        int newByte = random.nextInt(256);
        received[location] = newByte;
      }
    }
  }
}