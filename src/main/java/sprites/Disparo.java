package sprites;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import static Control.MainWindow.*;

//Clase disparo
public class Disparo {
    // Variables
    public boolean toRemove;
    private Image image = JERINGA1_IMG;
    private int posX = (int)(image.getWidth() / 2);
    public int posY = 300;
    private int velocidad = 3;
    static final int size = 6;

    //Contructor para el objeto Disparo
    public Disparo(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    //Método de actualización
    public void update() {
        posY-= velocidad;
    }

    //Método draw
    public void draw() {
        gc.drawImage(JERINGA1_IMG, posX - 12 , posY, 30 , 90); // Dibujar la imagen en una jeringa normal
        gc.setFill(Color.TRANSPARENT); // Cambiar el color a transparente para quitar el punto en la parte superior de la jeringa

        // Mejora la parte de la jeringa
        if ((puntuacion >= 50 && puntuacion <= 70) || (puntuacion >= 100 && puntuacion <= 120)
                || (puntuacion >=200 && puntuacion <=230)) { // IF una de las condiciones es verdadera, cambie la imagen para actualizar la jeringa y acelerar la velocidad
            gc.drawImage(JERINGA2_IMG, posX - 12, posY, 30, 90);
            velocidad = 12;
            gc.fillRect(posX - 5, posY-10, size+10, size+30);
        } else { // De lo contrario, solo usa una jeringa normal
            gc.fillOval(posX, posY, size, size);
        }
    }

    public boolean colision(ObjetoEnMovimiento objeto) {
        int distance = distancia(this.posX + size / 2, this.posY + size / 2, objeto.posX + objeto.size / 2, objeto.posY + objeto.size / 2);
        return distance  < objeto.size / 2 + size / 2;
    }

    int distancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}