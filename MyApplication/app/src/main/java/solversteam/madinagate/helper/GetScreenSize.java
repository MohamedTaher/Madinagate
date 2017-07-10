package solversteam.madinagate.helper;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Ibrahim on 26/01/2017.
 */

public class GetScreenSize {
    private int width, height;
    private Context context;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GetScreenSize(Context context) {
        this.context = context;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void getImageSize() {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);  // the values are saved in the screenSize object
        width = screenSize.x;
        height = screenSize.y;
    }
}
