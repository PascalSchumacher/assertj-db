package org.assertj.db.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.assertj.db.common.AbstractTest;
import org.assertj.db.type.DateTimeValue;
import org.assertj.db.type.DateValue;
import org.assertj.db.type.TimeValue;
import org.junit.Test;

/**
 * Test on the type got from {@code getPossibleTypesForComparison} method from {@code ValueType} enum.
 * 
 * @author Régis Pouiller
 * 
 */
public class ValueType_GetPossibleTypesForComparison_Test extends AbstractTest {

  /**
   * This method tests the result of {@code getPossibleTypesForComparison} method from {@code ValueType} enum.
   */
  @Test
  public void test_result_when_getting_possible_types_for_comparison() {
    assertThat(ValueType.getPossibleTypesForComparison(new byte[] { 1 }))
        .isEqualTo(new ValueType[] { ValueType.BYTES });
    assertThat(ValueType.getPossibleTypesForComparison(true)).isEqualTo(new ValueType[] { ValueType.BOOLEAN });
    assertThat(ValueType.getPossibleTypesForComparison("")).isEqualTo(
        new ValueType[] { ValueType.TEXT, ValueType.NUMBER, ValueType.DATE, ValueType.TIME, ValueType.DATE_TIME });
    assertThat(ValueType.getPossibleTypesForComparison(DateValue.of(2014, 10, 10))).isEqualTo(
        new ValueType[] { ValueType.DATE, ValueType.DATE_TIME });
    assertThat(ValueType.getPossibleTypesForComparison(TimeValue.of(10, 10))).isEqualTo(
        new ValueType[] { ValueType.TIME });
    assertThat(
        ValueType.getPossibleTypesForComparison(DateTimeValue.of(DateValue.of(2014, 10, 10), TimeValue.of(10, 10))))
        .isEqualTo(new ValueType[] { ValueType.DATE_TIME });
    assertThat(ValueType.getPossibleTypesForComparison((byte) 10)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison((short) 10)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison((int) 10)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison((long) 10)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison(10.5f)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison(10.5d)).isEqualTo(new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison(new BigInteger("10"))).isEqualTo(
        new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison(new BigDecimal(10.5f))).isEqualTo(
        new ValueType[] { ValueType.NUMBER });
    assertThat(ValueType.getPossibleTypesForComparison(null)).isEqualTo(new ValueType[] { ValueType.NOT_IDENTIFIED });
  }
}