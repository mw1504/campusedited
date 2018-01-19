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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import javafx.util.Pair;

import java.awt.*;

public class CampusChaos extends ApplicationAdapter {

    private Viewport viewport;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private BitmapFont font;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    private Map map;
    private Stage stage;

    private float clickedTileX;
    private float clickedTileY;

    private SectorInformation sectorDisplay;

    public class SectorInformation extends Actor {
        private ShapeRenderer sectorInformationRenderer = new ShapeRenderer();
        private float tileWidth;
        private float tileHeight;
        private OrthographicCamera cam;
        private BitmapFont font;
        private Sector selectedSector;
        private int timer = 0;
        private boolean draw = false;
        private Pair<Float,Float> tileOrigin;
        private SpriteBatch batch = new SpriteBatch();

        public SectorInformation(float tileWidth, float tileHeight, OrthographicCamera cam, BitmapFont font, Pair<Float,Float> tileOrigin, SpriteBatch batch) {
            this.tileHeight = tileHeight;
            this.tileWidth = tileWidth;
            this.cam = cam;
            this.font = font;
            this.tileOrigin = tileOrigin;
            //this.batch = batch;
        }

        @Override
        public void draw(Batch batch, float alpha) {

        }

        public void lemmiedraw(SpriteBatch batch) {
            System.out.println(this.tileOrigin.getKey() + ", " + this.tileOrigin.getValue());
            font.draw(batch, "Ass", 1000f, 274.23868f);
            if (this.timer > 0) {
                if (this.selectedSector.getName() != null) {
                    font.draw(batch, this.selectedSector.getName(), this.tileOrigin.getKey(), this.tileOrigin.getValue());
                } else {
                    font.draw(batch, "test", this.tileOrigin.getKey(), this.tileOrigin.getValue());
                }
                if (this.selectedSector.getAffiliation() != null) {
                    font.draw(batch, this.selectedSector.getAffiliation(), this.tileOrigin.getKey(), this.tileOrigin.getValue());
                } else {
                    font.draw(batch, "test", this.tileOrigin.getKey(), this.tileOrigin.getValue());
                }
                font.draw(batch,Integer.toString(this.selectedSector.getAmountOfUnits()),this.tileOrigin.getKey(), this.tileOrigin.getValue());
                font.draw(batch,Integer.toString(this.selectedSector.getBonus()),this.tileOrigin.getKey(), this.tileOrigin.getValue());
                this.timer--;
                if (this.timer == 0) {
                    this.draw = false;
                }
            }
        }

        public void setSector(Sector selectedSector) {
            this.selectedSector = selectedSector;
            this.timer = 60 * 20;
            this.draw = true;
        }

        public boolean isDraw() {
            return this.draw;
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

        Pair<Float,Float> tileDimensions = this.getTileDimensions();
        this.sectorDisplay = new SectorInformation(tileDimensions.getKey(),tileDimensions.getValue(),this.cam,this.font, this.getTileOrigin(new Pair<Integer,Integer>(18,1)), this.batch);
	}

    /**
     * Imports font for use in app
     */
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
        stage = new Stage(viewport, new SpriteBatch());
        stage.addActor(this.sectorDisplay);
        Gdx.input.setInputProcessor(stage);
        handleInput();
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(cam);
        renderer.render();
        //Draws Red Box.
        this.redBox();
        Pair<Integer, Integer> coord = this.getTileCoordinates();

        String sectorName = null;
        if(map.sectorAtCoord(coord)) {
            sectorName = map.getSector(coord).getName();
        }
        Pair<Float,Float> tileDimensions = this.getTileDimensions();
        batch.begin();
        if(sectorName != null){ //error here
            font.draw(batch,sectorName,20,(cam.viewportHeight - 20));
        }
        this.sectorDisplay.lemmiedraw(batch);
        batch.end();
        /*this.stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click!");
                tileClicked(x,y);
            }
        });*/
        //font.draw(batch, "I made it", 2500, this.tileOrigin.getValue());
        if(Gdx.input.isTouched(0)){
            tileClicked(Gdx.input.getX(),(-Gdx.input.getY() + (cam.viewportHeight)));
        }
        if (this.sectorDisplay.isDraw()) {
            Pair<Float,Float> tileOrigin = this.getTileOrigin(new Pair<Integer,Integer>(18,1));
            System.out.println(tileOrigin.getKey() + ", " + tileOrigin.getValue());
            Gdx.gl.glEnable(GL20.GL_BLEND);
            ShapeRenderer shaper = new ShapeRenderer();
            shaper.begin(ShapeRenderer.ShapeType.Line);
            shaper.setColor(0, 0, 1, 1);
            shaper.rect(tileOrigin.getKey(), tileOrigin.getValue(), 6* tileDimensions.getKey(), 4*tileDimensions.getValue());
            shaper.end();
            shaper.begin(ShapeRenderer.ShapeType.Filled);
            shaper.setColor(0, 0, 1, 0.5f);
            shaper.rect(tileOrigin.getKey(), tileOrigin.getValue(), 6* tileDimensions.getKey(), 4*tileDimensions.getValue());
            shaper.end();
        }
        stage.draw();
    }

    /**
     * Handles user input to move camera, ensures camera doesn't leave world
     */
    private void handleInput() {
        /*float camSpeed = 3;    // Camera speed in units per frame
        this.cameraZoom();
        this.cameraTranslate(camSpeed);
        this.edgeScrolling(camSpeed);*/

        // Escape to exit
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        this.edgeClamping();
    }

