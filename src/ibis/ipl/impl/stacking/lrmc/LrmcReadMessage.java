package ibis.ipl.impl.stacking.lrmc;

import java.io.IOException;

import ibis.io.SerializationInput;
import ibis.ipl.IbisIdentifier;
import ibis.ipl.ReadMessage;
import ibis.ipl.ReceivePort;
import ibis.ipl.SendPortIdentifier;
import ibis.ipl.impl.stacking.lrmc.io.BufferedArrayInputStream;

public class LrmcReadMessage implements ReadMessage {

    SerializationInput in;
    boolean isFinished = false;
    ObjectMulticaster om;
    private BufferedArrayInputStream bin;
    long count = 0;
    
    public LrmcReadMessage(SerializationInput in, ObjectMulticaster om,
            BufferedArrayInputStream bin) {
        this.in = in;
        this.om = om;
        this.bin = bin;
    }

    protected final void checkNotFinished() throws IOException {
        if (isFinished) {
            throw new IOException(
                    "Operating on a message that was already finished");
        }
    }

    public SendPortIdentifier origin() {
        int source = bin.getInputStream().getSource();
        IbisIdentifier id = om.lrmc.ibis.getId(source);
        return new LrmcSendPortIdentifier(id, om.lrmc.getName());
    }

    protected int available() throws IOException {
        checkNotFinished();
        return in.available();
    }

    public boolean readBoolean() throws IOException {
        checkNotFinished();
        return in.readBoolean();
    }

    public byte readByte() throws IOException {
        checkNotFinished();
        return in.readByte();
    }

    public char readChar() throws IOException {
        checkNotFinished();
        return in.readChar();
    }

    public short readShort() throws IOException {
        checkNotFinished();
        return in.readShort();
    }

    public int readInt() throws IOException {
        checkNotFinished();
        return in.readInt();
    }

    public long readLong() throws IOException {
        checkNotFinished();
        return in.readLong();
    }

    public float readFloat() throws IOException {
        checkNotFinished();
        return in.readFloat();
    }

    public double readDouble() throws IOException {
        checkNotFinished();
        return in.readDouble();
    }

    public String readString() throws IOException {
        checkNotFinished();
        return in.readString();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        checkNotFinished();
        return in.readObject();
    }

    public void readArray(boolean[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(byte[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(char[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(short[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(int[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(long[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(float[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(double[] destination) throws IOException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(Object[] destination) throws IOException,
            ClassNotFoundException {
        readArray(destination, 0, destination.length);
    }

    public void readArray(boolean[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(byte[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(char[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(short[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(int[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(long[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(float[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(double[] destination, int offset, int size)
            throws IOException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public void readArray(Object[] destination, int offset, int size)
            throws IOException, ClassNotFoundException {
        checkNotFinished();
        in.readArray(destination, offset, size);
    }

    public long bytesRead() throws IOException {
        long cnt = bin.bytesRead();
        long retval = cnt - count;
        count = cnt;
        return retval;
    }

    public long finish() throws IOException {
        isFinished = true;
        // TODO Auto-generated method stub
        return 0;
    }

    public void finish(IOException exception) {
        isFinished = true;
        // TODO Auto-generated method stub
        
    }

    public ReceivePort localPort() {
        return om.lrmc.receive;
    }

    public long sequenceNumber() {
        // Not supported.
        return 0;
    }

}
