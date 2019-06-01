import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import roundr.Proceso;


public class RoundRobin {
   
    int n;
    int quantum;
    int []instru;
    String archivo="Procesos.txt";
    Proceso[] procesos;//Arreglo que guarda los procesos
    ArrayList<String> buffer;
    ArrayList<String> inf;
    int cont=0;

   public RoundRobin (int n, int quantum) {
        this.n = n;
        this.quantum = quantum;
     
        this.procesos = new Proceso[n];
        this.buffer = new ArrayList<>();
        this.inf = new ArrayList<>();
        instru=new int[n];
        Proceso p;
        for (int i = 0; i < this.n; i++) {
            p = new Proceso();
            p.pcb = "P" + Integer.toString(i+1);
            p.id = "0" + Integer.toString(i+1);
            p.instru = (int)(Math.random()*(20+1-1)+1); //Los valores de las rafagas seran aleatorias entre 1-20
            instru[i]=p.instru;
            System.out.println(p.instru);
            p.estado = "N";
            p.posic = i + 1;
            p.espera=0;
            p.fin=0;
            procesos[i] = p; //Guardamos los procesos en el arreglo
        }
        tituloBuffer();//Guarda los procesos y los pone en nuevos
        colaListo();//Los pone en listo
        procesarCola();//Procesa
      
    }

   

    private void tituloBuffer() {
        this.buffer.add("Algoritmo de planificación Round Robin\n");
        this.buffer.add("N(Nuevo) L(Listo) E(Ejecución) T(Terminado)\n");
        this.buffer.add("Cantidad de procesos: " + this.n + "\n");
        this.buffer.add("Quantum: " + this.quantum + "\n");
        guardarBuffer();
    }

    private void informeProc(){
     int i=0;    
     this.inf.add("\nServ");
        for(Proceso p : procesos) {
         
            this.inf.add("\t"+(p.espera+instru[i++]));
        }   
      
     this.inf.add("\nRetorno");
        for(Proceso p : procesos) {
            this.inf.add("\t"+(p.fin));
        }  
       guardarInforme();
       
    }
    
    private void guardarBuffer() {
        this.buffer.add("\nProceso");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.pcb);
        }
        this.buffer.add("\nID");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.id);
        }
        this.buffer.add("\nInstruc");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.instru));
        }
        this.buffer.add("\nEstado");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + p.estado);
        }
        this.buffer.add("\nPosic");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.posic));
        }
        this.buffer.add("\nEspera");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.espera));
        }
        
         this.buffer.add("\nFin");
        for(Proceso p : procesos) {
            this.buffer.add("\t" + Integer.toString(p.fin));
        }
        this.buffer.add("\n");
       
        guardarArchivo();
        
    }

    private void colaListo() {
        for (int i = 0; i < this.n; i++) {
            this.procesos[i].estado = "L";
        }
        guardarBuffer();
    }

    private void procesarCola() {
        
        while (procesosNoTerminados()) { //Procesa mientras encuentra el primer proceso que no este terminado
            for (Proceso p : procesos) {
                trabajarProceso(p);
                Tespera(); 
                cont++;
            } 
        }
        reposicionarCola();//Pone los procesos terminados en 0 y el resto en orden
        informeProc();
        guardarBuffer();//Imprime el resultado final
       
    }

    private void reposicionarCola() {
        int posicion = 0;
        for (Proceso p : procesos) {
            if (p.estado.equals("T")) {
                p.posic = 0;
            } else {
                posicion += 1;
                p.posic = posicion;
            }
        }
    }

    private boolean procesosNoTerminados() {
        for (Proceso p : procesos) { //Si no encuentra ninguno diferente a T, entonces todos estan terminados
            if (!"T".equals(p.estado)) {
                return true;
            }
        }
        return false;
    }

    
    private void Tespera(){
        
       for (Proceso p : procesos) {
              if(!"T".equalsIgnoreCase(p.estado)){
                  System.out.println("Actual espera "+p.espera);  
                p.espera+=1;  
              }
           } 
        
    }
    
    private void trabajarProceso(Proceso p) {
        if (p.instru > quantum) {//Si es mayor que el quantum se disminuye en el valor de este mismo
            p.instru -= quantum;
            p.estado = "E";
            guardarBuffer();
            p.estado = (p.instru == 0) ? "T" : "L";
            if(p.estado.equalsIgnoreCase("T")){
              p.fin=cont;  
            }
        } else if (!"T".equals(p.estado)) { //De lo contrario se termina el proceso
            p.instru = 0;
            p.estado = "E";
            reposicionarCola();
            guardarBuffer();
            p.estado = "T";
            if(p.estado.equalsIgnoreCase("T")){
              p.fin=cont;  
            }
            //Asignar el tiempo en el cual termino
        }
    }

    private void guardarArchivo() {
        try {
            String buf = String.join("", this.buffer);
            FileWriter fw = new FileWriter(this.archivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(buf);
            pw.close();
            this.buffer.clear();
        } catch (Exception e) {
            System.out.println("Error al guardar " + this.archivo);
        }
    }
    
     private void guardarInforme() {
        try {
            String buf = String.join("", this.inf);
            FileWriter fw = new FileWriter(this.archivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(buf);
            pw.close();
            this.inf.clear();
        } catch (Exception e) {
            System.out.println("Error al guardar " + this.archivo);
        }
    }
}
