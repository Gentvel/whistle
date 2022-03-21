import com.whistle.code.simulate.DataBuilders;
import com.whistle.code.simulate.bean.Person;
import com.whistle.code.simulate.bean.Poster;
import com.whistle.code.simulate.bean.Reply;
import com.whistle.code.simulate.utils.Mock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class TestLog {

    private static final String personBytePath = "D:\\Projects\\simulated_data\\person\\poster.data";


    @Test
    public void test() {
        log.info("error");
        log.info(Mock.bool() ? "true" : "false");
        log.info("" + Mock.natural());
        log.info("" + Mock.natural(10000));
        log.info("" + Mock.natural(10000, 120000));
//        log.info(""+Mock.natural(10000,1200));
        log.info("" + Mock.character());
        log.info("" + Mock.date());
        log.info("" + Mock.datetime());
        log.info("" + Mock.cname());
        log.info("" + Mock.id());
        log.info("" + Mock.county(true));

    }

    @Test
    @DisplayName("模拟生成人物数据")
    public void testPerson() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Person> persons = DataBuilders.getPersons(8);
        persons.forEach(person -> log.info(person.toString()));
        stopWatch.stop();
        stopWatch.prettyPrint();

        List<Poster> posters = DataBuilders.getPosters(5, persons.size());
        posters.forEach(post -> log.info(post.toString()));

        List<Reply> replies = DataBuilders.getReplies(10, persons.size(), posters.size());
        replies.forEach(reply -> log.info(reply.toString()));
    }

    @Test
    @DisplayName("模拟读取数据")
    public void testRead() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\Projects\\simulated_data\\person\\person.data");
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer allocate = ByteBuffer.allocate(32);
            ByteBuffer fixed = ByteBuffer.allocate(3);
            ByteBuffer buffer = ByteBuffer.allocate(32 * 2);
            channel.read(fixed);
//            byte[] array = fixed.array();
            String a="Abc#$%我…（￥我";
            byte[] array = a.getBytes(StandardCharsets.UTF_8);
            int i = 8;
            log.info("{},{}",(char)array[i],isMessyCode((char) array[i]));


            while (i < array.length) {
                if ((char) array[i] > 127) {
                    if (i + 3 < array.length) {
                        i += 3;
                    }
                    break;
                } else {
                    ++i;
                }
            }
//            log.info("i={}", i);
//            String trim = new String(fixed.array());
//            log.info("{}least{},last{}", trim, isMessyCode(trim.charAt(trim.length() - 2)),isMessyCode(trim.charAt(trim.length() - 1)));
//            log.info(Arrays.toString(fixed.array()));
//            while (channel.read(allocate) != -1) {
//                allocate.flip();
//                byte[] array = allocate.array();
//                byte lastByte = array[array.length - 1];
//                byte secondLastByte = array[array.length - 2];
//                if (allocate.limit() + buffer.position() > buffer.limit()) {
//                    buffer.flip();
//                    log.info(new String(buffer.array()).trim());
//                    buffer.clear();
//                }
//                /*
//                 *中文字符有三个字节
//                 * @see https://www.cnblogs.com/liushui-sky/p/10483248.html
//                 *
//                 * 当第一位为1110开头时，说明表示该字的第一个字节
//                 */
//                if (Integer.toBinaryString((lastByte & 0xFF) + 0x100).substring(1).startsWith("1110")) {
//                    fixed.put(lastByte);
//                    buffer.put(array, 0, array.length - 1);
//                } else if (Integer.toBinaryString((secondLastByte & 0xFF) + 0x100).substring(1).startsWith("1110")) {
//                    fixed.put(lastByte);
//                    fixed.put(secondLastByte);
//                    buffer.put(array, 0, array.length - 2);
//                } else {
//                    buffer.put(array);
//                }
            //buffer.put(array);
//                allocate.clear();
//                if (fixed.position() > 0) {
//                    fixed.flip();
//                    allocate.put(fixed);
//                    fixed.clear();
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断字符后两位是否是乱码
     *
     * @param str 字符
     * @return 乱码数
     */
    public static boolean isMessyCode(char ch) {

        if (!Character.isLetterOrDigit(ch)) {
            if (Character.isSpaceChar(ch)) {
                return false;
            }
            return !isChinese(ch);

        }


        return false;
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    @Test
    @DisplayName("读取文件速率比较")
    public void bioRead() throws IOException {
        StopWatch stopWatch = new StopWatch("读取文件速率比较");
        stopWatch.start("normal");
        FileInputStream fileInputStream = new FileInputStream(personBytePath);
        byte[] buf = new byte[1024 * 1000];
        int read;
        while ((read = fileInputStream.read(buf)) != -1) {
            log.info("读取长度:{}", read);
        }
        log.info("aio读取结束....");
        stopWatch.stop();

        stopWatch.start("normal buffered");
        InputStream inputStream = Files.newInputStream(Path.of(personBytePath));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        while ((read = bufferedInputStream.read(buf)) != -1) {
            log.info("读取长度:{}", read);
        }
        stopWatch.stop();

        stopWatch.start("random access");
        RandomAccessFile randomAccessFile = new RandomAccessFile(Path.of(personBytePath).toFile(), "r");
        long length = randomAccessFile.length();
        for (long p = 1024 * 1000; p < length; p += 1024 * 1000) {
            randomAccessFile.seek(p);
            log.info("读去长度：{}", randomAccessFile.getFilePointer());
        }

        stopWatch.stop();
        stopWatch.start("channel");
        FileInputStream data = new FileInputStream(personBytePath);
        FileChannel channel = data.getChannel();
        ByteBuffer allocate = ByteBuffer.allocateDirect(1024 * 1000);
        while (true) {
            int count = channel.read(allocate);
            if (count <= -1) {
                break;
            }
            allocate.flip();
            log.info("读取长度：{}", allocate.limit());
            allocate.clear();
        }
        log.info("channel读取结束....");
        stopWatch.stop();

        stopWatch.start("MappedByteBuffer");
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        int capacity = map.capacity();

        for (int i = capacity / 10; i < capacity; i += capacity / 10) {
            log.info("读取长度:{},剩余长度：{}", i, capacity - i);
        }
        log.info("MappedByteBuffer读取结束....");
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());


    }


}
