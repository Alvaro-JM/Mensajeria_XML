package mensajeria.controlador;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import mensajeria.modelo.Paquete;
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
public class PaqueteControlador {
    private SimpleDateFormat formato;
    private final File file;
    private List<Paquete> listaPaquetes;

    public PaqueteControlador() throws ParserConfigurationException, SAXException, IOException, TransformerException, ParseException {
        formato = new SimpleDateFormat("dd/MM/yyyy");
        file = new File("Paquetes.xml");
        if (!file.exists()) {
            crearFicheroPrimeraVez();
        }
        leerPaquete();
    }
    
    private void leerPaquete() throws ParserConfigurationException, SAXException, IOException, ParseException {
        listaPaquetes = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document documento = null;
        
        DocumentBuilder builder = factory.newDocumentBuilder();
	documento = builder.parse(file);
        
        NodeList paquetes = documento.getElementsByTagName("paquete");
        
        for (int i = 0; i < paquetes.getLength(); i++) {
            Node paquete = paquetes.item(i);

            Element elemento = (Element) paquete;

            int id_paquete = Integer.parseInt(elemento.getElementsByTagName("id_paquete")
                    .item(0).getChildNodes().item(0).getNodeValue());
            Date fecha_entrega = formato.parse(elemento.getElementsByTagName("fecha_entrega")
                    .item(0).getChildNodes().item(0).getNodeValue());
            String direccion_destino = elemento.getElementsByTagName("direccion_destino")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String telefono_destino = elemento.getElementsByTagName("telefono_destino")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String direccion_origen = elemento.getElementsByTagName("direccion_origen")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String telefono_origen = elemento.getElementsByTagName("telefono_origen")
                    .item(0).getChildNodes().item(0).getNodeValue();
            int repartidor = Integer.parseInt(elemento.getElementsByTagName("repartidor")
                    .item(0).getChildNodes().item(0).getNodeValue());
            boolean activo = Boolean.parseBoolean(elemento.getElementsByTagName("activo")
                    .item(0).getChildNodes().item(0).getNodeValue());
            
            Paquete p = new Paquete(id_paquete, fecha_entrega, direccion_destino, telefono_destino, direccion_origen, telefono_origen, repartidor, activo);
            
            listaPaquetes.add(p);
        }
    }
    
    private void crearFicheroPrimeraVez() throws ParserConfigurationException, TransformerException, ParseException {
        Date fecha_entrega = formato.parse("00/00/0000");
        listaPaquetes = new ArrayList<>();
        listaPaquetes.add(new Paquete(0, fecha_entrega, "direccion_destino", "telefono_destino", "direccion_origen", "telefono_origen", 0, false));
        escribirPaquete(listaPaquetes);
    }
    
    public void escribirPaquete(List<Paquete> lista) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        documento = dom.createDocument(null, "paquetesXML", null);

        Element raiz = documento.createElement("paquetes");
        documento.getDocumentElement().appendChild(raiz);

        Element nodopaquete = null, nodoDatos = null;
        Text texto = null;
        for (Paquete paquete : listaPaquetes) {
            nodopaquete = documento.createElement("paquete");
            raiz.appendChild(nodopaquete);

            nodoDatos = documento.createElement("id_paquete");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(paquete.getId_paquete()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("fecha_entrega");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(formato.format(paquete.getFecha_entrega()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("direccion_destino");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(paquete.getDireccion_destino());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("telefono_destino");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(paquete.getTelefono_destino());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("direccion_origen");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(paquete.getDireccion_origen());
            nodoDatos.appendChild(texto);
            
            nodoDatos = documento.createElement("telefono_origen");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(paquete.getTelefono_origen());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("repartidor");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(paquete.getRepartidor()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("activo");
            nodopaquete.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(paquete.isActivo()));
            nodoDatos.appendChild(texto);
        }

        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(file);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    public List<Paquete> getListaPaquetes() {
        return listaPaquetes;
    }
    
}
