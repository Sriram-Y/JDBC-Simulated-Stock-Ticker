package com.mycompany.cmis440finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/cmis440finalproject"}, asyncSupported=true)
public class ETFServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger("ETFServlet");
    private Queue<AsyncContext> requestQueue;
    @EJB private DataBean dbean; 
    
    @Override
    public void init(ServletConfig config) {
        /* Queue for requests */
        requestQueue = new ConcurrentLinkedQueue<>();
        /* Register with the bean that provides price/volume updates */
        dbean.registerServlet(this);
        
    }
    
    /* PriceVolumeBean calls this method every second to send updates */
    public void send(String id, String vin, String make, String model, String year, String color) {
        /* Send update to all connected clients */
        for (AsyncContext acontext : requestQueue) {
            try {
                String msg = String.format("%s / %s / %s / %s / %s / %s", id, vin, make, model, year, color);
                PrintWriter writer = acontext.getResponse().getWriter();
                writer.write(msg);
                logger.log(Level.INFO, "Sent: {0}", msg);
                /* Close the connection
                 * The client (JavaScript) makes a new one instantly */
                acontext.complete();
            } catch (IOException ex) {
                logger.log(Level.INFO, ex.toString());
            }
        }
    }
    
    /* Service method */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        /* Put request in async mode. */
        final AsyncContext acontext = request.startAsync();
        /* Remove from the queue when done */
        acontext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent ae) throws IOException {
                requestQueue.remove(acontext);
                logger.log(Level.INFO, "Connection closed.");
            }
            @Override
            public void onTimeout(AsyncEvent ae) throws IOException {
                requestQueue.remove(acontext);
                logger.log(Level.INFO, "Connection timeout.");
            }
            @Override
            public void onError(AsyncEvent ae) throws IOException {
                requestQueue.remove(acontext);
                logger.log(Level.INFO, "Connection error.");
            }
            @Override
            public void onStartAsync(AsyncEvent ae) throws IOException { }
        });
        /* Add to the queue */
        requestQueue.add(acontext);
        logger.log(Level.INFO, "Connection open.");
    }
}