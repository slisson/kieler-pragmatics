package de.cau.cs.kieler.kex.model.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.cau.cs.kieler.core.KielerException;
import de.cau.cs.kieler.kex.controller.ErrorMessage;
import de.cau.cs.kieler.kex.controller.ExportResource;
import de.cau.cs.kieler.kex.controller.util.IOHandler;
import de.cau.cs.kieler.kex.model.Example;
import de.cau.cs.kieler.kex.model.ExampleResource;

/**
 * can be used for creating new extensions for KEX extension point.<br>
 * So it allows to create new examples or example categories.
 * 
 * @author pkl
 * 
 */

public class PluginExampleCreator {

    private final IPath workspacePath = Platform.getLocation();

    private Document parsedXML;

    private File pluginXML;

    /**
     * parses the given file.
     * 
     * @param file
     *            , which will be parsed
     * @return Document, parsed document
     * @throws SAXException
     *             , could be thrown by DOM framework
     * @throws IOException
     *             , could be thrown by DOM framework
     * @throws ParserConfigurationException
     *             , could be thrown by DOM framework
     */
    private Document parseDocument(final File file) throws SAXException, IOException,
            ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }

    /**
     * NOTE: parseElement could only be a type of Example or a example category in String
     * representation.
     * 
     * @param location
     * @param parseElement
     * @param destResources
     * @param deletableCategories
     * @param creatableCategories
     * @throws KielerException
     */
    public void addExtension(File location, Example parseElement, List<String> creatableCategories,
            String absOverviewPic) throws KielerException {

        Node pluginNode = getPluginNode(location);
        Node extensionKEX = filterExtensionKEX(pluginNode);

        checkDuplicate(extensionKEX, parseElement.getTitle(), creatableCategories);

        parseElement.setOverviewPic(createLocalPluginPath(absOverviewPic));

        addExampleCategories(extensionKEX, creatableCategories);

        extensionKEX.appendChild(toNode(parseElement, location));
        writePluginXML(pluginXML.getAbsolutePath());
    }

    // TODO testen...
    private void checkDuplicate(Node extensionKEX, String exampleTitle,
            List<String> creatableCategories) throws KielerException {
        NodeList childNodes = extensionKEX.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();
            if (PluginConstants.CATEGORY.equals(nodeName)) {
                Node namedItem = item.getAttributes().getNamedItem(PluginConstants.ID);
                if (namedItem != null) {
                    for (String creatableCategory : creatableCategories) {
                        if (creatableCategory.equals(namedItem.getNodeValue()))
                            throw new KielerException(ErrorMessage.DUPLICATE_ELEMENT
                                    + "The category \"" + creatableCategory
                                    + "\" exists already in choosen plugin project.");
                    }
                }
            } else if (PluginConstants.EXAMPLE.equals(nodeName)) {
                Node namedItem = item.getAttributes().getNamedItem(PluginConstants.TITLE);
                if (namedItem != null && exampleTitle.equals(namedItem.getNodeValue())) {
                    throw new KielerException(ErrorMessage.DUPLICATE_ELEMENT + "The example \""
                            + exampleTitle + "\" exists already in choosen plugin project.");
                }
            }
        }
    }

    private String createLocalPluginPath(String absOverviewPic) {
        String projectPath = this.pluginXML.getAbsolutePath().substring(0,
                this.pluginXML.getAbsolutePath().length() - IOHandler.PLUGIN_XML.length() - 1);
        return new Path(absOverviewPic.substring(projectPath.length())).toPortableString();
    }

    private void makeRootSource(File location, Example example) throws KielerException {
        File project = IOHandler.searchUP(location, IOHandler.PROJECT_FILE).getParentFile();
        String relativeLocation = location.getPath().substring(project.getPath().length());
        example.setRootDir((relativeLocation.length() > 0) ? relativeLocation.substring(1)
                : relativeLocation);
    }

    private Node getPluginNode(File locationFile) throws KielerException {

        try {
            this.pluginXML = IOHandler.filterPluginXML(locationFile);
            if (IOHandler.PLUGIN_XML.equals(this.pluginXML.getName())) {
                parsedXML = parseDocument(this.pluginXML);
            } else {
                // project path + .project file as path
                this.pluginXML = new File(this.pluginXML.getAbsolutePath() + File.separatorChar
                        + IOHandler.PLUGIN_XML);
                parsedXML = createPluginDocument();
            }
        } catch (ParserConfigurationException e) {
            String msg = ErrorMessage.NOT_PARSE_PLUGIN + "\n" + e.getLocalizedMessage();
            throw new KielerException(msg, e);
        } catch (SAXException e) {
            String msg = ErrorMessage.NOT_PARSE_PLUGIN + "\n" + e.getLocalizedMessage();
            throw new KielerException(msg, e);
        } catch (IOException e) {
            String msg = ErrorMessage.NOT_PARSE_PLUGIN + "\n" + e.getLocalizedMessage();
            throw new KielerException(msg, e);
        }

        NodeList plugins = this.parsedXML.getElementsByTagName(PluginConstants.PLUGIN);
        if (plugins.getLength() == 1) {
            return plugins.item(0);
        }
        // TODO
        throw new KielerException("");
    }

    private Node filterExtensionKEX(Node pluginNode) {
        Node extensionKEX = null;
        NodeList nodes = pluginNode.getChildNodes();
        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodes.item(i);
            if (PluginConstants.EXTENSION.equals(node.getNodeName())) {
                NamedNodeMap attributes = node.getAttributes();
                Node namedItem = attributes.getNamedItem(PluginConstants.POINT);
                if (PluginConstants.KEX_EXT_POINT.equals(namedItem.getNodeValue())) {
                    extensionKEX = node;
                    break;
                }
            }
        }
        if (extensionKEX == null) {

            Element extElement = pluginNode.getOwnerDocument().createElement(
                    PluginConstants.EXTENSION);
            extElement.setAttribute(PluginConstants.POINT, PluginConstants.KEX_EXT_POINT);
            pluginNode.appendChild(extElement);
            return filterExtensionKEX(pluginNode);
        }

        return extensionKEX;
    }

    private Document createPluginDocument() throws KielerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        DocumentBuilder docBuilder = null;
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException p) {
            throw new KielerException(ErrorMessage.DOC_BUILDER_NEW_ERROR + ", "
                    + p.getLocalizedMessage());
        }

        DOMImplementation impl = docBuilder.getDOMImplementation();
        Document doc = impl.createDocument(null, null, null);
        Element root = doc.createElement(PluginConstants.PLUGIN);
        doc.appendChild(root);

        Element child = doc.createElement(PluginConstants.EXTENSION);
        child.setAttribute(PluginConstants.POINT, PluginConstants.KEX_EXT_POINT);
        root.appendChild(child);
        return doc;
    }

    /**
     * creates example files to given location
     * 
     * @param finishedResources
     * 
     * @param exampleId
     * 
     * @param sourceProject
     */
    public void copyResources(File destFile, List<ExportResource> resources,
            List<IPath> finishedResources) throws KielerException {
        for (ExportResource resource : resources) {
            copyResource(resource, destFile.getPath(), finishedResources);
        }
    }

    private void copyResource(ExportResource resource, String destPath,
            List<IPath> finishedResources) throws KielerException {
        StringBuilder destLocation = new StringBuilder();
        try {

            String sourcePath = this.workspacePath.toOSString()
                    + resource.getResource().getFullPath().toOSString();

            destLocation.append(destPath).append(File.separatorChar)
                    .append(resource.getLocalPath());
            IPath destination = Path.fromPortableString(destLocation.toString());
            finishedResources.add(destination);

            IOHandler.writeResource(new File(sourcePath), destination.toFile());
        } catch (IOException e) {
            throw new KielerException(e.getMessage());
        }
    }

    public void deleteExampleResources(List<IPath> resources) {
        for (IPath path : resources)
            IOHandler.deleteFile(path.toFile());
    }

    private boolean addExampleCategories(Node node, List<String> creatableCategories) {
        List<String> creates = creatableCategories;
        for (String creatable : creates) {
            Element createdCategory = parsedXML.createElement(PluginConstants.CATEGORY);
            createdCategory.setAttribute(PluginConstants.ID, creatable);
            node.appendChild(createdCategory);
        }
        return true;
    }

    private void writePluginXML(String pluginPath) throws KielerException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(parsedXML);
            FileOutputStream os = new FileOutputStream(new File(pluginPath));
            StreamResult result = new StreamResult(os);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setParameter("eclipse version", "3.4");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throwWritePluginError(e);
        } catch (TransformerFactoryConfigurationError e) {
            throwWritePluginError(e);
        } catch (FileNotFoundException e) {
            throwWritePluginError(e);
        } catch (TransformerException e) {
            throwWritePluginError(e);
        }

    }

    private void throwWritePluginError(Throwable e) throws KielerException {
        throw new KielerException(new StringBuffer().append(ErrorMessage.NOT_WRITE_PLUGIN)
                .append(e.getLocalizedMessage()).toString());
    }

    private Node toNode(String categoryId) {
        Element createdElement = parsedXML.createElement(PluginConstants.CATEGORY);
        createdElement.setAttribute(PluginConstants.ID, categoryId);
        return createdElement;
    }

    private Node toNode(Example example, File location) throws KielerException {
        Element createdExample = parsedXML.createElement(PluginConstants.EXAMPLE);
        createdExample.setAttribute(PluginConstants.TITLE, example.getTitle());
        createdExample.setAttribute(PluginConstants.DESCRIPTION, example.getDescription());
        createdExample.setAttribute(PluginConstants.GENERATION_DATE, example.getGenerationDate()
                .toString());
        makeRootSource(location, example);

        String overviewPicPath = example.getOverviewPic();
        if (overviewPicPath != null)
            createdExample.setAttribute(PluginConstants.OVERVIEW_PIC, overviewPicPath);
        String author = example.getAuthor();
        if (author != null)
            createdExample.setAttribute(PluginConstants.AUTHOR, author);

        String contact = example.getContact();
        if (contact != null)
            createdExample.setAttribute(PluginConstants.CONTACT, contact);

        String rootDirectory = example.getRootDir();
        if (rootDirectory != null)
            createdExample.setAttribute(PluginConstants.ROOT_DIRECTORY, rootDirectory);

        for (String category : example.getCategories()) {
            createdExample.appendChild(toNode(category));
        }

        for (ExampleResource exResource : example.getResources()) {
            createdExample.appendChild(toNode(rootDirectory, exResource));
        }
        return createdExample;

    }

    private Node toNode(String relativePath, ExampleResource exResource) {
        Element createdExResource = parsedXML.createElement(PluginConstants.EXAMPLE_RESOURCE);
        createdExResource.setAttribute(PluginConstants.LOCAL_PATH,
                relativePath + "/" + exResource.getLocalPath());
        createdExResource.setAttribute(PluginConstants.RESOURCE_TYPE,
                ExampleResource.Type.map(exResource.getResourceType()));
        createdExResource.setAttribute(PluginConstants.DIRECT_OPEN,
                Boolean.toString(exResource.isDirectOpen()));
        return createdExResource;

    }

    public String copyOverviewPic(String destPath, String sourcePath, List<IPath> finishedResources)
            throws KielerException {
        File file = new File(sourcePath);
        String destLocation = destPath + File.separatorChar + file.getName();
        IPath destination = Path.fromPortableString(destLocation.toString());
        finishedResources.add(destination);
        try {
            IOHandler.writeResource(new File(sourcePath), destination.toFile());
        } catch (IOException e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append(ErrorMessage.PLUGIN_WRITE_ERROR).append("\ndestination: ")
                    .append(destPath).append(", image: ").append(sourcePath);
            throw new KielerException(errorMessage.toString());
        }
        return destLocation;

    }

    /**
     * makes a absolute path, relative to export project of workspace.
     * 
     * @param absolutePath
     * @return
     */
    public String makeRelativePath(String projectPath, String absolutePath) {
        // TODO der projekt pfad wird bei filerPluginProjekt ermittelt,
        // der muss hier herein gereicht werden und der vom absolutenpath
        // abgetrennt werden.
        return null;
    }

}
