package com.sidmeier.campuschaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sidmeier.campuschaos.utils.Constants;

public class CampusChaos extends ApplicationAdapter {

    private Viewport viewport;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private Sprite mapSprite;

    /**
     * Defines mapSprite, camera and viewport
     */
	@Override
	public void create () {
        batch = new SpriteBatch();
        mapSprite = new Sprite(new Texture(Gdx.files.internal("map_placeholder.jpg")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        viewport.apply();

        cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
	}

    /**
     * Updates camera position, clears screen and draws mapSprite
     */
	@Override
	public void render () {
        handleInput();
        cam.update();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        mapSprite.draw(batch);
        batch.end();
	}

    /**
     * Handles user input to move camera, ensures camera doesn't leave world
     */
    private void handleInput() {
        float camSpeed = 3;    // Camera speed in units per frame

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-camSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(camSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -camSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, camSpeed, 0);
        }

        // Edge scrolling
        float edgePercent = 5;  // Percentage of APP edge that will cause scrolling on mouseover
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

        float leftEdge = ((Constants.APP_WIDTH / 100) * edgePercent);
        float rightEdge = Constants.APP_WIDTH - leftEdge;
        float bottomEdge = ((Constants.APP_HEIGHT / 100) * edgePercent);
        float topEdge = Constants.APP_HEIGHT - bottomEdge;

        if (x <= leftEdge) {
            cam.translate(-camSpeed, 0, 0);
        } else if (x >= rightEdge) {
            cam.translate(camSpeed, 0, 0);
        }

        if (y <= bottomEdge) {
            cam.translate(0, camSpeed, 0);
        } else if (y >= topEdge) {
            cam.translate(0, -camSpeed, 0);
        }

        // Edge clamping
        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, Constants.WORLD_HEIGHT/cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, (Constants.WORLD_WIDTH - effectiveViewportWidth / 2f) - 1);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, (Constants.WORLD_HEIGHT - effectiveViewportHeight / 2f) - 1);

    }

    /**
     * Handles camera update on resizing of game window
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
    }

    /**
     * Memory management
     */
    @Override
	public void dispose () {
	    mapSprite.getTexture().dispose();
	}
}
