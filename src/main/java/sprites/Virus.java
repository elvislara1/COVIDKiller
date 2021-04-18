package sprites;

import javafx.scene.image.Image;
import static Control.MainWindow.*;

//Clase de virus (clase extend de ObjetoEnMovimiento)
public class Virus extends ObjetoEnMovimiento {

    //Variable
    public double velocitat = (puntuacion /15) + 1;

    //Clase de virus (clase extend de ObjetoEnMovimiento)
    public Virus(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    //Método de actualización
    public void update() {
        super.update();
        if(!haEstallado && !destruido) posY += velocitat; // if el objeto Virus explota y se destruye es falso, agrega VELOCIDAD a posY
        if(posY > HEIGHT) destruido = true; //if el virus llega al final (al fondo), destroyed verdadero
    }

    //Método draw
    public void draw() {
        if(haEstallado) { //if la explosión de virus es verdadera, cambia la imagen a EXPLOSION_IMG
            gc.drawImage(EXPLOSION_IMG,
                    posX, posY, size, size);
        } else { //De lo contrario, solo usa set image
            gc.drawImage(img, posX, posY, size, size);
        }
    }
}