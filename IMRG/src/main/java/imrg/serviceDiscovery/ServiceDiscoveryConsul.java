package imrg.serviceDiscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;

@Service
public class ServiceDiscoveryConsul {

	public void register(ServiceModel service) {
    	Consul client = Consul.builder().build(); 
    	AgentClient agentClient = client.agentClient();
    	Registration serviceRegistration = ImmutableRegistration.builder()
    	        .id(service.getServiceID())
    	        .name(service.getServiceID())
    	        .address(service.getAddress())
    	        .port(service.getPort())
    	        .tags(Collections.singletonList(service.getRegion()))
    	        .build();
    	agentClient.register(serviceRegistration);
	}
	
	public void deregister(String serviceID) {
		Consul client = Consul.builder().build(); 
		AgentClient agentClient = client.agentClient();
		agentClient.deregister(serviceID);
	}

	public List<String> getAllServiceID(){
		Consul client = Consul.builder().build();	
		CatalogClient catalogClient = client.catalogClient();
		Map<String, List<String>> map = catalogClient.getServices().getResponse();
		Set<String> key=map.keySet();
		List<String> lista= new ArrayList<String>();
		for (String value : key) {
			if (!value.contains("consul"))
				lista.add(value);
		} 
		return lista;
	}
	
	public ServiceModel getInfoService(ServiceModel serviceModel) {
		Consul client = Consul.builder().build();	
		AgentClient agentClient = client.agentClient();
		com.orbitz.consul.model.health.Service service=agentClient.getServices().get(serviceModel.getServiceID());
		serviceModel.setAddress(service.getAddress());
		serviceModel.setPort(service.getPort());
		serviceModel.setRegion(service.getTags().get(0));
		return serviceModel;
	}
}

