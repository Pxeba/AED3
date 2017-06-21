package aed3;
import java.io.*;

public interface Registro extends Comparable, Cloneable {

    public void setCodigo(int codigo);
    public int getCodigo();
    public byte[] getRegistro() throws IOException;
    public void setRegistro(byte[] registro) throws IOException;
    public Object clone() throws CloneNotSupportedException;
    public String getChaveSecundaria();
    public int getCodigoProjeto();
    
}
