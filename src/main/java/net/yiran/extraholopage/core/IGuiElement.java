package net.yiran.extraholopage.core;

import se.mickelus.mutil.gui.GuiElement;

import java.util.function.Predicate;

public interface IGuiElement {
    public void removeChild(Class<?> type);

    public void removeIfChild(Predicate<? super GuiElement> filter);
}
