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

package com.badlogic.gdx.ai.tests.steer.bullet;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.mini2Dx.gdx.math.Vector3;

/** An {@link InputProcessor} that allows you to manually move a {@link SteeringBulletEntity}.
 * 
 * @author Daniel Holderbaum
 * @author davebaol */
public class BulletTargetInputProcessor extends InputAdapter {

	SteeringBulletEntity target;
	Viewport viewport;
	btCollisionWorld world;
	Vector3 offset;

	public BulletTargetInputProcessor (SteeringBulletEntity target, Vector3 offset, Viewport viewport, btCollisionWorld world) {
		this.target = target;
		this.viewport = viewport;
		this.world = world;
		this.offset = offset;
	}

	private static final Collision<Vector3> output = new Collision<Vector3>(new Vector3(), new Vector3());

	boolean moveTarget = false;

	public boolean keyDown (int keycode) {
		if (keycode == Keys.SPACE) {
			moveTarget = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.SPACE) {
			moveTarget = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		setTargetPosition(screenX, screenY);
		return moveTarget;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		setTargetPosition(screenX, screenY);
		return moveTarget;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return setTargetPosition(screenX, screenY);
	}

	private boolean setTargetPosition (int screenX, int screenY) {
		if (moveTarget) {
			Ray pickRay = viewport.getPickRay(screenX, screenY);
			btCollisionObject body = rayTest(output, pickRay);

			if (body != null && body.userData != null && body.userData.equals("ground")) {
				output.point.add(offset);
				target.transform.setToTranslation(output.point.x, output.point.y, output.point.z);
				target.body.setWorldTransform(target.transform);
			}
			return true;
		}
		return false;
	}

	private static final com.badlogic.gdx.math.Vector3 rayFrom = new com.badlogic.gdx.math.Vector3();
	private static final com.badlogic.gdx.math.Vector3 rayTo = new com.badlogic.gdx.math.Vector3();
	private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

	private btCollisionObject rayTest (Collision<Vector3> output, Ray ray) {
		rayFrom.set(ray.origin);
		// 500 meters max from the origin
		rayTo.set(ray.direction).scl(500f).add(rayFrom);

		// we reuse the ClosestRayResultCallback, thus we need to reset its
		// values
		callback.setCollisionObject(null);
		callback.setClosestHitFraction(1f);
		callback.setRayFromWorld(rayFrom);
		callback.setRayToWorld(rayTo);

		world.rayTest(rayFrom, rayTo, callback);

		if (callback.hasHit()) {
			callback.getHitPointWorld(new com.badlogic.gdx.math.Vector3(output.point.x, output.point.y, output.point.z));
			callback.getHitNormalWorld(new com.badlogic.gdx.math.Vector3(output.normal.x, output.normal.y, output.normal.z));
			return callback.getCollisionObject();
		}

		return null;
	}
}
