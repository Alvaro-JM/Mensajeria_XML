package mensajeria.test;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import static junit.framework.Assert.assertEquals;
import mensajeria.controlador.EmpresaControlador;
import mensajeria.modelo.Empresa;
import mensajeria.vista.Ventana;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

/**
 *
 * @author Álvaro
 */
public class TestUnitarios {
    
    public TestUnitarios() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void test_leerFicheroEmpresa_cargarListaConDatosFichero_enControlador_comparacionTamaniosLista() {
        try {
            EmpresaControlador instance = new EmpresaControlador();
            int sizeBefore = instance.getListaEmpresas().size();
            instance.leerEmpresa();
            int sizeAfter = instance.getListaEmpresas().size();

            assertNotEquals(sizeBefore, sizeAfter);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TestUnitarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(TestUnitarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestUnitarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(TestUnitarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void test_aniadirEmpresa_enLista_enVentana_comparacionTamaniosLista() {
        Ventana instance = new Ventana();
        int sizeBefore = instance.listaEmpresas.size();

        instance.aniadirEmpresa("nombre_prueba", "cif_prueba", "director_prueba", "web_prueba");

        int sizeAfter = instance.listaEmpresas.size();

        assertEquals(sizeBefore + 1, sizeAfter);
    }
    
    @Test
    public void test_setNombreEmpresa_modificarEmpresa_enLista_enVentana_conNombreAleatorio(){
        Random random = new Random();
        Ventana instance = new Ventana();
        
        Empresa empresa = instance.listaEmpresas.get(0);
        
        String nameRandom = "";
        for (int i = 0; i < 10; i++) {
            nameRandom += (char)(random.nextInt(57) + 65);
        }
        
        empresa.setNombre_empresa(nameRandom);
        
        String nameAfter = empresa.getNombre_empresa();
        
        assertEquals(nameRandom, nameAfter);
    }
    
    @Test
    public void test_eliminarPrimeraEmpresaDeLista_enLista_enVentana_comparacionTamaniosLista(){
        Ventana instance = new Ventana();
        int sizeBefore = instance.listaEmpresas.size();

        instance.listaEmpresas.remove(0);
        
        int sizeAfter = instance.listaEmpresas.size();

        assertEquals(sizeBefore - 1, sizeAfter);
    }
}