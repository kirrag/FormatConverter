package ru.netology;

public class Employee {
	public long id;
	public String firstName;
	public String lastName;
	public String country;
	public int age;

	public Employee() {
		// Пустой конструктор
	}

	public Employee(long id, String firstName, String lastName, String country, int age) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.age = age;
	}

	public void setField(String nodeName, String textContent) {
		if (nodeName.equals("id")) {
			this.id = Long.parseLong(textContent, 10);
		} else if (nodeName.equals("firstName")) {
			this.firstName = textContent;
		} else if (nodeName.equals("lastName")) {
			this.lastName = textContent;
		} else if (nodeName.equals("country")) {
			this.country = textContent;
		} else if (nodeName.equals("age")) {
			this.age = Integer.parseInt(textContent, 10);
		}
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id='" + id + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", country='" + country + '\'' +
				", age='" + age + '\'' +
				'}';
	}

}
