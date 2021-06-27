package com.cutehub.UI;

public interface UIContoller {

    public boolean isUpdating();

    public void update(float dt);

    public void reAlignButton();

    public void render();

    public void create();

    public void dispose();
}
