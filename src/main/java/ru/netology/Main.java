package ru.netology;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
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

	public static String listToJson(List<Employee> list) {
		
		TypeToken<List<Employee>> listType = new TypeToken<List<Employee>>() {};

		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();

		String json = gson.toJson(list, listType.getType());

		return json;


	}

	public static void writeStrign(String json, String jsonFileName) {
		try (FileWriter file = new FileWriter(jsonFileName)) {
			file.write(json);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String[] columnMapping = { "id", "firstName", "lastName", "country", "age" };
		String csvFileName = "data.csv";
		String jsonFileName1 = "data.json";

		List<Employee> list = parseCSV(columnMapping, csvFileName);

		String json = listToJson(list);
		
		writeStrign(json, jsonFileName1);
	}
}
