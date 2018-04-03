package com.simplebiz.customjarforjbpm;

import java.io.StringWriter;

import javax.xml.bind.JAXB;

import com.simplebiz.employeeappraisal4.Employee;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void runCustomCode(Employee employee) {
		StringWriter sw = new StringWriter();
		JAXB.marshal(employee, sw);
		String xmlString = sw.toString();
		System.out.println("Employee IN XML "+xmlString);
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
