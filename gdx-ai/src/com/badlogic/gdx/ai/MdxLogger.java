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

/** @author davebaol */
public class MdxLogger implements Logger {

	public MdxLogger() {
	}

	@Override
	public void debug (String tag, String message) {
		Mdx.log.debug(tag, message);
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		Mdx.log.debug(tag, message);
		exception.printStackTrace();
	}

	@Override
	public void info (String tag, String message) {
		Mdx.log.info(tag, message);
	}

	@Override
	public void info (String tag, String message, Throwable exception) {
		Mdx.log.info(tag, message);
		exception.printStackTrace();
	}

	@Override
	public void error (String tag, String message) {
		Mdx.log.error(tag, message);
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		Mdx.log.error(tag, message);
		exception.printStackTrace();
	}

}
