package com.zvosframework.schedule.core.handler.impl;

import com.zvosframework.schedule.core.handler.annotation.ScheduleContext;
import com.zvosframework.schedule.core.handler.annotation.ScheduleHelper;
import com.zvosframework.schedule.core.glue.GlueTypeEnum;
import com.zvosframework.schedule.core.handler.IJobHandler;
import com.zvosframework.schedule.core.handler.annotation.ScheduleFileAppender;
import com.zvosframework.schedule.core.util.ScriptUtil;

import java.io.File;

/**
 * Created by xuxueli on 17/4/27.
 */
public class ScriptJobHandler extends IJobHandler {

    private int jobId;
    private long glueUpdatetime;
    private String gluesource;
    private GlueTypeEnum glueType;

    public ScriptJobHandler(int jobId, long glueUpdatetime, String gluesource, GlueTypeEnum glueType){
        this.jobId = jobId;
        this.glueUpdatetime = glueUpdatetime;
        this.gluesource = gluesource;
        this.glueType = glueType;

        // clean old script file
        File glueSrcPath = new File(JobFileAppender.getGlueSrcPath());
        if (glueSrcPath.exists()) {
            File[] glueSrcFileList = glueSrcPath.listFiles();
            if (glueSrcFileList != null) {
                for (File glueSrcFileItem : glueSrcFileList) {
                    if (glueSrcFileItem.getName().startsWith(jobId +"_")) {
                        glueSrcFileItem.delete();
                    }
                }
            }
        }

    }

    public long getGlueUpdatetime() {
        return glueUpdatetime;
    }

    @Override
    public void execute() throws Exception {

        // valid
        if (!glueType.isScript()) {
            JobHelper.handleFail("glueType["+ glueType +"] invalid.");
            return;
        }

        // cmd
        String cmd = glueType.getCmd();

        // make script file
        String scriptFileName = JobFileAppender.getGlueSrcPath()
                .concat(File.separator)
                .concat(String.valueOf(jobId))
                .concat("_")
                .concat(String.valueOf(glueUpdatetime))
                .concat(glueType.getSuffix());
        File scriptFile = new File(scriptFileName);
        if (!scriptFile.exists()) {
            ScriptUtil.markScriptFile(scriptFileName, gluesource);
        }

        // log file
        String logFileName = JobContext.getJobContext().getLogFileName();

        // script params：0=param、1=分片序号、2=分片总数
        String jobParam = JobHelper.getJobParam();
        String[] scriptParams = new String[3];
        scriptParams[0] = jobParam!=null?jobParam:"";
        scriptParams[1] = String.valueOf(JobContext.getJobContext().getShardIndex());
        scriptParams[2] = String.valueOf(JobContext.getJobContext().getShardTotal());

        // invoke
        JobHelper.log("----------- script file:"+ scriptFileName +" -----------");
        int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);

        if (exitValue == 0) {
            JobHelper.handleSuccess();
            return;
        } else {
            JobHelper.handleFail("script exit value("+exitValue+") is failed");
            return ;
        }

    }

}
