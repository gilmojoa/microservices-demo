package web.filter;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CustomZuulFilter extends ZuulFilter {

	protected Logger logger = Logger.getLogger(CustomZuulFilter.class
			.getName());
	
	@Override
	public Object run() {
		
		final RequestContext ctx = RequestContext.getCurrentContext(); 
		logger.info("in customzuulfilter method - context: " + ctx);
        ctx.addZuulRequestHeader("proxy-routed", "proxy-reroute-header"); 
        logger.info("context with header added: " + ctx);
        return null; 
	}

	@Override
	public boolean shouldFilter() {
        return true; 

	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
