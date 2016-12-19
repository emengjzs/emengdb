/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

import emengjzs.emengdb.db.SkipList;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by emengjzs on 2016/8/28.
 */

public class SkipListTest {

    static Assertions ASSERT;

    @Test
    public void testRandomInsert() {
        Integer a[] = new Integer[10000];
        for (int i = 0; i < 10000; i ++ ) {
            a[i] = i;
        }
        List<Integer> ints = Arrays.asList(a);
        Collections.shuffle(ints);
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        ints.forEach((skipList::insert));
        List<Integer> integers = skipList.toList();
        for (int i = 0; i < 10000; i ++ ) {
            Assert.assertEquals(i, integers.get(i).intValue());
        }
    }

    @Test
    public void testContains() {
        Integer a[] = new Integer[] {63, 42, 41, 0, 7, -4, 1};
        List<Integer> ints = Arrays.asList(a);
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        ints.forEach((skipList::insert));
        Assert.assertTrue(skipList.contains(63));
        Assert.assertTrue(skipList.contains(7));
        Assert.assertFalse(skipList.contains(2));
        Assert.assertTrue(skipList.contains(0));
    }


    @Test
    public void testEmptyIterator() {
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        ListIterator<Integer> integerListIterator = skipList.listIterator();
        Assert.assertFalse(integerListIterator.hasNext());
        assertThat(integerListIterator.hasPrevious()).isFalse();
    }

    @Test
    public void testOneElementIterator() {
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        skipList.insert(2);
        ListIterator<Integer> integerListIterator = skipList.listIterator();
        assertThat(integerListIterator.hasNext()).isTrue();
        assertThat(integerListIterator.hasPrevious()).isFalse();
        assertThat(integerListIterator.next()).isEqualTo(2);
        assertThat(integerListIterator.hasPrevious()).isFalse();
        assertThat(integerListIterator.hasNext()).isFalse();
    }

    @Test
    public void testMoreElementIterator() {
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        Integer a[] = {2, 23, 24, 25, 266};

        for (int x : a) {
            skipList.insert(x);
        }


        ListIterator<Integer> integerListIterator = skipList.listIterator();
        assertThat(integerListIterator.hasNext()).isTrue();
        assertThat(integerListIterator.hasPrevious()).isFalse();

        assertThat(integerListIterator.next()).isEqualTo(2);
        assertThat(integerListIterator.hasPrevious()).isFalse();
        assertThat(integerListIterator.hasNext()).isTrue();

        assertThat(integerListIterator.next()).isEqualTo(23);
        assertThat(integerListIterator.hasNext()).isTrue();
        assertThat(integerListIterator.hasPrevious()).isTrue();

        assertThat(integerListIterator.next()).isEqualTo(24);
        assertThat(integerListIterator.hasNext()).isTrue();
        assertThat(integerListIterator.hasPrevious()).isTrue();

        assertThat(integerListIterator.previous()).isEqualTo(23);
        assertThat(integerListIterator.hasNext()).isTrue();
        assertThat(integerListIterator.hasPrevious()).isTrue();


        integerListIterator = skipList.listIterator();
        Iterator<Integer> itr = Arrays.asList(a).iterator();
        integerListIterator.forEachRemaining((e) -> {
            assertThat(itr.next()).isEqualTo(e);
        });
    }

    @Test
    public void testMaxSeq() {
        Assertions.assertThat((0x01L << 56) - 1).isGreaterThan(0L);
    }

    @Test
    public void testSpecifiedIterator() {
        SkipList<Integer> skipList = new SkipList<>(Integer::compareTo);
        Integer a[] = {2, 23, 24, 25, 266};

        for (int x : a) {
            skipList.insert(x);
        }
        assertThat(skipList.listIterator(24).next()).isEqualTo(24);
        assertThat(skipList.listIterator(2).next()).isEqualTo(2);
        assertThat(skipList.listIterator(266).next()).isEqualTo(266);
        assertThat(skipList.listIterator(21).next()).isEqualTo(23);
        assertThat(skipList.listIterator(276).hasNext()).isFalse();
    }
;}


