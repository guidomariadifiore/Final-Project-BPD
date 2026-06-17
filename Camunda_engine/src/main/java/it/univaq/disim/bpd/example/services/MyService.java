package it.univaq.disim.bpd.example.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.spin.impl.xml.dom.DomXmlElement;
import org.springframework.stereotype.Service;

import it.univaq.disim.example.data.MyData;

@Service("myService")
public class MyService {

	public void helloWorldService() {
		System.out.println("\nHello world from a service!\n");
	}
	
	public void myMethod(DelegateExecution execution) {
		
		execution.setVariable("myVariable", "Hello World!");
		System.out.println("\n" + execution.getVariable("myVariable") + "\n");
	}
	
	public String anotherMethod(String processVariable) {
		
		return "\n" + processVariable + " printed out\n";
	}
	
	public MyData unmarshallMyData(DomXmlElement myDataXML) {
		return myDataXML.mapTo(MyData.class);
	}
}
