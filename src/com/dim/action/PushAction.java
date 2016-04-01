package com.dim.action;

import com.dim.DeviceResult;
import com.dim.comand.PushCommand;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlFileImpl;

import static com.dim.ui.NotificationHelper.info;
import static com.dim.utils.Logger.println;

/**
 * Created by dim on 16/3/31.
 */
public class PushAction extends BaseAction {


    private boolean isDataBase(String parentFileName) {


        return parentFileName.equals("databases");
    }

    private boolean isPreference(String parentFileName) {


        return parentFileName.equals("shared_prefs");
    }

    @Override
    void run(final DeviceResult deviceResult, final AnActionEvent anActionEvent) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                Object o = anActionEvent.getDataContext().getData(DataConstants.PSI_FILE);
                if (o instanceof XmlFileImpl) {
                    String parentFileName = ((XmlFileImpl) o).getVirtualFile().getParent().getName();
                    if (isPreference(parentFileName)) {
                        PushCommand pushCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                "data/data/" + deviceResult.packageName + "/shared_prefs/");
                        boolean run = pushCommand.run();
                        if (run) {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                        } else {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " failed !");

                        }

                    }


                } else if (o instanceof PsiFile) {

                    String parentFileName = ((PsiFile) o).getVirtualFile().getParent().getName();
                    if (isDataBase(parentFileName)) {
                        PushCommand pushCommand = new PushCommand(deviceResult, ((PsiFile) o).getVirtualFile().getPath(),
                                "data/data/" + deviceResult.packageName + "/databases/");
                        boolean run = pushCommand.run();
                        if (run) {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " success !");
                        } else {
                            info("push " + ((PsiFile) o).getVirtualFile().getName() + " failed !");

                        }
                    }
                }
            }
        }).start();


    }


}