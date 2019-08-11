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

package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import org.mini2Dx.core.assets.*;
import org.mini2Dx.gdx.json.StreamUtils;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;
import java.io.Reader;

/** {@link AssetLoader} for {@link BehaviorTree} instances. The behavior tree is loaded asynchronously.
 * 
 * @author davebaol */
@SuppressWarnings("rawtypes")
public class BehaviorTreeLoader implements AsyncAssetLoader<BehaviorTree> {

	BehaviorTree behaviorTree;

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		this.behaviorTree = null;

		Object blackboard = null;
		BehaviorTreeParser parser = null;
		if (assetDescriptor.getParameters() != null) {
			BehaviorTreeParameter treeParameter = (BehaviorTreeParameter) assetDescriptor.getParameters();
			blackboard = treeParameter.blackboard;
			parser = treeParameter.parser;
		}

		if (parser == null) parser = new BehaviorTreeParser();

		Reader reader = null;
		try {
			reader = assetDescriptor.getResolvedFileHandle().reader();
			this.behaviorTree = parser.parse(reader, blackboard);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}

	@Override
	public BehaviorTree loadOnGameThread(AssetManager assetManager, AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		BehaviorTree bundle = this.behaviorTree;
		this.behaviorTree = null;
		return bundle;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		return null;
	}

	public static class BehaviorTreeParameter implements AssetProperties<BehaviorTree> {
		public final Object blackboard;
		public final BehaviorTreeParser parser;

		public BehaviorTreeParameter () {
			this(null);
		}

		public BehaviorTreeParameter (Object blackboard) {
			this(blackboard, null);
		}

		public BehaviorTreeParameter (Object blackboard, BehaviorTreeParser parser) {
			this.blackboard = blackboard;
			this.parser = parser;
		}
	}

}
