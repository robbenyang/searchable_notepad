package com.example.searchablenotepad;

public class Data {
	private int id;
	private String data;
	private String file;

	public Data() {
		// Default constructor
	}

	public Data(int _id, String _data, String _file) {
		id = _id;
		data = _data;
		file = _file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
