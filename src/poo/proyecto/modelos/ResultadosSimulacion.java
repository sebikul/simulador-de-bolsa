package poo.proyecto.modelos;

import poo.proyecto.simulador.Simulador;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Almacena los resultados de la simulacion.
 */
public class ResultadosSimulacion implements Serializable {

    /**
     * Almacena la coleccion de titulos que se operaron en la suimulacion.
     */
    private final Collection<Titulo> titulos;

    /**
     * Almacena la lista de inversores que participaron en la simulacion.
     */
    private final ArrayList<Inversor> inversores;

    /**
     * Almacena la cantidad de ciclos que se simularon.
     */
    private final int ciclos;

    /**
     * Construye una nueva instancia de la clase.
     *
     * @param titulos    Titulos operados durante la simulacion
     * @param inversores Inversores que participaron en la simulacion
     * @param ciclos     Cantidad de ciclos que se simularon
     */
    public ResultadosSimulacion(ArrayList<Titulo> titulos, ArrayList<Inversor> inversores, int ciclos) {
        this.titulos = titulos;
        this.inversores = inversores;
        this.ciclos = ciclos;
    }

    /**
     * Carga los resultados de una simulacion desde un archivo.
     *
     * @param path Ruta del archivo guardado
     * @return Instancia de ResultadosSimulacion
     * @throws FileNotFoundException  Si no se encontro el archivo
     * @throws InvalidObjectException Si el archivo esta corrupto
     */
    static public final ResultadosSimulacion cargar(String path) throws FileNotFoundException, InvalidObjectException {


        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        ResultadosSimulacion resultadosSimulacion = null;

        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            InputStream buffer = new BufferedInputStream(fileInputStream);


            ObjectInputStream objectInputStream = new ObjectInputStream(buffer);

            resultadosSimulacion = (ResultadosSimulacion) objectInputStream.readObject();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultadosSimulacion == null) {
            throw new InvalidObjectException(path);
        }

        return resultadosSimulacion;
    }

    /**
     * Retorna la cantidad de ciclos que se simularon.
     *
     * @return Cantidad de ciclos que se simularon.
     */
    public int getCiclos() {
        return ciclos;
    }

    /**
     * Retorna los titulos que se operaron en la simulacion.
     *
     * @return Coleccion de titulos que se operaron
     */
    public Collection<Titulo> getTitulos() {
        return titulos;
    }

    /**
     * Retorna una lista de los inversores que participaron en la simulacion.
     *
     * @return Lista de inversores que participaron en la simulacion.
     */
    public ArrayList<Inversor> getInversores() {
        return inversores;
    }

    /**
     * Guarda los resultados de una simulacion en un archivo.
     *
     * @param path Ruta de destino
     * @throws AccessDeniedException      Si no se pudo acceder al archivo
     * @throws FileAlreadyExistsException Si ya existe el archivo
     */
    public final void guardar(String path) throws AccessDeniedException, FileAlreadyExistsException {

        File file = new File(path);

        if (file.exists()) {
            throw new FileAlreadyExistsException(path);
        }

        String filePath = file.getAbsolutePath();
        if (!filePath.endsWith(Simulador.FILE_TYPE)) {
            path = filePath + Simulador.FILE_TYPE;
            file = new File(path);
        }

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);


        } catch (Exception e) {
            e.printStackTrace();
            throw new AccessDeniedException(path);
        }

    }

    @Override
    public String toString() {

        String ret = "\nInversores:\n";

        for (Inversor inversor : inversores) {
            ret += inversor.getNombre() + " [" + inversor.getCapitalInicial()
                    + " --> " + inversor.getPatrimonio() + "]\n";
        }

        ret += "\nTitulos:\n";

        for (Titulo titulo : titulos) {
            ret += titulo.getSimbolo() + " [" + titulo.getValorInicial()
                    + " --> " + titulo.getValor() + "]\n";
        }

        return ret;
    }
}
