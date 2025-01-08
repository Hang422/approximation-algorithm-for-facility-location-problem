package Base; /**
 * projectName: FLP3.0
 * fileName: UI.MyFrame.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 21:36
 * copyright(c) 2017-2020 xxx公司
 */

import javax.swing.*;
import java.awt.*;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: UI.MyFrame
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 21:36
 **/
public class MyFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 800;

    public MyFrame(MyComponent myComponent) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        setSize(screenWidth,screenHeight);
        setTitle("FLP");
        setResizable(true);
        setLocation(screenWidth/4,screenHeight/4);

        myComponent.setHeightFrame(screenHeight/2);
        myComponent.setWidthFrame(screenWidth/2);
        add(myComponent);
    }
}

