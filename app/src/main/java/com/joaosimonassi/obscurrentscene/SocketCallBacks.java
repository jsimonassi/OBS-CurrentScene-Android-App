package com.joaosimonassi.obscurrentscene;

import java.util.List;

public interface SocketCallBacks {

    void onEvent(String currentScene);
    void onAddComplete(List<String> scenes);

}
