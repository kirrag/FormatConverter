package ru.netology;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBean;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
//import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

//import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

public class Main {

	private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
		List<Employee> list = null;

		try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {

			ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();

			strategy.setType(Employee.class);

			strategy.setColumnMapping(columnMapping);

			CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
					.withMappingStrategy(strategy)
					.build();

			list = csv.parse();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/*
	 * Recursive version parseXML
	 * private static List<Employee> listEmployee = new ArrayList<>();
	 * 
	 * private static List<Employee> parseXML(String fileName) {
	 * try {
	 * DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder builder = factory.newDocumentBuilder();
	 * Document doc = builder.parse(new File(fileName));
	 * Node root = doc.getDocumentElement();
	 * read(root);
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * return listEmployee;
	 * }
	 * 
	 * private static void read(Node node) {
	 * NodeList nodeList = node.getChildNodes();
	 * Employee employee = new Employee();
	 * for (int i = 0; i < nodeList.getLength(); i++) {
	 * Node node_ = nodeList.item(i);
	 * if (Node.ELEMENT_NODE == node_.getNodeType()) {
	 * if(node_.getNodeName().equals("employee")) {
	 * employee = new Employee();
	 * } else {
	 * employee.setField(node_.getNodeName(), node_.getTextContent());
	 * //System.out.println(node_.getNodeName() + " = " + node_.getTextContent());
	 * if(node_.getNodeName().equals("age")) {
	 * listEmployee.add(employee);
	 * }
	 * }
	 * read(node_);
	 * }
	 * }
	 * }
	 */
	private static List<Employee> parseXML(String fileName) {
		List<Employee> listEmployee = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(fileName));
			Node root = doc.getDocumentElement();
			NodeList nodeList0 = root.getChildNodes();
			Node node_ = null;
			for (int i = 0; i < nodeList0.getLength(); i++) {
				node_ = nodeList0.item(i);
				if (Node.ELEMENT_NODE == node_.getNodeType()) {
					NodeList nodeList1 = node_.getChildNodes();
					Employee employee = new Employee();
					for (int j = 0; j < nodeList1.getLength(); j++) {
						node_ = nodeList1.item(j);
						employee.setField(node_.getNodeName(), node_.getTextContent());
					}
					listEmployee.add(employee);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listEmployee;
	}

	private static String listToJson(List<Employee> list) {

		TypeToken<List<Employee>> listType = new TypeToken<List<Employee>>() {
		};

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();

		String json = gson.toJson(list, listType.getType());

		return json;
	}

	private static void writeStrign(String json, String jsonFileName) {
		try (FileWriter file = new FileWriter(jsonFileName)) {
			file.write(json);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readString(String fileName) {
		String json = "";
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String s;
			while ((s = br.readLine()) != null) {
				json = json + s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/*
	 * jsonToList with org.json.simple not work!!!
	 * public static List<Employee> jsonToList(String json) {
	 * List<Employee> listEmployee = new ArrayList<>();
	 * TypeToken<List<Employee>> listType = new TypeToken<List<Employee>>() {};
	 * 
	 * GsonBuilder builder = new GsonBuilder();
	 * builder.setPrettyPrinting();
	 * Gson gson = builder.create();
	 * 
	 * try {
	 * 
	 * JSONParser parser = new JSONParser();
	 * JSONArray jsonArray = new JSONArray();
	 * jsonArray = (JSONArray) parser.parse(json);
	 * 
	 * 
	 * 
	 * for (Object object : jsonArray) {
	 * Employee employee = gson.fromJson((String) object, listType.getType());
	 * listEmployee.add(employee);
	 * }
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * return listEmployee;
	 * }
	 */

	public static List<Employee> jsonToList(String json) {
		List<Employee> listEmployee = new ArrayList<>();

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();

		try {
			JsonElement element = JsonParser.parseString(json);
			JsonArray jsonArray = element.getAsJsonArray();
			for (JsonElement e : jsonArray) {
				Employee employee = gson.fromJson(e, Employee.class);
				listEmployee.add(employee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listEmployee;
	}

	public static void main(String[] args) {
		String[] columnMapping = { "id", "firstName", "lastName", "country", "age" };

		// CSC -> JSON
		String csvFileName = "data.csv";
		String jsonFileName = "data.json";

		List<Employee> list = parseCSV(columnMapping, csvFileName);

		String json = listToJson(list);

		writeStrign(json, jsonFileName);

		// XML -> JSON
		String xmlFileName = "data.xml";
		String jsonFileName2 = "data2.json";

		List<Employee> list2 = parseXML(xmlFileName);

		String json2 = listToJson(list2);
		writeStrign(json2, jsonFileName2);

		// JSON -> Object
		String json3 = readString("data.json");

		List<Employee> list3 = jsonToList(json3);

		for (Employee e : list3) {
			System.out.println(e);
		}
	}
}
