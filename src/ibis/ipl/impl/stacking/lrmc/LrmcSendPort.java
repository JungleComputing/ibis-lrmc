package ibis.ipl.impl.stacking.lrmc;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ibis.ipl.ConnectionFailedException;
import ibis.ipl.ConnectionsFailedException;
import ibis.ipl.IbisIdentifier;
import ibis.ipl.NoSuchPropertyException;
import ibis.ipl.PortType;
import ibis.ipl.ReceivePortIdentifier;
import ibis.ipl.SendPort;
import ibis.ipl.SendPortDisconnectUpcall;
import ibis.ipl.SendPortIdentifier;
import ibis.ipl.WriteMessage;

public class LrmcSendPort implements SendPort {
   
    private final PortType portType;
    private final LrmcSendPortIdentifier identifier;
    private final LrmcIbis ibis;

    public LrmcSendPort(PortType portType, LrmcIbis ibis, String name,
            SendPortDisconnectUpcall cu, Properties props) {
        this.portType = portType;
        identifier = new LrmcSendPortIdentifier(ibis.identifier(), name);
        this.ibis = ibis;
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    public void connect(ReceivePortIdentifier receiver)
            throws ConnectionFailedException {
        connect(receiver, 0, true);
    }

    public void connect(ReceivePortIdentifier receiver, long timeoutMillis,
            boolean fillTimeout) throws ConnectionFailedException {
        if (! identifier.name.equals(receiver.name())) {
            throw new ConnectionFailedException("LRMCIbis sendport connect requires that the "
                    + "receiveport has the same name", receiver);
        }
        // TODO Auto-generated method stub
    }

    public ReceivePortIdentifier connect(IbisIdentifier ibisIdentifier,
            String receivePortName) throws ConnectionFailedException {
        ReceivePortIdentifier id = new LrmcReceivePortIdentifier(ibisIdentifier, receivePortName);
        connect(id, 0, true);
        return id;
    }

    public ReceivePortIdentifier connect(IbisIdentifier ibisIdentifier,
            String receivePortName, long timeoutMillis, boolean fillTimeout)
            throws ConnectionFailedException {
        ReceivePortIdentifier id = new LrmcReceivePortIdentifier(ibisIdentifier, receivePortName);
        connect(id, timeoutMillis, fillTimeout);
        return id;
    }

    public void connect(ReceivePortIdentifier[] receivePortIdentifiers)
            throws ConnectionsFailedException {
        connect(receivePortIdentifiers, 0, true);
    }

    public void connect(ReceivePortIdentifier[] receivePortIdentifiers,
            long timeoutMillis, boolean fillTimeout)
            throws ConnectionsFailedException {
        // TODO Auto-generated method stub

    }

    public ReceivePortIdentifier[] connect(Map<IbisIdentifier, String> ports)
            throws ConnectionsFailedException {
        return connect(ports, 0, true);
    }

    public ReceivePortIdentifier[] connect(Map<IbisIdentifier, String> ports,
            long timeoutMillis, boolean fillTimeout)
            throws ConnectionsFailedException {
        // TODO Auto-generated method stub
        return null;
    }

    public ReceivePortIdentifier[] connectedTo() {
        // Not supported.
        return null;
    }

    public void disconnect(ReceivePortIdentifier receiver) throws IOException {
        // TODO Auto-generated method stub
    }

    public void disconnect(IbisIdentifier ibisIdentifier, String receivePortName)
            throws IOException {
        // TODO Auto-generated method stub
    }

    public PortType getPortType() {
        return portType;
    }

    public SendPortIdentifier identifier() {
        return identifier;
    }

    public ReceivePortIdentifier[] lostConnections() {
        // not supported
        return null;
    }

    public String name() {
        return identifier.name;
    }

    public WriteMessage newMessage() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getManagementProperty(String key) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCRSendPort");
    }

    public Map<String, String> managementProperties() {
        return new HashMap<String, String>();
    }

    public void printManagementProperties(PrintStream stream) {
    }

    public void setManagementProperties(Map<String, String> properties) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCSendPort");
    }

    public void setManagementProperty(String key, String value) throws NoSuchPropertyException {
        throw new NoSuchPropertyException("No properties in LRMCSendPort");
    }

}
