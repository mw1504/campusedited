package com.sidmeier.campuschaos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sidmeier.campuschaos.utils.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;
import javafx.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CampusChaos extends ApplicationAdapter {

    private Viewport viewport;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private BitmapFont font;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private Map map;
    private Stage stage;

    private float tileWidth;
    private float tileHeight;

    public class SectorInformation extends Actor {
        private ShapeRenderer sectorInformationRenderer = new ShapeRenderer();
        private float tileWidth;
        private float tileHeight;
        private OrthographicCamera cam;

        public SectorInformation(float tileWidth, float tileHeight, OrthographicCamera cam) {
            this.tileHeight = tileHeight;
            this.tileWidth = tileWidth;
            this.cam = cam;
        }

        @Override
        public void draw(Batch batch, float alpha) {
        sectorInformationRenderer.begin(ShapeRenderer.ShapeType.Line);
        sectorInformationRenderer.setColor(0, 0, 1, 1);
        sectorInformationRenderer.rect(this.cam.viewportWidth - 600, 20, 6* this.tileWidth, 4*tileHeight);
        sectorInformationRenderer.end();
        sectorInformationRenderer.begin(ShapeRenderer.ShapeType.Filled);
        sectorInformationRenderer.setColor(0, 0, 1, 0.5f);
        sectorInformationRenderer.rect(this.cam.viewportWidth - 600, 20, 6* this.tileWidth, 4*tileHeight);
        sectorInformationRenderer.end();
        }
    }

    /**
     * Defines map, renderer, camera and viewport
     */
	@Override
	public void create () {
	    map = new Map();

        batch = new SpriteBatch();
        font = generateFont("core/assets/FreeMonoBold.ttf", 48);

	    tiledMap = new TmxMapLoader().load("core/assets/SEPRMapSquare.tmx");
	    renderer = new OrthogonalTiledMapRenderer(tiledMap);

        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        viewport.apply();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Float[] displayedWH = getDisplayValues();

        float displayedMW = displayedWH[0];
        float displayedMH = displayedWH[1];

        cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);

        cam.zoom = (displayedMH<displayedMW?displayedMH:displayedMW)  - 0.001f;

        //cam.zoom = 1.65f;

	}

	private BitmapFont generateFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
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


        Pair<Integer, Integer> coord = tileSelect();
        String sectorName = null;
        if(map.sectorAtCoord(coord)) {
            sectorName = map.getSector(coord).getName();
        }
        if(sectorName != null){
            batch.begin();
            font.draw(batch,sectorName,20,(cam.viewportHeight - 20));
            batch.end();
        }

        SectorInformation test = new SectorInformation(tileWidth,tileHeight,cam);
        test.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                
            }
        });
        stage = new Stage(viewport, new SpriteBatch());
        stage.addActor(test);
        Gdx.input.setInputProcessor(stage);
        stage.draw();
	}


    /**
     * Handles user input to move camera, ensures camera doesn't leave world
     */
    private void handleInput() {
        /*float camSpeed = 3;    // Camera speed in units per frame

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
        }*/

        // Escape to exit
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // Edge clamping
        Float[] displayValues = getDisplayValues();

        float displayedMW = displayValues[0];
        float displayedMH = displayValues[1];

        cam.zoom = MathUtils.clamp(cam.zoom, 1f, (displayedMH<displayedMW?displayedMH:displayedMW));

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, (displayValues[2] - effectiveViewportWidth / 2f) - 1);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, (displayValues[3] - effectiveViewportHeight / 2f) - 1);

    }

    /**
     * Calculates display values for zoom calculations
     */
    private Float[] getDisplayValues() {
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        float totalMapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        float totalMapHeight = (mainLayer.getHeight() * mainLayer.getTileHeight()) - 128;

        float displayedMH = totalMapHeight/cam.viewportHeight;
        float displayedMW = totalMapWidth/cam.viewportWidth;

        return new Float[]{displayedMW, displayedMH, totalMapWidth, totalMapHeight};
    }

    /**
     * Highlights tile that user is hovering over with the mouse
     */
    private Pair<Integer, Integer> tileSelect(){
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        float mouseX = Gdx.input.getX();
        float mouseY = -Gdx.input.getY() + (cam.viewportHeight);

        float camX = (cam.position.x - ((cam.viewportWidth/2) * cam.zoom)) * (1/cam.zoom);
        float camY = (cam.position.y - ((cam.viewportHeight/2) * cam.zoom)) * (1/cam.zoom);

        this.tileWidth = mainLayer.getTileWidth() * (1/cam.zoom);
        this.tileHeight = mainLayer.getTileHeight() * (1/cam.zoom);

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

        return new Pair<Integer, Integer>(roundX, roundY);

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
        batch.dispose();
        font.dispose();
        stage.dispose();
	}
}
