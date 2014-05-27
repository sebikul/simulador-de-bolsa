package poo.proyecto.modelos;

import poo.proyecto.simulador.Simulador;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collection;

public class ResultadosSimulacion implements Serializable {

    private final Collection<Titulo> titulos;
    private final ArrayList<Inversor> inversores;
    private final int ciclos;

    public ResultadosSimulacion(ArrayList<Titulo> titulos, ArrayList<Inversor> inversores, int ciclos) {
        this.titulos = titulos;
        this.inversores = inversores;
        this.ciclos = ciclos;
    }

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

    public int getCiclos() {
        return ciclos;
    }

    public Collection<Titulo> getTitulos() {
        return titulos;
    }

    public ArrayList<Inversor> getInversores() {
        return inversores;
    }

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
