package imrg.serviceDiscovery;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.RequestContext;

@Controller
public class ServiceDiscoveryController {
	private static Logger log = LoggerFactory.getLogger(ServiceDiscoveryController.class);
	@Autowired
	ServiceDiscoveryConsul serviceDiscoveryConsul;
	@Autowired
	private HttpServletRequest request;
	
	//Registrazione
    @GetMapping("/IMRG/register")
    public String registerIMRW(Model model) {
        ServiceModel service = new ServiceModel();
        model.addAttribute("service", service);
        List<String> listRegion = Arrays.asList("Campania", "Lazio");
        model.addAttribute("listRegion", listRegion);
        return "register_form";
    }
 
    @PostMapping("/IMRG/register")
    public String registerIMRW(@ModelAttribute("service") ServiceModel service) {
    	serviceDiscoveryConsul.register(service);
    	return "register_success";
    }
    
	//Deregistrazione
    @GetMapping("/IMRG/deregister")
    public String deregisterIMRW(Model model) {
        ServiceModel service = new ServiceModel();
        model.addAttribute("service", service);
        List<String> listServiceID =serviceDiscoveryConsul.getAllServiceID() ;
        model.addAttribute("listServiceID", listServiceID);
        return "deregister_form";
    }
    @PostMapping("/IMRG/deregister")
    public String deregisterIMRW(@ModelAttribute("service") ServiceModel service) {
    	serviceDiscoveryConsul.deregister(service.getServiceID());
    	return "deregister_success";
    }
    
	//getInfo
    @GetMapping("/IMRG/getInfoService")
    public String getInfoIMRW(Model model) {
        ServiceModel service = new ServiceModel();
        model.addAttribute("service", service);
        List<String> listServiceID =serviceDiscoveryConsul.getAllServiceID() ;
        model.addAttribute("listServiceID", listServiceID);
        return "info_form";
    }
    
    @PostMapping("/IMRG/redirectToStation")
    public String redirectIMRW(@ModelAttribute("service") ServiceModel service,Model model) throws  MalformedURLException {
    	service=serviceDiscoveryConsul.getInfoService(service);
    	model.addAttribute("service", service);
    	String url_imrw= new String(service.getAddress()+":"+service.getPort()+"/imrw"+service.getRegion()+"/"+service.getServiceID()+"/index");
    	model.addAttribute("url_imrw",url_imrw);
    	return "redirectToStation";
    }
    
    @GetMapping("/IMRG/index")
    public String getIndex() {
		KeycloakPrincipal principal = (KeycloakPrincipal) request.getUserPrincipal();
		KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
		AccessToken accessToken = session.getToken();
		Access realmAccess = accessToken.getRealmAccess();
		Set<String> roles = realmAccess.getRoles();
    
		if(roles.contains("agenteManutentore") || roles.contains("DCO") ){
	        return "index_operatore";
		}else if(roles.contains("amministratore")){
	        return "index_admin";
		}
		return "error";
    }
   
    /*@GetMapping("/error")
    public String handleError(Model model) {
    	
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.info(status.toString());
        System.out.println("AIuto");
        model.addAttribute("codeError", status);
        return "error";
   }*/
}
