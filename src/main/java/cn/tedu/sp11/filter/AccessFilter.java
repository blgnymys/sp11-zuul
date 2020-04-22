package cn.tedu.sp11.filter;

import cn.tedu.web.util.JsonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        //获取指定的service-id
        RequestContext ctx=RequestContext.getCurrentContext();
        String serviceId= (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        if(serviceId.equals("item-service")){
            return true;
        }
        return false;

    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx=RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token=request.getParameter("token");
        if(token==null || token.trim().length()==0){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).toString());
        }
        return null;
    }
}
