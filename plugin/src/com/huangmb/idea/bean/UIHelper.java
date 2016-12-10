package com.huangmb.idea.bean;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * Created by huangmb on 2016/12/11.
 */
public class UIHelper {
    public static void showPopupBalloon(final String result, final Editor editor) {
        System.out.println(result);
        if (editor == null) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }

    public static void showNotification(String title,String content) {
        Notifications.Bus.notify(
                new Notification("Translate", title, content, NotificationType.INFORMATION));
    }
}
