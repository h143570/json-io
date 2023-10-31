package com.cedarsoftware.util.io;

import com.cedarsoftware.util.DeepEquals;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JsonReader / JsonWriter
 *
 * @author John DeRegnaucourt (jdereg@gmail.com)
 * <br>
 * Copyright (c) Cedar Software LLC
 * <br><br>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br><br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <br><br>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class UnmodifiableCollectionTest
{
    @Test
    public void testUnmodifiableCollection()
    {
        Collection<String> col = new ArrayList<>();
        col.add("foo");
        col.add("bar");
        col.add("baz");
        col.add("qux");
        col = Collections.unmodifiableCollection(col);
        String json = TestUtil.toJson(col);
        List<String> root = TestUtil.toJava(json);
        assert root.size() == 4;
        assert DeepEquals.deepEquals(root, List.of("foo", "bar", "baz", "qux"));

        col = new ArrayList<>();
        col.add("foo");
        col.add("bar");
        col.add("baz");
        col.add("qux");
        col = Collections.unmodifiableList((List<String>)col);
        json = TestUtil.toJson(col);
        root = TestUtil.toJava(json);
        assert root.size() == 4;
        assert DeepEquals.deepEquals(root, List.of("foo", "bar", "baz", "qux"));
    }

    @Test
    public void testUnmodifiableSet()
    {
        Set<String> col = new LinkedHashSet<>();
        col.add("foo");
        col.add("bar");
        col.add("baz");
        col.add("qux");
        col = Collections.unmodifiableSet(col);
        String json = TestUtil.toJson(col);
        Set<String> root = TestUtil.toJava(json);
        assert root.size() == 4;
        assert DeepEquals.deepEquals(root, Set.of("foo", "bar", "baz", "qux"));

        col = new HashSet<>();
        col.add("foo");
        col.add("bar");
        col.add("baz");
        col.add("qux");
        col = Collections.unmodifiableSet(col);
        json = TestUtil.toJson(col);
        root = TestUtil.toJava(json);
        assert root instanceof Set;
        assert root.size() == 4;
        assert DeepEquals.deepEquals(root, Set.of("foo", "bar", "baz", "qux"));

        col = new TreeSet<>();
        col.add("foo");
        col.add("bar");
        col.add("baz");
        col.add("qux");
        col = Collections.unmodifiableSortedSet((SortedSet<String>) col);
        json = TestUtil.toJson(col);
        root = TestUtil.toJava(json);
        assert root instanceof SortedSet;
        assert root.size() == 4;
        assert DeepEquals.deepEquals(root, Set.of("bar", "baz", "foo", "qux"));
        // DeepEquals does not impose order on Sets for equivalency, which is correct
        assertEquals(root.iterator().next(), "bar");
    }

    @Test
    public void testUnmodifiableMap()
    {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("foo", "foot");
        map.put("bar", "bart");
        map.put("baz", "bastard");
        map.put("qux", "quixotic");
        map = Collections.unmodifiableMap(map);
        String json = TestUtil.toJson(map);
        Map root = TestUtil.toJava(json);
        assert root instanceof Map;
        assert root.size() == 4;
        assert root.get("foo").equals("foot");
        assert root.get("bar").equals("bart");
        assert root.get("baz").equals("bastard");
        assert root.get("qux").equals("quixotic");

        map = new TreeMap<>();
        map.put("foo", "foot");
        map.put("bar", "bart");
        map.put("baz", "bastard");
        map.put("qux", "quixotic");
        map = Collections.unmodifiableSortedMap((SortedMap<String, String>) map);
        json = TestUtil.toJson(map);
        root = TestUtil.toJava(json);
        assert root instanceof SortedMap;
        assert root.size() == 4;
        assert root.get("foo").equals("foot");
        assert root.get("bar").equals("bart");
        assert root.get("baz").equals("bastard");
        assert root.get("qux").equals("quixotic");
        // Ensure order (at least first item)
        assertEquals(root.keySet().iterator().next(), "bar");
    }

    @Test
    public void testUnmodifiableMapHolder()
    {
        UnmodifiableMapHolder holder = new UnmodifiableMapHolder();
        String json = TestUtil.toJson(holder);
        UnmodifiableMapHolder holder1 = TestUtil.toJava(json);
        assert holder1.getMap().get("North").equals(0);
        assert holder1.getMap().get("South").equals(1);
        assert holder1.getMap().get("East").equals(2);
        assert holder1.getMap().get("West").equals(3);
    }

    public static class UnmodifiableMapHolder
    {
        public UnmodifiableMapHolder()
        {
            Map directions = new LinkedHashMap<>();
            directions.put("North", 0);
            directions.put("South", 1);
            directions.put("East", 2);
            directions.put("West", 3);
            map = Collections.unmodifiableMap(directions);
        }

        public Map getMap()
        {
            return map;
        }

        public void setMap(Map map)
        {
            this.map = map;
        }

        private Map map;
    }
}