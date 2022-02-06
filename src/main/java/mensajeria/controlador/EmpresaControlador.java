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
import mensajeria.modelo.Empresa;
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
public class EmpresaControlador {
    private final File file;
    private List<Empresa> listaEmpresas;

    public EmpresaControlador() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        listaEmpresas = new ArrayList<>();
        file = new File("Empresas.xml");
        if (!file.exists()) {
            crearFicheroPrimeraVez();
        }
        leerEmpresa();
    }
    
    public void leerEmpresa() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document documento = null;
        
        DocumentBuilder builder = factory.newDocumentBuilder();
	documento = builder.parse(file);
        
        NodeList empresas = documento.getElementsByTagName("empresa");
        
        for (int i = 0; i < empresas.getLength(); i++) {
            Node empresa = empresas.item(i);

            Element elemento = (Element) empresa;

            int id_empresa = Integer.parseInt(elemento.getElementsByTagName("id_empresa")
                    .item(0).getChildNodes().item(0).getNodeValue());
            String nombre_empresa = elemento.getElementsByTagName("nombre_empresa")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String cif = elemento.getElementsByTagName("cif")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String director = elemento.getElementsByTagName("director")
                    .item(0).getChildNodes().item(0).getNodeValue();
            String web = elemento.getElementsByTagName("web")
                    .item(0).getChildNodes().item(0).getNodeValue();
            boolean activo = Boolean.parseBoolean(elemento.getElementsByTagName("activo")
                    .item(0).getChildNodes().item(0).getNodeValue());
            
            Empresa e = new Empresa(id_empresa, nombre_empresa, cif, director, web, activo);
            listaEmpresas.add(e);
        }
    }
    
    private void crearFicheroPrimeraVez() throws ParserConfigurationException, TransformerException{
        listaEmpresas.add(new Empresa(0, "nombre", "cif", "director", "web", false));
        escribirEmpresa(listaEmpresas);
    }
    
    public void escribirEmpresa(List<Empresa> lista) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        documento = dom.createDocument(null, "empresasXML", null);

        Element raiz = documento.createElement("empresas");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoempresa = null, nodoDatos = null;
        Text texto = null;
        for (Empresa empresa : listaEmpresas) {
            nodoempresa = documento.createElement("empresa");
            raiz.appendChild(nodoempresa);

            nodoDatos = documento.createElement("id_empresa");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(empresa.getId_empresa()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("nombre_empresa");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(empresa.getNombre_empresa());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("cif");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(empresa.getCif());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("director");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(empresa.getDirector());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("web");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(empresa.getWeb());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("activo");
            nodoempresa.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(empresa.isActivo()));
            nodoDatos.appendChild(texto);
        }

        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(file);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }
    
}
