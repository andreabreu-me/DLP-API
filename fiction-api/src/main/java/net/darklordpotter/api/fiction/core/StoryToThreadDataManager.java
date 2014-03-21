package net.darklordpotter.api.fiction.core;

import com.google.common.base.Splitter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 2014-02-04
 *
 * @author Michael Rose
 */
public class StoryToThreadDataManager {
    public static Supplier<Map<Long, ThreadData>> threadLinkSupplier() {
        return Suppliers.memoizeWithExpiration(new ThreadLinkSupplier(), 4, TimeUnit.HOURS);
    }

    private static class ThreadLinkSupplier implements Supplier<Map<Long,ThreadData>> {
        private static Logger log = LoggerFactory.getLogger(ThreadLinkSupplier.class);

        @Override
        public Map<Long, ThreadData> get() {
            String file = "/home/fffn/dlp/threadconnections.txt";

            long start = System.nanoTime();
            log.info("Loading thread connections from {}", file);

            List<String> linkages;
            try {
                linkages = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
            } catch (IOException e) {
                return new HashMap<Long, ThreadData>();
            }

            Map<Long,ThreadData> threadMappings = Maps.newHashMapWithExpectedSize(linkages.size());
            Splitter colonSplitter = Splitter.on(':').trimResults();
            for (String linkage  : linkages) {
                // 8052743:24521:84:31
                // ffnId^  dlp^   ^votenum
                Iterable<String> parts = colonSplitter.split(linkage);
                Iterator<String> partIterator = parts.iterator();

                long ffnId = Long.parseLong(partIterator.next());
                long dlpId = Long.parseLong(partIterator.next());
                long voteNum = Long.parseLong(partIterator.next());
                long voteTotal = Long.parseLong(partIterator.next());

                threadMappings.put(ffnId, new ThreadData(ffnId, dlpId, voteNum, voteTotal));
            }

            log.info("Loading of thread connections took {} ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-start));

            return threadMappings;
        }
    }
}
