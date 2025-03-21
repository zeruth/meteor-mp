/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * @author Rustem V. Rafikov
 */
package javax.imageio.event;


import javax.imageio.ImageWriter;
import java.util.EventListener;

public interface IIOWriteProgressListener extends EventListener {
    void imageStarted(ImageWriter source, int imageIndex);

    void imageProgress(ImageWriter source, float percentageDone);

    void imageComplete(ImageWriter source);

    void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex);

    void thumbnailProgress(ImageWriter source, float percentageDone);

    void thumbnailComplete(ImageWriter source);

    void writeAborted(ImageWriter source);
}
