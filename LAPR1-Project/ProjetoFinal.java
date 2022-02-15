/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetofinal;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.FileTerminal;
import com.panayotis.gnuplot.terminal.GNUPlotTerminal;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

/**
 *
 * @authors Luís Correia Tiago Loureiro Márcio Fernandes Luís Araujo
 *
 */
public class ProjetoFinal {

    static Scanner ler = new Scanner(System.in);
    static final int NUM_LINHAS = 30000;
    static double n;
    static String data = "";   //Variável global para ser usado em todo o código

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String path = "";
        boolean verificarNaoInterativo = true;
        if (args.length == 2 && args[0].equalsIgnoreCase("-nome")) {        //Se tiver dois argumentos e um dele for o "-nome", o ficheiro vai ser o segundo argumento
            verificarNaoInterativo = false;
            String ficheiro = args[1];
            String[] dateTime = new String[NUM_LINHAS]; //Array de data e tempo juntos
            String[][] date_Time = new String[NUM_LINHAS][2];
            int[] values = new int[NUM_LINHAS];
            double[] valuesToPlot = new double[NUM_LINHAS];       //Array com os valores que serão apresentados no gráfico de acordo com a resolução temporal
            String[] datasResolucaoTemporal = new String[NUM_LINHAS];
            int numDados = 0, x, escolha, escolhaAnalise, escolhaResolucao = 0, modeloPrevisao;
            for (x = 0; x < NUM_LINHAS; x++) {          //Inicialização do array a -1, pois o consumo nunca será negativo
                valuesToPlot[x] = -1;
            }
            do {
                System.out.println("+===================== MENU PRINCIPAL =======================+");
                System.out.println("| 1 - Leitura do ficheiro                                    |");
                System.out.println("| 2 - Análise da série                                       |");
                System.out.println("| 3 - Cálculo do número de observações                       |");
                System.out.println("| 4 - Ordenação dos valores de consumo                       |");
                System.out.println("| 5 - Sair                                                   |");
                System.out.println("+============================================================+");
                System.out.println("Introduza a sua escolha");
                escolha = ler.nextInt();
                ler.nextLine();
                switch (escolha) {
                    case 1:
                        numDados = lerFicheiro(dateTime, values, ficheiro);
                        separarDataHora(date_Time, dateTime, numDados);
                        break;
                    case 2:
                        if (verificarPassagem1(numDados)) {
                            for (x = 0; x < NUM_LINHAS; x++) {          //Necessário alterar o array para este ficar novamente na sua forma inicial
                                valuesToPlot[x] = -1;
                                datasResolucaoTemporal[x] = null;
                            }
                            escolhaResolucao = escolherResolucaoTemporal(numDados, escolhaResolucao, date_Time, values, valuesToPlot, datasResolucaoTemporal, verificarNaoInterativo);
                            if (escolhaResolucao != 5) { //5 é utilizado como valor de saída
                                System.out.println("1 - Análise da série com diferentes resoluções");
                                System.out.println("2 - Filtragem/Suavização da série");
                                System.out.println("3 - Previsão de valores futuros");
                                System.out.println("4 - Sair");
                                escolhaAnalise = ler.nextInt();
                                ler.nextLine();
                                switch (escolhaAnalise) {
                                    case 1:
                                        graph(valuesToPlot, numDados, graphTitle(escolhaResolucao), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                                        saveToFile(valuesToPlot, ficheiro.split("\\.")[0] + " " + graphTitle(escolhaResolucao), verificarNaoInterativo, path);
                                        break;
                                    case 2:
                                        modeloPrevisao = escolhaModelo();
                                        filter(values, date_Time, numDados, valuesToPlot, escolhaResolucao, modeloPrevisao, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                                        break;
                                    case 3:
                                        modeloPrevisao = escolhaModelo();
                                        if (modeloPrevisao == 1 || modeloPrevisao == 2) {
                                            System.out.println("Introduza o momento de previsão (Separado por hífens)");
                                            String momentoPrevisao = ler.nextLine();
                                            previsao(valuesToPlot, datasResolucaoTemporal, escolhaResolucao, modeloPrevisao, momentoPrevisao, verificarNaoInterativo, n);
                                        }
                                        break;
                                    case 4:
                                        break;
                                }
                            } else {
                                escolhaResolucao = 0;   //Se a escolha for igual a 5 (saída ou inválida) volta a 0
                            }
                        }
                        break;
                    case 3:
                        if (verificarPassagem1(numDados)) {
                            calcIntervals(values, date_Time, numDados);
                        }
                        break;
                    case 4:
                        if (verificarPassagem1(numDados)) {
                            for (x = 0; x < NUM_LINHAS; x++) {          //Necessário alterar o array para este ficar novamente na sua forma inicial
                                valuesToPlot[x] = -1;
                            }
                            escolhaResolucao = escolherResolucaoTemporal(numDados, escolhaResolucao, date_Time, values, valuesToPlot, datasResolucaoTemporal, verificarNaoInterativo);
                            if (escolhaResolucao != 5) {
                                int escolhaOrdenacao;
                                System.out.println("Deseja fazer a ordenação:");
                                System.out.println("1 - Crescente");
                                System.out.println("2 - Decrescente");
                                System.out.println("3 - Sair");
                                escolhaOrdenacao = ler.nextInt();
                                ler.nextLine();
                                double[] aux = new double[numDados];
                                double[] aux2 = new double[numDados];
                                for (int i = 0; i < numDados; i++) {
                                    aux[i] = valuesToPlot[i];
                                    aux2[i] = valuesToPlot[i];
                                }
                                switch (escolhaOrdenacao) {
                                    case 1:
                                        sortValuesIncreasing(aux, date_Time, numDados);
                                        System.out.println("Ordenação crescente efetuada");
                                        graph(aux, numDados, (graphTitle(escolhaResolucao) + " crescente"), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                                        saveToFile(aux, ficheiro.split("\\.")[0] + " " + (graphTitle(escolhaResolucao) + " crescente"), verificarNaoInterativo, path);
                                        break;
                                    case 2:
                                        sortValuesDecreasing(aux2, date_Time, numDados);
                                        System.out.println("Ordenação decresente efetuada");
                                        graph(aux2, numDados, (graphTitle(escolhaResolucao) + " decrescente"), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                                        saveToFile(aux2, ficheiro.split("\\.")[0] + " " + (graphTitle(escolhaResolucao) + " decrescente"), verificarNaoInterativo, path);
                                        break;
                                    case 3:
                                        break;
                                    default:
                                        System.out.println("Escolha Inválida");
                                        break;
                                }
                            }
                        }
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Escolha Inválida");
                        break;
                }
                for (x = 0; x < 5; x++) {
                    System.out.println();
                }
            } while (escolha != 5);
        } else {
//----------------------------------------------------------------------------//
//                          Modo não-interativo                               //  
//----------------------------------------------------------------------------//
            if (args.length == 12) { //Modo não-interativo
                verificarNaoInterativo = true;
                int escolhaResolucao = 0;
                int modelo = 0;
                int ordenacao = 0;
                if (args[0].equalsIgnoreCase("-nome") && args[2].equalsIgnoreCase("-resolucao") && args[4].equalsIgnoreCase("-modelo") && args[6].equalsIgnoreCase("-tipoOrdenacao") && args[8].equalsIgnoreCase("-parModelo") && args[10].equalsIgnoreCase("-momentoPrevisao")) {
                    String ficheiro = args[1];
                    File file_to_read = new File(args[1]);
                    if (file_to_read.exists()) {
                        if (Integer.parseInt(args[3]) >= 11 && Integer.parseInt(args[3]) <= 14 || Integer.parseInt(args[3]) >= 1 && Integer.parseInt(args[3]) <= 4) {
                            escolhaResolucao = Integer.parseInt(args[3]);
                            if (Integer.parseInt(args[5]) == 1 || Integer.parseInt(args[5]) == 2) {
                                modelo = Integer.parseInt(args[5]);
                                if (Integer.parseInt(args[7]) == 1 || Integer.parseInt(args[7]) == 2) {
                                    ordenacao = Integer.parseInt(args[7]);
                                    if (Integer.parseInt(args[5]) == 1) {
                                        if (Double.parseDouble(args[9]) >= 0) {
                                            n = Double.parseDouble(args[9]);
                                        } else {
                                            System.out.println("Valor inválido para n");
                                            verificarNaoInterativo = false;
                                        }
                                    } else {
                                        if (Integer.parseInt(args[5]) == 2) {
                                            if (Double.parseDouble(args[9]) > 0 && Double.parseDouble(args[9]) <= 1) {
                                                n = Double.parseDouble(args[9]);
                                            } else {
                                                System.out.println("Valor inválido para o alpha");
                                                verificarNaoInterativo = false;
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Escolha errada para a ordenação");
                                    verificarNaoInterativo = false;
                                }
                            } else {
                                System.out.println("Escolha inválida para o modelo");
                                verificarNaoInterativo = false;
                            }
                        } else {
                            System.out.println("Escolha inválida para a resolução temporal");
                            verificarNaoInterativo = false;
                        }
                    } else {
                        System.out.println("Ficheiro inexistente");
                        verificarNaoInterativo = false;
                    }
                } else {
                    System.out.println("Comandos inválidos");
                    verificarNaoInterativo = false;
                }
                if (verificarNaoInterativo == true) {
                    String foldName = args[1].split("\\.")[0];
                    LocalDateTime now = LocalDateTime.now();
                    data = Integer.toString(now.getHour()) + "-" + Integer.toString(now.getMinute()) + "-" + Integer.toString(now.getSecond());
                    new File(foldName + "_naoInterativo" + "_" + data).mkdirs();
                    path = ".\\" + foldName + "_naoInterativo" + "_" + data + "\\";
                    PrintStream out = new PrintStream(new FileOutputStream(path + foldName + "_" + "não_interativo" + "_" + data + ".txt", true));
                    System.setOut(out);
                    int x;
                    String[] dateTime = new String[NUM_LINHAS]; //Array de data e tempo juntos
                    String[][] date_Time = new String[NUM_LINHAS][2];
                    int[] values = new int[NUM_LINHAS];
                    double[] valuesToPlot = new double[NUM_LINHAS];
                    for (x = 0; x < NUM_LINHAS; x++) {
                        valuesToPlot[x] = -1;
                    }
                    String[] datasResolucaoTemporal = new String[NUM_LINHAS];
                    String ficheiro = args[1];
                    int numDados = lerFicheiro(dateTime, values, ficheiro);
                    separarDataHora(date_Time, dateTime, numDados);
                    escolhaResolucao = escolherResolucaoTemporal(numDados, escolhaResolucao, date_Time, values, valuesToPlot, datasResolucaoTemporal, verificarNaoInterativo);
                    graph(valuesToPlot, numDados, graphTitle(escolhaResolucao), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                    saveToFile(valuesToPlot, ficheiro.split("\\.")[0] + " " + graphTitle(escolhaResolucao), verificarNaoInterativo, path);
                    filter(values, date_Time, numDados, valuesToPlot, escolhaResolucao, modelo, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                    previsao(valuesToPlot, datasResolucaoTemporal, escolhaResolucao, modelo, args[11], verificarNaoInterativo, n);
                    calcIntervals(values, date_Time, numDados);
                    double[] aux = new double[numDados];
                    double[] aux2 = new double[numDados];
                    for (int i = 0; i < numDados; i++) {
                        aux[i] = valuesToPlot[i];
                        aux2[i] = valuesToPlot[i];
                    }
                    if (ordenacao == 1) {
                        sortValuesIncreasing(aux, date_Time, numDados);
                        System.out.println("Ordenação crescente efetuada");
                        graph(aux, numDados, (graphTitle(escolhaResolucao) + " crescente"), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                        saveToFile(aux, ficheiro.split("\\.")[0] + " " + (graphTitle(escolhaResolucao) + " crescente"), verificarNaoInterativo,path);
                    } else {
                        sortValuesDecreasing(aux2, date_Time, numDados);
                        System.out.println("Ordenação decresente efetuada");
                        graph(aux2, numDados, (graphTitle(escolhaResolucao) + " decrescente"), 0, verificarNaoInterativo, ficheiro.split("\\.")[0], path);
                        saveToFile(aux2, ficheiro.split("\\.")[0] + " " + (graphTitle(escolhaResolucao) + " decrescente"), verificarNaoInterativo,path);
                    }
                }
            } else {
                if (args[0].equalsIgnoreCase("-nome") == false) {
                    System.out.println("Comandos inválidos");
                } else {
                    System.out.println("Número inválido de argumentos");
                    verificarNaoInterativo = false;
                }
            }
        }
    } //----------------------------------------------------------------------------//

    public static int lerFicheiro(String[] dateTime, int[] values, String ficheiro) throws FileNotFoundException {
        int contLinhas = 0;
        if ((new File(ficheiro)).exists()) {
            Scanner readFile = new Scanner(new File(ficheiro));
            String linha;
            linha = readFile.nextLine();    //Ignorar primeira linha
            while (readFile.hasNextLine()) {
                linha = readFile.nextLine();
                String[] content = linha.split(",");
                dateTime[contLinhas] = content[0];
                values[contLinhas] = Integer.parseInt(content[1]);
                contLinhas++;
            }
            System.out.println("Ficheiro Lido");
        } else {
            System.out.println("Ficheiro inexistente.");
        }
        return contLinhas;
    }

//----------------------------------------------------------------------------//
    public static void separarDataHora(String[][] date_Time, String[] dateTime, int numDados) {
        int x;
        for (x = 0; x < numDados; x++) {
            date_Time[x][0] = dateTime[x].split(" ")[0];
            date_Time[x][1] = dateTime[x].split(" ")[1];
        }
    }

//----------------------------------------------------------------------------//   
    public static int escolherResolucaoTemporal(int numDados, int escolhaResolucao, String[][] date_Time, int[] values, double[] valuesToPlot, String[] datasResolucaoTemporal, boolean verificarNaoInterativo) {
        if (!verificarNaoInterativo) {
            System.out.println("Escolha uma resolução temporal para os quais deseja analisar a série:");
            System.out.println("1 - Períodos do dia");
            System.out.println("2 - Diário");
            System.out.println("3 - Mensal");
            System.out.println("4 - Anual");
            System.out.println("5 - Sair");
            escolhaResolucao = ler.nextInt();
        }
        switch (escolhaResolucao) {
            case 1:
                System.out.println("11 - Manhã");
                System.out.println("12 - Tarde");
                System.out.println("13 - Noite");
                System.out.println("14 - Madrugada");
                System.out.println("5 - Sair");
                escolhaResolucao = ler.nextInt();
                if (escolhaResolucao >= 11 && escolhaResolucao <= 14) {
                    resolucaoTemporal(date_Time, values, valuesToPlot, escolhaResolucao, numDados, datasResolucaoTemporal);
                } else {
                    if (escolhaResolucao == 5) {
                    } else {
                        System.out.println("Escolha Inválida");
                        escolhaResolucao = 5;       //O 5 é usado como a opção "saída" para a resolução temporal
                    }
                }
                break;
            case 2:
                resolucaoTemporal(date_Time, values, valuesToPlot, escolhaResolucao, numDados, datasResolucaoTemporal);
                break;
            case 3:
                resolucaoTemporal(date_Time, values, valuesToPlot, escolhaResolucao, numDados, datasResolucaoTemporal);
                break;
            case 4:
                resolucaoTemporal(date_Time, values, valuesToPlot, escolhaResolucao, numDados, datasResolucaoTemporal);
                break;
            case 5:
                break;
            default:
                System.out.println("Escolha inválida");
                escolhaResolucao = 5;       //O 5 é usado como a opção "saída" para a resolução temporal
                break;
        }
        return escolhaResolucao;
    }
//----------------------------------------------------------------------------//   

    public static void sortValuesIncreasing(double[] values, String[][] date_Time, int numDados) { //bubble sort
        int numDadosPlot = 0;
        for (int x = 0; x < numDados; x++) {
            if (values[x] != -1) {
                numDadosPlot++;
            }
        }
        for (int i = 0; i < numDadosPlot; i++) {
            for (int j = 0; j < numDadosPlot - i - 1; j++) {
                if (values[j] > values[j + 1]) {            //Comparação do valor com todos os seguintes.                                                  
//                    String[] aux = date_Time[j];
//                    date_Time[j] = date_Time[j + 1];
//                    date_Time[j + 1] = aux;
                    double auxVal = values[j];
                    values[j] = values[j + 1];
                    values[j + 1] = auxVal;
                }
            }
        }
    }

//----------------------------------------------------------------------------//
    public static void sortValuesDecreasing(double[] values, String[][] date_Time, int numDados) { //tiago sort
        int numDadosPlot = 0;
        for (int x = 0; x < numDados; x++) {
            if (values[x] != -1) {
                numDadosPlot++;
            }
        }

        for (int i = 0; i < numDadosPlot - 1; i++) {
            for (int j = i + 1; j < numDadosPlot; j++) {
                if (values[i] < values[j]) {            //Comparação do valor com todos os seguintes. 
//                    String[] aux = date_Time[i];
//                    date_Time[i] = date_Time[j];
//                    date_Time[j] = aux;
                    double auxVal = values[i];
                    values[i] = values[j];
                    values[j] = auxVal;
                }
            }
        }
    }

//----------------------------------------------------------------------------//
    public static boolean verificarPassagem1(int numDados) {
        boolean verifica = true;
        if (numDados == 0) {
            System.out.println("Deve passar pelo primeiro ponto para a leitura do ficheiro ser efetuada");
            verifica = false;
        }
        return verifica;
    }

//----------------------------------------------------------------------------//
    public static void resolucaoTemporal(String[][] date_Time, int[] values, double[] valuesToPlot, int escolhaResolucao, int numDados, String[] datasResolucaoTemporal) {  //Cria as condições necessárias para resolução temporal (array valuesToPlot)
        int x;
        int[] somaMensal = new int[NUM_LINHAS];
        switch (escolhaResolucao) {
            case 2:
                System.out.println("Foi escolhida uma resolução temporal diária");
                obterArrayDias(date_Time, datasResolucaoTemporal, numDados);
                diario(date_Time, values, valuesToPlot, numDados);
                break;
            case 3:
                int numMeses;
                System.out.println("Foi escolhida uma resolução temporal mensal");
                numMeses = obterArrayMeses(date_Time, datasResolucaoTemporal, numDados);
                calcularSomaMensal(date_Time, values, datasResolucaoTemporal, numDados, numMeses, somaMensal);
                for (x = 0; x < numMeses; x++) {
                    valuesToPlot[x] = somaMensal[x];
                }
                break;
            case 4:
                System.out.println("Foi escolhida uma resolução temporal anual");
                obterArrayAnos(date_Time, datasResolucaoTemporal, numDados);
                anual(date_Time, values, valuesToPlot, numDados);
                break;
            case 11:
                System.out.println("Foi escolhida uma resolução temporal da manhã");
                obterArrayDias(date_Time, datasResolucaoTemporal, numDados);
                periodoDia(6, 0, 0, 11, 59, 0, date_Time, values, valuesToPlot, numDados);
                break;
            case 12:
                System.out.println("Foi escolhida uma resolução temporal da tarde");
                obterArrayDias(date_Time, datasResolucaoTemporal, numDados);
                periodoDia(12, 0, 0, 17, 59, 0, date_Time, values, valuesToPlot, numDados);
                break;
            case 13:
                System.out.println("Foi escolhida uma resolução temporal da noite");
                obterArrayDias(date_Time, datasResolucaoTemporal, numDados);
                periodoDia(18, 0, 0, 23, 59, 0, date_Time, values, valuesToPlot, numDados);
                break;
            case 14:
                System.out.println("Foi escolhida uma resolução temporal da madrugada");
                obterArrayDias(date_Time, datasResolucaoTemporal, numDados);
                periodoDia(0, 0, 0, 5, 59, 0, date_Time, values, valuesToPlot, numDados);
                break;
        }
    }

//----------------------------------------------------------------------------//
//          Obtenção de um array com todos os meses dos ficheiros             //
//----------------------------------------------------------------------------//    
    public static int obterArrayMeses(String[][] date_Time, String[] meses, int numDados) {
        int x, posArray = 0;
        String mesAnoAtual = null;
        for (x = 0; x < numDados; x++) {
            if (x == 0) {
                meses[posArray] = (date_Time[x][0].split("-")[0] + "-" + date_Time[x][0].split("-")[1]);     //Pega-se no ano (primeiro split) e no mês (segundo split), juntando ambos numa string que nos dá ANO-MÊS
                mesAnoAtual = meses[posArray];
                posArray++;
            } else {
                if ((date_Time[x][0].split("-")[0] + "-" + date_Time[x][0].split("-")[1]).equalsIgnoreCase(mesAnoAtual) == false) {     //Se o ano e mês forem diferentes do atual, é acrescentado ao array de meses
                    meses[posArray] = date_Time[x][0].split("-")[0] + "-" + date_Time[x][0].split("-")[1];
                    mesAnoAtual = meses[posArray];                  //Mes atual é alterado para o mais recente 
                    posArray++;
                }
            }
        }
//        for (int i = 0; meses[i] != null; i++) {
//            System.out.println(meses[i]);
//        }
        return posArray;                //Retornar a quantidade de meses
    }
//----------------------------------------------------------------------------//
//                  Cálculo da soma do consumo mensal                         //
//----------------------------------------------------------------------------//  

    public static void calcularSomaMensal(String[][] date_Time, int[] values, String[] meses, int numDados, int numMeses, int[] somaMensal) {
        int posMes, x;
        for (posMes = 0; posMes < numMeses; posMes++) {
            somaMensal[posMes] = 0;                                         //A soma mensal de cada mês inicia em 0
            for (x = 0; x < numDados; x++) {
                if ((date_Time[x][0].split("-")[0] + "-" + date_Time[x][0].split("-")[1]).equalsIgnoreCase(meses[posMes])) {    //Se o valor no ficheiro tiver a mesmo mês e ano, o valor é adicionado à soma mensal
                    somaMensal[posMes] = somaMensal[posMes] + values[x];
                }
            }
        }
    }

//----------------------------------------------------------------------------//
    public static void periodoDia(int hi, int mi, int si, int hf, int mf, int sf, String[][] date_Time, int[] values, double[] valuesToPlot, int numDados) {
        int index = 0;
        String data = date_Time[0][0];
        int soma = 0;
        for (int i = 0; date_Time[i][0] != null; i++) {
            String[] hh_mm_ss = date_Time[i][1].split(":");
            int hh = Integer.parseInt(hh_mm_ss[0]);
            int mm = Integer.parseInt(hh_mm_ss[1]);
            int ss = Integer.parseInt(hh_mm_ss[2]);

            if ((hh >= hi && mm >= mi && ss >= si) && (hh <= hf && mm <= mf && ss <= sf)) {
                if (data.compareToIgnoreCase(date_Time[i][0]) == 0) {
                    soma += values[i];
                } else {
                    valuesToPlot[index] = soma;
                    data = date_Time[i][0];
                    soma = values[i];
                    index++;
                }
            }
        }
    }

//----------------------------------------------------------------------------//
    public static void diario(String[][] date_Time, int[] values, double[] valuesToPlot, int numDados) {
        int index = 0;
        String data = date_Time[0][0];
        int soma = 0;

        for (int i = 0; date_Time[i][0] != null; i++) {

            if (data.compareToIgnoreCase(date_Time[i][0]) == 0) {
                soma += values[i];
            } else {
                valuesToPlot[index] = soma;
                data = date_Time[i][0];
                soma = values[i];
                index++;
            }
        }
    }
//----------------------------------------------------------------------------//

    public static void obterArrayDias(String[][] date_Time, String[] dias, int numDados) {
        int x, posArray = 0;
        String diaAtual = null;
        for (x = 0; x < numDados; x++) {
            if (x == 0) {
                diaAtual = date_Time[x][0].split("-")[1] + date_Time[x][0].split("-")[2];
                dias[posArray] = diaAtual;
                posArray++;
            } else {
                if ((date_Time[x][0].split("-")[1] + "-" + date_Time[x][0].split("-")[2]).equalsIgnoreCase(diaAtual) == false) {
                    diaAtual = date_Time[x][0].split("-")[1] + "-" + date_Time[x][0].split("-")[2];
                    dias[posArray] = (date_Time[x][0].split("-")[0] + "-" + diaAtual);
                    posArray++;
                }
            }
        }
//        for (int i = 0; dias[i] != null; i++) {
//            System.out.println(dias[i]);
//        }
    }
//----------------------------------------------------------------------------//

    public static void obterArrayAnos(String[][] date_Time, String[] anos, int numDados) {
        int x, posArray = 0;
        String anoAtual = null;
        for (x = 0; x < numDados; x++) {
            if (x == 0) {
                anoAtual = date_Time[x][0].split("-")[0];
                anos[posArray] = anoAtual;
                posArray++;
            } else {
                if (date_Time[x][0].split("-")[0].equalsIgnoreCase(anoAtual) == false) {
                    anoAtual = date_Time[x][0].split("-")[0];
                    anos[posArray] = anoAtual;
                    posArray++;
                }
            }
        }
    }
//----------------------------------------------------------------------------//

    public static void anual(String[][] date_Time, int[] values, double[] valuesToPlot, int numDados) {
        int x, posArray = 0, somaAnual = 0;
        String anoAtual = null;
        for (x = 0; x < numDados; x++) {
            if (x == 0) {
                anoAtual = date_Time[x][0].split("-")[0];
                somaAnual = somaAnual + values[x];
                valuesToPlot[posArray] = somaAnual;
            } else {
                if (date_Time[x][0].split("-")[0].equalsIgnoreCase(anoAtual)) {
                    somaAnual = somaAnual + values[x];      //Nota: É uma soma e por isso é preciso adicionar o seu valor anterior!!
                    valuesToPlot[posArray] = somaAnual;
                } else {
                    posArray++;
                    anoAtual = date_Time[x][0].split("-")[0];
                    somaAnual = values[x];
                    valuesToPlot[posArray] = somaAnual;
                }
            }
        }
    }
//----------------------------------------------------------------------------//

    public static String graphTitle(int escolhaResolucao) {
        String titulo = null;
        switch (escolhaResolucao) {
            case 11:
                titulo = "Manhã";
                break;
            case 12:
                titulo = "Tarde";
                break;
            case 13:
                titulo = "Noite";
                break;
            case 14:
                titulo = "Madrugada";
                break;
            case 2:
                titulo = "Diário";
                break;
            case 3:
                titulo = "Mensal";
                break;
            case 4:
                titulo = "Anual";
                break;
        }
        return titulo;
    }
//----------------------------------------------------------------------------//

    public static void graph(double[] values, int numDados, String titulo, int posInicial, boolean verificarNaoInterativo, String ficheiro, String path) throws IOException {
        String graphEscolha = null;
        if (!verificarNaoInterativo) {
            int numDadosPlot = 0, x;
            JavaPlot p = new JavaPlot();
            p.getAxis("x").setLabel("Unidade de Tempo");
            p.getAxis("y").setLabel("Consumption_MW");
            for (x = 0; x < numDados; x++) {        //Verificação de quantos dados são diferentes de -1 
                if (values[x] != -1) {
                    numDadosPlot++;
                }
            }
            PlotStyle myPlotStyle = new PlotStyle();
            myPlotStyle.setStyle(Style.LINESPOINTS);
            myPlotStyle.setPointType(7);
            myPlotStyle.setPointSize(1);
            double[][] tab;
            tab = new double[100000][2];
            for (int i = 0; i < numDadosPlot; i++) { //a resolução vai ser o i 
                tab[i][0] = i + posInicial;
                tab[i][1] = values[i]; //y
            }
            DataSetPlot s = new DataSetPlot(tab);
            s.setTitle(titulo);
            s.setPlotStyle(myPlotStyle);
            p.addPlot(s);
            p.plot();
            System.out.println("Pretende gravar o gráfico como ficheiro .png? (S/N)");
            graphEscolha = ler.nextLine();
        }
        if (verificarNaoInterativo || graphEscolha.equalsIgnoreCase("S")) {
            graphToFile(values, numDados, titulo, posInicial, ficheiro + " " + titulo, path);
            System.out.println("Ficheiro criado com sucesso");
        }
    }

//--------------------------------------------------------------------------//
    public static void calcIntervals(int[] values, String[][] date_Time, int numDados) {
        long somaTotal = 0;
        float mediaGlobal;
        for (int i = 0; i < numDados; i++) {
            somaTotal += values[i];
        }

        mediaGlobal = (float) (somaTotal / numDados);

        //limites do intervalo 1 -------------- Menor valor até limite 2
        int menorValor = values[0];
        for (int i = 0; i < numDados; i++) {
            if (values[i] < menorValor) {
                menorValor = values[i];
            }
        }

        float limite1 = mediaGlobal - (float) (mediaGlobal * 0.2);

        //limites do intervalo 2 -------------- limite1 até limite2
        float limite2 = mediaGlobal + (float) (mediaGlobal * 0.2);

        //limites do intervalo 3 -------------- limite2 até maior valor
        int maiorValor = values[0];
        for (int i = 0; i < numDados; i++) {
            if (values[i] > maiorValor) {
                maiorValor = values[i];
            }
        }
        int observe1 = 0;
        int observe2 = 0;
        int observe3 = 0;
        for (int i = 0; i < numDados; i++) {
            if (values[i] > menorValor && values[i] < limite1) { //está no intervalo 2
                observe1++;
            }
            if (values[i] >= limite1 && values[i] < limite2) { //está no intervalo 2
                observe2++;
            }
            if (values[i] >= limite2 && values[i] < maiorValor) { //está no intervalo 2
                observe3++;
            }

        }
        System.out.println(observe1 + " observações no Intervalo ] " + menorValor + " , " + limite1 + " [");
        System.out.println(observe2 + " observações no Intervalo [ " + limite1 + " , " + limite2 + " [");
        System.out.println(observe3 + " observações no Intervalo [ " + limite2 + " , " + maiorValor + " [");
    }

//----------------------------------------------------------------------------------------------
    public static void filter(int[] values, String[][] date_Time, int numDados, double[] valuesToPlot, int escolhaResolucao, int modeloFiltragem, boolean verificarNaoInterativo, String ficheiro, String path) throws FileNotFoundException {
        String mensagem = graphTitle(escolhaResolucao);
        String fileTitle = "";
        double[] serie_temporal2 = new double[numDados + 1];
        for (int x = 0; x < numDados + 1; x++) {
            serie_temporal2[x] = -1;
        }
        switch (modeloFiltragem) {
            case 1:
                fileTitle = "MMS";
                mms(serie_temporal2, valuesToPlot, verificarNaoInterativo);
                System.out.println("O erro é " + calcErro(valuesToPlot, serie_temporal2));
                System.out.println();
                two_graph(valuesToPlot, mensagem, serie_temporal2, "MMS", (int) n - 1, verificarNaoInterativo, ficheiro + " " + graphTitle(escolhaResolucao) + " " + fileTitle, path);

                saveToFile(serie_temporal2, ficheiro + " " + graphTitle(escolhaResolucao) + " " + fileTitle, verificarNaoInterativo,path);
                break;
            case 2:
                fileTitle = "MMEP";
                mmep(serie_temporal2, valuesToPlot, numDados, verificarNaoInterativo);
                System.out.println("O erro é " + calcErro(valuesToPlot, serie_temporal2));
                System.out.println();
                two_graph(valuesToPlot, mensagem, serie_temporal2, "MMEP", 0, verificarNaoInterativo, ficheiro + " " + graphTitle(escolhaResolucao) + " " + fileTitle, path);

                saveToFile(serie_temporal2, ficheiro + " " + graphTitle(escolhaResolucao) + " " + fileTitle, verificarNaoInterativo,path);
                break;
            case 3:
                break;
            default:
                System.out.println("Opção inválida");
                break;
        }
    }

    public static void mms(double[] serie_temporal2, double[] serie_temporal, boolean verificarNaoInterativo) {
        if (!verificarNaoInterativo) {
            System.out.println("Introduza o n para o cálculo da Média Móvel Simples:");
            n = ler.nextInt();
            ler.nextLine();
        }
        int index = 0;
        for (int i = (int) n - 1; serie_temporal[i] != -1; i++) {
            int soma = 0;
            for (int j = i; j > i - n; j--) {
                soma += serie_temporal[j];
            }
            serie_temporal2[index] = (double) (soma) / n;
            index++;
        }
    }

    public static void mmep(double[] serie_temporal2, double[] serie_temporal, int numDados, boolean verificarNaoInterativo) {
        if (!verificarNaoInterativo) {
            System.out.println("Introduza o alfa para o cálculo da Média Móvel Exponencialmente Pesada (alfa pertence a ]0,1]):");
            do {
                n = ler.nextDouble();
                ler.nextLine();
                if (n <= 0 || n > 1) {
                    System.out.println("Insira um valor de alfa no intervalo ]0,1]");
                }
            } while (n <= 0 || n > 1);
        }

        serie_temporal2[0] = serie_temporal[0];
        for (int i = 1; serie_temporal[i] != -1; i++) {
            serie_temporal2[i] = n * serie_temporal[i] + (1 - n) * serie_temporal2[i - 1];
        }

    }

    public static void two_graph(double[] serie_temporal, String mensagem, double[] serie_temporal2, String mensagem2, int posInicial, boolean verificarNaoInterativo, String nomePng, String path) {
        String graphEscolha = null;
        if (!verificarNaoInterativo) {
            JavaPlot p = new JavaPlot();
            p.getAxis("x").setLabel("Unidade de Tempo");
            p.getAxis("y").setLabel("Consumption_MW");
            PlotStyle myPlotStyle = new PlotStyle();
            myPlotStyle.setStyle(Style.LINESPOINTS);
            myPlotStyle.setPointType(7);
            myPlotStyle.setPointSize(1);
            double[][] tab;
            tab = new double[100000][2];
            for (int i = 0; serie_temporal[i] != -1; i++) { //a resolucao vai ser o i 
                tab[i][0] = i;
                tab[i][1] = serie_temporal[i]; //y
            }
            DataSetPlot s = new DataSetPlot(tab);
            s.setTitle(mensagem);
            s.setPlotStyle(myPlotStyle);

            PlotStyle myPlotStyle2 = new PlotStyle();
            myPlotStyle2.setStyle(Style.LINES);
            myPlotStyle2.setPointType(9);
            myPlotStyle2.setPointSize(1);
            myPlotStyle2.setLineWidth(3);
            double[][] tab2;
            tab2 = new double[100000][2];
            for (int i = 0; serie_temporal2[i] != -1; i++) { //a resolucao vai ser o i 
                tab2[i][0] = i + posInicial;
                tab2[i][1] = serie_temporal2[i]; //y
            }
            DataSetPlot s2 = new DataSetPlot(tab2);
            s2.setTitle(mensagem2);
            s2.setPlotStyle(myPlotStyle2);

            p.addPlot(s);
            p.addPlot(s2);

            p.plot();
            System.out.println("Pretende gravar o gráfico como ficheiro .png? (S/N)");
            graphEscolha = ler.nextLine();
        }
        if (verificarNaoInterativo || graphEscolha.equalsIgnoreCase("S")) {
            two_graphToFile(serie_temporal, mensagem, serie_temporal2, mensagem2, posInicial, nomePng, path);
            System.out.println("Ficheiro criado com sucesso");
        }
    }

    public static double calcErro(double[] serie_temporal, double[] serie_temporal2) {
        double soma = 0, erro;
        int n = 0;
        for (int i = 0; serie_temporal2[i] != -1; i++) {
            soma += Math.abs(serie_temporal2[i] - serie_temporal[i]);
            n++;
        }
        erro = soma / n;
        return erro;
    }

    public static void saveToFile(double[] valuesToPlot, String nome, boolean verificaNaoInterativo, String path) throws FileNotFoundException {
        String escolha = null;
        if (!verificaNaoInterativo) {
            System.out.println("Pretende gravar a série temporal em ficheiro .csv? (S/N)");
            escolha = ler.nextLine();
        }
        LocalDateTime now = LocalDateTime.now();
        data = Integer.toString(now.getHour()) + "-" + Integer.toString(now.getMinute()) + "-" + Integer.toString(now.getSecond());
        if (verificaNaoInterativo || escolha.equalsIgnoreCase("S")) {
            File ficheiro = new File(path + nome + "_" + data + ".csv");
            PrintWriter pw = new PrintWriter(ficheiro);   //Printwriter para poder escrever nesse ficheiro
            pw.println("Consumption_MW");
            for (int i = 0; valuesToPlot[i] != -1; i++) {
                pw.println(valuesToPlot[i]);
            }
            pw.close();
            System.out.println("Ficheiro criado com sucesso.");
        }
    }

//----------------------------------------------------------------------------//
    public static void previsao(double[] valuesToPlot, String[] datasResolucaoTemporal, int escolhaResolucao, int modeloPrevisao, String momentoPrevisao, boolean verificarNaoInterativo, double n) {
        int x, posArray = 0;
        boolean existeDataFicheiro = false;
        switch (modeloPrevisao) {
            case 1:                                                // Média Móvel Simples                
                for (x = 0; datasResolucaoTemporal[x] != null; x++) {
                    if (momentoPrevisao.equalsIgnoreCase(datasResolucaoTemporal[x])) {
                        existeDataFicheiro = true;
                        posArray = x;
                    }
                }
                if (existeDataFicheiro) {
                    double previsao[] = mms_Previsao(valuesToPlot, posArray, verificarNaoInterativo, (int) n);
                    System.out.println("O valor da previsão de consumo para " + momentoPrevisao + " é " + previsao[posArray - 1] + " MW");
                } else {
                    String momentoSeguinte = calcularMomentoSeguinte(datasResolucaoTemporal);  //Momento a seguir ao final no ficheiro
                    if (momentoSeguinte.equalsIgnoreCase(momentoPrevisao)) {
                        for (x = 0; valuesToPlot[x] != -1; x++) {
                            posArray = x;       //Última posição
                        }
                        double previsao[] = mms_Previsao(valuesToPlot, posArray, verificarNaoInterativo, (int) n);
                        System.out.println("O valor da previsão de consumo para " + momentoPrevisao + " é " + previsao[posArray - 1] + " MW");
                    } else {
                        System.out.println("Data inválida ou impossível de prever com os dados no ficheiro");
                    }
                }
                break;
            case 2:                                                // Média Móvel Exponencialmente Pesada
                for (x = 0; datasResolucaoTemporal[x] != null; x++) {
                    if (momentoPrevisao.equalsIgnoreCase(datasResolucaoTemporal[x])) {
                        existeDataFicheiro = true;
                        posArray = x;
                    }
                }
                if (existeDataFicheiro) {
                    double previsao[] = mmep_Previsao(valuesToPlot, posArray, verificarNaoInterativo, n);
                    System.out.println("O valor da previsão de consumo para " + momentoPrevisao + " é " + previsao[posArray] + " MW");
                } else {
                    String momentoSeguinte = calcularMomentoSeguinte(datasResolucaoTemporal);  //Momento a seguir ao final no ficheiro
                    if (momentoSeguinte.equalsIgnoreCase(momentoPrevisao)) {
                        for (x = 0; valuesToPlot[x] != -1; x++) {
                            posArray = x;       //Última posição
                        }
                        double previsao[] = mmep_Previsao(valuesToPlot, posArray + 1, verificarNaoInterativo, n);       //posArray+1 é o momento a seguir ao último!
                        System.out.println("O valor da previsão de consumo para " + momentoPrevisao + " é " + previsao[posArray + 1] + " MW");
                    } else {
                        System.out.println("Data inválida ou impossível de prever com os dados no ficheiro");
                    }
                }
                break;
            case 3:
                break;
        }

    }

//----------------------------------------------------------------------------//
    public static int escolhaModelo() {
        int escolha;
        System.out.println("Qual a média que deseja usar?");
        System.out.println("1 - Média móvel simples");
        System.out.println("2 - Média móvel exponencialmente pesada");
        System.out.println("3 - Sair");
        escolha = ler.nextInt();
        ler.nextLine();
        return escolha;
    }
//----------------------------------------------------------------------------//

    public static double[] mmep_Previsao(double[] serie_temporal, int numDados, boolean verificarNaoInterativo, double alfa) {
        double[] mmep = new double[numDados + 1];
        if (!verificarNaoInterativo) {
            System.out.println("Introduza o alfa para o cálculo da Média Móvel Exponencialmente Pesada (alfa pertence a ]0,1]):");
            do {
                alfa = ler.nextDouble();
                ler.nextLine();
                if (alfa <= 0 || alfa > 1) {
                    System.out.println("Insira um valor de alfa no intervalo ]0,1]");
                }
            } while (alfa <= 0 || alfa > 1);
        }

        mmep[0] = serie_temporal[0];
        for (int i = 0; i < numDados; i++) {
            mmep[i + 1] = alfa * serie_temporal[i] + (1 - alfa) * mmep[i];
        }
        return mmep;
    }
//----------------------------------------------------------------------------//

    public static double[] mms_Previsao(double[] serie_temporal, int numDados, boolean verificarNaoInterativo, int n) {
        if (!verificarNaoInterativo) {
            System.out.println("Introduza o n para o cálculo da Média Móvel Simples:");
            n = ler.nextInt();
            ler.nextLine();
        }
        double[] mms = new double[serie_temporal.length];
        for (int i = 0; i < serie_temporal.length; i++) {
            mms[i] = -1;
        }
        mms[0] = 0;
        int index = 1;
        for (int i = n - 1; serie_temporal[i] != -1; i++) {
            int soma = 0;
            for (int j = i; j > i - n; j--) {
                soma += serie_temporal[j];
            }
            mms[index] = (double) (soma) / n;
            index++;
        }

        return mms;
    }

    public static String calcularMomentoSeguinte(String[] datasResolucaoTemporal) {
        // buscar o último
        int year = 0, month = 0, day = 0;
        String datafinal = null;
        for (int i = 0; datasResolucaoTemporal[i] != null; i++) {
            datafinal = datasResolucaoTemporal[i];
        }

        int count = 0;
        for (int i = 0; i < datafinal.length(); i++) {
            if (datafinal.charAt(i) == '-') {
                count++;
            }
        }

        if (count == 0) {
            year = Integer.parseInt(datafinal) + 1;
            return Integer.toString(year);
        } else {
            if (count == 1) {
                year = Integer.parseInt(datafinal.split("-")[0]);
                month = Integer.parseInt(datafinal.split("-")[1]) + 1;
                if (month == 13) {
                    month = 1;
                    year++;
                }

                if (month < 10) {
                    return Integer.toString(year) + "-0" + Integer.toString(month);
                } else {
                    return Integer.toString(year) + "-" + Integer.toString(month);
                }
            } else {
                year = Integer.parseInt(datafinal.split("-")[0]);
                month = Integer.parseInt(datafinal.split("-")[1]);
                day = Integer.parseInt(datafinal.split("-")[2]) + 1;
                if (day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) {
                    day = 1;
                    month++;
                } else {
                    if (day == 32) {
                        if (month == 12) {
                            day = 1;
                            month = 1;
                            year++;
                        } else {
                            day = 1;
                            month++;
                        }
                    }
                    if (year % 4 == 0) {
                        if (day == 30 && month == 2) {
                            day = 1;
                            month++;
                        }
                    } else {
                        if (day == 29 && month == 2) {
                            day = 1;
                            month++;
                        }
                    }

                    if (day < 10) {
                        if (month < 10) {
                            return Integer.toString(year) + "-0" + Integer.toString(month) + "-0" + Integer.toString(day);
                        } else {
                            return Integer.toString(year) + "-" + Integer.toString(month) + "-0" + Integer.toString(day);
                        }
                    } else {
                        if (month < 10) {
                            return Integer.toString(year) + "-0" + Integer.toString(month) + "-" + Integer.toString(day);
                        } else {
                            return Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
                        }
                    }
                }
            }
        }

        return "";
    }

    public static void graphToFile(double[] values, int numDados, String titulo, int posInicial, String nomeFicheiro, String path) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        data = Integer.toString(now.getHour()) + "-" + Integer.toString(now.getMinute()) + "-" + Integer.toString(now.getSecond());
        int numDadosPlot = 0, x;
        JavaPlot p = new JavaPlot();
        GNUPlotTerminal terminal = new FileTerminal("png", path + nomeFicheiro + "_" + data + ".png");
        p.setTerminal(terminal);
        p.getAxis("x").setLabel("Unidade de Tempo");
        p.getAxis("y").setLabel("Consumption_MW");
        for (x = 0; x < numDados; x++) {        //Verificação de quantos dados são diferentes de -1 
            if (values[x] != -1) {
                numDadosPlot++;
            }
        }
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        double[][] tab;
        tab = new double[100000][2];
        for (int i = 0; i < numDadosPlot; i++) { //a resolução vai ser o i 
            tab[i][0] = i + posInicial;
            tab[i][1] = values[i]; //y
        }
        DataSetPlot s = new DataSetPlot(tab);
        s.setTitle(titulo);
        s.setPlotStyle(myPlotStyle);
        p.addPlot(s);
        p.plot();
    }

    public static void two_graphToFile(double[] serie_temporal, String mensagem, double[] serie_temporal2, String mensagem2, int posInicial, String nomeFicheiro, String path) {
        LocalDateTime now = LocalDateTime.now();
        data = Integer.toString(now.getHour()) + "-" + Integer.toString(now.getMinute()) + "-" + Integer.toString(now.getSecond());
        File file = new File(nomeFicheiro + ".png"); //Cria o ficheiro png
        JavaPlot p = new JavaPlot();
        GNUPlotTerminal terminal = new FileTerminal("png", path + nomeFicheiro + "_" + data + ".png");
        p.setTerminal(terminal);
        p.getAxis("x").setLabel("Unidade de Tempo");
        p.getAxis("y").setLabel("Consumption_MW");
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        double[][] tab;
        tab = new double[100000][2];
        for (int i = 0; serie_temporal[i] != -1; i++) { //a resolucao vai ser o i 
            tab[i][0] = i;
            tab[i][1] = serie_temporal[i]; //y
        }
        DataSetPlot s = new DataSetPlot(tab);
        s.setTitle(mensagem);
        s.setPlotStyle(myPlotStyle);

        PlotStyle myPlotStyle2 = new PlotStyle();
        myPlotStyle2.setStyle(Style.LINES);
        myPlotStyle2.setPointType(9);
        myPlotStyle2.setPointSize(1);
        myPlotStyle2.setLineWidth(3);
        double[][] tab2;
        tab2 = new double[100000][2];
        for (int i = 0; serie_temporal2[i] != -1; i++) { //a resolucao vai ser o i 
            tab2[i][0] = i + posInicial;
            tab2[i][1] = serie_temporal2[i]; //y
        }
        DataSetPlot s2 = new DataSetPlot(tab2);
        s2.setTitle(mensagem2);
        s2.setPlotStyle(myPlotStyle2);

        p.addPlot(s);
        p.addPlot(s2);

        p.plot();

    }

}
