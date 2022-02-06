package mensajeria.controlador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mensajeria.modelo.Repartidor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Álvaro
 */
public class RepartidorControlador {
    private final File file;
    private List<Repartidor> listaRepartidores;

    public RepartidorControlador() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        file = new File("Repartidores.xml");
        if (!file.exists()) {
            crearFicheroPrimeraVez();
        }
        leerRepartidor();
    }
    
    private void leerRepartidor() throws IOException, SAXException, ParserConfigurationException {
        listaRepartidores = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document documento = null;
        
        DocumentBuilder builder = factory.newDocumentBuilder();
	documento = builder.parse(file);
        
        NodeList repartidores = documento.getElementsByTagName("repartidor");
        
        for (int i = 0; i < repartidores.getLength(); i++) {
            Node repartidor = repartidores.item(i);

            Element elemento = (Element) repartidor;

            int id_repartidor = Integer.parseInt(elemento.getElementsByTagName("id_repartidor")
                    .item(0).getChildNodes().item(0).getNodeValue());
            String dni = elemento.getElementsByTagName("dni")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String nombre_repartidor = elemento.getElementsByTagName("nombre_repartidor")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String telefono_repartidor = elemento.getElementsByTagName("telefono_repartidor")
                    .item(0).getChildNodes().item(0).getNodeValue();
            int antiguedad = Integer.parseInt(elemento.getElementsByTagName("antiguedad")
                    .item(0).getChildNodes().item(0).getNodeValue());
            int oficina = Integer.parseInt(elemento.getElementsByTagName("oficina")
                    .item(0).getChildNodes().item(0).getNodeValue());
            boolean activo = Boolean.parseBoolean(elemento.getElementsByTagName("activo")
                    .item(0).getChildNodes().item(0).getNodeValue());
            
            Repartidor r = new Repartidor(id_repartidor, dni, nombre_repartidor,
                    telefono_repartidor, antiguedad, oficina, activo);
            listaRepartidores.add(r);
        }
    }
    
    private void crearFicheroPrimeraVez() throws IOException, ParserConfigurationException, TransformerException {
        listaRepartidores = new ArrayList<>();
        listaRepartidores.add(new Repartidor(0, "dni", "nombre_repartidor", "telefono_repartidor", 0, 0, false));
        escribirRepartidor(listaRepartidores);
    }
    
    public void escribirRepartidor(List<Repartidor> lista) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        documento = dom.createDocument(null, "repartidoresXML", null);

        Element raiz = documento.createElement("repartidores");
        documento.getDocumentElement().appendChild(raiz);

        Element nodorepartidor = null, nodoDatos = null;
        Text texto = null;
        for (Repartidor repartidor : listaRepartidores) {
            nodorepartidor = documento.createElement("repartidor");
            raiz.appendChild(nodorepartidor);

            nodoDatos = documento.createElement("id_repartidor");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(repartidor.getId_repartidor()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("dni");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(repartidor.getDni());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("nombre_repartidor");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(repartidor.getNombre_repartidor());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("telefono_repartidor");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(repartidor.getTelefono_repartidor());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("antiguedad");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(repartidor.getAntiguedad()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("oficina");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(repartidor.getOficina()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("activo");
            nodorepartidor.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(repartidor.isActivo()));
            nodoDatos.appendChild(texto);
        }

        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(file);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    public List<Repartidor> getListaRepartidores() {
        return listaRepartidores;
    }
    
}
