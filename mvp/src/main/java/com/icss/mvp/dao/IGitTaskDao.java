package com.icss.mvp.dao;

import java.util.List;


import com.icss.mvp.entity.GitLogs;


public interface IGitTaskDao
{
    /**
     * 
     * <pre>
     * <b>描述：保存GIT日志</b>
     *  @param logList
     *  @return
     * </pre>
     */
    int saveLogList(List<GitLogs> gitLogs);
}
