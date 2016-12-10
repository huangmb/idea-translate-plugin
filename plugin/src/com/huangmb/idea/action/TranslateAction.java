package com.huangmb.idea.action;

import com.huangmb.idea.bean.BaseTask;
import com.huangmb.idea.bean.TranslateResult;
import com.huangmb.idea.bean.UIHelper;
import com.huangmb.idea.utils.TaskManager;
import com.huangmb.idea.utils.TranslateUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import org.apache.http.util.TextUtils;

/**
 * Created by huangmb on 2016/12/10.
 */
public class TranslateAction extends AnAction {
    private static final String NAME = "TranslateAction";
    @Override
    public void actionPerformed(final AnActionEvent anActionEvent) {
        final Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        if (editor == null){
            return;
        }
        String word = getWordToTranslate(editor);
        System.out.println(word);
        if (TextUtils.isBlank(word)){
            return;
        }
        TranslateUtils.query(word,
                new TaskManager.CallBack() {
                    @Override
                    public void onSuccess(BaseTask task, TranslateResult result) {
                        String s = result.toString();
                        UIHelper.showNotification(NAME + " [DEBUG]", s);
                        UIHelper.showPopupBalloon(s,editor);
                    }

                    @Override
                    public void onFailed(BaseTask task, Exception e) {
                        UIHelper.showNotification(NAME + " [DEBUG]", e.toString());
                        UIHelper.showPopupBalloon(e.getMessage(),editor);
                    }
                }
        );


    }

    private String getWordToTranslate(Editor editor) {
        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int caretOffset = caretModel.getOffset();

        int lineNum = document.getLineNumber(caretOffset);
        int lineStartOffset = document.getLineStartOffset(lineNum);
        int lineEndOffset = document.getLineEndOffset(lineNum);
        String lineContent = document.getText(new TextRange(lineStartOffset, lineEndOffset));
        char[] chars = lineContent.toCharArray();
        int start = 0, end = 0, cursor = caretOffset - lineStartOffset;

        if (!Character.isLetter(chars[cursor])) {
            UIHelper.showPopupBalloon("Caret not in a word",editor);
            return null;
        }

        for (int ptr = cursor; ptr >= 0; ptr--) {
            if (!Character.isLetter(chars[ptr])) {
                start = ptr + 1;
                break;
            }
        }

        int lastLetter = 0;
        for (int ptr = cursor; ptr < lineEndOffset - lineStartOffset; ptr++) {
            lastLetter = ptr;
            if (!Character.isLetter(chars[ptr])) {
                end = ptr;
                break;
            }
        }
        if (end == 0) {
            end = lastLetter + 1;
        }

        String ret = new String(chars, start, end-start);
        System.out.println("Selected words: " + ret);
        return ret;
    }

}
