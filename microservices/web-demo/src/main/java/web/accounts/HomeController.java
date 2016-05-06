package web.accounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller.
 * 
 * @author Paul Chapman
 */
@Controller
public class HomeController {

	@Value("${deploy.environment}")
    public String deployment_environment;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("env", deployment_environment);
		return "index";
	}

}
	