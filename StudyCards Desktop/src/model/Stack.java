package model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import util.EventType;
import util.ModelMessage;

public class Stack {
	private ArrayList<Card> cards;
	private String title;

	public Stack() {
		cards = new ArrayList<Card>();
		setTitle("");
		
	}

	public Stack(Element stackEle) {
		this();
		this.title = stackEle.getAttribute("TITLE");
		
		NodeList cardNodes = stackEle.getElementsByTagName("CARD");

		for (int i = 0; i < cardNodes.getLength(); i++) {
			addCard(new Card((Element) cardNodes.item(i)));
		}

	}

	public void addCard(Card card) {
		cards.add(card);
		Model.getModel().notifyControllers(
				new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
		
	}
	
	public void deleteCard(int index) {
		if (isInBounds(index, cards.size()))
			return;
		
		cards.remove(index);
		Model.getModel().notifyControllers(
				new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
	}
	
	public void editCard(Card editedCard, int index){
		if (!isInBounds(index, cards.size()))
			return;
		
		cards.set(index, editedCard);
		Model.getModel().notifyControllers(
				new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
	}
	

	public void deleteCards(int[] cards) {
		if (cards.length == 0)
			return;
		
		for (int i = 0; i < cards.length; i++){
	
			if (isInBounds(cards[0],this.cards.size()))
				this.cards.remove(cards[0]);
		}
			
		Model.getModel().notifyControllers(
				new ModelMessage(EventType.MODEL_STACK_CHANGE, null));
	}
	
	private boolean isInBounds(int index, int size) {
		//utility method for array
		return (index < size && index >= 0);
		
	}

	public List<Card> getAllCards() {
		return cards;
	}
	
	public Card getCard(int i) {
		return cards.get(i);
		
	}
	
	public void clear() {
		cards.clear();
		
	}
	public int getNumOfCards(){
		return cards.size();
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stack other = (Stack) obj;
		
		if (other.cards.size() != this.cards.size())
			return false;
		
		for (Card myC : cards)
			for (Card otherC : other.cards)
				if (! myC.equals(otherC))
					return false;
			
		return true;
	}

	public String toString() {
		String r = "";

		for (Card c : cards) {
			r += c.toString();
		}

		return r;
	}

	public Element buildXMLTree(Document doc) {

		Element root = doc.createElement("STACK");
		root.setAttribute("TITLE",this.title);
		for (Card c : cards) {

			Element e = c.buildXMLTree(root, doc);
			root.appendChild(e);
		}

		return root;
	}

	public void setTitle(String title) {
		if (title == null || title.equals("")){
			title = "Untitled Stack";
		}		
		this.title = title;
	}

	public String getTitle() {
		return title;
	}


}
