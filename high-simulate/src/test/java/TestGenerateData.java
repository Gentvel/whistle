import com.whistle.code.simulate.DataBuilders;
import com.whistle.code.simulate.bean.Person;
import com.whistle.code.simulate.bean.Poster;
import com.whistle.code.simulate.bean.Reply;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * data      syn      copy
 * 1000      2730     2431
 * 10000     3997     4063
 * 100000    11832    59575
 * 1000000   79414
 * 8core     67179
 * 16core    77518
 *
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class TestGenerateData {
    private static final Integer PERSON_ORIGIN_NUMBER = 1000000;
    private static final Integer POSTER_ORIGIN_NUMBER = PERSON_ORIGIN_NUMBER * 3;
    private static final Integer REPLY_ORIGIN_NUMBER = PERSON_ORIGIN_NUMBER * 2;
    private static final AtomicInteger PERSON_NUMBER = new AtomicInteger(PERSON_ORIGIN_NUMBER);
    private static final AtomicInteger POSTER_NUMBER = new AtomicInteger(POSTER_ORIGIN_NUMBER);
    private static final AtomicInteger REPLY_NUMBER = new AtomicInteger(REPLY_ORIGIN_NUMBER);
    private static boolean insertFile = true;
    private static final Integer THREAD_NUMBER = 4;

    private static String personBytePath = "D:\\Projects\\simulated_data\\person\\person.data";
    private static String posterBytePath = "D:\\Projects\\simulated_data\\person\\poster.data";
    private static String replyBytePath = "D:\\Projects\\simulated_data\\person\\reply.data";


    private static final StopWatch SW = new StopWatch("Mock Data");

    private static final CyclicBarrier PERSON_CYCLIC_BARRIER = new CyclicBarrier(THREAD_NUMBER, () -> {
        SW.stop();
        log.info("the person data simulated ! starting create poster data");
        SW.start("Poster data mock");
    });

    private static final CyclicBarrier POSTER_CYCLIC_BARRIER = new CyclicBarrier(THREAD_NUMBER, () -> {
        SW.stop();
        log.info("the poster data simulated ! starting create reply data");
        SW.start("Reply data mock");
    });

    private static final CyclicBarrier REPLY_CYCLIC_BARRIER = new CyclicBarrier(THREAD_NUMBER + 1, () -> {
        log.info("the reply data simulated ! all work success");
    });

    @Test
    public void testThreadBuild() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
//        CopyOnWriteArrayList<Person> persons = new CopyOnWriteArrayList<>();
//        CopyOnWriteArrayList<Poster> posters = new CopyOnWriteArrayList<>();
//        CopyOnWriteArrayList<Reply> replies = new CopyOnWriteArrayList<>();
        List<Person> persons = Collections.synchronizedList(new ArrayList<>(PERSON_ORIGIN_NUMBER));
        List<Poster> posters = Collections.synchronizedList(new ArrayList<>(POSTER_ORIGIN_NUMBER));
        List<Reply> replies = Collections.synchronizedList(new ArrayList<>(REPLY_ORIGIN_NUMBER));
        SW.start("Person data mock");
        for (int i = 0; i < THREAD_NUMBER; i++) {
            executorService.execute(() -> {
                log.info(Thread.currentThread().getName() + " starting create person.");
                while (PERSON_NUMBER.decrementAndGet() >= 0) {
                    persons.add(DataBuilders.getPerson());
//                    if(persons.size()%10==0){
//                        log.info("current create person numbers : {}",persons.size());
//                    }
                }
                log.info("person build stop ! current wait:{}", PERSON_CYCLIC_BARRIER.getNumberWaiting());
                try {
                    PERSON_CYCLIC_BARRIER.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                while (POSTER_NUMBER.decrementAndGet() >= 0) {
                    posters.add(DataBuilders.getPoster(PERSON_ORIGIN_NUMBER));
//                    if(posters.size()%10==0){
//                        log.info("current create posters numbers : {}",posters.size());
//                    }
                }
                log.info("poster build stop ! current wait:{}", POSTER_CYCLIC_BARRIER.getNumberWaiting());
                try {
                    POSTER_CYCLIC_BARRIER.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                while (REPLY_NUMBER.decrementAndGet() >= 0) {
                    replies.add(DataBuilders.getReply(PERSON_ORIGIN_NUMBER, POSTER_ORIGIN_NUMBER));
//                    if(replies.size()%10==0){
//                        log.info("current create replies numbers : {}",replies.size());
//                    }

                }
                log.info("reply build stop ! current wait:{}", REPLY_CYCLIC_BARRIER.getNumberWaiting());
                try {
                    REPLY_CYCLIC_BARRIER.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                log.info("current thread {} task is done.", Thread.currentThread().getName());
            });
        }
        log.info("main thread stop ! current wait:{}", REPLY_CYCLIC_BARRIER.getNumberWaiting());
        try {
            REPLY_CYCLIC_BARRIER.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        SW.stop();
        log.info("Person counts:{},Posters counts: {} ,reply counts {}", persons.size(), posters.size(), replies.size());
        log.info("cost: {} ms.", SW.getTotalTimeMillis());
        if (insertFile) {
            SW.start("写入人物数据");
            if (persons.size() == PERSON_ORIGIN_NUMBER) {
//                persons.forEach(person -> log.info(person.toString()));
                //insertFileObject(persons, personObjectPath);
                insertFileChannel(persons, personBytePath);
                log.info("写入人物数据成功");
            }
            SW.stop();
            SW.start("写入帖子数据");
            if (posters.size() == POSTER_ORIGIN_NUMBER) {
//                posters.forEach(poster -> log.info(poster.toString()));
//                insertFileObject(posters, posterObjectPath);
                insertFileChannel(posters, posterBytePath);
                log.info("写入帖子数据成功");
            }
            SW.stop();
            SW.start("写入回复数据");
            if (replies.size() == REPLY_ORIGIN_NUMBER) {
//                replies.forEach(reply -> log.info(reply.toString()));
//                insertFileObject(replies, replyObjectPath);
                insertFileChannel(replies,replyBytePath);
                log.info("写入回复数据成功");

            }
            SW.stop();
            log.info("cost: {} ms,\r\n{}", SW.getTotalTimeMillis(), SW.prettyPrint());
        }

        executorService.shutdown();

    }

    private <T> void insertFileChannel(List<T> collection, String fileName) {
        ByteBuffer map = ByteBuffer.allocate(1024*10);
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             FileChannel channel = fileOutputStream.getChannel()){
            collection.forEach((object) -> {
                byte[] bytes = object.toString().getBytes(StandardCharsets.UTF_8);
                //缓冲区已满
                if (map.position()+bytes.length > map.limit()) {
                    map.flip();
                    try {
                        channel.write(map);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    map.clear();
                }
                map.put(bytes);
            });
            map.flip();
            channel.write(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void insertFileObject(List<T> collection, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            int size = collection.size();
            for (int i = 0; i < size; i++) {
                objectOutputStream.writeObject(collection.get(i));
                if (i % 100000 == 0) {
                    objectOutputStream.flush();
                    objectOutputStream.reset();
                    log.debug("写入10W数据，reset中...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
