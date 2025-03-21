/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.imaging.formats.tiff.constants;

import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Alias Sketchbook Pro multi-layer TIFF
 * <BR>
 * http://www.awaresystems.be/imaging/tiff/tifftags/docs/alias.html
 */
public interface AliasSketchbookProTagConstants {
    TagInfoAscii EXIF_TAG_ALIAS_LAYER_METADATA = new TagInfoAscii(
            "Alias Layer Metadata", 0xc660, -1,
            TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);

    List<TagInfo> ALL_ALIAS_SKETCHBOOK_PRO_TAGS =
            Collections.unmodifiableList(Arrays.asList(new TagInfo[]{
                    EXIF_TAG_ALIAS_LAYER_METADATA}));
}
