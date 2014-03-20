package net.darklordpotter.api.fiction.core;

import com.google.common.base.Supplier;

import java.util.Map;
import java.util.Set;

/**
 * 2014-03-19
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class ThreadLinker {
    private final Supplier<Map<Long,ThreadData>> threadLinkSupplier;

    public ThreadLinker(Supplier<Map<Long, ThreadData>> threadLinkSupplier) {
        this.threadLinkSupplier = threadLinkSupplier;
    }

    public Set<Long> storyIds() {
        return threadLinkSupplier.get().keySet();
    }

    public ThreadData findThreadData(long storyId) {
        return threadLinkSupplier.get().get(storyId);
    }
}
