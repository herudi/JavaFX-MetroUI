/*
    Animation For JavaFX.
*/

package herudi.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;


/* 
    Created on : Sep 13, 2014, 8:45:48 AM
    Author     : herudi-pc
*/

public class FadeInRightTransition extends configAnimasi {
    /**
     * Create new FadeInUpTransition
     * 
     * @param node The node to affect
     */
    public FadeInRightTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 20, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    )
                )
                .build()
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0));
        node.toFront();
    }
}
