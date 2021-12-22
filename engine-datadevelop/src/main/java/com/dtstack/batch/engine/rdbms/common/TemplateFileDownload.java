/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.batch.engine.rdbms.common;

import com.dtstack.engine.common.exception.RdosDefineException;
import org.apache.commons.lang3.StringUtils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sanyue
 * @date 2019/8/23
 */
public class TemplateFileDownload implements IDownload {

    private RandomAccessFile aFile;

    private int bytesRead;

    private FileChannel inChannel;

    private ByteBuffer buf;

    private CharBuffer cb;

    private CharsetDecoder decoder;

    private String filePath;

    private int bufferSize = 512;

    private int charBufferSize = 512;

    private String CODE = "UTF-8";

    private String MODE = "r";

    public TemplateFileDownload(String filePath, Integer bufferSize) {
        this.filePath = filePath;
        this.bufferSize = bufferSize;
    }

    public TemplateFileDownload(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void configure() {
        try {
            aFile = new RandomAccessFile(filePath, MODE);
        } catch (Exception e) {
            throw new RdosDefineException(String.format("文件找不到，原因是：%s", e.getMessage()));
        }
        inChannel = aFile.getChannel();
        buf = ByteBuffer.allocate(bufferSize);
        try {
            bytesRead = inChannel.read(buf);
        } catch (Exception e) {
            throw new RdosDefineException(String.format("读取字节异常，原因是：%s", e.getMessage()));
        }
        //Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
        Charset charset = Charset.forName(CODE);
        decoder = charset.newDecoder();
        cb = CharBuffer.allocate(charBufferSize);
    }

    @Override
    public List<String> getMetaInfo() {
        return new ArrayList<>();
    }

    @Override
    public Object readNext() {
        buf.flip();
        decoder.decode(buf, cb, false);
        cb.flip();
        StringBuilder sb = new StringBuilder();
        while(cb.hasRemaining()){
            sb.append(cb.get());

        }
        buf.clear();
        cb.clear();
        try {
            bytesRead = inChannel.read(buf);
        } catch (Exception e) {
            throw new RdosDefineException(String.format("readNext异常，原因是：%s", e.getMessage()));
        }
        return sb.toString();
    }

    @Override
    public boolean reachedEnd() {
        return bytesRead == -1;
    }

    @Override
    public void close() throws Exception {
        aFile.close();
    }

    @Override
    public String getFileName() {
        if (StringUtils.isNotBlank(filePath) && filePath.contains("/")) {
            return filePath.substring(filePath.lastIndexOf("/") + 1);
        }
        return filePath;
    }
}
