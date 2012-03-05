/**
 * 
 * 	Copyright 2012 Vince. All rights reserved.
 * 	
 * 	Redistribution and use in source and binary forms, with or without modification, are
 * 	permitted provided that the following conditions are met:
 * 	
 * 	   1. Redistributions of source code must retain the above copyright notice, this list of
 * 	      conditions and the following disclaimer.
 * 	
 * 	   2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 	      of conditions and the following disclaimer in the documentation and/or other materials
 * 	      provided with the distribution.
 * 	
 * 	THIS SOFTWARE IS PROVIDED BY Vince ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * 	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * 	FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Vince OR
 * 	CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * 	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * 	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.network;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.ChatPing;
import de.vistahr.lanchat.model.ChatResponse;



/**
 * Simple LanChat Protocol
 * 
 * Message-Definition:
 * <lanchat type="message" version="1">
 * 		<tstamp></tstamp>
 * 		<from></from>
 * 		<message></message>
 * 		<id></id>
 * </lanchat>
 * 
 * Ping-Definition:
 * <lanchat type="ping" version="1">
 * 		<from></from>
 * 		<id></id>
 * </lanchat>
 */
public class SLCP implements Protocol {
	
	
	public final String version;
	public static String ENCODING = "UTF-8"; // TODO
	
	
	
	public SLCP(String version) {
		this.version = version;
	}
	
	public SLCP(int version) {
		this.version = "" + version;
	}
	

	@Override
	public ChatResponse parse(String incoming) throws ParseException {
		
		try {
			DocumentBuilderFactory docBFac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBFac.newDocumentBuilder();
			
			Document xml = docBuilder.parse(new InputSource(new StringReader(incoming)));
			
			xml.getDocumentElement().normalize();
			
			Element lanchat = xml.getDocumentElement();
			
			// parse nodes
			NodeList nl;
			
			nl = lanchat.getElementsByTagName("tstamp");
			Node dateNode = nl.item(0);
			long tstamp = 0;
			try {
				tstamp = Long.parseLong(dateNode.getTextContent());
			} catch(NumberFormatException e) {
				throw new ParseException("invalid input", 0);
			}
			
			
			
			nl = lanchat.getElementsByTagName("from");
			Node fromNode = nl.item(0);
			String from = fromNode.getTextContent();
			
			
			String message = "";
			try {
				nl = lanchat.getElementsByTagName("message");
				Node messageNode = nl.item(0);
				message = messageNode.getTextContent();
			} catch(NullPointerException e) {
				// continue - ChatPing has no message
			}
				
			nl = lanchat.getElementsByTagName("id");
			Node IDNode = nl.item(0);
			String ID = IDNode.getTextContent();
			
			
			if(message == null)
				return new ChatPing(from, new Date(tstamp), Integer.parseInt(ID));
			
			
			return new ChatMessage(from, message, new Date(tstamp), Integer.parseInt(ID));
			

		} catch (SAXParseException e) {
			throw new ParseException("invalid input", 0);
			 
		} catch (NullPointerException e) {
			throw new ParseException("invalid input", 0);
			
		} catch (SAXException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Generatemapper to send a message
	 * @param message
	 * @return valid xml simple lanchat string
	 */
	public String generateMessage(ChatMessage message) {
		return generate(message, "message");
	}
	
	
	/**
	 * Generatemapper to send a ping
	 * @param ping
	 * @return valid xml simple lanchat string
	 */
	public String generatePing(ChatPing ping) {
		return generate(ping, "ping");
	}

	
	@Override
	public String generate(ChatResponse message, String type) {
		String xmlString = "";
		
		// build XML
		try {
			DocumentBuilderFactory docBFac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBFac.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			Element root = doc.createElement("lanchat");
			root.setAttribute("version", version);
			

			if(!type.equals("message") && !type.equals("ping"))
					throw new IllegalArgumentException("type must be message or ping : ");
			
			root.setAttribute("type", type);
			
			Element msg = null;
			if(type.equals("message")) {
				msg = doc.createElement("message");
				msg.appendChild(doc.createTextNode(message.getChatMessage()));
			}
			
			Element date = doc.createElement("tstamp");
			date.appendChild(doc.createTextNode("" + message.getWritten().getTime()));
			root.appendChild(date);
			
			Element from = doc.createElement("from");
			from.appendChild(doc.createTextNode(message.getChatName()));
			root.appendChild(from);
			
			
			if(type.equals("message"))
				root.appendChild(msg); 
			
			Element id = doc.createElement("id");
			id.appendChild(doc.createTextNode("" + message.getID()));
			
			
			root.appendChild(id);
			
			doc.appendChild(root);
			
			
			//set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "no");
            trans.setOutputProperty(OutputKeys.ENCODING, ENCODING);
            trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
            
            
            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            
            xmlString = sw.toString();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		return xmlString;
	}


}
