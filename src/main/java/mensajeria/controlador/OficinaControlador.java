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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mensajeria.modelo.Oficina;
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
public class OficinaControlador {
    private final File file;
    private List<Oficina> listaOficinas;

    public OficinaControlador() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        file = new File("Oficinas.xml");
        if (!file.exists()) {
            crearFicheroPrimeraVez();
        }
        leerOficina();
    }
    
    private void leerOficina() throws SAXException, ParserConfigurationException, IOException {
        listaOficinas = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document documento = null;
        
        DocumentBuilder builder = factory.newDocumentBuilder();
	documento = builder.parse(file);
        
        NodeList oficinas = documento.getElementsByTagName("oficina");
        
        for (int i = 0; i < oficinas.getLength(); i++) {
            Node oficina = oficinas.item(i);

            Element elemento = (Element) oficina;

            int id_oficina = Integer.parseInt(elemento.getElementsByTagName("id_oficina")
                    .item(0).getChildNodes().item(0).getNodeValue());
            String direccion_oficina = elemento.getElementsByTagName("direccion_oficina")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String telefono_oficina = elemento.getElementsByTagName("telefono_oficina")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String email = elemento.getElementsByTagName("email")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String encargado = elemento.getElementsByTagName("encargado")
                    .item(0).getChildNodes().item(0).getNodeValue();
            int empresa = Integer.parseInt(elemento.getElementsByTagName("empresa")
                    .item(0).getChildNodes().item(0).getNodeValue());
            boolean activo = Boolean.parseBoolean(elemento.getElementsByTagName("activo")
                    .item(0).getChildNodes().item(0).getNodeValue());
            
            Oficina o = new Oficina(id_oficina, direccion_oficina, telefono_oficina, email, encargado, empresa, activo);
            listaOficinas.add(o);
        }
    }
    
    private void crearFicheroPrimeraVez() throws IOException, TransformerException, ParserConfigurationException {
        listaOficinas = new ArrayList<>();
        listaOficinas.add(new Oficina(0, "direccion", "telefono", "email", "encargado", 0, false));
        escribirOficina(listaOficinas);
    }
    
    public void escribirOficina(List<Oficina> lista) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        documento = dom.createDocument(null, "oficinasXML", null);

        Element raiz = documento.createElement("oficinas");
        documento.getDocumentElement().appendChild(raiz);

        Element nodooficina = null, nodoDatos = null;
        Text texto = null;
        for (Oficina oficina : listaOficinas) {
            nodooficina = documento.createElement("oficina");
            raiz.appendChild(nodooficina);

            nodoDatos = documento.createElement("id_oficina");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(oficina.getId_oficina()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("direccion_oficina");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(oficina.getDireccion_oficina());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("telefono_oficina");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(oficina.getTelefono_oficina());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("email");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(oficina.getEmail());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("encargado");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(oficina.getEncargado());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("empresa");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(oficina.getEmpresa()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("activo");
            nodooficina.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(oficina.isActivo()));
            nodoDatos.appendChild(texto);
        }

        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(file);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }
    
    public List<Oficina> getListaOficinas() {
        return listaOficinas;
    }
    
}
