Reading the Conversation ID From Another Parameter
##################################################

:date: 2015-09-16T11:21:00+02:00
:tags: Java, JSF, CDI
:category: Programming
:author: Jens Frey
:summary: How to read the JSF conversation ID from another parameter than cid.

.. note:: This was tested with Wildfly 10.0.0.Final, Mojarra 2.2.12 and Java JDK 1.8.0_51

When you are working with a :code:`@ConversationScoped` bean you may want to read JSF's conversation ID (:code:`cid`) from another request parameter such as for example a parameter named :code:`state`.

At first I tried to find a configuration option to change this behaviour, but failed to find one. In the end the problem can be solved using the :code:`org.jboss.weld.context.http.HttpConversationContext` class from the Weld CDI implementation if you don't mind being a little implementation dependent.

This class allows you to activate a specific context ID or :code:`cid` so you have access to your *normal* :code:`@ConversationScoped` beans. The only pitfall here to remember is that you have to call :code:`deactivate()` first or you may end up with some kind of a *Context is already active* Exception.

Well you basically do this in your :code:`@PostConstruct` method.

RemapCID.java
  .. code-block:: Java

     @PostConstruct
      private void init() {
          final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

          // This basically makes the state parameter the conversation id
          final String state = request.getParameter("state");
          httpConversationContext.deactivate();
          httpConversationContext.activate(state);
      }

I admit this is a pretty pragmatic solution, but it seems to work. Maybe the :code:`@ConversationScoped` needs to let you specify the parameters to read it's ID from.

Hope this helps, until next time.
