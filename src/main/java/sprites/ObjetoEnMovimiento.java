package sprites;

import javafx.scene.image.Image;

import static Control.MainWindow.*;

// Clase ObjetivoEnMovimiento (clase root para Enfermero y Virus)
public class ObjetoEnMovimiento {
    // Variables
    public int posX;
    int posY;
    int size;
    public boolean haEstallado, destruido;
    Image img;
    private int explosionStep = 0;

    //Constructor para ObjetoEnMovimiento
    public ObjetoEnMovimiento(int posX, int posY, int size, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        img = image;
    }

    //Método de disparo - método vacío para el enfermero
    public Disparo disparo() {
        return null;
    }

    //Método de actualización
    public void update() {
        if(haEstallado) explosionStep++; //if explosión es verdadera, ++ a explosión
        destruido = explosionStep > EXPLOSION_STEPS;
    }

    //Método de dibujo - método vacío para ambas clases secundarias
    public void draw() {}

    //Método colision
    public boolean colide(ObjetoEnMovimiento other) { // hit with force when moving.
        int d = distance(this.posX + size / 2, this.posY + size /2,
                other.posX + other.size / 2, other.posY + other.size / 2);
        return d < other.size / 2 + this.size / 2 ;
    }
    //Metodo distancia
    int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    //Explosión
    public void explotar() {
        haEstallado = true;
        explosionStep = -1;
    }
}