import com.panda.compress.PandaZip;
import org.junit.jupiter.api.Test;


public class PandaZipTest {
    @Test
    void zipTest() throws Exception {
        String source = "C:\\Users\\liujian\\Desktop\\unzip\\刘建资料";
        String dest = "C:\\Users\\liujian\\Desktop\\unzip\\刘建资料.zip";
        PandaZip.zip(source, dest);
    }

    @Test
    void unZipTest() throws Exception {
        String source = "C:\\Users\\liujian\\Desktop\\zip\\刘建资料.zip";
        String dest = "C:\\Users\\liujian\\Desktop\\zip";
        PandaZip.unzip(source, dest);
    }
}
