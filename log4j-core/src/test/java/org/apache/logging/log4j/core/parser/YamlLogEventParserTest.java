/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class YamlLogEventParserTest extends LogEventParserTest {

    private YamlLogEventParser parser;

    private static final String YAML = "---\n" +
            "timeMillis: 1493121664118\n" +
            "thread: \"main\"\n" +
            "level: \"INFO\"\n" +
            "loggerName: \"HelloWorld\"\n" +
            "marker:\n" +
            " name: \"child\"\n" +
            " parents:\n" +
            " - name: \"parent\"\n" +
            "   parents:\n" +
            "   - name: \"grandparent\"\n" +
            "message: \"Hello, world!\"\n" +
            "thrown:\n" +
            " commonElementCount: 0\n" +
            " message: \"error message\"\n" +
            " name: \"java.lang.RuntimeException\"\n" +
            " extendedStackTrace:\n" +
            " - class: \"logtest.Main\"\n" +
            "   method: \"main\"\n" +
            "   file: \"Main.java\"\n" +
            "   line: 29\n" +
            "   exact: true\n" +
            "   location: \"classes/\"\n" +
            "   version: \"?\"\n" +
            "contextStack:\n" +
            "- \"one\"\n" +
            "- \"two\"\n" +
            "endOfBatch: false\n" +
            "loggerFqcn: \"org.apache.logging.log4j.spi.AbstractLogger\"\n" +
            "contextMap:\n" +
            " bar: \"BAR\"\n" +
            " foo: \"FOO\"\n" +
            "threadId: 1\n" +
            "threadPriority: 5\n" +
            "source:\n" +
            " class: \"logtest.Main\"\n" +
            " method: \"main\"\n" +
            " file: \"Main.java\"\n" +
            " line: 29";

    @Before
    public void setup() {
        parser = new YamlLogEventParser();
    }

    @Test
    public void testString() throws ParseException {
        LogEvent logEvent = parser.parseFrom(YAML);
        assertLogEvent(logEvent);
    }

    @Test(expected = ParseException.class)
    public void testStringEmpty() throws ParseException {
        parser.parseFrom("");
    }

    @Test(expected = ParseException.class)
    public void testStringInvalidYaml() throws ParseException {
        parser.parseFrom("foobar");
    }

    @Test(expected = ParseException.class)
    public void testStringInvalidProperty() throws ParseException {
        parser.parseFrom("---\nfoo: \"bar\"\n");
    }

    @Test
    public void testByteArray() throws ParseException {
        LogEvent logEvent = parser.parseFrom(YAML.getBytes(StandardCharsets.UTF_8));
        assertLogEvent(logEvent);
    }

    @Test
    public void testByteArrayOffsetLength() throws ParseException {
        byte[] bytes = ("abc" + YAML + "def").getBytes(StandardCharsets.UTF_8);
        LogEvent logEvent = parser.parseFrom(bytes, 3, bytes.length - 6);
        assertLogEvent(logEvent);
    }

    @Test
    public void testReader() throws ParseException, IOException {
        LogEvent logEvent = parser.parseFrom(new StringReader(YAML));
        assertLogEvent(logEvent);
    }

    @Test
    public void testInputStream() throws ParseException, IOException {
        LogEvent logEvent = parser.parseFrom(new ByteArrayInputStream(YAML.getBytes(StandardCharsets.UTF_8)));
        assertLogEvent(logEvent);
    }

}
