package main;

import model.Anime;
import org.jetbrains.annotations.NotNull;

public interface Listener {

    /**
     * Click Anime
     *
     * @return void
     */
    void onClickListener(@NotNull Anime anime);

}
