/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetofinal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ASUS
 */
public class ProjetoFinalTest {
    
    public ProjetoFinalTest() {
    }


    /**
     * Test of sortValuesIncreasing method, of class ProjetoFinal.
     */
    @Test
    public void testSortValuesIncreasing() {
        System.out.println("sortValuesIncreasing");
        double[] values = {1234243, 3231312, 1233114, 1323131, 132323, 42322213, 322431223, 23412333, 2311232, 324434234, 23423244, 23441231, 2343231, 23431233, 23424343, 12331313, 233242312, 3243242, 234123231, 23413222, 23131234,324344423, 12312313, 32423421, 23466, -1};
        String[][] date_Time = null;
        int numDados = 26;
        ProjetoFinal.sortValuesIncreasing(values, date_Time, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sortValuesDecreasing method, of class ProjetoFinal.
     */
    @Test
    public void testSortValuesDecreasing() {
        System.out.println("sortValuesDecreasing");
        double[] values = {1234243, 3231312, 1233114, 1323131, 132323, 42322213, 322431223, 23412333, 2311232, 324434234, 23423244, 23441231, 2343231, 23431233, 23424343, 12331313, 233242312, 3243242, 234123231, 23413222, 23131234,324344423, 12312313, 32423421, 23466, -1};
        String[][] date_Time = null;
        int numDados = 26;
        ProjetoFinal.sortValuesDecreasing(values, date_Time, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verificarPassagem1 method, of class ProjetoFinal.
     */
    @Test
    public void testVerificarPassagem1() {
        System.out.println("verificarPassagem1");
        int numDados = 0;
        boolean expResult = false;
        boolean result = ProjetoFinal.verificarPassagem1(numDados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of obterArrayMeses method, of class ProjetoFinal.
     */
    @Test
    public void testObterArrayMeses() {
        System.out.println("obterArrayMeses");
        String[][] date_Time = null;
        String[] meses = null;
        int numDados = 0;
        int expResult = 0;
        int result = ProjetoFinal.obterArrayMeses(date_Time, meses, numDados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calcularSomaMensal method, of class ProjetoFinal.
     */
    @Test
    public void testCalcularSomaMensal() {
        System.out.println("calcularSomaMensal");
        String[][] date_Time = null;
        int[] values = null;
        String[] meses = null;
        int numDados = 0;
        int numMeses = 0;
        int[] somaMensal = null;
        ProjetoFinal.calcularSomaMensal(date_Time, values, meses, numDados, numMeses, somaMensal);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of periodoDia method, of class ProjetoFinal.
     */
    @Test
    public void testPeriodoDia() {
        System.out.println("periodoDia");
        int hi = 0;
        int mi = 0;
        int si = 0;
        int hf = 0;
        int mf = 0;
        int sf = 0;
        String[][] date_Time = null;
        int[] values = null;
        double[] valuesToPlot = null;
        int numDados = 0;
        ProjetoFinal.periodoDia(hi, mi, si, hf, mf, sf, date_Time, values, valuesToPlot, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of diario method, of class ProjetoFinal.
     */
    @Test
    public void testDiario() {
        System.out.println("diario");
        String[][] date_Time = null;
        int[] values = null;
        double[] valuesToPlot = null;
        int numDados = 0;
        ProjetoFinal.diario(date_Time, values, valuesToPlot, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obterArrayDias method, of class ProjetoFinal.
     */
    @Test
    public void testObterArrayDias() {
        System.out.println("obterArrayDias");
        String[][] date_Time = null;
        String[] dias = null;
        int numDados = 0;
        ProjetoFinal.obterArrayDias(date_Time, dias, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obterArrayAnos method, of class ProjetoFinal.
     */
    @Test
    public void testObterArrayAnos() {
        System.out.println("obterArrayAnos");
        String[][] date_Time = null;
        String[] anos = null;
        int numDados = 0;
        ProjetoFinal.obterArrayAnos(date_Time, anos, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of anual method, of class ProjetoFinal.
     */
    @Test
    public void testAnual() {
        System.out.println("anual");
        String[][] date_Time = null;
        int[] values = null;
        double[] valuesToPlot = null;
        int numDados = 0;
        ProjetoFinal.anual(date_Time, values, valuesToPlot, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of graphTitle method, of class ProjetoFinal.
     */
    @Test
    public void testGraphTitle() {
        System.out.println("graphTitle");
        int escolhaResolucao = 0;
        String expResult = "";
        String result = ProjetoFinal.graphTitle(escolhaResolucao);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of calcIntervals method, of class ProjetoFinal.
     */
    @Test
    public void testCalcIntervals() {
        System.out.println("calcIntervals");
        int[] values = null;
        String[][] date_Time = null;
        int numDados = 0;
        ProjetoFinal.calcIntervals(values, date_Time, numDados);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of filter method, of class ProjetoFinal.
     */
    @Test
    public void testFilter() {
        System.out.println("filter");
        int[] values = null;
        String[][] date_Time = null;
        int numDados = 0;
        double[] valuesToPlot = null;
        int escolhaResolucao = 0;
        int modeloFiltragem = 0;
        boolean verificarNaoInterativo = false;
        String expResult = "";
        String result = ProjetoFinal.filter(values, date_Time, numDados, valuesToPlot, escolhaResolucao, modeloFiltragem, verificarNaoInterativo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mms method, of class ProjetoFinal.
     */
    @Test
    public void testMms() {
        System.out.println("mms");
        double[] serie_temporal = {1.7825259E7,1.7295849E7,1.0603059E7};
        boolean verificarNaoInterativo = false;
        double[] expResult = {1.7560554E7,1.3949454E7};
        double[] result = ProjetoFinal.mms(serie_temporal, verificarNaoInterativo);
        assertArrayEquals(expResult,result, 1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mmep method, of class ProjetoFinal.
     */
    @Test
    public void testMmep() {
        System.out.println("mmep");
        double[] serie_temporal = {1.7825259E7,1.7295849E7,1.0603059E7};
        int numDados = 30000;
        boolean verificarNaoInterativo = false;
        double[] expResult = {1.7825259E7,1.7560554E7,1.40818065E7};
        double[] result = ProjetoFinal.mmep(serie_temporal, numDados, verificarNaoInterativo);
        assertArrayEquals(expResult, result, 1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



   

    /**
     * Test of calcularMomentoSeguinte method, of class ProjetoFinal.
     */
    @Test
    public void testCalcularMomentoSeguinte() {
        System.out.println("calcularMomentoSeguinte");
        String[] datasResolucaoTemporal = {"2019-12-31", null};
        String expResult = "2020-01-01";
        String result = ProjetoFinal.calcularMomentoSeguinte(datasResolucaoTemporal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}