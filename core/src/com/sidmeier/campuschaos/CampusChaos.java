package com.sidmeier.campuschaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CampusChaos extends ApplicationAdapter {

    private Viewport viewport;
    private OrthographicCamera cam;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    /**
     * Defines map, renderer, camera and viewport
     */
	@Override
	public void create () {
	    map = new TmxMapLoader().load("core/assets/SEPRMapSquare.tmx");
	    renderer = new OrthogonalTiledMapRenderer(map);

        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        viewport.apply();

        cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
	}

    /**
     * Updates camera position, clears screen and uses renderer to draw map
     */
	@Override
	public void render () {
        handleInput();
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(cam);
        renderer.render();
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

        float leftEdge = ((cam.viewportWidth / 100) * edgePercent);
        float rightEdge = cam.viewportWidth - leftEdge;
        float bottomEdge = ((cam.viewportHeight / 100) * edgePercent);
        float topEdge = cam.viewportHeight - bottomEdge;

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
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)map.getLayers().get(0);
        float totalMapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        float totalMapHeight = mainLayer.getHeight() * mainLayer.getTileHeight();

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, totalMapHeight/cam.viewportHeight);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, (totalMapWidth - effectiveViewportWidth / 2f) - 1);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, (totalMapHeight - effectiveViewportHeight / 2f) - 1);

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
        map.dispose();
        renderer.dispose();
	}
}
