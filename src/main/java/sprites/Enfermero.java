package sprites;

import javafx.scene.image.Image;
import static Control.MainWindow.*;

//Clase de enfermero (clase extend de ObjetoEnMovimiento)
public class Enfermero extends ObjetoEnMovimiento {

    //Constructor para objeto enfermero
    public Enfermero(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    //Metodo disparo
    public Disparo disparo() {
        return new Disparo(posX + size / 2 - Disparo.size / 2, posY - Disparo.size); // Devuelve nuevo objeto disparo
    }

    //Metodo draw
    public void draw() {
        if(haEstallado) { //if la explosión de Enfermero es verdadero, cambia la imagen a ENFERMO_IMG
            gc.drawImage(ENFERMO_IMG,
                    posX, posY, size, size);
        } else {
            gc.drawImage(img, posX, posY, size, size); //De lo contrario, solo usa set image
        }
    }
}
