Have a ReST Endpoint Catch All
##############################

:date: 2015-11-22T17:44:25+01:00
:tags: Java, ReST
:category: Programming
:author: Jens Frey
:summary: How to handle unspecific calls to a ReST endpoint.

If you need a ReST endpoint which returns kind of a default page when
a client accesses a unmapped path, you need an implementation like the
one shown below.

Index.java
    .. code-block:: Java

       @ApplicationScoped @Path("/") @Produces(MediaType.TEXT_HTML)
        public class Index {

        @GET @Path("/{any : .*}")
        public Response root() {
            return Response.ok().entity("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Response</title>\n" +
            "    </head>\n" +
                "    <body>\n" +
            "        <h1>Catch All Response</h1>\n" +
                "    </body>\n" +
            "</html>").build();
        }

        }

