package de.ruedigermoeller.serialization.testclasses.libtests;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import de.ruedigermoeller.serialization.testclasses.basicstuff.FrequentCollections;
import de.ruedigermoeller.serialization.testclasses.basicstuff.FrequentPrimitives;
import de.ruedigermoeller.serialization.testclasses.basicstuff.Primitives;
import de.ruedigermoeller.serialization.testclasses.basicstuff.SmallThing;
import de.ruedigermoeller.serialization.testclasses.enterprise.ObjectOrientedDataType;
import de.ruedigermoeller.serialization.testclasses.enterprise.ObjectOrientedInt;
import de.ruedigermoeller.serialization.testclasses.enterprise.SimpleOrder;
import de.ruedigermoeller.serialization.testclasses.enterprise.Trader;
import de.ruedigermoeller.serialization.testclasses.remoting.ShortRemoteCall;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTUtil;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

/**
 * Created by ruedi on 23/05/15.
 */
public class FSTJsonTest extends SerTest {

    boolean minbin;
    FSTConfiguration defconf;
    FSTObjectInput in;
    FSTObjectOutput out;

    public FSTJsonTest(String desc, boolean minbin) {
        super(desc);
        this.minbin = minbin;
    }

    @Override
    public void init() {
        if (minbin) {
            defconf = FSTConfiguration.createMinBinConfiguration();
        } else {
            defconf = FSTConfiguration.createJsonConfiguration(false,true);
            defconf.setShareReferences(false);
        }
        defconf.cpMap("fprim", FrequentPrimitives.class)
            .cpMap("trader", Trader.class)
            .cpMap("od", ObjectOrientedDataType.class)
            .cpMap("oi", ObjectOrientedInt.class)
            .cpMap("order", SimpleOrder.class)
            .cpMap("se", Primitives.SampleEnum.class)
            .cpMap( "rc", Primitives.class )
            .cpMap( "im", Image.class )
            .cpMap( "md", Media.class )
            .cpMap( "mc", MediaContent.class );
//        defconf.setShareReferences(false);
        in = new FSTObjectInput(defconf);
        out = new FSTObjectOutput(defconf);
    }

    public String getColor() {
        return "#4040a0";
    }

    @Override
    protected void readTest(ByteArrayInputStream bin, Class cl) {
        try {
            in.resetForReuse(bin);
            Object res = in.readObject(cl);
            resObject = res;
        } catch (Throwable e) {
            FSTUtil.printEx(e);
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void writeTest(Object toWrite, OutputStream bout, Class aClass) {
        out.resetForReUse(bout);
        try {
            out.writeObject(toWrite, aClass);
            out.flush();
        } catch (Throwable e) {
            FSTUtil.printEx(e);
            throw new RuntimeException(e);
        }
    }
}
