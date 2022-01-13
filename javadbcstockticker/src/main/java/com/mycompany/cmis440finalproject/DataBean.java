package com.mycompany.cmis440finalproject;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/* Updates price and volume information every second */
@Startup
@Singleton
public class DataBean extends DBConnect{
    /* Use the container's timer service */
    @Resource TimerService tservice;
    private ETFServlet servlet;
    
    private volatile String id = "";
    private volatile String vin = "";
    private volatile String make = "";
    private volatile String model = "";
    private volatile String year = "";
    private volatile String color = "";
    
    private volatile int i = 0;
    
    private static final Logger logger = Logger.getLogger("DataBean");
    public DBConnect dbc = new DBConnect();
    public String fileAsLine = null;
    
    @PostConstruct
    public void init(){
        /* Intialize the EJB and create a timer */
        logger.log(Level.INFO, "Initializing EJB.");
        servlet = null;
        tservice.createIntervalTimer(2000, 2000, new TimerConfig());
        
        try{
            dbc.getData();
        }
        catch(Exception e){
            
        }
    }
    
    public void registerServlet(ETFServlet servlet) {
        /* Associate a servlet to send updates to */
        this.servlet = servlet;
    }
    
    @Timeout
    public void timeout() throws Exception{
        
        id = data.get(i++);
        vin = data.get(i++);
        make = data.get(i++);
        model = data.get(i++);
        year = data.get(i++);
        color = data.get(i++);
        
        if (servlet != null)
            servlet.send(id, vin, make, model, year, color);
    }
}