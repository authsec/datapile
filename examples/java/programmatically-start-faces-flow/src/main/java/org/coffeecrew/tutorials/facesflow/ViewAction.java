//<editor-fold defaultstate="collapsed" desc="Clavid License">
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//</editor-fold>
package org.coffeecrew.tutorials.facesflow;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowHandler;
import javax.inject.Named;

/**
 *
 * @author Jens Frey (jens.frey@swisscom.com)
 */
@Named @javax.faces.view.ViewScoped
public class ViewAction implements Serializable {

    private static final String FLOW_NAME = "flow";

    private static final long serialVersionUID = 1L;

    public String startFlow() {

        /*
         If you omit these when starting the flow you receive the following error:

         WELD-001303: No active contexts for scope type javax.faces.flow.FlowScoped
         */
        FacesContext context = FacesContext.getCurrentInstance();
        FlowHandler handler = context.getApplication().getFlowHandler();

        handler.transition(context, null, handler.getFlow(context, "", FLOW_NAME), null, "");

        return FLOW_NAME;
    }

}
