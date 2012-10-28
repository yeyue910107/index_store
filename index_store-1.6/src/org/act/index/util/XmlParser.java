/**
 * 
 */
package org.act.index.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.act.index.common.MailDoc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Administrator
 * 
 */
public class XmlParser {
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder builder;
	private Document document;
	private String fileName;

	public void init() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXmlDoc(String fileName, MailDoc mailDoc) {
		Element root = this.document.createElement("mails");
		this.document.appendChild(root);
		Element mail = this.document.createElement("mail");
		Element mailId = this.document.createElement("mailId");
		mailId.appendChild(this.document.createTextNode(String.valueOf(mailDoc
				.getMailId())));
		mail.appendChild(mailId);
		Element userId = this.document.createElement("userId");
		userId.appendChild(this.document.createTextNode(String.valueOf(mailDoc
				.getUserId())));
		mail.appendChild(userId);
		Element from = this.document.createElement("from");
		from.appendChild(this.document.createTextNode(mailDoc.getFrom()));
		mail.appendChild(from);
		Element to = this.document.createElement("to");
		to.appendChild(this.document.createTextNode(mailDoc.getTo()));
		mail.appendChild(to);
		Element cc = this.document.createElement("cc");
		cc.appendChild(this.document.createTextNode(mailDoc.getCc()));
		mail.appendChild(cc);
		Element bcc = this.document.createElement("bcc");
		bcc.appendChild(this.document.createTextNode(mailDoc.getBcc()));
		mail.appendChild(bcc);
		Element subject = this.document.createElement("subject");
		subject.appendChild(this.document.createTextNode(mailDoc.getSubject()));
		mail.appendChild(subject);
		Element body = this.document.createElement("body");
		body.appendChild(this.document.createTextNode(mailDoc.getBody()));
		mail.appendChild(body);
		root.appendChild(mail);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("create xml success.");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXmlDoc(String fileName, List<MailDoc> mailDocs) {

	}

	public void parserXmlDoc(String fileName, MailDoc mailDoc) {
		try {
			// DocumentBuilderFactory dbf =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(fileName);
			Document document = builder.parse(file);
			NodeList mails = document.getChildNodes();
			Node mail = mails.item(0);
			NodeList mailInfo = mail.getChildNodes();
			Node node = mailInfo.item(1);
			NodeList mailData = node.getChildNodes();
			/*for (int k = 0; k < mailData.getLength(); k++) {
				System.out.println(mailData.item(k).getNodeName() + ":"
						+ mailData.item(k).getTextContent());
			}*/
			mailDoc.setMailId(Integer.parseInt(mailData.item(1).getTextContent()));
			mailDoc.setUserId(Integer.parseInt(mailData.item(3).getTextContent()));
			mailDoc.setFrom(mailData.item(5).getTextContent());
			mailDoc.setTo(mailData.item(7).getTextContent());
			mailDoc.setCc(mailData.item(9).getTextContent());
			mailDoc.setBcc(mailData.item(11).getTextContent());
			mailDoc.setSubject(mailData.item(13).getTextContent());
			mailDoc.setBody(mailData.item(15).getTextContent());
			
			//System.out.println("parse xml success.");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void parserXmlDoc(String fileName, List<MailDoc> mailDocs) {
		try {
			// DocumentBuilderFactory dbf =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = builder.parse(fileName);
			NodeList mails = document.getChildNodes();
			for (int i = 0; i < mails.getLength(); i++) {
				Node mail = mails.item(i);
				NodeList mailInfo = mail.getChildNodes();
				for (int j = 0; j < mailInfo.getLength(); j++) {
					Node node = mailInfo.item(j);
					NodeList mailData = node.getChildNodes();
					mailDocs.get(i)
							.setMailId(
									Integer.parseInt(mailData.item(0)
											.getTextContent()));
					mailDocs.get(i)
							.setUserId(
									Integer.parseInt(mailData.item(1)
											.getTextContent()));
					mailDocs.get(i).setFrom(mailData.item(2).getTextContent());
					mailDocs.get(i).setTo(mailData.item(3).getTextContent());
					mailDocs.get(i).setCc(mailData.item(4).getTextContent());
					mailDocs.get(i).setBcc(mailData.item(5).getTextContent());
					mailDocs.get(i).setSubject(
							mailData.item(6).getTextContent());
					mailDocs.get(i).setBody(mailData.item(7).getTextContent());
					/*for (int k = 0; k < mailData.getLength(); k++) {
						System.out.println(mailData.item(k).getNodeName() + ":"
								+ mailData.item(k).getTextContent());
					}*/
				}
			}
			//System.out.println("parse xml success.");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
