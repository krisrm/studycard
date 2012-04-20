package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import util.EventType;
import util.FileMenuItem;
import util.ModelMessage;


public class Model extends AbstractModel {

	private static Model model;

	private Stack cardstack;
	private File currentFile;
	private boolean hasChangedSinceSave = false;
	private List<FileMenuItem> recentlyOpened;
	
	private Model(){
		cardstack = new Stack();
	}
	
	public static Model getModel(){
		if (model == null)
			model = new Model();
		
		return model;
	}
	
	public Stack getStack(){
		
		return cardstack;
	}
	
	public Stack makeNewStack(){
		cardstack = new Stack();
		currentFile = null;
		hasChangedSinceSave = false;
		
		notifyControllers(new ModelMessage(EventType.MODEL_TITLE_CHANGE, null));
		notifyControllers(new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
		return cardstack;
	}
	
	
	public void loadCards(File loadFile) throws IOException {
		if (loadFile == null)
			return;
		cardstack.clear();
		
		currentFile = loadFile;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(loadFile);
			parseXML(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e){
			FileMenuItem item = new FileMenuItem(currentFile);
			recentlyOpened.remove(item);
			currentFile = null;
			notifyControllers(new ModelMessage(EventType.MODEL_TITLE_CHANGE, null));
			throw e;
		}
		
		FileMenuItem item = new FileMenuItem(currentFile);
		recentlyOpened.remove(item);
		recentlyOpened.add(0,item);
		
		if (recentlyOpened.size() > Settings.MAX_RECENT_ITEMS){
			recentlyOpened.remove(recentlyOpened.size()-1);
		}
		
		hasChangedSinceSave = false;
		
		notifyControllers(new ModelMessage(EventType.MODEL_TITLE_CHANGE, null));
		notifyControllers(new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
	}
	public void saveCards(File saveFile) {
		if (saveFile == null)
			return;
		
		currentFile = saveFile;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			//build the XML from the model
			doc = buildXML(doc);
			
			Source source = new DOMSource(doc);
			
	        Result result = new StreamResult(saveFile);

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	       	xformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        xformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
	        xformer.transform(source, result);
	        
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		hasChangedSinceSave = false;
		notifyControllers(new ModelMessage(EventType.MODEL_TITLE_CHANGE, null));
	}
	
	private Document buildXML(Document doc) {
		Element e = cardstack.buildXMLTree(doc);
		doc.appendChild(e);
		
		return doc;
	}
	
	private void parseXML(Document doc){
		Element e = (Element) doc.getElementsByTagName("STACK").item(0);
		cardstack = new Stack(e);
		
	}
	
	public String toString(){
		return cardstack.toString();
	}

	public File getCurrentFile() {
		return currentFile;
	}
	
	public void setChangedSinceSave(){
		hasChangedSinceSave = true;
		notifyControllers(new ModelMessage(EventType.MODEL_TITLE_CHANGE, null));
	}
	
	public boolean hasChangedSinceSave(){
		return hasChangedSinceSave;
	}
	
	public String getFileTitle(){
		if (currentFile == null)
			return "";
		
		return currentFile.getName();	
	}

	public void createNewCard(String frontText, String backText) {
		Card card = new Card();
		card.setFront(frontText);
		card.setBack(backText);
		
		this.getStack().addCard(card);
		setChangedSinceSave();
		
	}
	
	public void startRecentlyOpened(List<FileMenuItem> previous){
		recentlyOpened = new ArrayList<FileMenuItem>();

		if (previous == null){
			return;
		}
		
		recentlyOpened.addAll(previous);
		
	}

	public List<FileMenuItem> getRecentlyOpened() {
		return recentlyOpened;
	}
	
	
}