    private void cameraZoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            this.cam.zoom -= 0.02;
        }
    }

    private void cameraTranslate(float camSpeed) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.cam.translate(-camSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.cam.translate(camSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.cam.translate(0, -camSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.cam.translate(0, camSpeed, 0);
        }
    }

    private void edgeScrolling(float camSpeed) {
        float edgePercent = 5;  // Percentage of APP edge that will cause scrolling on mouseover
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

        float leftEdge = (this.cam.viewportWidth / 100) * edgePercent;
        float rightEdge = this.cam.viewportWidth - leftEdge;
        float bottomEdge = (this.cam.viewportHeight / 100) * edgePercent;
        float topEdge = this.cam.viewportHeight - bottomEdge;

        if (x <= leftEdge) {
            this.cam.translate(-camSpeed, 0, 0);
        } else if (x >= rightEdge) {
            this.cam.translate(camSpeed, 0, 0);
        }

        if (y <= bottomEdge) {
            this.cam.translate(0, camSpeed, 0);
        } else if (y >= topEdge) {
            this.cam.translate(0, -camSpeed, 0);
        }
    }

    private void edgeClamping() {
        Float[] displayValues = this.getDisplayValues();

        float displayedMW = displayValues[0];
        float displayedMH = displayValues[1];

        this.cam.zoom = MathUtils.clamp(this.cam.zoom, 1f, (displayedMH<displayedMW?displayedMH:displayedMW));

        float effectiveViewportWidth = this.cam.viewportWidth * this.cam.zoom;
        float effectiveViewportHeight = this.cam.viewportHeight * this.cam.zoom;

        this.cam.position.x = MathUtils.clamp(this.cam.position.x, effectiveViewportWidth / 2f, (displayValues[2] - effectiveViewportWidth / 2f) - 1);
        this.cam.position.y = MathUtils.clamp(this.cam.position.y, effectiveViewportHeight / 2f, (displayValues[3] - effectiveViewportHeight / 2f) - 1);
    }

    private void tileClicked(float x, float y) {
        Pair<Float,Float> camOffset = this.getCameraOffset();
        Pair<Float,Float> tileDimensions = this.getTileDimensions();

        // Calculates tile coordinate (on map) that mouse is over
        int tileX = (int) ((x + camOffset.getKey())/tileDimensions.getKey());
        int tileY = (int) ((y + camOffset.getValue())/tileDimensions.getValue());
        Pair<Integer,Integer> coord = new Pair<Integer, Integer>(tileX, tileY);
        this.sectorDisplay.setSector(this.map.getSector(coord));
    }

    /**
     * Calculates display values for zoom calculations
     */
    private Float[] getDisplayValues() {
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        // Calculates total map dimensions
        float totalMapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        float totalMapHeight = (mainLayer.getHeight() * mainLayer.getTileHeight()) - 128;

        // ???
        float displayedMW = totalMapWidth/cam.viewportWidth;
        float displayedMH = totalMapHeight/cam.viewportHeight;

        return new Float[]{displayedMW, displayedMH, totalMapWidth, totalMapHeight};
    }

    /**
     * Highlights tile that user is hovering over with the mouse
     */
    private void redBox(){
        Pair<Float,Float> tileDimensions = this.getTileDimensions();
        Pair<Float,Float> tileOrigin = this.getTileOrigin(this.getTileCoordinates());
        //System.out.println(roundX + ", " + roundY);
        //System.out.println((tileX - (tileX % 1)) + ", " + (tileY - (tileY % 1)));

        // Renders outline and fill of tile highlight
        Gdx.gl.glEnable(GL20.GL_BLEND);  // To allow opacity (alpha channel)
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(tileOrigin.getKey(), tileOrigin.getValue(), tileDimensions.getKey(), tileDimensions.getValue());
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.5f);
        shapeRenderer.rect(tileOrigin.getKey(), tileOrigin.getValue(), tileDimensions.getKey(), tileDimensions.getValue());
        shapeRenderer.end();
    }

    private void blueBox() {

    }

    private Pair<Integer,Integer> getTileCoordinates() {
        // Gets mouse offsets from screen 'origin'
        float mouseX = Gdx.input.getX();
        float mouseY = -Gdx.input.getY() + (cam.viewportHeight);

        // Gets camera offsets from map origin
        Pair<Float,Float> camOffset = this.getCameraOffset();

        Pair<Float,Float> tileDimensions = this.getTileDimensions();
        // Calculates tile coordinate (on map) that mouse is over
        int tileX = (int) ((mouseX + camOffset.getKey())/tileDimensions.getKey());
        int tileY = (int) ((mouseY + camOffset.getValue())/tileDimensions.getValue());
        // Calculates viewport coordinates of bottom left of selected tile

        return new Pair<Integer,Integer>(tileX,tileY);
    }

    private Pair<Float,Float> getCameraOffset() {
        return new Pair<Float,Float>((cam.position.x - ((cam.viewportWidth/2) * cam.zoom)) * (1/cam.zoom),
                                        (cam.position.y - ((cam.viewportHeight/2) * cam.zoom)) * (1/cam.zoom));
    }

    private Pair<Float,Float> getTileDimensions() {
        TiledMapTileLayer mainLayer = (TiledMapTileLayer)this.tiledMap.getLayers().get(0);
        float tileWidth = mainLayer.getTileWidth() * (1/this.cam.zoom);
        float tileHeight = mainLayer.getTileHeight() * (1/this.cam.zoom);
        return new Pair<Float,Float>(tileWidth,tileHeight);
    }

    private Pair<Float,Float> getTileOrigin(Pair<Integer,Integer> tileCoord) {
        Pair<Float,Float> camOffset = this.getCameraOffset();
        Pair<Float,Float> tileDimensions = this.getTileDimensions();
        float selectX = (tileCoord.getKey() * tileDimensions.getKey()) - camOffset.getKey();
        float selectY = (tileCoord.getValue() * tileDimensions.getKey()) - camOffset.getValue();
        return new Pair<Float,Float> (selectX,selectY);
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
