package com.android.one_adapter_example.interfaces;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface ExampleView {

    void setAll(@NotNull List<Object> items);
    void clearAll();
    void addOne(int i, @NotNull Object newItem);
    void setOne(@NotNull Object updatedItem);
    void removeIndex(int it);
    void removeItem(@NotNull Object removedItem);
    void addAll(@NotNull List<Object> loadMoreItems);
}
