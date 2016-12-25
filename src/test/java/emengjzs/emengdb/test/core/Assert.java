package emengjzs.emengdb.test.core;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p>
 * Copyright 2012-2016 the original author or authors.
 */

import org.assertj.core.api.*;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.api.filter.*;
import org.assertj.core.condition.AllOf;
import org.assertj.core.condition.AnyOf;
import org.assertj.core.condition.DoesNotHave;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.groups.Properties;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.*;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.Files;
import org.assertj.core.util.URLs;
import org.assertj.core.util.introspection.FieldSupport;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

/**
 * Entry point for assertion methods for different data types. Each method in this class is a static factory for the
 * type-specific assertion objects. The purpose of this class is to make test code more readable.
 * <p>
 * For example:
 *
 * <pre><code class='java'> int removed = employees.removeFired();
 * {@link Assertions#assertThat(int) assertThat}(removed).{@link IntegerAssert#isZero isZero}();
 *
 * List&lt;Employee&gt; newEmployees = employees.hired(TODAY);
 * {@link Assertions#assertThat(Iterable) assertThat}(newEmployees).{@link IterableAssert#hasSize(int) hasSize}(6);</code></pre>
 * <p/>
 * This class only contains all <code>assertThat</code> methods, if you have ambiguous method compilation error, use either {@link AssertionsForClassTypes} or {@link AssertionsForInterfaceTypes}
 * and if you need both, fully qualify you assertThat method.
 * <p/>
 * Java 8 is picky when choosing the right <code>assertThat</code> method if the object under test is generic and bounded,
 * for example if foo is instance of T that extends Exception, java 8  will complain that it can't resolve
 * the proper <code>assertThat</code> method (normally <code>assertThat(Throwable)</code> as foo might implement an interface like List,
 * if that occurred <code>assertThat(List)</code> would also be a possible choice - thus confusing java 8.
 * <p>
 * This why {@link Assertions} have been split in {@link AssertionsForClassTypes} and {@link AssertionsForInterfaceTypes}
 * (see http://stackoverflow.com/questions/29499847/ambiguous-method-in-java-8-why).
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ted Young
 * @author Joel Costigliola
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Julien Meddah
 * @author William Delanoue
 */
public class Assert {

