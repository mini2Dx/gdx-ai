/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.*;

import java.io.File;

/** @author davebaol */
public class MdxFileSystem implements FileSystem {

	public MdxFileSystem() {
	}

	@Override
	public FileHandleResolver newResolver (FileType fileType) {
		switch (fileType) {
		case INTERNAL:
			return new InternalFileHandleResolver();
		case EXTERNAL:
			return new ExternalFileHandleResolver();
		case LOCAL:
			return new LocalFileHandleResolver();
		}
		return null; // Should never happen
	}

	@Override
	public FileHandle newFileHandle (String fileName) {
		return Mdx.files.external(fileName);
	}

	@Override
	public FileHandle newFileHandle (File file) {
		return Mdx.files.external(file.getAbsolutePath());
	}

	@Override
	public FileHandle newFileHandle (String fileName, FileType type) {
		switch(type)
		{
		default:
		case INTERNAL:
			return Mdx.files.internal(fileName);
		case EXTERNAL:
			return Mdx.files.external(fileName);
		case LOCAL:
			return Mdx.files.local(fileName);
		}
	}

	@Override
	public FileHandle newFileHandle (File file, FileType type) {
		switch(type)
		{
		default:
		case INTERNAL:
			return Mdx.files.internal(file.getPath());
		case EXTERNAL:
			return Mdx.files.external(file.getPath());
		case LOCAL:
			return Mdx.files.local(file.getPath());
		}
	}

}
