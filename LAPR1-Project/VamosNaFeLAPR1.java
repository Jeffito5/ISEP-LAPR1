package vamosnafelapr1;


import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class VamosNaFeLAPR1 {
    final static int MAX_LENGTH = 100000;
    final static int COLUMNS = 2;
    static int c;    
    
    public static void main(String[] args) throws FileNotFoundException {
        int ex;
        String[][] data_hora = new String[MAX_LENGTH][COLUMNS]; // campo extra para pontos
        int[] valores = new int[MAX_LENGTH];
        Scanner sc = new Scanner(System.in);
        c = 0;
        do {
            System.out.println("+===================== MENU PRINCIPAL =======================+");
            System.out.println("| 1 - Leitura do ficheiro                                    |");
            System.out.println("| 15 - Sair                                                  |");
            System.out.println("+============================================================+");
            
            
            ex = sc.nextInt();
            switch (ex) {
                case 1: 
                    readFile(data_hora, valores);
                    Test1_Basic(valores);
                    break;
                default: 
                    break;
            }
            
        }while (ex<=14);
         
    }
    
    public static void readFile(String[][] data_hora, int[] valores) throws FileNotFoundException {
        File fich = new File("DAYTON.csv");
        if (fich.exists()) {
            Scanner sc = new Scanner(fich);
            
            // ler linha do cabeçalho
            sc.nextLine();           
            while (sc.hasNextLine()) { // ler até acabar o ficheiro
                // atribuição às variaveis
                String linha = sc.nextLine();
                String[] campos = linha.split(",");
                String[] dataehora = campos[0].split(" ");
                data_hora[c][0] = dataehora[0];
                data_hora[c][1] = dataehora[1];
                valores[c] = Integer.parseInt(campos[1]);
                c++;
            }
            sc.close();
            System.out.println("Ficheiro lido com sucesso!");
            
        }else{
            System.out.println("O ficheiro não existe!");
        }        
    }
    

    public static void Test1_Basic(int[] valores)
    {
        JavaPlot p = new JavaPlot();

        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        //RgbPlotColor = new RgbPlotColor(
        myPlotStyle.setPointType(7);

        double tab[][] = new double[30000][3];
        for (int i = 0; i < c; i++) {
            tab[i][0] = i;
            tab[i][1] = valores[i];
        }
        
        DataSetPlot s = new DataSetPlot(tab);
        s.setTitle("Teste");
        s.setPlotStyle(myPlotStyle);

        //p.newGraph();
        p.addPlot(s);

        p.newGraph();
        p.plot();
    }    
}
