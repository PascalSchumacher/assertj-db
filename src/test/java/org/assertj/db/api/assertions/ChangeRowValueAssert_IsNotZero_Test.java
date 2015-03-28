/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.db.api.assertions;

import org.assertj.db.common.AbstractTest;
import org.assertj.db.common.NeedReload;
import org.assertj.db.type.Changes;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests on the methods which verifies if a value is not equal to zero.
 *
 * @author Régis Pouiller
 *
 */
public class ChangeRowValueAssert_IsNotZero_Test extends AbstractTest {

  /**
   * This method tests that the value is not equal to zero.
   */
  @Test
  @NeedReload
  public void test_if_value_is_not_zero() {
    Changes changes = new Changes(source).setStartPointNow();
    updateChangesForOtherTests();
    changes.setEndPointNow();

    assertThat(changes).change().rowAtEndPoint()
            .value("var1").isNotZero()
            .value("var3").isNotZero()
            .value("var4").isNotZero()
            .value("var5").isNotZero()
            .value("var13").isNotZero()
            .value("var14").isNotZero()
            .change().rowAtEndPoint()
            .value("var1").isNotZero()
            .value("var3").isNotZero()
            .value("var4").isNotZero()
            .value("var5").isNotZero()
            .value("var13").isNotZero()
            .value("var14").isNotZero();
  }

  /**
   * This method should fail because the value is equal to zero.
   */
  @Test
  @NeedReload
  public void should_fail_because_value_is_zero() {
    try {
      Changes changes = new Changes(source).setStartPointNow();
      updateChangesForOtherTests();
      changes.setEndPointNow();

      assertThat(changes).change(3).rowAtEndPoint()
              .value("var3").isNotZero();

      fail("An exception must be raised");
    }
    catch (AssertionError e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("[Value at index 2 of Row at end point of Change at index 3 of Changes on tables of 'sa/jdbc:h2:mem:test' source] \n" +
                                                                                    "Expecting:\n" +
                                                                                    "  <0>\n" +
                                                                                    "not to be equal to: \n" +
                                                                                    "  <0>");
    }
  }

  /**
   * This method should fail because the value is not a number.
   */
  @Test
  @NeedReload
  public void should_fail_because_value_is_not_a_boolean() {
    try {
      Changes changes = new Changes(source).setStartPointNow();
      updateChangesForOtherTests();
      changes.setEndPointNow();

      assertThat(changes).change().rowAtEndPoint().value("var2")
                       .as("var2").isNotZero();

      fail("An exception must be raised");
    }
    catch (AssertionError e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("[var2] \n" +
                                                                                    "Expecting:\n" +
                                                                                    "  <true>\n" +
                                                                                    "to be of type\n" +
                                                                                    "  <NUMBER>\n" +
                                                                                    "but was of type\n" +
                                                                                    "  <BOOLEAN>");
    }
  }

}