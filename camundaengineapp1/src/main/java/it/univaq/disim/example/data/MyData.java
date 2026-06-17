package it.univaq.disim.example.data;

import java.io.Serializable;

import spinjar.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import spinjar.javax.xml.bind.annotation.XmlAccessType;
import spinjar.javax.xml.bind.annotation.XmlAccessorType;
import spinjar.javax.xml.bind.annotation.XmlElement;
import spinjar.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "myData", namespace = "http://disim.univaq.it/examples/simplesoapservice")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4380558941247815667L;
	
	@XmlElement(name = "title", namespace = "http://disim.univaq.it/examples/simplesoapservice") 
	private String title;
	
	@XmlElement(name = "text", namespace = "http://disim.univaq.it/examples/simplesoapservice")
	private String body;
	
	@XmlElement(name = "id", namespace = "http://disim.univaq.it/examples/simplesoapservice")
	private int id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
