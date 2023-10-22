package com.cedarsoftware.util.io;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License")
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
class InnerClassTest
{
    static public class A
    {
        public String a;

        class B
        {
            public String b;

            public B()
            {
                // No args constructor for B
            }
        }
    }

    @Test
    void testChangedClass()
    {
        Dog dog = new Dog();
        dog.x = 10;
        Dog.Leg leg = dog.new Leg();       // original Java
        leg.y = 20;
        String json0 = TestUtil.getJsonString(dog);
        TestUtil.printLine("json0=" + json0);
        Map<String, Object> args = new HashMap<>();
        args.put(JsonReader.USE_MAPS, true);
        JsonObject job = JsonReader.jsonToJava(json0, args);
        job.put("phantom", new TestObject("Eddie"));
        String json1 = TestUtil.getJsonString(job);
        TestUtil.printLine("json1=" + json1);
        assertTrue(json1.contains("phantom"));
        assertTrue(json1.contains("TestObject"));
        assertTrue(json1.contains("_other"));
    }

    @Test
    void testInner()
    {
        A a = new A();
        a.a = "aaa";

        String json = TestUtil.getJsonString(a);
        TestUtil.printLine("json = " + json);
        A o1 = (A) TestUtil.readJsonObject(json);
        assertEquals("aaa", o1.a);

        InnerClassTest.A.B b = a.new B();        // Original Java
        b.b = "bbb";
        json = TestUtil.getJsonString(b);
        TestUtil.printLine("json = " + json);
        InnerClassTest.A.B o2 = TestUtil.readJsonObject(json);
        assertEquals("bbb", o2.b);
    }

    @Test
    void testInnerInstance()
    {
        Dog dog = new Dog();
        dog.x = 10;
        Dog.Leg leg = dog.new Leg();
        leg.y = 20;
        String json0 = TestUtil.getJsonString(dog);
        TestUtil.printLine("json0=" + json0);

        String json1 = TestUtil.getJsonString(leg);
        TestUtil.printLine("json1=" + json1);
        Dog.Leg go = (Dog.Leg) TestUtil.readJsonObject(json1);
        assertEquals(20, go.y);
        assertEquals(10, go.getParentX());
    }
}