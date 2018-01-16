package com.sidmeier.campuschaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sidmeier.campuschaos.utils.Constants;
import javafx.util.Pair;

public class CampusChaos extends ApplicationAdapter {

    private Viewport viewport;
    private OrthographicCamera cam;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private Map map;

    /**
     * Defines map, renderer, camera and viewport
     */
	@Override
	public void create () {
	    map = initMap();
	    tiledMap = new TmxMapLoader().load("core/assets/SEPRMapSquare.tmx");
	    renderer = new OrthogonalTiledMapRenderer(tiledMap);

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
        hoverSelectTile();
	}

	// TODO Import sectors and locations from file
    public Map initMap() {
	    Map map = new Map();
        Sector ronCookeHub = new Sector("Ron Cooke Hub");
        Sector hesHall = new Sector("Heslington Hall");
        Sector centralHall = new Sector("Central Hall");
        Pair<Integer, Integer> ronCookeHubCoord = new Pair<Integer, Integer>(19,8);
        Pair<Integer, Integer> hesHallCoord = new Pair<Integer, Integer>(9,7);
        Pair<Integer, Integer> centralHallCoord = new Pair<Integer, Integer>(5,8);
        map.addSector(ronCookeHubCoord, ronCookeHub);
        map.addSector(hesHallCoord, hesHall);
        map.addSector(centralHallCoord, centralHall);
        return map;
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
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        float totalMapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        float totalMapHeight = mainLayer.getHeight() * mainLayer.getTileHeight();

        float displayedMH = totalMapHeight/cam.viewportHeight;
        float displayedMW = totalMapWidth/cam.viewportWidth;

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, (displayedMH<displayedMW?displayedMH:displayedMW));

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, (totalMapWidth - effectiveViewportWidth / 2f) - 1);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, (totalMapHeight - effectiveViewportHeight / 2f) - 1);

    }

    /**
     * Highlights tile that user is hovering over with the mouse
     */
    private void hoverSelectTile(){
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        float mouseX = Gdx.input.getX();
        float mouseY = -Gdx.input.getY() + (cam.viewportHeight);

        float camX = (cam.position.x - ((cam.viewportWidth/2) * cam.zoom)) * (1/cam.zoom);
        float camY = (cam.position.y - ((cam.viewportHeight/2) * cam.zoom)) * (1/cam.zoom);

        float tileWidth = mainLayer.getTileWidth() * (1/cam.zoom);
        float tileHeight = mainLayer.getTileHeight() * (1/cam.zoom);

        float tileX = (mouseX + camX)/tileWidth;
        float tileY = (mouseY + camY)/tileHeight;

        int roundX = (int) tileX;
        int roundY = (int) tileY;

        float selectX = (roundX * tileWidth) - camX;
        float selectY = (roundY * tileWidth) - camY;

        //System.out.println(roundX + ", " + roundY);
        //System.out.println((tileX - (tileX % 1)) + ", " + (tileY - (tileY % 1)));

        Gdx.gl.glEnable(GL20.GL_BLEND);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(selectX, selectY, tileWidth, tileHeight);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.5f);
        shapeRenderer.rect(selectX, selectY, tileWidth, tileHeight);
        shapeRenderer.end();

        Pair<Integer, Integer> coord = new Pair<Integer, Integer>(roundX, roundY);
        if(map.sectorAtCoord(coord)) {
            System.out.println(map.getSector(coord).name);
        }

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
        tiledMap.dispose();
        renderer.dispose();
	}
}
