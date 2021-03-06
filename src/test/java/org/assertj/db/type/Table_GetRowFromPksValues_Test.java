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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.db.type;

import org.assertj.db.common.AbstractTest;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests on getting a {@code Row} in a {@code Table} from primary keys values.
 * 
 * @author Régis Pouiller
 * 
 */
public class Table_GetRowFromPksValues_Test extends AbstractTest {

  /**
   * This method test getting a row from primary keys values without finding it.
   */
  @Test
  public void test_getting_row_from_primary_keys_values_without_finding() {
    Table table = new Table(source, "movie");

    assertThat(table.getRowFromPksValues()).isNull();
    assertThat(table.getRowFromPksValues(1L, 3)).isNull();
  }

  /**
   * This method test getting a row from primary keys values with finding it.
   */
  @Test
  public void test_getting_row_from_primary_keys_values_with_finding() {
    Table table = new Table(source, "movie");

    assertThat(table.getRowFromPksValues(3).getValuesList()).containsExactly(new BigDecimal(3), "Avatar",
        new BigDecimal(2009));
    assertThat(table.getRowFromPksValues(1L).getValuesList()).containsExactly(new BigDecimal(1), "Alien",
        new BigDecimal(1979));
  }

}
