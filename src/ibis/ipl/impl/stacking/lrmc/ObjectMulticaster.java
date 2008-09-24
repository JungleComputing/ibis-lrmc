package ibis.ipl.impl.stacking.lrmc;

import ibis.io.SerializationFactory;
import ibis.io.SerializationInput;
import ibis.io.SerializationOutput;

import ibis.ipl.Ibis;
import ibis.ipl.IbisIdentifier;
import ibis.ipl.PortType;
import ibis.ipl.ReadMessage;
import ibis.ipl.impl.stacking.lrmc.io.BufferedArrayInputStream;
import ibis.ipl.impl.stacking.lrmc.io.BufferedArrayOutputStream;
import ibis.ipl.impl.stacking.lrmc.io.LrmcInputStream;
import ibis.ipl.impl.stacking.lrmc.io.LrmcOutputStream;
import ibis.ipl.impl.stacking.lrmc.io.MessageReceiver;
import ibis.ipl.impl.stacking.lrmc.util.Message;
import ibis.ipl.impl.stacking.lrmc.util.MessageCache;
import ibis.util.TypedProperties;

import java.io.IOException;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;


// TODO find a way to share destination arrays in messages --Rob

public class ObjectMulticaster implements MessageReceiver {

    private final int MESSAGE_SIZE;

    private final int MESSAGE_CACHE_SIZE;

    LabelRoutingMulticast lrmc; 
    
    private LrmcOutputStream os; 
    
    private BufferedArrayOutputStream bout;
    private BufferedArrayInputStream bin;
    
    private SerializationOutput sout;
    private SerializationInput sin;    
          
    private long totalData = 0;
    private long lastBytesWritten = 0;
    
    private MessageCache cache; 

    private boolean finish = false;
    private boolean receiverDone = false;
    private Thread receiver = null;
    
    private InputStreams inputStreams = new InputStreams();
    
    private boolean destinationSet = false;
    private IbisIdentifier [] destination = null; 
   
    public ObjectMulticaster(LrmcIbis ibis, PortType type, String name) throws IOException {
        TypedProperties tp = new TypedProperties(ibis.properties());
        this.MESSAGE_SIZE = tp.getIntProperty("lrmc.messageSize", 8 * 1024);
        this.MESSAGE_CACHE_SIZE = tp.getIntProperty("lrmc.messageCacheSize",
                1500);
        
        cache = new MessageCache(MESSAGE_CACHE_SIZE, MESSAGE_SIZE);
                
        lrmc = new LabelRoutingMulticast(ibis, this, cache, name);
        
        os = new LrmcOutputStream(lrmc, cache);

        bout = new BufferedArrayOutputStream(os, MESSAGE_SIZE);
        bin = new BufferedArrayInputStream(null);
        
        String serialization;
        
        if (type.hasCapability(PortType.SERIALIZATION_DATA)) {
            serialization = "data";    
        } else if (type.hasCapability(PortType.SERIALIZATION_OBJECT_SUN)) {
            serialization = "sun";            
        } else if (type.hasCapability(PortType.SERIALIZATION_OBJECT_IBIS)) {
            serialization = "ibis";            
        } else if (type.hasCapability(PortType.SERIALIZATION_OBJECT)) {
            serialization = "object";
        } else {
            serialization = "byte";
        }
        sout = SerializationFactory.createSerializationOutput(serialization, bout);
        sin = SerializationFactory.createSerializationInput(serialization, bin);
    }
    
    public synchronized void setDestination(IbisIdentifier[] dest) {         
        destination = dest;
        cache.setDestinationSize(dest.length);
    }

    public void gotDone(int id) {
        // nothing
    }

    public boolean gotMessage(Message m) {

        LrmcInputStream tmp;

        // Fix: combined find call and add call into get().
        // There was a race here. (Ceriel)
        tmp = inputStreams.get(m.sender, cache);

        // tmp.addMessage(m);         
        // inputStreams.hasData(tmp);
        // Fix: avoid race: message may have been read before setting
        // hasData. (Ceriel)
        return inputStreams.hasData(tmp, m);
    }
    
    public int send(IbisIdentifier[] id, Object o) throws IOException {
        setDestination(id);
        return send(o);
    }
    
    private synchronized int send(Object o) throws IOException {

        // check if a new destination array is available....
        if (destination != null) {
            lrmc.setDestination(destination);
            destination = null;
            destinationSet = true;            
        } else if (!destinationSet) { 
            throw new IOException("No destination set!");
        }            
        
        // We only want to return the number of bytes written in this bcast, so 
        // reset the count.
        bout.resetBytesWritten();               
        
        os.reset();

        int retval = os.currentID;
        
        // write the object and reset the stream
        sout.reset(true);              
        sout.writeObject(o);
        sout.flush();

        bout.forcedFlush();

        lastBytesWritten = bout.bytesWritten();
        
        totalData += lastBytesWritten;
        
        return retval;
    }

    public long lastSize() {
        return lastBytesWritten;
    }
    
    LrmcReadMessage explicitReceive() throws IOException {
        
        Object result = null; 
//        IOException ioe = null;
//        ClassNotFoundException cnfe = null;

        // Get the next stream that has some data
        LrmcInputStream stream = inputStreams.getNextFilledStream(); 

        if (stream == null) {
            throw new DoneException("Someone wants us to stop");
        }

        // Plug it into the deserializer
        bin.setInputStream(stream);
        bin.resetBytesRead();
        
        // Read an object
        try { 
            result = sin.readObject();
        } finally {
            inputStreams.returnStream(stream);
            totalData += bin.bytesRead();
        }

        return result;
    }

    public Object receive() throws IOException, ClassNotFoundException {
        synchronized(this) {
            if (finish) {
                throw new DoneException("Someone wants us to stop ...");
            }
            receiverDone = false;
            receiver = Thread.currentThread();
        }
        Object o;
        try {
            o = explicitReceive();
        } finally {
            synchronized(this) {
                receiverDone = true;
                if (finish) {
                    notifyAll();
                    throw new DoneException("Someone wants us to stop ...");
                }
                receiver = null;
            }
        }
        return o;
    } 
    
    public long bytesRead() { 
        return bin.bytesRead();
    }
    
    public long bytesWritten() { 
        return bout.bytesWritten();
    }
    
    public long totalBytes() { 
        return totalData;
    }
    
    public void done() {
        synchronized(this) {
            finish = true;
            inputStreams.terminate();
            notifyAll();
            // we can tell the receiver thread, but we don't know that
            // it will actually finish, so we cannot join it.
            if (receiver != null) {
                // Wait until this is noticed.
                while (! receiverDone) {
                    try {
                        wait();
                    } catch(Exception e) {
                        // What to do here? (TODO)
                    }
                }
            }
        }
        try {
            os.close();
            
            //sout.close(); // don't close this one. It keeps on talking...
            sin.close();
            
            lrmc.done();
        } catch (IOException e) {
            // ignore, we tried ...
        }
    }
    
    public synchronized void haveObject(LrmcInputStream stream) {

        Object result = null; 
        boolean succes = true;

        if (finish) {
            return;
        }

        // Plug it into the deserializer
        bin.setInputStream(stream);
        bin.resetBytesRead();
        
        // Read an object
        try { 
            result = sin.readObject();
        } catch (Exception e) {
            succes = false;
        } finally {
            // Return the stream to the queue (if necessary) 
            inputStreams.returnStream(stream);     
            totalData += bin.bytesRead();
        }

        if (succes) { 
            objects.addLast(result);
            notifyAll();
        }       
    }    
}
