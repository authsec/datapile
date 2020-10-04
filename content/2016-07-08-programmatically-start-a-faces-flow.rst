Programmatically Start A JSF Flow
#################################

:date: 2016-07-08T21:44:25+01:00
:tags: Java, JSF
:category: Programming
:author: Jens Frey
:summary: How to programmatically start a faces flow.

.. note:: This was tested with Wildfly 10.0.0.Final, Mojarra 2.2.12 and Java JDK 1.8.0_51

There are situations where you can not start a JSF (2.2) flow in the usual way by simply redirecting to the flow's start page from your button action.

This is for example the case when you want to start a JSF flow from e.g. a view action method. In this case it is not sufficient to simple return the view name of the flow as this will not just start it.

To properly start the flow you need to first tell the flow handler that you are now transitioning into a flow. You can do this with the following code:

.. code-block:: Java

   FacesContext context = FacesContext.getCurrentInstance();
   FlowHandler handler = context.getApplication().getFlowHandler();
   handler.transition(context, null, handler.getFlow(context, "", FLOW_NAME), null, "");

   return FLOW_NAME;

If done like this, that flow should start as expected. However, if you don't set up the :code:`FlowHandler.transition` properly you will end up with the following error:

.. code-block:: text

   WELD-001303: No active contexts for scope type javax.faces.flow.FlowScoped

.. note:: You need to reach the page running the view action through a :code:`GET` request. So using the normal :code:`h:commandButton` will not work in this case as this issues a :code:`POST` request (except you add :code:`?faces-redirect=true` to your action parameter of course).

The full source for this example can be found `here <https://github.com/authsec/examples/tree/master/java/programmatically-start-faces-flow>`_.
