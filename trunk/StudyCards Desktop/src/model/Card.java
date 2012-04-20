 /* Copyright 2012 Kristofer Mitchell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/

package model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Card {
	
	private long attempts;
	private long corrects;
	private String front;
	private String back;
	
	public Card(){
		attempts = 0;
		corrects = 0;
		front = "";
		back = "";
		
	}
	
	public Card(Element cardEle){
		this();
		
		Node frontNode = cardEle.getElementsByTagName("FRONT").item(0);
		Node backNode = cardEle.getElementsByTagName("BACK").item(0);
		
		if (frontNode != null)
			front = frontNode.getTextContent();
		
		if (backNode != null)
			back = backNode.getTextContent();
		
		
		if (cardEle.hasAttribute("attempts"))
			attempts = Long.parseLong(cardEle.getAttribute("attempts"));
		
		if (cardEle.hasAttribute("corrects"))
			corrects = Long.parseLong(cardEle.getAttribute("corrects"));
		
	}
	
	public Card(String frontText, String backText) {
		this();
		front = frontText;
		back = backText;
	}

	public boolean hasScore(){
		return (attempts > 0);
	}
	
	public double getScore(){
		if (attempts == 0)
			return -1;
		
		return (corrects/attempts);
	}
	
	public float getPercentage(){
		if (attempts == 0)
			return 0;
		
		return ((float) corrects/attempts) * 100;
	}

	public void setFront(String front) {
		this.front = front;
	}
	
	public String getFront() {
		return front;
	}
	
	public void setBack(String back) {
		this.back = back;
	}
	
	public String getBack() {
		return back;
	}
	
	public void attempt(boolean correct){
		attempts ++;
		if (correct)
			corrects ++;
		
	}
	
	public void addCorrect(){
		corrects++;
	}
	
	public void addAttempt(){
		attempts++;
	}

	public Element buildXMLTree(Element root, Document doc) {
		Element cardEle = doc.createElement("CARD");
		
		Attr attemptsAttr = doc.createAttribute("attempts");
		attemptsAttr.setNodeValue(Long.toString(attempts));
		
		Attr correctsAttr = doc.createAttribute("corrects");
		correctsAttr.setNodeValue(Long.toString(corrects));
		
		cardEle.setAttributeNode(attemptsAttr);
		cardEle.setAttributeNode(correctsAttr);
		
		Element textE;
		
		textE = doc.createElement("FRONT");
		textE.setTextContent(front);
		cardEle.appendChild(textE);
		
		textE = doc.createElement("BACK");
		textE.setTextContent(back);
		cardEle.appendChild(textE);
		
		return cardEle;
	}
	
	public String toString(){
		String r = "";
		
		r += "CARD: (" + corrects + "/" + attempts + ")\n";
		r += "Q: '" + front + "'\n";
		r += "A: '" + back + "'\n";
		
		return r;
	}

	
}
