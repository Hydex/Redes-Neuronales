/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package contra2;

import rjava.*;
import java.io.*;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;

public class RJava {
    
    private Rengine re;
    private StringBuffer rCodigo;
    private String SALTO = "\r\n";
    private int entradas;
    private int salidas;
    private int ejemplos;
    private int ocultas;
    private int pasos;
    
    public void init(String ruta, int entradas, int salidas, int ejemplos, int ocultas, int pasos){
        this.entradas = entradas;
        this.salidas = salidas;
        this.ejemplos = ejemplos;
        this.ocultas = ocultas;
        this.pasos = pasos;
        // just making sure we have the right version of everything
	if (!Rengine.versionCheck()) {
	    System.err.println("** Version mismatch - Java files don't match library version.");
	    System.exit(1);
	}
        System.out.println("Creating Rengine (with arguments)");
		// 1) we pass the arguments from the command line
		// 2) we won't use the main loop at first, we'll start it later
		//    (that's the "false" as second argument)
		// 3) the callbacks are implemented by the TextConsole class above
	re=new Rengine(new String[]{}, false, new TextConsole());
        System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            re = null;
        }
        if(re != null){
            rCodigo = new StringBuffer();
            String libreria = "library(\"neuralnet\")";
            rCodigo.append(libreria+SALTO);
            re.eval(libreria);
            parsearDatosEntradaCSVaR(ruta);
            generarMatrizDeDatos();            
            System.out.println("R Codigo: ");
            System.out.println(rCodigo);
        }
    }
    
    public boolean[] obtenerSalidas(int[] valEntradas){
        StringBuffer sbComp = new StringBuffer("net.result <- compute(net.contra,cbind(");
        for(int i=0;i<valEntradas.length;i++){
            sbComp.append("c("+valEntradas[i]+")");
            if(i<valEntradas.length-1)sbComp.append(",");
        }
        sbComp.append("))");
        rCodigo.append(sbComp.toString()+SALTO);
        re.eval(sbComp.toString());
        //System.out.println("##"+sbComp.toString()+"##");
        REXP rexpR = re.eval("net.result$net.result[1,]");
        //System.out.println("valores::"+rexpR);
        double[] valores = rexpR.asDoubleArray();
        REXP rexpU = re.eval("net.contra[\"weights\"]$weights[[1]][["+(ocultas>0?2:1)+"]][1,]");
        //System.out.println("umbrales::"+rexpU);
        double[] pesosUmbrales = rexpU.asDoubleArray();
        boolean[] valoresBoleanos = new boolean[salidas];
        for(int i=0; i<salidas; i++)
            valoresBoleanos[i] = valores[i] >= pesosUmbrales[i];
        return valoresBoleanos;
    }
    
    public void generarMatrizDeDatos(){
            StringBuffer sbDatos = new StringBuffer("net.datos <- cbind(");
            StringBuffer sbNet = new StringBuffer("net.contra <- neuralnet(");
            StringBuffer sbNetAux1 = new StringBuffer("");
            StringBuffer sbNetAux2 = new StringBuffer("");
            for(int i=0;i<entradas;i++){
                sbDatos.append("S"+(i+1));
                sbDatos.append(",");
                sbNetAux1.append("S"+(i+1));
                if(i<entradas-1)sbNetAux1.append("+");
            }
            for(int i=0;i<salidas;i++){
                sbDatos.append("T"+(i+1));
                sbNetAux2.append("T"+(i+1));
                if(i<salidas-1){
                    sbDatos.append(",");
                    sbNetAux2.append("+");
                }
            }
            sbDatos.append(")");
            sbNet.append(sbNetAux2+"~"+sbNetAux1+",net.datos,hidden = "+ocultas+", stepmax="+pasos+")");
            rCodigo.append(sbDatos.toString()+SALTO);
            rCodigo.append(sbNet.toString()+SALTO);
            re.eval(sbDatos.toString());
            re.eval(sbNet.toString());
            System.out.println(re.eval("print(net.datos)"));
            System.out.println(re.eval("print(net.contra)"));
    } 
    
    public void parsearDatosEntradaCSVaR(String ruta){
        String csvFile = ruta;
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	try {
            br = new BufferedReader(new FileReader(csvFile));
            StringBuffer[] s = new StringBuffer[entradas];
            StringBuffer[] t = new StringBuffer[salidas];
            for(int i=0; i<entradas;i++) s[i] = new StringBuffer("S"+(i+1)+" <- c(");
            for(int i=0; i<salidas;i++) t[i] = new StringBuffer("T"+(i+1)+" <- c(");
            int j = 0;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(cvsSplitBy);
                int i;
                for(i=0; i<entradas;i++){
                    s[i].append(datos[i]);
                    if(j<ejemplos-1)s[i].append(",");
                }
                for(; i<salidas+entradas;i++){
                    t[i-entradas].append(datos[i]);
                    if(j<ejemplos-1)t[i-entradas].append(",");
                }
                j++;
            }
            for(int i=0; i<entradas;i++) s[i].append(")");
            for(int i=0; i<salidas;i++) t[i].append(")");
            
            for(int i=0; i<entradas;i++) {
                rCodigo.append(s[i].toString()+SALTO);
                re.eval(s[i].toString());                
            }
            for(int i=0; i<salidas;i++) {
                rCodigo.append(t[i].toString()+SALTO);
                re.eval(t[i].toString());
            }
            
            for(int i=0; i<entradas;i++) re.eval("print("+s[i].toString()+")");
            for(int i=0; i<salidas;i++) re.eval("print("+t[i].toString()+")");
 
	} catch (FileNotFoundException e) {
            e.printStackTrace();
	} catch (IOException e) {
            e.printStackTrace();
	} finally {
            if (br != null) {
                try {
                        br.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
            }
	}
    }
    
    public void pintarSalida(boolean[] bools){
        for(int i = 0; i<bools.length; i++)
            System.out.print("\t"+bools[i]);
        System.out.println();
    }
    
    public void pintarSalida(double[] bools){
        for(int i = 0; i<bools.length; i++)
            System.out.print("\t"+bools[i]);
        System.out.println();
    }
    
    /*public static void main(String[] args) {        
        int entradas = 11;
        int salidas = 6;
        int ejemplos = 128;
        int ocultas = 0;
        int pasos = 2500;
        RJava rJava = new RJava();
        rJava.init("D:\\datos.csv", entradas, salidas, ejemplos, ocultas, pasos);
        rJava.pintarSalida(rJava.obtenerSalidas(new int[]{0,0,0,1,1,0,-1,1,-1,1,-1}));
    }*/
}
