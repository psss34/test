package imrg.serviceDiscovery;



public class ServiceModel {
	private  String serviceID;
	private  String address;
	private  int port; //Attenzione modifica il file resourcers.application.properties
	private  String region ; //Collections.singletonList("IMRG")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}