    /**
     * Create assertion for {@link Predicate}.
     *
     * @param actual the actual value.
     * @param <T> the type of the value contained in the {@link Predicate}.
     * @return the created assertion object.
     *
     * @since 3.5.0
     */
    public <T> PredicateAssert<T> that(Predicate<T> actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link IntPredicate}.
     *
     * @return the created assertion object.
     *
     * @since 3.5.0
     */
    @CheckReturnValue
    public IntPredicateAssert that(IntPredicate actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link LongPredicate}.
     *
     * @return the created assertion object.
     *
     * @since 3.5.0
     */
    @CheckReturnValue
    public LongPredicateAssert that(LongPredicate actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link DoublePredicate}.
     *
     * @return the created assertion object.
     *
     * @since 3.5.0
     */
    @CheckReturnValue
    public DoublePredicateAssert that(DoublePredicate actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.util.concurrent.CompletableFuture}.
     *
     * @param actual the actual value.
     * @param <T> the type of the value contained in the {@link java.util.concurrent.CompletableFuture}.
     *
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <T> CompletableFutureAssert<T> that(CompletableFuture<T> actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.util.Optional}.
     *
     * @param actual the actual value.
     * @param <T> the type of the value contained in the {@link java.util.Optional}.
     *
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <T> OptionalAssert<T> that(Optional<T> actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.util.OptionalDouble}.
     *
     * @param actual the actual value.
     *
     * @return the created assertion object.
     */
    @CheckReturnValue
    public OptionalDoubleAssert that(OptionalDouble actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.util.OptionalInt}.
     *
     * @param actual the actual value.
     *
     * @return the created assertion object.
     */
    @CheckReturnValue
    public OptionalIntAssert that(OptionalInt actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.util.OptionalInt}.
     *
     * @param actual the actual value.
     *
     * @return the created assertion object.
     */
    @CheckReturnValue
    public OptionalLongAssert that(OptionalLong actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link BigDecimalAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractBigDecimalAssert<?> that(BigDecimal actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link UriAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractUriAssert<?> that(URI actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link UrlAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractUrlAssert<?> that(URL actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link BooleanAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractBooleanAssert<?> that(boolean actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link BooleanAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractBooleanAssert<?> that(Boolean actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link BooleanArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractBooleanArrayAssert<?> that(boolean[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ByteAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractByteAssert<?> that(byte actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ByteAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractByteAssert<?> that(Byte actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ByteArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractByteArrayAssert<?> that(byte[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link CharacterAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractCharacterAssert<?> that(char actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link CharArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractCharArrayAssert<?> that(char[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link CharacterAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractCharacterAssert<?> that(Character actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ClassAssert}</code>
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractClassAssert<?> that(Class<?> actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link DoubleAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractDoubleAssert<?> that(double actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link DoubleAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractDoubleAssert<?> that(Double actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link DoubleArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractDoubleArrayAssert<?> that(double[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link FileAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractFileAssert<?> that(File actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link InputStreamAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractInputStreamAssert<?, ? extends InputStream> that(InputStream actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link FloatAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractFloatAssert<?> that(float actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link FloatAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractFloatAssert<?> that(Float actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link FloatArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractFloatArrayAssert<?> that(float[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link IntegerAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractIntegerAssert<?> that(int actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link IntArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractIntArrayAssert<?> that(int[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link IntegerAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractIntegerAssert<?> that(Integer actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link FactoryBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
     * in order to perform assertions on it.
     * <p>
     * Navigational methods provided:<ul>
     * <li>{@link AbstractIterableAssert#first() first()}</li>
     * <li>{@link AbstractIterableAssert#last() last()}</li>
     * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
     * </ul>
     * <p>
     * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
     * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
     * <p>
     * Example with {@code String} element assertions:
     * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
     *
     * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
     * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
     *   {@literal @}Override
     *   public StringAssert createAssert(String string) {
     *     return new StringAssert(string);
     *   }
     * };
     *
     * // assertion succeeds with String assertions chained after first()
     * that(hobbits, stringAssertFactory).first()
     *                                         .startsWith("fro")
     *                                         .endsWith("do");</code></pre>
     *
     * @param actual the actual value.
     * @param assertFactory the factory used to create the elements assert instance.
     * @return the created assertion object.
     * @since 2.5.0 / 3.5.0
     */
    //@format:off
    public <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
    FactoryBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> that(Iterable<? extends ELEMENT> actual,
                                                                                 AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
        return assertThat(actual, assertFactory);
    }

    /**
     * Creates a new instance of <code>{@link ClassBasedNavigableIterableAssert}</code> allowing to navigate to any {@code Iterable} element
     * in order to perform assertions on it.
     * <p>
     * Navigational methods provided:<ul>
     * <li>{@link AbstractIterableAssert#first() first()}</li>
     * <li>{@link AbstractIterableAssert#last() last()}</li>
     * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
     * </ul>
     * <p>
     * The available assertions after navigating to an element depend on the given {@code assertClass}
     * (AssertJ can't find the element assert type by itself because of Java type erasure).
     * <p>
     * Example with {@code String} element assertions:
     * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newHashSet("frodo", "sam", "pippin");
     *
     * // assertion succeeds with String assertions chained after first()
     * that(hobbits, StringAssert.class).first()
     *                                        .startsWith("fro")
     *                                        .endsWith("do");</code></pre>
     *
     * @param actual the actual value.
     * @param assertClass the class used to create the elements assert instance.
     * @return the created assertion object.
     * @since 2.5.0 / 3.5.0
     */
    public <ACTUAL extends Iterable<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
    ClassBasedNavigableIterableAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> that(ACTUAL actual,
                                                                               Class<ELEMENT_ASSERT> assertClass) {
        return assertThat(actual, assertClass);
    }

    /**
     * Creates a new instance of <code>{@link FactoryBasedNavigableListAssert}</code> allowing to navigate to any {@code List} element
     * in order to perform assertions on it.
     * <p>
     * Navigational methods provided:<ul>
     * <li>{@link AbstractIterableAssert#first() first()}</li>
     * <li>{@link AbstractIterableAssert#last() last()}</li>
     * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
     * </ul>
     * <p>
     * The available assertions after navigating to an element depend on the {@code ELEMENT_ASSERT} parameter of the given
     * {@link AssertFactory AssertFactory&lt;ELEMENT, ELEMENT_ASSERT&gt;} (AssertJ can't figure it out because of Java type erasure).
     * <p>
     * Example with {@code String} element assertions:
     * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
     *
     * // build an AssertFactory for StringAssert (much nicer with Java 8 lambdas)
     * AssertFactory&lt;String, StringAssert&gt; stringAssertFactory = new AssertFactory&lt;String, StringAssert&gt;() {
     *   {@literal @}Override
     *   public StringAssert createAssert(String string) {
     *     return new StringAssert(string);
     *   }
     * };
     *
     * // assertion succeeds with String assertions chained after first()
     * that(hobbits, stringAssertFactory).first()
     *                                         .startsWith("fro")
     *                                         .endsWith("do");</code></pre>
     *
     * @param actual the actual value.
     * @param assertFactory the factory used to create the elements assert instance.
     * @return the created assertion object.
     * @since 2.5.0 / 3.5.0
     */
    public <ACTUAL extends List<? extends ELEMENT>, ELEMENT, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
    FactoryBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> that(List<? extends ELEMENT> actual,
                                                                             AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
        return assertThat(actual, assertFactory);
    }

    /**
     * Creates a new instance of <code>{@link ClassBasedNavigableListAssert}</code> tallowing to navigate to any {@code List} element
     * in order to perform assertions on it.
     * <p>
     * Navigational methods provided:<ul>
     * <li>{@link AbstractIterableAssert#first() first()}</li>
     * <li>{@link AbstractIterableAssert#last() last()}</li>
     * <li>{@link AbstractIterableAssert#element(int) element(index)}</li>
     * </ul>
     * <p>
     * The available assertions after navigating to an element depend on the given {@code assertClass}
     * (AssertJ can't find the element assert type by itself because of Java type erasure).
     * <p>
     * Example with {@code String} element assertions:
     * <pre><code class='java'> List&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
     *
     * // assertion succeeds with String assertions chained after first()
     * that(hobbits, StringAssert.class).first()
     *                                        .startsWith("fro")
     *                                        .endsWith("do");</code></pre>
     *
     * @param actual the actual value.
     * @param assertClass the class used to create the elements assert instance.
     * @return the created assertion object.
     * @since 2.5.0 / 3.5.0
     */
    public <ELEMENT, ACTUAL extends List<? extends ELEMENT>, ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
    ClassBasedNavigableListAssert<?, ACTUAL, ELEMENT, ELEMENT_ASSERT> that(List<? extends ELEMENT> actual,
                                                                           Class<ELEMENT_ASSERT> assertClass) {
        return assertThat(actual, assertClass);
    }

//@format:on

    /**
     * Creates a new instance of <code>{@link LongAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLongAssert<?> that(long actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link LongAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLongAssert<?> that(Long actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link LongArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLongArrayAssert<?> that(long[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ObjectAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <T> AbstractObjectAssert<?, T> that(T actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ObjectArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <T> AbstractObjectArrayAssert<?, T> that(T[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ShortAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractShortAssert<?> that(short actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ShortAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractShortAssert<?> that(Short actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ShortArrayAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractShortArrayAssert<?> that(short[] actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link StringAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractCharSequenceAssert<?, String> that(String actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link DateAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractDateAssert<?> that(Date actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ZonedDateTimeAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractZonedDateTimeAssert<?> that(ZonedDateTime actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link LocalDateTimeAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLocalDateTimeAssert<?> that(LocalDateTime actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link java.time.OffsetDateTime}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractOffsetDateTimeAssert<?> that(OffsetDateTime actual) {
        return assertThat(actual);
    }

    /**
     * Create assertion for {@link java.time.OffsetTime}.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractOffsetTimeAssert<?> that(OffsetTime actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link LocalTimeAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLocalTimeAssert<?> that(LocalTime actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link LocalDateAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractLocalDateAssert<?> that(LocalDate actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ThrowableAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created {@link ThrowableAssert}.
     */
    @CheckReturnValue
    public AbstractThrowableAssert<?, ? extends Throwable> that(Throwable actual) {
        return assertThat(actual);
    }


    /**
     * Allows to catch an {@link Throwable} more easily when used with Java 8 lambdas.
     *
     * <p>
     * This caught {@link Throwable} can then be asserted.
     * </p>
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre><code class='java'> {@literal @}Test
     * public void testException() {
     *   // when
     *   Throwable thrown = catchThrowable(() -> { throw new Exception("boom!"); });
     *
     *   // then
     *   that(thrown).isInstanceOf(Exception.class)
     *                     .hasMessageContaining("boom");
     * } </code></pre>
     *
     * @param shouldRaiseThrowable The lambda with the code that should raise the exception.
     * @return The captured exception or <code>null</code> if none was raised by the callable.
     */
    public Throwable catchThrowable(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return AssertionsForClassTypes.catchThrowable(shouldRaiseThrowable);
    }

    /**
     * Entry point to check that an exception of type T is thrown by a given {@code throwingCallable}
     * which allows to chain assertions on the thrown exception.
     * <p>
     * Example:
     * <pre><code class='java'> assertThatExceptionOfType(IOException.class)
     *           .isThrownBy(() -> { throw new IOException("boom!"); })
     *           .withMessage("boom!"); </code></pre>
     *
     *
     * @param exceptionType the actual value.
     * @return the created {@link ThrowableTypeAssert}.
     */
    @CheckReturnValue
    public <T extends Throwable> ThrowableTypeAssert<T> assertThatExceptionOfType(final Class<? extends T> exceptionType) {
        return assertThatExceptionOfType(exceptionType);
    }

    // -------------------------------------------------------------------------------------------------
    // fail methods : not assertions but here to have a single entry point to all AssertJ features.
    // -------------------------------------------------------------------------------------------------

    /**
     * Only delegate to {@link Fail#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} so that Assertions offers a
     * full feature entry point to all AssertJ Assert features (but you can use {@link Fail} if you prefer).
     */
    public void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
        Fail.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
    }

    /**
     * Only delegate to {@link Fail#fail(String)} so that Assertions offers a full feature entry point to all Assertj
     * Assert features (but you can use Fail if you prefer).
     */
    public void fail(String failureMessage) {
        Fail.fail(failureMessage);
    }

    /**
     * Only delegate to {@link Fail#fail(String, Throwable)} so that Assertions offers a full feature entry point to all
     * AssertJ features (but you can use Fail if you prefer).
     */
    public void fail(String failureMessage, Throwable realCause) {
        Fail.fail(failureMessage, realCause);
    }

    /**
     * Only delegate to {@link Fail#failBecauseExceptionWasNotThrown(Class)} so that Assertions offers a full feature
     * entry point to all AssertJ features (but you can use Fail if you prefer).
     *
     * {@link Assertions#shouldHaveThrown(Class)} can be used as a replacement.
     */
    public void failBecauseExceptionWasNotThrown(Class<? extends Throwable> exceptionClass) {
        Fail.shouldHaveThrown(exceptionClass);
    }

    /**
     * Only delegate to {@link Fail#shouldHaveThrown(Class)} so that Assertions offers a full feature
     * entry point to all AssertJ features (but you can use Fail if you prefer).
     */
    public void shouldHaveThrown(Class<? extends Throwable> exceptionClass) {
        Fail.shouldHaveThrown(exceptionClass);
    }

    /**
     * In error messages, sets the threshold when iterable/array formatting will on one line (if their String description
     * is less than this parameter) or it will be formatted with one element per line.
     * <p>
     * The following array will be formatted on one line as its length < 80
     *
     * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
     *
     * // formatted as:
     *
     * ["A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice"]</code></pre>
     * whereas this array is formatted on multiple lines (one element per line)
     *
     * <pre><code class='java'> String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards! (Discworld)");
     *
     * // formatted as:
     *
     * ["A Game of Thrones",
     *  "The Lord of the Rings",
     *  "Assassin's Apprentice",
     *  "Guards! Guards! (Discworld)"]</code></pre>
     *
     * @param maxLengthForSingleLineDescription the maximum length for an iterable/array to be displayed on one line
     */
    public void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
        StandardRepresentation.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription);
    }

    // ------------------------------------------------------------------------------------------------------
    // properties methods : not assertions but here to have a single entry point to all AssertJ features.
    // ------------------------------------------------------------------------------------------------------

    /**
     * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
     * to
     * all AssertJ features (but you can use {@link Properties} if you prefer).
     * <p>
     * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
     * <p>
     * <pre><code class='java'> // extract simple property values having a java standard type (here String)
     * that(extractProperty(&quot;name&quot;, String.class).from(fellowshipOfTheRing))
     *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
     *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
     *
     * // extracting property works also with user's types (here Race)
     * that(extractProperty(&quot;race&quot;, String.class).from(fellowshipOfTheRing))
     *           .contains(HOBBIT, ELF).doesNotContain(ORC);
     *
     * // extract nested property on Race
     * that(extractProperty(&quot;race.name&quot;, String.class).from(fellowshipOfTheRing))
     *           .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
     *           .doesNotContain(&quot;Orc&quot;);</code></pre>
     */
    public <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
        return Properties.extractProperty(propertyName, propertyType);
    }

    /**
     * Only delegate to {@link Properties#extractProperty(String)} so that Assertions offers a full feature entry point
     * to
     * all AssertJ features (but you can use {@link Properties} if you prefer).
     * <p>
     * Typical usage is to chain <code>extractProperty</code> with <code>from</code> method, see examples below :
     * <p>
     * <pre><code class='java'> // extract simple property values, as no type has been defined the extracted property will be considered as Object
     * // to define the real property type (here String) use extractProperty(&quot;name&quot;, String.class) instead.
     * that(extractProperty(&quot;name&quot;).from(fellowshipOfTheRing))
     *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;, &quot;Legolas&quot;)
     *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
     *
     * // extracting property works also with user's types (here Race), even though it will be considered as Object
     * // to define the real property type (here String) use extractProperty(&quot;name&quot;, Race.class) instead.
     * that(extractProperty(&quot;race&quot;).from(fellowshipOfTheRing)).contains(HOBBIT, ELF).doesNotContain(ORC);
     *
     * // extract nested property on Race
     * that(extractProperty(&quot;race.name&quot;).from(fellowshipOfTheRing)).contains(&quot;Hobbit&quot;, &quot;Elf&quot;).doesNotContain(&quot;Orc&quot;); </code></pre>
     */
    public Properties<Object> extractProperty(String propertyName) {
        return Properties.extractProperty(propertyName);
    }

    /**
     * Utility method to build nicely a {@link Tuple} when working with {@link IterableAssert#extracting(String...)} or
     * {@link ObjectArrayAssert#extracting(String...)}
     *
     * @param values the values stored in the {@link Tuple}
     * @return the built {@link Tuple}
     */
    public Tuple tuple(Object... values) {
        return Tuple.tuple(values);
    }

    /**
     * Globally sets whether
     * <code>{@link org.assertj.core.api.AbstractIterableAssert#extracting(String) IterableAssert#extracting(String)}</code>
     * and
     * <code>{@link org.assertj.core.api.AbstractObjectArrayAssert#extracting(String) ObjectArrayAssert#extracting(String)}</code>
     * should be allowed to extract private fields, if not and they try it fails with exception.
     *
     * @param allowExtractingPrivateFields allow private fields extraction. Default {@code true}.
     */
    public void setAllowExtractingPrivateFields(boolean allowExtractingPrivateFields) {
        FieldSupport.extraction().setAllowUsingPrivateFields(allowExtractingPrivateFields);
    }

    /**
     * Globally sets whether the use of private fields is allowed for comparison.
     * The following (incomplete) list of methods will be impacted by this change :
     * <ul>
     * <li>
     * <code><code>{@link org.assertj.core.api.AbstractIterableAssert#usingElementComparatorOnFields(java.lang.String...)}</code>
     * </li>
     * <li><code> </code></li>
     * </ul>
     *
     * If the value is <code>false</code> and these methods try to compare private fields, it will fail with an exception.
     *
     * @param allowComparingPrivateFields allow private fields comparison. Default {@code true}.
     */
    public void setAllowComparingPrivateFields(boolean allowComparingPrivateFields) {
        FieldSupport.comparison().setAllowUsingPrivateFields(allowComparingPrivateFields);
    }

    // ------------------------------------------------------------------------------------------------------
    // Data utility methods : not assertions but here to have a single entry point to all AssertJ features.
    // ------------------------------------------------------------------------------------------------------

    /**
     * Only delegate to {@link MapEntry#entry(K key, V value)} so that Assertions offers a full feature entry point to
     * all
     * AssertJ features (but you can use {@link MapEntry} if you prefer).
     * <p>
     * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
     * <p>
     *
     * <pre><code class='java'> Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
     *
     * that(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
     */
    public <K, V> MapEntry<K, V> entry(K key, V value) {
        return MapEntry.entry(key, value);
    }

    /**
     * Only delegate to {@link Index#atIndex(int)} so that Assertions offers a full feature entry point to all AssertJ
     * features (but you can use {@link Index} if you prefer).
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> List&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
     * that(elvesRings).contains(vilya, atIndex(0)).contains(nenya, atIndex(1)).contains(narya, atIndex(2));</code></pre>
     */
    public Index atIndex(int index) {
        return Index.atIndex(index);
    }

    /**
     * Assertions entry point for double {@link Offset}.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(8.1).isEqualTo(8.0, offset(0.1));</code></pre>
     */
    public Offset<Double> offset(Double value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for float {@link Offset}.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(8.2f).isCloseTo(8.0f, offset(0.2f));</code></pre>
     */
    public Offset<Float> offset(Float value) {
        return Offset.offset(value);
    }

    /**
     * Alias for {@link #offset(Double)} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(8.1).isCloseTo(8.0, within(0.1));</code></pre>
     */
    public Offset<Double> within(Double value) {
        return Offset.offset(value);
    }

    /**
     * Alias for {@link #offset(Double)} to use with real number assertions.
     * <p>
     * Typical usage :
     * <pre><code class='java'> that(8.1).isEqualTo(8.0, withPrecision(0.1));</code></pre>
     */
    public Offset<Double> withPrecision(Double value) {
        return Offset.offset(value);
    }

    /**
     * Alias for {@link #offset(Float)} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(8.2f).isCloseTo(8.0f, within(0.2f));</code></pre>
     */
    public Offset<Float> within(Float value) {
        return Offset.offset(value);
    }

    /**
     * Alias for {@link #offset(Float)} to use with real number assertions.
     * <p>
     * Typical usage :
     * <pre><code class='java'> that(8.2f).isEqualTo(8.0f, withPrecision(0.2f));</code></pre>
     */
    public Offset<Float> withPrecision(Float value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for BigDecimal {@link Offset} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(BigDecimal.TEN).isCloseTo(new BigDecimal("10.5"), within(BigDecimal.ONE));</code></pre>
     */
    public Offset<BigDecimal> within(BigDecimal value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for Byte {@link Offset} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that((byte)10).isCloseTo((byte)11, within((byte)1));</code></pre>
     */
    public Offset<Byte> within(Byte value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for Integer {@link Offset} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(10).isCloseTo(11, within(1));</code></pre>
     */
    public Offset<Integer> within(Integer value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for Short {@link Offset} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(10).isCloseTo(11, within(1));</code></pre>
     */
    public Offset<Short> within(Short value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for Long {@link Offset} to use with isCloseTo assertions.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(5l).isCloseTo(7l, within(2l));</code></pre>
     */
    public Offset<Long> within(Long value) {
        return Offset.offset(value);
    }

    /**
     * Assertions entry point for Double {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
     * percentages.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(11.0).isCloseTo(10.0, withinPercentage(10.0));</code></pre>
     */
    public Percentage withinPercentage(Double value) {
        return withPercentage(value);
    }

    /**
     * Assertions entry point for Integer {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
     * percentages.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(11).isCloseTo(10, withinPercentage(10));</code></pre>
     */
    public Percentage withinPercentage(Integer value) {
        return withPercentage(value);
    }

    /**
     * Assertions entry point for Long {@link org.assertj.core.data.Percentage} to use with isCloseTo assertions for
     * percentages.
     * <p>
     * Typical usage :
     *
     * <pre><code class='java'> that(11L).isCloseTo(10L, withinPercentage(10L));</code></pre>
     */
    public Percentage withinPercentage(Long value) {
        return withPercentage(value);
    }

    // ------------------------------------------------------------------------------------------------------
    // Condition methods : not assertions but here to have a single entry point to all AssertJ features.
    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new <code>{@link AllOf}</code>
     *
     * @param <T> the type of object the given condition accept.
     * @param conditions the conditions to evaluate.
     * @return the created {@code AnyOf}.
     * @throws NullPointerException if the given array is {@code null}.
     * @throws NullPointerException if any of the elements in the given array is {@code null}.
     */
    public <T> Condition<T> allOf(Condition<? super T>... conditions) {
        return AllOf.allOf(conditions);
    }

    /**
     * Creates a new <code>{@link AllOf}</code>
     *
     * @param <T> the type of object the given condition accept.
     * @param conditions the conditions to evaluate.
     * @return the created {@code AnyOf}.
     * @throws NullPointerException if the given iterable is {@code null}.
     * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
     */
    public <T> Condition<T> allOf(Iterable<? extends Condition<? super T>> conditions) {
        return AllOf.allOf(conditions);
    }

    /**
     * Only delegate to {@link AnyOf#anyOf(Condition...)} so that Assertions offers a full feature entry point to all
     * AssertJ features (but you can use {@link AnyOf} if you prefer).
     * <p>
     * Typical usage (<code>jedi</code> and <code>sith</code> are {@link Condition}) :
     * <p>
     *
     * <pre><code class='java'> that(&quot;Vader&quot;).is(anyOf(jedi, sith));</code></pre>
     */
    public <T> Condition<T> anyOf(Condition<? super T>... conditions) {
        return AnyOf.anyOf(conditions);
    }

    /**
     * Creates a new <code>{@link AnyOf}</code>
     *
     * @param <T> the type of object the given condition accept.
     * @param conditions the conditions to evaluate.
     * @return the created {@code AnyOf}.
     * @throws NullPointerException if the given iterable is {@code null}.
     * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
     */
    public <T> Condition<T> anyOf(Iterable<? extends Condition<? super T>> conditions) {
        return AnyOf.anyOf(conditions);
    }

    /**
     * Creates a new </code>{@link DoesNotHave}</code>.
     *
     * @param condition the condition to inverse.
     * @return The Not condition created.
     */
    public <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
        return DoesNotHave.doesNotHave(condition);
    }

    /**
     * Creates a new </code>{@link Not}</code>.
     *
     * @param condition the condition to inverse.
     * @return The Not condition created.
     */
    public <T> Not<T> not(Condition<? super T> condition) {
        return Not.not(condition);
    }

    // --------------------------------------------------------------------------------------------------
    // Filter methods : not assertions but here to have a single entry point to all AssertJ features.
    // --------------------------------------------------------------------------------------------------

    /**
     * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
     * AssertJ features (but you can use {@link Filters} if you prefer).
     * <p>
     * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
     * <p>
     * Typical usage with {@link Condition} :
     * <p>
     *
     * <pre><code class='java'> that(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
     * <p>
     * and with filter language based on java bean property :
     * <p>
     *
     * <pre><code class='java'> that(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;).greaterThan(7).get())
     *           .containsOnly(james, rose);</code></pre>
     */
    public <E> Filters<E> filter(E[] array) {
        return Filters.filter(array);
    }

    /**
     * Only delegate to {@link Filters#filter(Object[])} so that Assertions offers a full feature entry point to all
     * AssertJ features (but you can use {@link Filters} if you prefer).
     * <p>
     * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
     * <p>
     * Typical usage with {@link Condition} :
     * <p>
     *
     * <pre><code class='java'> that(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
     * <p>
     * and with filter language based on java bean property :
     * <p>
     *
     * <pre><code class='java'> that(filter(players).with(&quot;pointsPerGame&quot;).greaterThan(20).and(&quot;assistsPerGame&quot;).greaterThan(7).get())
     *            .containsOnly(james, rose);</code></pre>
     */
    public <E> Filters<E> filter(Iterable<E> iterableToFilter) {
        return Filters.filter(iterableToFilter);
    }

    /**
     * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
     * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
     * value matches one of the given values.
     * <p>
     * As often, an example helps:
     *
     * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
     * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
     * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
     * Employee noname = new Employee(4L, null, 50);
     *
     * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
     *
     * that(employees).filteredOn("age", in(800, 26))
     *                      .containsOnly(yoda, obiwan, luke);</code></pre>
     *
     * @param values values to match (one match is sufficient)
     * @return the created "in" filter
     */
    public InFilter in(Object... values) {
        return InFilter.in(values);
    }

    /**
     * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
     * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
     * value matches does not match any of the given values.
     * <p>
     * As often, an example helps:
     *
     * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
     * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
     * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
     * Employee noname = new Employee(4L, null, 50);
     *
     * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
     *
     * that(employees).filteredOn("age", notIn(800, 50))
     *                      .containsOnly(luke);</code></pre>
     *
     * @param valuesNotToMatch values not to match (none of the values must match)
     * @return the created "not in" filter
     */
    public NotInFilter notIn(Object... valuesNotToMatch) {
        return NotInFilter.notIn(valuesNotToMatch);
    }

    /**
     * Create a {@link FilterOperator} to use in {@link AbstractIterableAssert#filteredOn(String, FilterOperator)
     * filteredOn(String, FilterOperation)} to express a filter keeping all Iterable elements whose property/field
     * value matches does not match the given value.
     * <p>
     * As often, an example helps:
     *
     * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
     * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
     * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
     * Employee noname = new Employee(4L, null, 50);
     *
     * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
     *
     * that(employees).filteredOn("age", not(800))
     *                      .containsOnly(luke, noname);</code></pre>
     *
     * @param valueNotToMatch the value not to match
     * @return the created "not" filter
     */
    public NotFilter not(Object valueNotToMatch) {
        return NotFilter.not(valueNotToMatch);
    }

    // --------------------------------------------------------------------------------------------------
    // File methods : not assertions but here to have a single entry point to all AssertJ features.
    // --------------------------------------------------------------------------------------------------

    /**
     * Loads the text content of a file, so that it can be passed to {@link #that(String)}.
     * <p>
     * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
     * with {@link #that(File)}.
     * </p>
     *
     * @param file the file.
     * @param charset the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(File file, Charset charset) {
        return Files.contentOf(file, charset);
    }

    /**
     * Loads the text content of a file, so that it can be passed to {@link #that(String)}.
     * <p>
     * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
     * with {@link #that(File)}.
     * </p>
     *
     * @param file the file.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws IllegalArgumentException if the given character set is not supported on this platform.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(File file, String charsetName) {
        return Files.contentOf(file, charsetName);
    }



    /**
     * Loads the text content of a file with the default character set, so that it can be passed to
     * {@link #that(String)}.
     * <p>
     * Note that this will load the entire file in memory; for larger files, there might be a more efficient alternative
     * with {@link #that(File)}.
     * </p>
     *
     * @param file the file.
     * @return the content of the file.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(File file) {
        return Files.contentOf(file, Charset.defaultCharset());
    }

    /**
     * Loads the text content of a file into a list of strings with the default charset, each string corresponding to a
     * line.
     * The line endings are either \n, \r or \r\n.
     *
     * @param file the file.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(File file) {
        return Files.linesOf(file, Charset.defaultCharset());
    }

    /**
     * Loads the text content of a file into a list of strings, each string corresponding to a line.
     * The line endings are either \n, \r or \r\n.
     *
     * @param file the file.
     * @param charset the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(File file, Charset charset) {
        return Files.linesOf(file, charset);
    }

    /**
     * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param file the file.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(File file, String charsetName) {
        return Files.linesOf(file, charsetName);
    }

    // --------------------------------------------------------------------------------------------------
    // URL/Resource methods : not assertions but here to have a single entry point to all AssertJ features.
    // --------------------------------------------------------------------------------------------------

    /**
     * Loads the text content of a URL, so that it can be passed to {@link #that(String)}.
     * <p>
     * Note that this will load the entire contents in memory.
     * </p>
     *
     * @param url the URL.
     * @param charset the character set to use.
     * @return the content of the URL.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(URL url, Charset charset) {
        return URLs.contentOf(url, charset);
    }

    /**
     * Loads the text content of a URL, so that it can be passed to {@link #that(String)}.
     * <p>
     * Note that this will load the entire contents in memory.
     * </p>
     *
     * @param url the URL.
     * @param charsetName the name of the character set to use.
     * @return the content of the URL.
     * @throws IllegalArgumentException if the given character set is not supported on this platform.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(URL url, String charsetName) {
        return URLs.contentOf(url, charsetName);
    }

    /**
     * Loads the text content of a URL with the default character set, so that it can be passed to
     * {@link #that(String)}.
     * <p>
     * Note that this will load the entire file in memory; for larger files.
     * </p>
     *
     * @param url the URL.
     * @return the content of the file.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public String contentOf(URL url) {
        return URLs.contentOf(url, Charset.defaultCharset());
    }

    /**
     * Loads the text content of a URL into a list of strings with the default charset, each string corresponding to a
     * line.
     * The line endings are either \n, \r or \r\n.
     *
     * @param url the URL.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(URL url) {
        return URLs.linesOf(url, Charset.defaultCharset());
    }

    /**
     * Loads the text content of a URL into a list of strings, each string corresponding to a line.
     * The line endings are either \n, \r or \r\n.
     *
     * @param url the URL.
     * @param charset the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(URL url, Charset charset) {
        return URLs.linesOf(url, charset);
    }

    /**
     * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
     * either \n, \r or \r\n.
     *
     * @param url the URL.
     * @param charsetName the name of the character set to use.
     * @return the content of the file.
     * @throws NullPointerException if the given charset is {@code null}.
     * @throws RuntimeIOException if an I/O exception occurs.
     */
    public List<String> linesOf(URL url, String charsetName) {
        return URLs.linesOf(url, charsetName);
    }

    // --------------------------------------------------------------------------------------------------
    // Date formatting methods : not assertions but here to have a single entry point to all AssertJ features.
    // --------------------------------------------------------------------------------------------------

    /**
     * Instead of using default strict date/time parsing, it is possible to use lenient parsing mode for default date
     * formats parser to interpret inputs that do not precisely match supported date formats (lenient parsing).
     * <p>
     * With strict parsing, inputs must match exactly date/time format.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre><code class='java'> final Date date = Dates.parse("2001-02-03");
     * final Date dateTime = parseDatetime("2001-02-03T04:05:06");
     * final Date dateTimeWithMs = parseDatetimeWithMs("2001-02-03T04:05:06.700");
     *
     * Assertions.setLenientDateParsing(true);
     *
     * // assertions will pass
     * that(date).isEqualTo("2001-01-34");
     * that(date).isEqualTo("2001-02-02T24:00:00");
     * that(date).isEqualTo("2001-02-04T-24:00:00.000");
     * that(dateTime).isEqualTo("2001-02-03T04:05:05.1000");
     * that(dateTime).isEqualTo("2001-02-03T04:04:66");
     * that(dateTimeWithMs).isEqualTo("2001-02-03T04:05:07.-300");
     *
     * // assertions will fail
     * that(date).hasSameTimeAs("2001-02-04"); // different date
     * that(dateTime).hasSameTimeAs("2001-02-03 04:05:06"); // leniency does not help here</code></pre>
     *
     * To revert to default strict date parsing, call {@code setLenientDateParsing(false)}.
     *
     * @param value whether lenient parsing mode should be enabled or not
     */
    public void setLenientDateParsing(boolean value) {
        AbstractDateAssert.setLenientDateParsing(value);
    }

    /**
     * Add the given date format to the ones used to parse date String in String based Date assertions like
     * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
     * <p>
     * User date formats are used before default ones in the order they have been registered (first registered, first
     * used).
     * <p>
     * AssertJ is gonna use any date formats registered with one of these methods :
     * <ul>
     * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
     * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
     * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
     * <li>{@link #registerCustomDateFormat(String)}</li>
     * </ul>
     * <p>
     * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
     * <p>
     * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
     * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
     * <p>
     * Code examples:
     *
     * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
     * that(date).isEqualTo("2003-04-26");
     *
     * try {
     *   // date with a custom format : failure since the default formats don't match.
     *   that(date).isEqualTo("2003/04/26");
     * } catch (AssertionError e) {
     *   that(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
     *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
     * }
     *
     * // registering a custom date format to make the assertion pass
     * registerCustomDateFormat(new SimpleDateFormat("yyyy/MM/dd")); // registerCustomDateFormat("yyyy/MM/dd") would work to.
     * that(date).isEqualTo("2003/04/26");
     *
     * // the default formats are still available and should work
     * that(date).isEqualTo("2003-04-26");</code></pre>
     *
     * @param userCustomDateFormat the new Date format used for String based Date assertions.
     */
    public void registerCustomDateFormat(DateFormat userCustomDateFormat) {
        AbstractDateAssert.registerCustomDateFormat(userCustomDateFormat);
    }

    /**
     * Add the given date format to the ones used to parse date String in String based Date assertions like
     * {@link org.assertj.core.api.AbstractDateAssert#isEqualTo(String)}.
     * <p>
     * User date formats are used before default ones in the order they have been registered (first registered, first
     * used).
     * <p>
     * AssertJ is gonna use any date formats registered with one of these methods :
     * <ul>
     * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(String)}</li>
     * <li>{@link org.assertj.core.api.AbstractDateAssert#withDateFormat(java.text.DateFormat)}</li>
     * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
     * <li>{@link #registerCustomDateFormat(String)}</li>
     * </ul>
     * <p>
     * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
     * <p>
     * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
     * {@link org.assertj.core.api.AbstractDateAssert#withDefaultDateFormatsOnly()}.
     * <p>
     * Code examples:
     *
     * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
     * that(date).isEqualTo("2003-04-26");
     *
     * try {
     *   // date with a custom format : failure since the default formats don't match.
     *   that(date).isEqualTo("2003/04/26");
     * } catch (AssertionError e) {
     *   that(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
     *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
     * }
     *
     * // registering a custom date format to make the assertion pass
     * registerCustomDateFormat("yyyy/MM/dd");
     * that(date).isEqualTo("2003/04/26");
     *
     * // the default formats are still available and should work
     * that(date).isEqualTo("2003-04-26");</code></pre>
     *
     * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
     */
    public void registerCustomDateFormat(String userCustomDateFormatPattern) {
        AbstractDateAssert.registerCustomDateFormat(userCustomDateFormatPattern);
    }

    /**
     * Remove all registered custom date formats => use only the defaults date formats to parse string as date.
     * <p>
     * Beware that the default formats are expressed in the current local timezone.
     * <p>
     * Defaults date format are:
     * <ul>
     * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
     * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code> (for {@link Timestamp} String representation support)</li>
     * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
     * <li><code>yyyy-MM-dd</code></li>
     * </ul>
     * <p>
     * Example of valid string date representations:
     * <ul>
     * <li><code>2003-04-26T03:01:02.999</code></li>
     * <li><code>2003-04-26 03:01:02.999</code></li>
     * <li><code>2003-04-26T13:01:02</code></li>
     * <li><code>2003-04-26</code></li>
     * </ul>
     */
    public void useDefaultDateFormatsOnly() {
        AbstractDateAssert.useDefaultDateFormatsOnly();
    }

    /**
     * Delegates the creation of the {@link Assert} to the {@link AssertProvider#assertThat()} of the given component.
     *
     * <p>
     * Read the comments on {@link AssertProvider} for an example of its usage.
     * </p>
     *
     * @param component
     *          the component that creates its own assert
     * @return the associated {@link Assert} of the given component
     */
    public <T> T that(final AssertProvider<T> component) {
        return assertThat(component);
    }

    /**
     * Creates a new instance of <code>{@link CharSequenceAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public AbstractCharSequenceAssert<?, ? extends CharSequence> that(CharSequence actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link IterableAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <ELEMENT> AbstractIterableAssert<?, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> that(Iterable<? extends ELEMENT> actual) {
        return new IterableAssert<>(actual);
    }

    /**
     * Creates a new instance of <code>{@link IterableAssert}</code>.
     * <p>
     * <b>Be aware that calls to most methods on returned IterableAssert will consume Iterator so it won't be possible to
     * iterate over it again.</b> Calling multiple methods on returned IterableAssert is safe as Iterator's elements are
     * cached by IterableAssert first time Iterator is consumed.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <ELEMENT> AbstractIterableAssert<?, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> that(Iterator<? extends ELEMENT> actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ListAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> that(List<? extends ELEMENT> actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link ListAssert}</code> from the given {@link Stream}.
     * <p>
     * <b>Be aware that to create the returned {@link ListAssert} the given the {@link Stream} is consumed so it won't be
     * possible to use it again.</b> Calling multiple methods on the returned {@link ListAssert} is safe as it only
     * interacts with the {@link List} built from the {@link Stream}.
     *
     * @param actual the actual {@link Stream} value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> that(Stream<? extends ELEMENT> actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of {@link PathAssert}
     *
     * @param actual the path to test
     * @return the created assertion object
     */
    @CheckReturnValue
    public AbstractPathAssert<?> that(Path actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link MapAssert}</code>.
     * <p>
     * Returned type is {@link MapAssert} as it overrides method to annotate them with {@link SafeVarargs} avoiding
     * annoying warnings.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <K, V> MapAssert<K, V> that(Map<K, V> actual) {
        return assertThat(actual);
    }

    /**
     * Creates a new instance of <code>{@link GenericComparableAssert}</code> with
     * standard comparison semantics.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public <T extends Comparable<? super T>> AbstractComparableAssert<?, T> that(T actual) {
        return assertThat(actual);
    }

    /**
     * Returns the given assertion. This method improves code readability by surrounding the given assertion with
     * <code>assertThat</code>.
     * <p>
     * Consider for example the following MyButton and MyButtonAssert classes:
     * <pre><code class='java'> public class MyButton extends JButton {
     *
     *   private boolean blinking;
     *
     *   public boolean isBlinking() { return this.blinking; }
     *
     *   public void setBlinking(boolean blink) { this.blinking = blink; }
     *
     * }
     *
     * private static class MyButtonAssert implements AssertDelegateTarget {
     *
     *   private MyButton button;
     *   MyButtonAssert(MyButton button) { this.button = button; }
     *
     *   void isBlinking() {
     *     // standard assertion from core assertThat
     *     that(button.isBlinking()).isTrue();
     *   }
     *
     *   void isNotBlinking() {
     *     // standard assertion from core assertThat
     *     that(button.isBlinking()).isFalse();
     *   }
     * }</code></pre>
     *
     * As MyButtonAssert implements AssertDelegateTarget, you can use <code>assertThat(buttonAssert).isBlinking();</code>
     * instead of <code>buttonAssert.isBlinking();</code> to have easier to read assertions:
     * <pre><code class='java'> {@literal @}Test
     * public void AssertDelegateTarget_example() {
     *
     *   MyButton button = new MyButton();
     *   MyButtonAssert buttonAssert = new MyButtonAssert(button);
     *
     *   // you can encapsulate MyButtonAssert assertions methods within assertThat
     *   that(buttonAssert).isNotBlinking(); // same as : buttonAssert.isNotBlinking();
     *
     *   button.setBlinking(true);
     *
     *   that(buttonAssert).isBlinking(); // same as : buttonAssert.isBlinking();
     * }</code></pre>
     *
     * @param <T> the generic type of the user-defined assert.
     * @param assertion the assertion to return.
     * @return the given assertion.
     */
    @CheckReturnValue
    public <T extends AssertDelegateTarget> T that(T assertion) {
        return assertion;
    }

    /**
     * Register a {@link Representation} that will be used in all following assertions.
     * <p>
     * {@link Representation} are used to format types in assertions error messages.
     * <p>
     * Example :
     * <pre><code class='java'> private class Example {}
     *
     * private class CustomRepresentation extends StandardRepresentation {
     *
     *   // override needed to hook specific formatting
     *   {@literal @}Override
     *   public String toStringOf(Object o) {
     *     if (o instanceof Example) return "Example";
     *     // fallback to default formatting.
     *     return super.toStringOf(o);
     *   }
     *
     *   // change String representation
     *   {@literal @}Override
     *   protected String toStringOf(String s) {
     *     return "$" + s + "$";
     *   }
     * }
     *
     * Assertions.useRepresentation(new CustomRepresentation());
     *
     * // this assertion fails ...
     * that(new Example()).isNull();
     * // ... with error :
     * // "expected:<[null]> but was:<[Example]>"
     *
     * // this one fails ...
     * that("foo").startsWith("bar");
     * // ... with error :
     * // Expecting:
     * //   <$foo$>
     * // to start with:
     * //   <$bar$></code></pre>
     *
     * @since 2.5.0 / 3.5.0
     */
    public void useRepresentation(Representation customRepresentation) {
        AbstractAssert.setCustomRepresentation(customRepresentation);
    }

    /**
     * Assertions error messages uses a {@link Representation} to format the different types involved, using this method
     * you can control the formatting of a given type by providing a specific formatter.
     *
     *
     * <p>
     * Registering a formatter makes it available for all AssertJ {@link Representation}:
     * <ul>
     * <li>{@link StandardRepresentation}</li>
     * <li>{@link UnicodeRepresentation}</li>
     * <li>{@link HexadecimalRepresentation}</li>
     * <li>{@link BinaryRepresentation}</li>
     * </ul>
     * <p>
     * Example :
     * <pre><code class='java'> // without specific formatter
     * that(STANDARD_REPRESENTATION.toStringOf(123L)).isEqualTo("123L");
     *
     * // register a formatter for Long
     * Assertions.registerFormatterForType(Long.class, value -> "$" + value + "$");
     *
     * // now Long will be formatted between in $$ in error message.
     * that(STANDARD_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
     *
     * // fails with error : expected:<$456$> but was:<$123$>
     * that(123L).isEqualTo(456L);</code></pre>
     *
     * @param type
     * @param formatter
     *
     * @since 3.5.0
     */
    public void registerFormatterForType(Class<?> type, Function<Object, String> formatter) {
        StandardRepresentation.registerFormatterForType(type, formatter);
    }

    /**
     * Fallback to use {@link StandardRepresentation} to revert the effect of calling {@link #useRepresentation(Representation)}.
     *
     * @since 2.5.0 / 3.5.0
     */
    public void useDefaultRepresentation() {
        StandardRepresentation.removeAllRegisteredFormatters();
        AbstractAssert.setCustomRepresentation(STANDARD_REPRESENTATION);
    }

    /**
     * Creates a new </code>{@link Assertions}</code>.
     */
    public Assert() {

    }


    public Assert Assert() {
        return new Assert();
    }

}
