package Control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import sprites.Disparo;
import sprites.Enfermero;
import sprites.ObjetoEnMovimiento;
import sprites.Virus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MainWindow extends Application {
    //VARIABLES
    public static GraphicsContext gc;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static int puntuacion;
    private static final Random random = new Random();
    //Tamaño del jugador
    private static final int PLAYER_SIZE = 60;
    private double mouseX;
    ObjetoEnMovimiento player;
    List<Disparo> disparos;
    List<Virus> viruses;
    private boolean gameOver = false;
    private final int MAX_VIRUS = 7;
    private final int MAX_DISPAROS = MAX_VIRUS * 2;
    public static final int EXPLOSION_STEPS = 15;

    //IMAGES
    public static final Image PLAYER_IMG = new Image("img/enfermero_sano.png");
    public static final Image ENFERMO_IMG = new Image("img/enfermero_enfermo.png");
    public static final Image JERINGA1_IMG = new Image("img/jeringa_normal.png");
    public static final Image JERINGA2_IMG = new Image("img/jeringa_mejorada.png");
    static final Image VIRUS1_IMG = new Image("img/virus_lv1.png");
    static final Image VIRUS2_IMG = new Image("img/virus_lv2.png");
    static final Image VIRUS3_IMG = new Image("img/virus_lv3.png");
    static final Image VIRUS4_IMG = new Image("img/virus_lv4.png");
    public static final Image EXPLOSION_IMG = new Image("img/explosion.png");
    static final Image LATIERRA_IMG = new Image("img/tierra.png");

    //start JAVAFX
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT); // Nuevo Canvas objecto
        gc = canvas.getGraphicsContext2D(); // Colocar gc en canvas
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc))); // Nuevo Timeline objeto y conjunto KeyFrame
        timeline.setCycleCount(Timeline.INDEFINITE);// timeline ciclos indefinidos
        timeline.play(); // Play timeline

        //Parte del evento del raton
        //-------------------------------------
        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> mouseX = e.getX());
        canvas.setOnMouseClicked(e -> {
            if(disparos.size() < MAX_DISPAROS) disparos.add(player.disparo()); // Si los disparos son inferiores a MAX_SHOT, agregar un disparo
            if(gameOver) { // Si gameOver es verdadero, gameover pasara a falso
                gameOver = false;
                setup(); // Configurar juego nuevo
            }
        });
        setup();  // Configurar juego nuevo
        stage.setScene(new Scene(new StackPane(canvas))); // Establecer un nuevo StackPane que tenga canvas para el escenario
        stage.setTitle("COVIDKiller"); // Establecer título
        stage.show(); // Mostrar escenario
    }

    // Método de configuración
    private void setup() {
        disparos = new ArrayList<>(); // Nuevo ArrayList para disparos
        viruses = new ArrayList<>(); // Nuevo ArrayList para virus
        player = new Enfermero(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG); // New Nurse Player
        puntuacion = 0; // Establecer puntuación en 0
        IntStream.range(0, MAX_VIRUS).mapToObj(i -> this.newVirus()).forEach(viruses::add); // Establecer nuevos virus para que caigan
    }

    //Ejecutar gráficos
    private void run(GraphicsContext gc) {
        gc.setFill(new ImagePattern(LATIERRA_IMG, 0, 0, 1, 1, true)); // Establecer fondo a la imagen de La Tierra
        gc.fillRect(0, 0, WIDTH, HEIGHT); // Rellena un rectángulo con la pintura de relleno actual.
        gc.setTextAlign(TextAlignment.CENTER); // Establecer la alineación del texto al centro
        gc.setFont(Font.font(20)); // Establecer tamaño de fuente
        gc.setFill(Color.WHITESMOKE); // Establecer color de fuente
        gc.fillText("Puntuación: " + puntuacion, 85,  20); // Rellene el texto que muestra la puntuación en la parte superior izquierda

        if (puntuacion >= 0 && puntuacion < 20){
            gc.fillText("Nivel: " + "Primera ola",90,40);
        } else if (puntuacion >= 20 && puntuacion < 50){
            gc.fillText("Nivel: " + "Segunda ola",90,40);
        } else if (puntuacion >= 50 && puntuacion <= 80){
            gc.fillText("Nivel: " + "Tercera ola",90,40);
        } else if (puntuacion >= 80){
            gc.fillText("Nivel: " + "Cuarta ola",90,40);;
        }
        
        //Parte de Game Over
        if(gameOver) {
            for( int i = 0; i < viruses.size(); i++ ) { // Detener todos los virus cuando acabe el juego
                viruses.get(i).velocitat = 0; // Establecer la velocidad del virus en 0
            }
            gc.setFill(Color.BLACK); // Establecer fondo en negro
            gc.fillRect(0, 0, WIDTH, HEIGHT); // Rellena un rectángulo con la pintura de relleno actual.
            gc.setFont(Font.font(35)); // Establecer tamaño de fuente
            gc.setFill(Color.RED); // Establecer color de fuente
            gc.fillText("Game Over \n Tu puntuación es: " + puntuacion
                    + "\n Haz click para jugar de nuevo", WIDTH / 2, HEIGHT /2.5); // Completar el texto que muestra la puntuación final en el medio
        }

        player.update(); // Actualizar jugador
        player.draw(); // Pintar jugador
        player.posX = (int) mouseX; // Mover jugador a la posición x del mouse

        viruses.stream().peek(ObjetoEnMovimiento::update).peek(ObjetoEnMovimiento::draw).forEach(e -> {
            if(player.colide(e) && !player.haEstallado) {
                player.explotar();
            }
        });

        for (int i = disparos.size() - 1; i >=0 ; i--) {
            Disparo disparo = disparos.get(i);
            if(disparo.posY < 0 || disparo.toRemove)  { // Si la jeringa llega a la parte superior, retirela
                disparos.remove(i);
                continue;
            }
            disparo.update(); // Actualizar disparo a disparo
            disparo.draw(); // Pintar disparo a disparo
            for (Virus virus : viruses) { // Para cada objeto de virus
                if(disparo.colision(virus) && !virus.haEstallado) { // Si el virus recibe un disparo, agrega puntuacion y lo elimina
                    puntuacion++;
                    virus.explotar();
                    disparo.toRemove = true;
                }
            }
        }

        for (int i = viruses.size() - 1; i >= 0; i--){ // Si el virus se destruyó, agregue un nuevo objeto de virus
            if(viruses.get(i).destruido)  {
                viruses.set(i, newVirus());
            }
        }
        gameOver = player.destruido;
    }

    // Agregar nuevo virus dependiendo de la puntuación de los jugadores
    Virus newVirus() {
        if (puntuacion >= 0 && puntuacion < 20) return new Virus(50 + random.nextInt(WIDTH - 100), 0, PLAYER_SIZE, VIRUS1_IMG);
        if (puntuacion >= 20 && puntuacion < 50) return new Virus(50 + random.nextInt(WIDTH - 100), 0, PLAYER_SIZE, VIRUS2_IMG);
        if (puntuacion >= 50 && puntuacion <= 80) return new Virus(50 + random.nextInt(WIDTH - 100), 0, PLAYER_SIZE, VIRUS3_IMG);
        if (puntuacion >= 80) return new Virus(50 + random.nextInt(WIDTH - 100), 0, PLAYER_SIZE, VIRUS4_IMG);
        return null;
    }

    // Control.Main
    public static void main(String[] args) {
        launch();
    }
}
