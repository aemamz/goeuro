package com.goeuro.search.pojo;

public class GoEuroLocation {

	private String id;
	private String name;
	private String type;
	private String latitude;
	private String longtiude;
	
	
	public GoEuroLocation(String id, String name, String type, String latitude,
			String longtiude) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.latitude = latitude;
		this.longtiude = longtiude;
	}
	
	public GoEuroLocation() {
		super();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtiude() {
		return longtiude;
	}
	public void setLongtiude(String longtiude) {
		this.longtiude = longtiude;
	}
	
}
