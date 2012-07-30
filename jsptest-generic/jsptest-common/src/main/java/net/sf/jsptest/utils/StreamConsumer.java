/*
 * Copyright 2008 Lasse Koskela.
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
package net.sf.jsptest.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @author Lasse Koskela
 */
public class StreamConsumer implements Runnable {

    private final String outputPrefix;
    private final InputStream stream;
    private final PrintWriter log;

    public StreamConsumer(String streamName, InputStream stream, PrintWriter log) {
        this.outputPrefix = streamName + ":";
        this.stream = stream;
        this.log = log;
    }

    public void run() {
        String output = null;
        try {
            output = consume(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.println(outputPrefix);
        log.println(output);
    }

    private String consume(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream(8096);
        byte[] buffer = new byte[8096];
        int r = -1;
        while ((r = stream.read(buffer, 0, buffer.length)) != -1) {
            result.write(buffer, 0, r);
        }
        stream.close();
        return result.toString();
    }
}
